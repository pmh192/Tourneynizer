package com.tourneynizer.tourneynizer.controller;

import com.tourneynizer.tourneynizer.error.BadRequestException;
import com.tourneynizer.tourneynizer.error.InternalErrorException;
import com.tourneynizer.tourneynizer.model.ErrorMessage;
import com.tourneynizer.tourneynizer.model.TeamRequest;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.service.SessionService;
import com.tourneynizer.tourneynizer.service.TeamRequestService;
import com.tourneynizer.tourneynizer.service.UserService;
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
    private final UserService userService;

    @Autowired
    public TeamRequestController(TeamRequestService teamRequestService, SessionService sessionService, UserService userService) {
        this.teamRequestService = teamRequestService;
        this.sessionService = sessionService;
        this.userService = userService;
    }


    @PostMapping("/api/team/{id}/request")
    public ResponseEntity<?> requestTeam(@CookieValue("session") String session,
                                         @PathVariable("id") long id) {

        try {
            User user = sessionService.findBySession(session);
            teamRequestService.requestTeam(id, user);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Object>(Collections.emptyMap(), new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/api/user/{userId}/request/team/{teamId}")
    public ResponseEntity<?> requestUser(@CookieValue("session") String session,
                                             @PathVariable("userId") long userId, @PathVariable("teamId") long teamId) {
        try {
            User user = sessionService.findBySession(session);

            User requested = userService.findById(userId);
            if (requested == null) { throw new BadRequestException("No user found with id: " + userId); }

            teamRequestService.requestUser(teamId, requested, user);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Object>(Collections.singletonMap("status", "success"), new HttpHeaders(),
                HttpStatus.OK);
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

    @GetMapping("/api/user/requests/sent")
    public ResponseEntity<?> getRequestsForUser(@CookieValue("session") String session) {
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
    public ResponseEntity<?> getRequestsByUser(@CookieValue("session") String session) {
        List<TeamRequest> requests;
        try {
            User user = sessionService.findBySession(session);
            requests = teamRequestService.findAllRequestsByUser(user);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Object>(requests, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/api/team/{id}/requests/sent")
    public ResponseEntity<?> getRequestsForTeam(@CookieValue("session") String session, @PathVariable("id") long id) {
        List<TeamRequest> requests;
        try {
            User user = sessionService.findBySession(session);
            requests = teamRequestService.findAllRequestsForTeam(id, user);
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
            teamRequestService.declineRequest(user, id);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Object>(Collections.singletonMap("status", "success"), new HttpHeaders(), HttpStatus.OK);
    }
}
