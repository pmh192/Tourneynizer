package com.tourneynizer.tourneynizer.controller;

import com.tourneynizer.tourneynizer.error.BadRequestException;
import com.tourneynizer.tourneynizer.error.InternalErrorException;
import com.tourneynizer.tourneynizer.model.ErrorMessage;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.service.SessionService;
import com.tourneynizer.tourneynizer.service.TeamRequestService;
import com.tourneynizer.tourneynizer.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@Controller("UserController")
public class UserController {

    private final UserService userService;
    private final SessionService sessionService;
    private final TeamRequestService teamRequestService;

    public UserController(UserService userService, SessionService sessionService, TeamRequestService teamRequestService) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.teamRequestService = teamRequestService;
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

    @GetMapping("/api/user/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        User user;
        try {
            user = userService.findById(id);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(user, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/api/user/find")
    public ResponseEntity<?> getByEmail(@RequestParam String email) {
        User user;
        try {
            user = userService.findByEmail(email);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(user, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/api/user/{id}/sendRequest")
    public ResponseEntity<?> sendRequest(@PathVariable("id") long id,
                                         @CookieValue("session") String session,
                                         @RequestBody Map<String, String> values) {

        try {
            User user = sessionService.findBySession(session);
            teamRequestService.requestTeam(values, user);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Object>(Collections.emptyMap(), new HttpHeaders(), HttpStatus.OK);
    }
}
