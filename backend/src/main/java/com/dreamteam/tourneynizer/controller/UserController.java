package com.dreamteam.tourneynizer.controller;

import com.dreamteam.tourneynizer.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @PostMapping("/greeting")

    public ResponseEntity<String> greeting(@RequestParam String email,
                                         @RequestParam String name,
                                         @RequestParam String password) {

        User user = new User(email, name, "");
        user.setPlaintextPassword(password);

        return new ResponseEntity<>("Success", new HttpHeaders(), HttpStatus.OK);
    }
}
