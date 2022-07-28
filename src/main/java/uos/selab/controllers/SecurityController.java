package uos.selab.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import uos.selab.domains.Member;
import uos.selab.exceptions.ResourceNotFoundException;
import uos.selab.repositories.MemberRepository;
import uos.selab.utils.JwtTokenProvider;

@CrossOrigin(origins = {"http://localhost:3000", "http://3.133.233.81:3000"})
@RequiredArgsConstructor
@RestController()
@RequestMapping("/security")
public class SecurityController {

	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final MemberRepository memberRepo;

	// 로그인
	@PostMapping("/login")
	public String login(@RequestBody Map<String, String> user) {
		Member member = memberRepo.findById(user.get("id"))
				.orElseThrow(() -> new ResourceNotFoundException("Not found Member with id = " + user.get("id")));

        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("Wrong password");
        }

		List<String> roles = new ArrayList<>();
		roles.add("ROLE_MEMBER");

		return jwtTokenProvider.createToken(member, roles);
	}
}
