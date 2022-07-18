package uos.selab.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import uos.selab.domains.Contract;
import uos.selab.domains.Contract.ContractBuilder;
import uos.selab.domains.Member;
import uos.selab.dtos.InsertContractDTO;
import uos.selab.dtos.PrintContractDTO;
import uos.selab.dtos.SelectContractDTO;
import uos.selab.exceptions.ResourceNotFoundException;
import uos.selab.mappers.ContractMapper;
import uos.selab.repositories.ContractRepository;
import uos.selab.repositories.ItemRepository;
import uos.selab.repositories.MemberRepository;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/contracts")
public class ContractController {

	private final ContractRepository contractRepo;
	private final MemberRepository memberRepo;
	private final ItemRepository itemRepo;

	@GetMapping()
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "Contract 리스트 조회", protocols = "http")
	public ResponseEntity<List<PrintContractDTO>> findAll(SelectContractDTO contractDTO) {
		List<Contract> contracts = contractRepo.findAllByDTO(contractDTO);

		if (contracts.isEmpty()) {
			throw new ResourceNotFoundException("Not found Contracts");
		}

		return new ResponseEntity<>(toPrintDTO(contracts), HttpStatus.OK);
	}
	
	@GetMapping("/count")
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "Contract 리스트 개수 확인", protocols = "http")
	public ResponseEntity<Integer> countAll(SelectContractDTO contractDTO) {
		List<Contract> contracts = contractRepo.findAllByDTO(contractDTO);

		return new ResponseEntity<>(contracts.size(), HttpStatus.OK);
	}

	@GetMapping("/{num}")
	@ApiOperation(value = "contract_num으로 Contract 정보 조회", protocols = "http")
	public ResponseEntity<PrintContractDTO> findById(@PathVariable("num") Integer contract_num) {

		Contract contract = contractRepo.findById(contract_num)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Contract with id = " + contract_num));

		return new ResponseEntity<>(toPrintDTO(contract), HttpStatus.OK);
	}

//	@PostMapping()
//	@ApiOperation(value = "Contract 등록", protocols = "http")
	public Contract insert(InsertContractDTO contractDTO) {

		Member seller = contractDTO.getSellerNum() != 0 ? memberRepo.getById(contractDTO.getSellerNum()) : null;
		Member buyer = contractDTO.getBuyerNum() != 0 ? memberRepo.getById(contractDTO.getBuyerNum()) : null;

		ContractBuilder contractBuilder = Contract.builder();
		contractBuilder.seller(seller)
					   .buyer(buyer)
					   .item(itemRepo.getById(contractDTO.getItemNum()))
					   .stateCode(contractDTO.getStateCode())
					   .createdAt(new Date());
		if (contractDTO.getPrice() != 0)
			contractBuilder.price(contractDTO.getPrice());

		Contract newContract = contractRepo.save(contractBuilder.build());

		// 저장 및 결과 반환
		return newContract; //new ResponseEntity<>(toPrintDTO(newContract), HttpStatus.OK)
	}

	// 단일 PrintContractDTO 생성 함수
	private PrintContractDTO toPrintDTO(Contract contract) {

		PrintContractDTO printContract = ContractMapper.INSTANCE.toPrintDTO(contract);

		printContract.setItemNum(contract.getItem().getItemNum());
		printContract.setItemTitle(contract.getItem().getTitle());
		printContract.setSellerNum(contract.getSeller().getMemberNum());
		printContract.setSellerNickName(contract.getSeller().getNickname());
		printContract.setBuyerNum(contract.getBuyer().getMemberNum());
		printContract.setBuyerNickName(contract.getBuyer().getNickname());

		return printContract;
	}

	// PrintContractDTO 리스트 생성 함수
	private List<PrintContractDTO> toPrintDTO(List<Contract> contracts) {
		List<PrintContractDTO> printContracts = new ArrayList<>();

		for (Contract contract : contracts) {
			printContracts.add(toPrintDTO(contract));
		}

		return printContracts;
	}
}