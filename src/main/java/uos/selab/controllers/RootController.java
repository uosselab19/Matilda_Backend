package uos.selab.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import uos.selab.domains.CustomUserDetails;

@RestController("/")
public class RootController {

	@GetMapping("/")
	public String firstMethod() {
		return "Welcome!";
	}

	@GetMapping("/member/api")
	public String firstMethod2(Authentication authentication) {
		// Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		return userDetails.getId() + "/" + userDetails.getNum();
	}
}