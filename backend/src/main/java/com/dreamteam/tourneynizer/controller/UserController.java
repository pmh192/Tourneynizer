package com.dreamteam.tourneynizer.controller;

import com.dreamteam.tourneynizer.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {

    @PostMapping("/greeting")
    public ResponseEntity<User> greeting(@RequestBody User value) {
        return new ResponseEntity<>(value, new HttpHeaders(), HttpStatus.OK);
    }
}
