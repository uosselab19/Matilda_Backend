package uos.selab.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import uos.selab.domains.Contract;
import uos.selab.domains.Item;
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
	public ResponseEntity<List<PrintContractDTO>> findAll() {
		List<Contract> contracts = contractRepo.findAll();
		
		if (contracts.isEmpty()) {
			throw new ResourceNotFoundException("Not found Contracts");
		}
		
		List<PrintContractDTO> list = new ArrayList<PrintContractDTO>( contracts.size() );
        for ( Contract contract : contracts ) {
            list.add( PrintContractDTO.builder().itemTitle(contract.getItem().getTitle())
            		.sellerNickName(contract.getSeller() ==null ? "none" : contract.getSeller().getNickname())
            		.buyerNickName(contract.getBuyer().getNickname())
            		.stateCode(contract.getStateCode()).price(contract.getPrice()).build());
        }
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@GetMapping("/{num}")
	@ApiOperation(value = "contract_num으로 Contract 정보 조회", protocols = "http")
	public ResponseEntity<PrintContractDTO> findById(@PathVariable("num") Integer contract_num) {
		Contract contract = contractRepo.findById(contract_num)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Contract with id = " + contract_num));
		
		PrintContractDTO result = PrintContractDTO.builder().itemTitle(contract.getItem().getTitle())
        		.sellerNickName(contract.getSeller() ==null ? "none" : contract.getSeller().getNickname())
        		.buyerNickName(contract.getBuyer().getNickname())
        		.stateCode(contract.getStateCode()).price(contract.getPrice()).build();
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@PostMapping("/select")
	@ApiOperation(value = "SelectContractDTO로 Contract 목록 조회", protocols = "http")
	public ResponseEntity<List<PrintContractDTO>> findAll(@RequestBody SelectContractDTO contractDTO) {
		List<Contract> contracts = null;
		
		switch(contractDTO.getKeywordType()) {
			case SELLER:
				contracts = contractRepo.findALLBySeller(memberRepo.getById(contractDTO.getSellerNum()));
				break;
			case BUYER:
				contracts = contractRepo.findALLByBuyer(memberRepo.getById(contractDTO.getBuyerNum()));
				break;
			case SELLER_OR_BUYER:
				contracts = contractRepo.findALLBySellerOrBuyer(memberRepo.getById(contractDTO.getSellerNum()),memberRepo.getById(contractDTO.getBuyerNum()));
				break;
			case ITEM:
				contracts = contractRepo.findALLByItem(itemRepo.getById(contractDTO.getItemNum()));
				break;
		}
		
		if (contracts.isEmpty()) {
			throw new ResourceNotFoundException("Not found Contracts");
		}
		
		List<PrintContractDTO> list = new ArrayList<PrintContractDTO>( contracts.size() );
        for ( Contract contract : contracts ) {
            list.add( PrintContractDTO.builder().itemTitle(contract.getItem().getTitle())
            		.sellerNickName(contract.getSeller() ==null ? "none" : contract.getSeller().getNickname())
            		.buyerNickName(contract.getBuyer().getNickname())
            		.stateCode(contract.getStateCode()).price(contract.getPrice()).build());
        }
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@PostMapping()
	@ApiOperation(value = "Contract 등록", protocols = "http")
	public ResponseEntity<HashMap<String, String>> insert(@RequestBody InsertContractDTO contractDTO) {
		//InsertContractDTO에서 받아온 JSON에서 데이터 추출
		
		Member seller = contractDTO.getSellerNum() != -1 ? memberRepo.getById(contractDTO.getSellerNum()) : null;
		Member buyer = contractDTO.getBuyerNum() != -1 ? memberRepo.getById(contractDTO.getBuyerNum()) : null;
		Item item = itemRepo.getById(contractDTO.getItemNum());
		
		//ContractDTO를 Contract 객체로 매핑
		Contract contract = ContractMapper.INSTANCE.toEntity(contractDTO);
		//Contract 객체의 부족한 속성 부여
		contract.setSeller(seller);
		contract.setBuyer(buyer);
		contract.setItem(item);
		contract.setCreatedAt(new Date());
		 
		Contract resultContract = contractRepo.save(contract);
		
		//Item 객체의 부족한 속성 부여
		//item.setMember(resultContract.getStateCode() == "TR" ? buyer : seller); // TR만 buyer가 소유자인 경우
		item.setMember(buyer); // 현재는 buyer가 소유자이므로 item member를 buyer로
		item.setPrice(resultContract.getPrice());
		item.setStateCode(resultContract.getStateCode());
		Item resultItem = itemRepo.save(item);
		
		//반환 값 지정
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("contract_num", String.valueOf(resultContract.getContractNum()));
		result.put("item_title", String.valueOf(resultItem.getTitle()));
		result.put("owner_name", String.valueOf(resultItem.getMember().getNickname()));
		
		//저장 및 결과 반환
		return new ResponseEntity<>(result, HttpStatus.OK); 
	}
}