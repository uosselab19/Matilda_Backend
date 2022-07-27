package uos.selab.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import uos.selab.domains.Category;
import uos.selab.domains.Item;
import uos.selab.domains.Item.ItemBuilder;
import uos.selab.domains.Member;
import uos.selab.dtos.InsertContractDTO;
import uos.selab.dtos.InsertContractDTO.InsertContractDTOBuilder;
import uos.selab.dtos.InsertItemDTO;
import uos.selab.dtos.PrintDetailItemDTO;
import uos.selab.dtos.PrintItemDTO;
import uos.selab.dtos.SelectItemDTO;
import uos.selab.dtos.UpdateDetailItemDTO;
import uos.selab.dtos.UpdateItemDTO;
import uos.selab.exceptions.DataFormatException;
import uos.selab.exceptions.ResourceNotFoundException;
import uos.selab.mappers.ItemMapper;
import uos.selab.repositories.CategoryRepository;
import uos.selab.repositories.ItemRepository;
import uos.selab.repositories.MemberRepository;

@CrossOrigin(origins = {"http://localhost:3000", "http://3.133.233.81:3000"})
@RequiredArgsConstructor
@RestController()
@RequestMapping("/items")
@Transactional(readOnly = true)
public class ItemController {

	// Repository와 통신하는 클래스
	private final ItemRepository itemRepo;
	private final MemberRepository memberRepo;
	private final CategoryRepository categoryRepo;

	private final ContractController contractController;

	@GetMapping()
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "Item 리스트 검색", protocols = "http")
	public ResponseEntity<List<PrintItemDTO>> findAll(@Valid SelectItemDTO itemDTO) {

		// 조건에 맞는 아이템 검색
		List<Item> items = itemRepo.findAllByDTO(itemDTO);

		// 검색 된 아이템이 없으면 예외 발생
		if (items.isEmpty()) {
			throw new ResourceNotFoundException("Not found Items");
		}

		// printDTO 형식으로 반환
		return new ResponseEntity<>(toPrintDTO(items), HttpStatus.OK);
	}

	@GetMapping("/count")
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "Item 리스트 개수 확인", protocols = "http")
	public ResponseEntity<Integer> countAll(@Valid SelectItemDTO itemDTO) {

		// 조건에 맞는 아이템 검색
		List<Item> items = itemRepo.findAllByDTO(itemDTO);

		// 개수만 출력
		return new ResponseEntity<>(items.size(), HttpStatus.OK);
	}

	@GetMapping("/{num}")
	@ApiOperation(value = "특정 Item 조회", protocols = "http")
	public ResponseEntity<PrintDetailItemDTO> findOne(@PathVariable("num") Integer num) {

		// 조건에 맞는 아이템 검색, 검색 된 아이템이 없으면 예외 발생
		Item item = itemRepo.findById(num)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Item with id = " + num));

		// printDTO 형식으로 반환
		return new ResponseEntity<>(toPrintDetailDTO(item), HttpStatus.OK);
	}

	@GetMapping("/user/{num}")
	@ApiOperation(value = "특정 사용자의 Item 조회", protocols = "http")
	public ResponseEntity<List<PrintItemDTO>> findUserItem(@PathVariable("num") Integer num) {

		// mumberNum을 통해 아이템 검색
		List<Item> items = itemRepo.findByMember(memberRepo.getById(num));

		// 검색 된 아이템이 없으면 예외 발생
		if (items.isEmpty()) {
			throw new ResourceNotFoundException("Not found Items");
		}

		// printDTO 형식으로 반환
		return new ResponseEntity<>(toPrintDTO(items), HttpStatus.OK);
	}

	@GetMapping("/category/{catCode}")
	@ApiOperation(value = "특정 카테고리의 Item 조회", protocols = "http")
	public ResponseEntity<List<PrintItemDTO>> findCategoryItem(@PathVariable("catCode") String catCode) {
		
		// catCode를 통해 category 검색, 잘못된 catCode가 들어오면 예외 발생
		Category category = categoryRepo.findById(catCode)
				.orElseThrow(() -> new DataFormatException("Wrong Category with catCode = " + catCode));
		
		// category를 기준으로 아이템 검색
		List<Item> items = itemRepo.findByCategory(category);

		// 검색 된 아이템이 없다면 예외 발생
		if (items.isEmpty()) {
			throw new ResourceNotFoundException("Not found Items");
		}

		// printDTO 형식으로 반환
		return new ResponseEntity<>(toPrintDTO(items), HttpStatus.OK);
	}

	@PostMapping()
	@ApiOperation(value = "신규 Item 추가", protocols = "http")
	@Transactional()
	public ResponseEntity<PrintItemDTO> insert(@RequestBody @Valid InsertItemDTO itemDTO) {

		// 아이템 이름으로 지정할 임의의 문자열로 현재 시간 사용
		Date now = new Date();
		
		// memberNum을 통해 member 검색, 잘못된 memberNum이 들어오면 예외 발생
		Member member = memberRepo.findById(itemDTO.getMemberNum())
				.orElseThrow(() -> new DataFormatException("Wrong Member with memberNum = " + itemDTO.getMemberNum()));
		
		// catCode를 통해 category 검색, 잘못된 catCode가 들어오면 예외 발생
		Category category = categoryRepo.findById(itemDTO.getCatCode())
				.orElseThrow(() -> new DataFormatException("Wrong Category with catCode = " + itemDTO.getCatCode()));
		
		// itemDTO를 사용해 Item 객체 생성
		ItemBuilder itemBuilder = Item.builder();
		itemBuilder.member(member)
				.category(category).title(now.toString()).imgUrl(itemDTO.getImgUrl())
				.objectUrl(itemDTO.getObjectUrl()).stateCode("CR");

		// 생성한 아이템 저장
		Item newItem = itemRepo.save(itemBuilder.build());

		// 아이템 생성 컨트랙트 생성
		InsertContractDTOBuilder insertContractBuilder = InsertContractDTO.builder();
		insertContractBuilder.itemNum(newItem.getItemNum())
							 .sellerNum(newItem.getMember().getMemberNum())
							 .stateCode(newItem.getStateCode());
		contractController.insert(insertContractBuilder.build());

		// printDTO 형식으로 반환
		return new ResponseEntity<>(toPrintDTO(newItem), HttpStatus.CREATED);
	}

	@PutMapping("/{num}")
	@ApiOperation(value = "기존 Item 단순 수정: title, description, price", protocols = "http")
	@Transactional()
	public ResponseEntity<PrintItemDTO> update(@PathVariable("num") Integer num, @Valid @RequestBody UpdateItemDTO itemDTO) {

		// 수정 할 아이템 검색. 검색 된 아이템이 없다면 예외 발생
		Item item = itemRepo.findById(num)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Item with id = " + num));

		// ItemMapper를 사용해 item 객체의 내용 수정
		ItemMapper.INSTANCE.updateFromDto(itemDTO, item);

		// 수정사항 DB에 저장
		Item newItem = itemRepo.save(item);

		// printDTO 형식으로 반환
		return new ResponseEntity<>(toPrintDTO(newItem), HttpStatus.OK);
	}

	@PutMapping("/change/{num}")
	@ApiOperation(value = "Item 상태 변경: mint, 판매 등록/중지/중단, 거래", protocols = "http")
	@Transactional()
	public ResponseEntity<PrintItemDTO> change(@PathVariable("num") Integer num, @Valid @RequestBody UpdateDetailItemDTO itemDTO) {

		// 수정 할 아이템 검색. 검색 된 아이템이 없다면 예외 발생
		Item item = itemRepo.findById(num)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Item with id = " + num));

		// contract 작성 준비
		InsertContractDTOBuilder insertContractBuilder = InsertContractDTO.builder();
		insertContractBuilder.itemNum(item.getItemNum());

		// option별 명령 수행
		switch (itemDTO.getOption()) {
			// 초기 NFT 민팅
			case MINT:
				if (itemRepo.findByNftAddress(itemDTO.getNftAddress()).isPresent()) {
					throw new DuplicateKeyException("Duplicated NFT Address");
				}
				item.setNftAddress(itemDTO.getNftAddress());
				item.setStateCode("NOS");
				break;
			// 판매 등록
			case STATE_OS:
				item.setStateCode("OS");
				break;
			// 판매 중지
			case STATE_NOS:
				item.setStateCode("NOS");
				break;
			// 거래 체결
			case TRADE:
				// 거래 완료 기록
				insertContractBuilder.sellerNum(item.getMember().getMemberNum()).buyerNum(itemDTO.getBuyerNum())
						.stateCode("TR").price(item.getPrice());
				contractController.insert(insertContractBuilder.build());

				// 소유자 변경 및 판매 중지 상태로 변경
				item.setMember(memberRepo.getById(itemDTO.getBuyerNum()));
				item.setStateCode("NOS");

				insertContractBuilder = InsertContractDTO.builder();
				insertContractBuilder.itemNum(item.getItemNum());
				break;
			// 판매 중단
			case STOP:
				item.setStateCode("ST");
				break;
		}

		insertContractBuilder.sellerNum(item.getMember().getMemberNum()).stateCode(item.getStateCode());
		contractController.insert(insertContractBuilder.build());

		// 수정사항 DB에 저장
		Item newItem = itemRepo.save(item);

		// printDTO 형식으로 반환
		return new ResponseEntity<>(toPrintDTO(newItem), HttpStatus.OK);
	}

	// 단일 PrintItemDTO 생성 함수
	private PrintItemDTO toPrintDTO(Item item) {

		PrintItemDTO printItem = ItemMapper.INSTANCE.toPrintDTO(item);

		printItem.setMemberNum(item.getMember().getMemberNum());
		printItem.setMemberNickName(item.getMember().getNickname());
		printItem.setMemberThumbImgUrl(item.getMember().getThumbProfileImg());
		printItem.setCatCode(item.getCategory().getCatCode());

		return printItem;
	}

	// PrintItemDTO 리스트 생성 함수
	private List<PrintItemDTO> toPrintDTO(List<Item> items) {
		List<PrintItemDTO> printItems = new ArrayList<>();

		for (Item item : items) {
			printItems.add(toPrintDTO(item));
		}

		return printItems;
	}

	// 단일 PrintDetailItemDTO 생성 함수
	private PrintDetailItemDTO toPrintDetailDTO(Item item) {

		PrintDetailItemDTO printItem = ItemMapper.INSTANCE.toPrintDetailDTO(item);

		printItem.setMemberNum(item.getMember().getMemberNum());
		printItem.setMemberNickName(item.getMember().getNickname());
		printItem.setMemberThumbImgUrl(item.getMember().getThumbProfileImg());
		printItem.setCatCode(item.getCategory().getCatCode());

		return printItem;
	}
}