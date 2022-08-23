package uos.selab.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import uos.selab.domains.Member;
import uos.selab.dtos.PrintLoginDTO;
import uos.selab.exceptions.ForbiddenException;
import uos.selab.exceptions.ResourceNotFoundException;
import uos.selab.repositories.MemberRepository;
import uos.selab.utils.JwtTokenProvider;

@CrossOrigin(origins = { "http://localhost:3000", "http://3.133.233.81:3000" })
@RequiredArgsConstructor
@RestController()
@RequestMapping("/security")
@Transactional(readOnly = true)
public class SecurityController {

	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final MemberRepository memberRepo;

	// 로그인
	@PostMapping("/login")
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "로그인", protocols = "http")
	@Transactional()
	public ResponseEntity<PrintLoginDTO> login(@RequestBody Map<String, String> loginInfo) {
		// 입력한 id로 member 검색
		Member member = memberRepo.findById(loginInfo.get("id"))
				.orElseThrow(() -> new ResourceNotFoundException("Not found Member with id = " + loginInfo.get("id")));

		// 암호 일치 확인
		if (!passwordEncoder.matches(loginInfo.get("password"), member.getPassword())) {
			throw new IllegalArgumentException("Wrong password");
		}

		// refresh token 발급 및 저장
		member.setRefreshToken(jwtTokenProvider.createRefreshToken());
		member = memberRepo.save(member);

		// access token 발급 및 반환
		PrintLoginDTO printLogin = new PrintLoginDTO();

		List<String> roles = new ArrayList<>();
		roles.add("ROLE_MEMBER");

		printLogin.setId(member.getMemberNum());
		printLogin.setAccessToken(jwtTokenProvider.createAccessToken(member, roles));
		printLogin.setRefreshToken(member.getRefreshToken());

		return new ResponseEntity<>(printLogin, HttpStatus.OK);
	}

	// 토큰 재발급
	@PostMapping("/refresh")
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "토큰 재발급", protocols = "http")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header"),
			@ApiImplicitParam(name = "REFRESH-TOKEN", value = "refresh-token", required = true, dataType = "String", paramType = "header") })
	@Transactional()
	public ResponseEntity<PrintLoginDTO> refreshToken(@RequestHeader(value = "X-AUTH-TOKEN") String accessToken,
			@RequestHeader(value = "REFRESH-TOKEN") String refreshToken, @RequestBody String ignore) {

		// accessToken이 만료되지 않았거나 문제가 있는 token이라면 예외 발생
		if (!jwtTokenProvider.validateTokenExceptExpiration(accessToken))
			// accessToken이 만료되지 않은 정상 토큰이라면 해당 내용을 알림
			if (jwtTokenProvider.validateAccessToken(accessToken)) {
				// ignore의 값이 yes라면 accessToken의 만료 여부를 무시하고 재발급
				if (ignore.equals("yes")) ;
				throw new AccessDeniedException("The accessToken has not yet expired");
			}
			// accessToken에 문제가 있는 경우라면 해당 내용을 알림
			else
				throw new ForbiddenException("The accessToken is not valid");

		Claims claims = jwtTokenProvider.getClaims(accessToken);
		int num = Integer.parseInt(claims.get("num").toString());

		Member member = memberRepo.findById(num)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Member with memberNum = " + num));

		// member에 저장된 refresh token이 만료되었으면 예외 발생
		if (!jwtTokenProvider.validateToken(member.getRefreshToken()))
			throw new AccessDeniedException("The refreshToken has expired");
		// 전달받은 refreshToken이 저장된 값과 다르다면 예외 발생
		if (!refreshToken.equals(member.getRefreshToken()))
			throw new AccessDeniedException("Wrong refreshToken");

		// access token 발급 및 반환
		PrintLoginDTO printLogin = new PrintLoginDTO();

		List<String> roles = new ArrayList<>();
		roles.add("ROLE_MEMBER");
		printLogin.setAccessToken(jwtTokenProvider.createAccessToken(member, roles));
		printLogin.setRefreshToken(member.getRefreshToken());

		return new ResponseEntity<>(printLogin, HttpStatus.OK);
	}

	// 로그아웃
	@PostMapping("/auth/logout")
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "로그아웃", protocols = "http")
	@Transactional()
	public ResponseEntity<String> logout(@RequestHeader(value = "X-AUTH-TOKEN") String accessToken) {

		Claims claims = jwtTokenProvider.getClaims(accessToken);
		int num = Integer.parseInt(claims.get("num").toString());

		Member member = memberRepo.findById(num)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Member with memberNum = " + num));

		// member에 저장된 refresh token을 무효화
		member.setRefreshToken(null);
		memberRepo.save(member);

		return new ResponseEntity<>("logout success", HttpStatus.OK);
	}

	// 토큰의 유효성검사
	@PostMapping("/validCheck")
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "토큰 유효성 검사", protocols = "http")
	@Transactional()
	public ResponseEntity<String> validCheck(@RequestHeader(value = "X-AUTH-TOKEN") String accessToken) {

		// accessToken이 만료되지 않았거나 문제가 있는 token이라면 예외 발생
		if (!jwtTokenProvider.validateTokenExceptExpiration(accessToken))
			// accessToken이 만료되지 않은 정상 토큰이라면 해당 내용을 알림
			if (jwtTokenProvider.validateAccessToken(accessToken))
				return new ResponseEntity<>("valid token", HttpStatus.OK);
			// accessToken에 문제가 있는 경우라면 해당 내용을 알림
			else
				throw new ForbiddenException("The accessToken is not valid");
		// accessToken이 만료되었고, 그 외에는 문제가 없는 경우 해당 내용을 알림
		else
			throw new AccessDeniedException("The accessToken has expired");
	}
}
