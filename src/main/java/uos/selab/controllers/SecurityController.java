package uos.selab.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import uos.selab.domains.Member;
import uos.selab.repositories.MemberRepository;
import uos.selab.utils.JwtTokenProvider;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/security")
public class SecurityController {

    // private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepo;

    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) {
        Member member = memberRepo.findById(user.get("id"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디입니다."));
        /* if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        } */
        if (!user.get("password").toString().equals(member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_MEMBER");
        
        return jwtTokenProvider.createToken(member, roles);
    }
}
