package uos.selab.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import uos.selab.dtos.UpdateMemberKlaytnDTO;
import uos.selab.exceptions.ResourceNotFoundException;
import uos.selab.mappers.MemberMapper;
import uos.selab.repositories.MemberRepository;

@CrossOrigin(origins = { "http://localhost:3000", "http://3.133.233.81:3000", "https://localhost:3000",
        "http://172.16.163.170:3000", "https://172.16.163.170:3000" })
@RequiredArgsConstructor
@RestController()
@RequestMapping("/members")
@Transactional(readOnly = true)
public class MemberController {

    private final MemberRepository memberRepo;
    private final PasswordEncoder passwordEncoder;
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
                .orElseThrow(() -> new ResourceNotFoundException("Not found Member with MemberNum = " + num));

        return new ResponseEntity<>(toPrintDTO(member), HttpStatus.OK);
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "신규 Member 생성", protocols = "http")
    @Transactional()
    public ResponseEntity<PrintMemberDTO> insert(@RequestBody @Valid InsertMemberDTO memberDTO) {
        if (memberRepo.findById(memberDTO.getId()).isPresent()) {
            throw new DuplicateKeyException("Duplicated ID");
        }

        Member newMember = MemberMapper.INSTANCE.toEntity(memberDTO);

        newMember.setPassword(passwordEncoder.encode(newMember.getPassword()));
        newMember.setCreatedAt(new Date());

        newMember = memberRepo.save(newMember);

        return new ResponseEntity<>(toPrintDTO(newMember), HttpStatus.CREATED);
    }

    @PutMapping("/auth/{num}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "기존 Member 수정", protocols = "http")
    @Transactional()
    public ResponseEntity<PrintMemberDTO> update(@PathVariable("num") Integer num,
            @Valid @RequestBody UpdateMemberDTO memberDTO) {
        Member member = memberRepo.findById(num)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Member with MemberNum = " + num));

        MemberMapper.INSTANCE.updateFromDto(memberDTO, member);

        member.setPreset(gson.toJson(memberDTO.getPresetList()));

        Member newMember = memberRepo.save(member);

        return new ResponseEntity<>(toPrintDTO(newMember), HttpStatus.OK);
    }

    @PutMapping("/auth/klaytn/{num}")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "Member의 Klaytn 정보 수정", protocols = "http")
    @Transactional()
    public ResponseEntity<PrintMemberDTO> updateKlaytn(@PathVariable("num") Integer num,
            @Valid @RequestBody UpdateMemberKlaytnDTO memberDTO) {
        Member member = memberRepo.findById(num)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Member with MemberNum = " + num));

        MemberMapper.INSTANCE.updateKlaytnFromDto(memberDTO, member);

        Member newMember = memberRepo.save(member);

        return new ResponseEntity<>(toPrintDTO(newMember), HttpStatus.OK);
    }

    // 단일 PrintMemberDTO 생성 함수
    private PrintMemberDTO toPrintDTO(Member member) {

        PrintMemberDTO printMember = MemberMapper.INSTANCE.toPrintDTO(member);

        printMember
                .setPresetList(gson.fromJson(member.getPreset(), new TypeToken<ArrayList<HashMap<String, Integer>>>() {
                }.getType()));

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