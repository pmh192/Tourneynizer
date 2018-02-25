package com.tourneynizer.tourneynizer.controller;

import com.tourneynizer.tourneynizer.error.BadRequestException;
import com.tourneynizer.tourneynizer.model.ErrorMessage;
import com.tourneynizer.tourneynizer.model.TeamRequest;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.service.SessionService;
import com.tourneynizer.tourneynizer.service.TeamRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller("TeamRequestController")
public class TeamRequestController {

    private final TeamRequestService teamRequestService;
    private final SessionService sessionService;

    @Autowired
    public TeamRequestController(TeamRequestService teamRequestService, SessionService sessionService) {
        this.teamRequestService = teamRequestService;
        this.sessionService = sessionService;
    }


    @PostMapping("/api/team/{id}/request")
    public ResponseEntity<?> sendRequest(@CookieValue("session") String session,
                                         @PathVariable("id") long id) {

        try {
            User user = sessionService.findBySession(session);
            teamRequestService.requestTeam(id, user);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Object>(Collections.emptyMap(), new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/api/team/{teamId}/requests/{requestId}/accept")
    public ResponseEntity<?> acceptRequest(@CookieValue("session") String session,
                                           @PathVariable("teamId") long teamId,
                                           @PathVariable("requestId") long requestId) {

        try {
            User user = sessionService.findBySession(session);
            teamRequestService.acceptUserRequest(user, teamId, requestId);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @GetMapping("/api/user/requests/team")
    public ResponseEntity<?> getTeamRequests(@CookieValue("session") String session) {
        List<TeamRequest> requests;
        try {
            User user = sessionService.findBySession(session);
            requests = teamRequestService.findAllRequestsForUser(user);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Object>(requests, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/api/user/requests/pending")
    public ResponseEntity<?> getUserRequests(@CookieValue("session") String session) {
        List<TeamRequest> requests;
        try {
            User user = sessionService.findBySession(session);
            requests = teamRequestService.findAllRequestsByUser(user);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Object>(requests, new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("/api/requests/{id}")
    public ResponseEntity<?> deleteRequest(@CookieValue("session") String session,
                                           @PathVariable("id") long id) {
        try {
            User user = sessionService.findBySession(session);
            teamRequestService.deleteRequest(user, id);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Object>(Collections.singletonMap("status", "success"), new HttpHeaders(), HttpStatus.OK);
    }
}
