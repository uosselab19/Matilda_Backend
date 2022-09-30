package uos.selab.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import uos.selab.domains.Item;
import uos.selab.exceptions.ResourceNotFoundException;
import uos.selab.repositories.ItemRepository;

@CrossOrigin(origins = { "http://localhost:8000", "http://3.133.233.81:8000", "http://localhost:8100",
        "http://3.133.233.81:8100" })
@RequiredArgsConstructor
@RestController()
@RequestMapping("/objects")
@Transactional(readOnly = true)
public class ObjectController {

    // Repository와 통신하는 클래스
    private final ItemRepository itemRepo;

    @GetMapping("/auth/objUrl/{num}")
    @ApiOperation(value = "특정 Item의 objectURL 조회", protocols = "http")
    public ResponseEntity<String> findObjectUrl(@PathVariable("num") Integer num) {

        // 조건에 맞는 아이템 검색, 검색 된 아이템이 없으면 예외 발생
        Item item = itemRepo.findById(num)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Item with ItemNum = " + num));

        // ObjectUrl만 반환
        return new ResponseEntity<>(item.getObjectUrl(), HttpStatus.OK);
    }

}