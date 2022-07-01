package uos.selab.controllers;

import java.util.ArrayList;
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
import uos.selab.domains.Item;
import uos.selab.domains.Item.ItemBuilder;
import uos.selab.dtos.InsertItemDTO;
import uos.selab.dtos.PrintDetailItemDTO;
import uos.selab.dtos.PrintItemDTO;
import uos.selab.dtos.SelectItemDTO;
import uos.selab.dtos.UpdateItemDTO;
import uos.selab.exceptions.ResourceNotFoundException;
import uos.selab.mappers.ItemMapper;
import uos.selab.repositories.CategoryRepository;
import uos.selab.repositories.ItemRepository;
import uos.selab.repositories.MemberRepository;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/items")
public class ItemController {

	private final ItemRepository itemRepo;
	private final MemberRepository memberRepo;
	private final CategoryRepository categoryRepo;

	@GetMapping()
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(value = "Item 리스트 검색", protocols = "http")
	public ResponseEntity<List<PrintItemDTO>> findAll(@RequestBody SelectItemDTO itemDTO) {
		List<Item> items = itemRepo.findAllByDTO(itemDTO);

		if (items.isEmpty()) {
			throw new ResourceNotFoundException("Not found Items");
		}

		return new ResponseEntity<>(toPrintDTO(items), HttpStatus.OK);
	}

	@GetMapping("/{num}")
	@ApiOperation(value = "특정 Item 조회", protocols = "http")
	public ResponseEntity<PrintDetailItemDTO> findOne(@PathVariable("num") Integer num) {
		Item item = itemRepo.findById(num)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Item with id = " + num));

		return new ResponseEntity<>(toPrintDetailDTO(item), HttpStatus.OK);
	}

	@GetMapping("/user/{num}")
	@ApiOperation(value = "특정 사용자의 Item 조회", protocols = "http")
	public ResponseEntity<List<PrintItemDTO>> findUserItem(@PathVariable("num") Integer num) {
		List<Item> items = itemRepo.findByMember(memberRepo.getById(num));

		if (items.isEmpty()) {
			throw new ResourceNotFoundException("Not found Items");
		}

		return new ResponseEntity<>(toPrintDTO(items), HttpStatus.OK);
	}

	@GetMapping("/category/{catCode}")
	@ApiOperation(value = "특정 카테고리의 Item 조회", protocols = "http")
	public ResponseEntity<List<PrintItemDTO>> findCategoryItem(@PathVariable("catCode") String catCode) {
		List<Item> items = itemRepo.findByCategory(categoryRepo.getById(catCode));

		if (items.isEmpty()) {
			throw new ResourceNotFoundException("Not found Items");
		}

		return new ResponseEntity<>(toPrintDTO(items), HttpStatus.OK);
	}

	@PostMapping()
	@ApiOperation(value = "신규 Item 추가", protocols = "http")
	public ResponseEntity<PrintItemDTO> insert(@RequestBody InsertItemDTO itemDTO) {

		Date now = new Date();

		// itemDTO를 사용해 Item 객체 생성
		ItemBuilder itemBuilder = Item.builder();
		itemBuilder.member(memberRepo.getById(itemDTO.getMemberNum()))
				.category(categoryRepo.getById(itemDTO.getCatCode())).title(now.toString())
				.imgUrl(itemDTO.getImgUrl()).objectUrl(itemDTO.getObjectUrl())
				.stateCode("CR");

		// 생성한 아이템 저장
		Item newItem = itemRepo.save(itemBuilder.build());

		// 아이템 생성 컨트랙트 실행
		//
		//

		return new ResponseEntity<>(toPrintDTO(newItem), HttpStatus.OK);
	}

	@PutMapping("/{num}")
	@ApiOperation(value = "기존 Item 변경", protocols = "http")
	public ResponseEntity<PrintItemDTO> update(@PathVariable("num") Integer num,
			@Valid @RequestBody UpdateItemDTO itemDTO) {

		Item item = itemRepo.findById(num)
				.orElseThrow(() -> new ResourceNotFoundException("Not found Item with id = " + num));
		System.out.println("name : " + itemDTO.getTitle());
		ItemMapper.INSTANCE.updateFromDto(itemDTO, item);
		System.out.println("name : " + item.getTitle());
		Item newItem = itemRepo.save(item);

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