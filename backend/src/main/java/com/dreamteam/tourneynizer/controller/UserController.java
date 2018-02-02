package com.dreamteam.tourneynizer.controller;

import com.dreamteam.tourneynizer.error.BadRequestException;
import com.dreamteam.tourneynizer.error.InternalErrorException;
import com.dreamteam.tourneynizer.model.ErrorMessage;
import com.dreamteam.tourneynizer.model.User;
import com.dreamteam.tourneynizer.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Controller("UserController")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/user/create")
    public ResponseEntity<?> create(@RequestBody Map<String, String> values) {

        User user;
        try {
            user = userService.insertUser(values.get("email"), values.get("name"), values.get("password"));
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(user, new HttpHeaders(), HttpStatus.OK);
    }
}
