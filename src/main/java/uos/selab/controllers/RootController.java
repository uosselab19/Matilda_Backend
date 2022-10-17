package uos.selab.controllers;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = { "http://localhost:3000", "http://3.133.233.81:3000", "http://www.matilda-hanium.click:3000" })
@RestController("/")
@Transactional(readOnly = true)
public class RootController {

    @GetMapping("/")
    public String firstMethod() {
        return "Welcome!";
    }

}