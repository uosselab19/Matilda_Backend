package uos.selab.controllers;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import uos.selab.domains.Member;
import uos.selab.dtos.InsertMemberDTO;
import uos.selab.dtos.UpdateMemberDTO;
import uos.selab.exceptions.ResourceNotFoundException;
import uos.selab.mappers.MemberMapper;
import uos.selab.repositories.MemberRepository;


@RequiredArgsConstructor
@RestController()
@RequestMapping("/members")
public class MemberController {

	private final MemberRepository memberRepo;

	@GetMapping()
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "Member 리스트 조회", protocols = "http")
	public List<Member> findAll() {
		List<Member> members = memberRepo.findAll();
		
		if (members.isEmpty()) {
			throw new ResourceNotFoundException("Not found Members");
		}
		
		return members;
	}

	@GetMapping("/{num}")
	public ResponseEntity<Member> findOne(@PathVariable("num") Integer num) {
		Member member = memberRepo.findById(num)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Member with id = " + num));
		
		return new ResponseEntity<>(member, HttpStatus.OK);
	}

	@PostMapping()
	public Member insert(@RequestBody InsertMemberDTO memberDTO) {	
		Member newMember = MemberMapper.INSTANCE.toEntity(memberDTO);
		
		newMember.setCreatedAt(new Date());

		return memberRepo.save(newMember);
	}

	@PutMapping("/{num}")
	public Member update(@PathVariable("num") Integer num, @Valid @RequestBody UpdateMemberDTO memberDTO) {
		
		Member member = memberRepo.findById(num).orElseThrow(() -> new ResourceNotFoundException("Not found Member with id = " + num));
		System.out.println("name : " + memberDTO.getNickname());
		MemberMapper.INSTANCE.updateFromDto(memberDTO, member);
		System.out.println("name : " + member.getNickname());
	    Member newMember = memberRepo.save(member);
	    
		return newMember;
	}
}