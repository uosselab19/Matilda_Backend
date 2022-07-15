package uos.selab.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import uos.selab.domains.CustomUserDetails;
import uos.selab.domains.Member;
import uos.selab.exceptions.ResourceNotFoundException;
import uos.selab.repositories.MemberRepository;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

	private final MemberRepository memberRepo;

	@Override
	public UserDetails loadUserByUsername(String id) {
		Member member = memberRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(""));

		CustomUserDetails user = CustomUserDetails.builder().id(member.getId()).password(member.getPassword()).build();

		return user;
	}

}