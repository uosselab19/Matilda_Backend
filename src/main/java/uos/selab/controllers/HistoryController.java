package uos.selab.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import uos.selab.domains.History;
import uos.selab.domains.History.HistoryBuilder;
import uos.selab.domains.Member;
import uos.selab.dtos.InsertHistoryDTO;
import uos.selab.dtos.PrintHistoryDTO;
import uos.selab.dtos.SelectHistoryDTO;
import uos.selab.exceptions.ResourceNotFoundException;
import uos.selab.mappers.HistoryMapper;
import uos.selab.repositories.HistoryRepository;
import uos.selab.repositories.ItemRepository;
import uos.selab.repositories.MemberRepository;

@CrossOrigin(origins = { "http://localhost:3000", "http://3.133.233.81:3000", "http://www.matilda-hanium.click:3000" })
@RequiredArgsConstructor
@RestController()
@RequestMapping("/histories")
@Transactional(readOnly = true)
public class HistoryController {

    private final HistoryRepository historyRepo;
    private final MemberRepository memberRepo;
    private final ItemRepository itemRepo;

    @GetMapping()
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "History 리스트 조회", protocols = "http")
    public ResponseEntity<List<PrintHistoryDTO>> findAll(@Valid SelectHistoryDTO historyDTO) {
        List<History> histories = historyRepo.findAllByDTO(historyDTO);

        if (histories.isEmpty()) {
            throw new ResourceNotFoundException("Not found Histories");
        }

        return new ResponseEntity<>(toPrintDTO(histories), HttpStatus.OK);
    }

    @GetMapping("/count")
    @ResponseStatus(value = HttpStatus.OK)
    @ApiOperation(value = "History 리스트 개수 확인", protocols = "http")
    public ResponseEntity<Integer> countAll(@Valid SelectHistoryDTO historyDTO) {
        // count시에는 무한개의 take 사용
        historyDTO.setTake(Integer.MAX_VALUE);

        List<History> histories = historyRepo.findAllByDTO(historyDTO);

        return new ResponseEntity<>(histories.size(), HttpStatus.OK);
    }

    @GetMapping("/{num}")
    @ApiOperation(value = "history_num으로 History 정보 조회", protocols = "http")
    public ResponseEntity<PrintHistoryDTO> findById(@PathVariable("num") Integer history_num) {

        History history = historyRepo.findById(history_num)
                .orElseThrow(() -> new ResourceNotFoundException("Not found History with id = " + history_num));

        return new ResponseEntity<>(toPrintDTO(history), HttpStatus.OK);
    }

//	@PostMapping()
//	@ApiOperation(value = "History 등록", protocols = "http")
    public History insert(InsertHistoryDTO historyDTO) {

        Member seller = historyDTO.getSellerNum() != 0 ? memberRepo.getById(historyDTO.getSellerNum()) : null;
        Member buyer = historyDTO.getBuyerNum() != 0 ? memberRepo.getById(historyDTO.getBuyerNum()) : null;

        HistoryBuilder historyBuilder = History.builder();
        historyBuilder.seller(seller).buyer(buyer).item(itemRepo.getById(historyDTO.getItemNum()))
                .stateCode(historyDTO.getStateCode()).transactionHash(historyDTO.getTransactionHash())
                .price(historyDTO.getPrice()).createdAt(new Date());

        History newHistory = historyRepo.save(historyBuilder.build());

        // 저장 및 결과 반환
        return newHistory; // new ResponseEntity<>(toPrintDTO(newHistory), HttpStatus.OK)
    }

    // 단일 PrintHistoryDTO 생성 함수
    private PrintHistoryDTO toPrintDTO(History history) {

        PrintHistoryDTO printHistory = HistoryMapper.INSTANCE.toPrintDTO(history);

        printHistory.setItemNum(history.getItem().getItemNum());
        printHistory.setItemTitle(history.getItem().getTitle());
        printHistory.setSellerNum(history.getSeller().getMemberNum());
        printHistory.setSellerNickName(history.getSeller().getNickname());
        if (history.getBuyer() != null) {
            printHistory.setBuyerNum(history.getBuyer().getMemberNum());
            printHistory.setBuyerNickName(history.getBuyer().getNickname());
        }
        return printHistory;
    }

    // PrintHistoryDTO 리스트 생성 함수
    private List<PrintHistoryDTO> toPrintDTO(List<History> histories) {
        List<PrintHistoryDTO> printHistories = new ArrayList<>();

        for (History history : histories) {
            printHistories.add(toPrintDTO(history));
        }

        return printHistories;
    }
}