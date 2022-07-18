package uos.selab.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import uos.selab.domains.Member;
import uos.selab.dtos.InsertMemberDTO;
import uos.selab.dtos.PrintMemberDTO;
import uos.selab.dtos.UpdateMemberDTO;
import uos.selab.exceptions.ResourceNotFoundException;
import uos.selab.mappers.MemberMapper;
import uos.selab.repositories.MemberRepository;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/members")
public class MemberController {

	private final MemberRepository memberRepo;
	
	private Gson gson = new Gson();

	@GetMapping()
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "Member 리스트 조회", protocols = "http")
	public ResponseEntity<List<PrintMemberDTO>> findAll() {
		List<Member> members = memberRepo.findAll();

		if (members.isEmpty()) {
			throw new ResourceNotFoundException("Not found Members");
		}

		return new ResponseEntity<>(toPrintDTO(members), HttpStatus.OK);
	}

	@GetMapping("/{num}")
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "특정 Member 조회", protocols = "http")
	public ResponseEntity<PrintMemberDTO> findOne(@PathVariable("num") Integer num) {
		Member member = memberRepo.findById(num)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Member with id = " + num));

		return new ResponseEntity<>(toPrintDTO(member), HttpStatus.OK);
	}

	@PostMapping()
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "신규 Member 생성", protocols = "http")
	public ResponseEntity<PrintMemberDTO> insert(@RequestBody InsertMemberDTO memberDTO) {
		Member newMember = MemberMapper.INSTANCE.toEntity(memberDTO);

		newMember.setCreatedAt(new Date());

		newMember = memberRepo.save(newMember);

		return new ResponseEntity<>(toPrintDTO(newMember), HttpStatus.CREATED);
	}

	@PutMapping("/{num}")
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "기존 Member 수정", protocols = "http")
	public ResponseEntity<PrintMemberDTO> update(@PathVariable("num") Integer num, @Valid @RequestBody UpdateMemberDTO memberDTO) {
		Member member = memberRepo.findById(num)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Member with id = " + num));

		MemberMapper.INSTANCE.updateFromDto(memberDTO, member);

		member.setPreset(gson.toJson(memberDTO.getPresetList()));

		Member newMember = memberRepo.save(member);

		return new ResponseEntity<>(toPrintDTO(newMember), HttpStatus.OK);
	}

	// 단일 PrintMemberDTO 생성 함수
	private PrintMemberDTO toPrintDTO(Member member) {

		PrintMemberDTO printMember = MemberMapper.INSTANCE.toPrintDTO(member);
		
		printMember.setPresetList(gson.fromJson(member.getPreset(), new TypeToken<ArrayList<HashMap<String, Integer>>>() {}.getType()));

		
		return printMember;
	}

	// PrintMemberDTO 리스트 생성 함수
	private List<PrintMemberDTO> toPrintDTO(List<Member> members) {
		List<PrintMemberDTO> printMembers = new ArrayList<>();

		for (Member member : members) {
			printMembers.add(toPrintDTO(member));
		}

		return printMembers;
	}

}