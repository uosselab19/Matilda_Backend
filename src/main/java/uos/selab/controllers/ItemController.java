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

import com.querydsl.core.util.StringUtils;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import uos.selab.domains.Category;
import uos.selab.domains.Item;
import uos.selab.domains.Item.ItemBuilder;
import uos.selab.domains.Member;
import uos.selab.dtos.InsertHistoryDTO;
import uos.selab.dtos.InsertHistoryDTO.InsertHistoryDTOBuilder;
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

@CrossOrigin(origins = { "http://localhost:3000", "http://3.133.233.81:3000", "http://www.matilda-hanium.click:3000",
        "http://localhost:8000", "http://3.133.233.81:8000", "http://www.matilda-hanium.click:8000",
        "http://localhost:8100", "http://3.133.233.81:8100", "http://www.matilda-hanium.click:8100" })
@RequiredArgsConstructor
@RestController()
@RequestMapping("/items")
@Transactional(readOnly = true)
public class ItemController {

    // Repository??? ???????????? ?????????
    private final ItemRepository itemRepo;
    private final MemberRepository memberRepo;
    private final CategoryRepository categoryRepo;

    private final HistoryController historyController;

    @GetMapping()
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "Item ????????? ??????", protocols = "http")
    public ResponseEntity<List<PrintItemDTO>> findAll(@Valid SelectItemDTO itemDTO) {

        // ????????? ?????? ????????? ??????
        List<Item> items = itemRepo.findAllByDTO(itemDTO);

        // ?????? ??? ???????????? ????????? ?????? ??????
        if (items.isEmpty()) {
            throw new ResourceNotFoundException("Not found Items");
        }

        // printDTO ???????????? ??????
        return new ResponseEntity<>(toPrintDTO(items), HttpStatus.OK);
    }

    @GetMapping("/count")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "Item ????????? ?????? ??????", protocols = "http")
    public ResponseEntity<Integer> countAll(@Valid SelectItemDTO itemDTO) {
        // count????????? ???????????? take ??????
        itemDTO.setTake(Integer.MAX_VALUE);

        // ????????? ?????? ????????? ??????
        List<Item> items = itemRepo.findAllByDTO(itemDTO);

        // ????????? ??????
        return new ResponseEntity<>(items.size(), HttpStatus.OK);
    }

    @GetMapping("/{num}")
    @ApiOperation(value = "?????? Item ??????", protocols = "http")
    public ResponseEntity<PrintDetailItemDTO> findOne(@PathVariable("num") Integer num) {

        // ????????? ?????? ????????? ??????, ?????? ??? ???????????? ????????? ?????? ??????
        Item item = itemRepo.findById(num)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Item with ItemNum = " + num));

        // printDTO ???????????? ??????
        return new ResponseEntity<>(toPrintDetailDTO(item), HttpStatus.OK);
    }

    @PostMapping("/new")
    @ApiOperation(value = "?????? Item ??????", protocols = "http")
    @Transactional()
    public ResponseEntity<PrintDetailItemDTO> insert(@RequestBody @Valid InsertItemDTO itemDTO) {

        // ????????? ????????? ????????? ?????? ?????? ??????
        if (StringUtils.isNullOrEmpty(itemDTO.getTitle())) {
            Date now = new Date();
            itemDTO.setTitle(now.toString());
        }

        // memberNum??? ?????? member ??????, ????????? memberNum??? ???????????? ?????? ??????
        Member member = memberRepo.findById(itemDTO.getMemberNum())
                .orElseThrow(() -> new DataFormatException("Wrong Member with MemberNum = " + itemDTO.getMemberNum()));

        // catCode??? ?????? category ??????, ????????? catCode??? ???????????? ?????? ??????
        Category category = categoryRepo.findById(itemDTO.getCatCode())
                .orElseThrow(() -> new DataFormatException("Wrong Category with catCode = " + itemDTO.getCatCode()));

        // itemDTO??? ????????? Item ?????? ??????
        ItemBuilder itemBuilder = Item.builder();
        itemBuilder.member(member).category(category).title(itemDTO.getTitle()).imgUrl(itemDTO.getImgUrl())
                .objectUrl(itemDTO.getObjectUrl()).stateCode("CR");

        // ????????? ????????? ??????
        Item newItem = itemRepo.save(itemBuilder.build());

        // ????????? ?????? ???????????? ??????
        InsertHistoryDTOBuilder insertHistoryBuilder = InsertHistoryDTO.builder();
        insertHistoryBuilder.itemNum(newItem.getItemNum()).sellerNum(newItem.getMember().getMemberNum())
                .stateCode(newItem.getStateCode());
        historyController.insert(insertHistoryBuilder.build());

        // printDTO ???????????? ??????
        return new ResponseEntity<>(toPrintDetailDTO(newItem), HttpStatus.CREATED);
    }

    @PutMapping("/auth/{num}")
    @ApiOperation(value = "?????? Item ?????? ??????: title, description", protocols = "http")
    @Transactional()
    public ResponseEntity<PrintDetailItemDTO> update(@PathVariable("num") Integer num,
            @Valid @RequestBody UpdateItemDTO itemDTO) {

        // ?????? ??? ????????? ??????. ?????? ??? ???????????? ????????? ?????? ??????
        Item item = itemRepo.findById(num)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Item with ItemNum = " + num));

        // ItemMapper??? ????????? item ????????? ?????? ??????
        ItemMapper.INSTANCE.updateFromDto(itemDTO, item);

        // ???????????? DB??? ??????
        Item newItem = itemRepo.save(item);

        // printDTO ???????????? ??????
        return new ResponseEntity<>(toPrintDetailDTO(newItem), HttpStatus.OK);
    }

    @PutMapping("/auth/change/{num}")
    @ApiOperation(value = "Item ?????? ??????: mint, ?????? ??????/??????/??????, ??????", protocols = "http")
    @Transactional()
    public ResponseEntity<PrintDetailItemDTO> change(@PathVariable("num") Integer num,
            @Valid @RequestBody UpdateDetailItemDTO itemDTO) {

        // ?????? ??? ????????? ??????. ?????? ??? ???????????? ????????? ?????? ??????
        Item item = itemRepo.findById(num)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Item with itemNum = " + num));

        // history ?????? ??????
        InsertHistoryDTOBuilder insertHistoryBuilder = InsertHistoryDTO.builder();

        // option??? ?????? ??????
        switch (itemDTO.getOption()) {
            // ?????? NFT ??????
            case MINT:
                if (itemRepo.findByTokenId(itemDTO.getTokenId()).isPresent()) {
                    throw new DuplicateKeyException("Duplicated Token ID");
                }

                // ?????? ?????? ??????
                insertHistoryBuilder.itemNum(item.getItemNum()).sellerNum(item.getMember().getMemberNum())
                        .stateCode("MT").transactionHash(itemDTO.getTransactionHash());
                historyController.insert(insertHistoryBuilder.build());

                item.setTokenId(itemDTO.getTokenId());
                item.setTokenUri(itemDTO.getTokenUri());
                item.setStateCode("NOS");
                break;

            // ?????? ??????
            case STATE_OS:
                item.setPrice(itemDTO.getPrice());
                item.setStateCode("OS");

                insertHistoryBuilder.itemNum(item.getItemNum()).sellerNum(item.getMember().getMemberNum())
                        .price(item.getPrice())
                        .transactionHash(itemDTO.getTransactionHash()).stateCode("OS");
                historyController.insert(insertHistoryBuilder.build());
                break;

            // ?????? ??????
            case STATE_NOS:
                item.setPrice(null);
                item.setStateCode("NOS");

                insertHistoryBuilder.itemNum(item.getItemNum()).sellerNum(item.getMember().getMemberNum())
                        .stateCode("NOS").transactionHash(itemDTO.getTransactionHash());
                historyController.insert(insertHistoryBuilder.build());
                break;

            // ?????? ??????
            case TRADE:
                // ?????? ?????? ??????
                insertHistoryBuilder.itemNum(item.getItemNum()).sellerNum(item.getMember().getMemberNum())
                        .buyerNum(itemDTO.getBuyerNum())
                        .stateCode("TR").price(item.getPrice()).transactionHash(itemDTO.getTransactionHash());
                historyController.insert(insertHistoryBuilder.build());

                // ????????? ?????? ??? ?????? ?????? ????????? ??????
                item.setMember(memberRepo.getById(itemDTO.getBuyerNum()));
                item.setPrice(null);
                item.setStateCode("NOS");
                break;

            // ?????? ??????
            case STOP:
                item.setPrice(null);
                item.setStateCode("ST");

                insertHistoryBuilder.itemNum(item.getItemNum()).sellerNum(item.getMember().getMemberNum())
                        .transactionHash(itemDTO.getTransactionHash()).stateCode("ST");
                historyController.insert(insertHistoryBuilder.build());
                break;
        }

        // ???????????? DB??? ??????
        Item newItem = itemRepo.save(item);

        // printDTO ???????????? ??????
        return new ResponseEntity<>(toPrintDetailDTO(newItem), HttpStatus.OK);
    }

    // ?????? PrintItemDTO ?????? ??????
    private PrintItemDTO toPrintDTO(Item item) {

        PrintItemDTO printItem = ItemMapper.INSTANCE.toPrintDTO(item);

        printItem.setMemberThumbImgUrl(item.getMember().getThumbProfileImg());
        printItem.setCatCode(item.getCategory().getCatCode());

        return printItem;
    }

    // PrintItemDTO ????????? ?????? ??????
    private List<PrintItemDTO> toPrintDTO(List<Item> items) {
        List<PrintItemDTO> printItems = new ArrayList<>();

        for (Item item : items) {
            printItems.add(toPrintDTO(item));
        }

        return printItems;
    }

    // ?????? PrintDetailItemDTO ?????? ??????
    private PrintDetailItemDTO toPrintDetailDTO(Item item) {

        PrintDetailItemDTO printItem = ItemMapper.INSTANCE.toPrintDetailDTO(item);

        printItem.setMemberNum(item.getMember().getMemberNum());
        printItem.setMemberNickName(item.getMember().getNickname());
        printItem.setMemberThumbImgUrl(item.getMember().getThumbProfileImg());
        printItem.setCatCode(item.getCategory().getCatCode());

        return printItem;
    }
}