package com.tourneynizer.tourneynizer.controller;

import com.tourneynizer.tourneynizer.error.BadRequestException;
import com.tourneynizer.tourneynizer.error.InternalErrorException;
import com.tourneynizer.tourneynizer.model.ErrorMessage;
import com.tourneynizer.tourneynizer.model.TournamentRequest;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.service.SessionService;
import com.tourneynizer.tourneynizer.service.TournamentRequestService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller("TournamentRequestController")
public class TournamentRequestController {

    private final TournamentRequestService tournamentRequestService;
    private final SessionService sessionService;

    public TournamentRequestController(TournamentRequestService tournamentRequestService, SessionService sessionService) {
        this.tournamentRequestService = tournamentRequestService;
        this.sessionService = sessionService;
    }

    @PostMapping("/api/tournament/{tournamentId}/team/{teamId}/request")
    public ResponseEntity<?> requestTournament(@PathVariable("tournamentId") long tournamentId,
                                               @PathVariable("teamId") long teamId,
                                               @CookieValue("session") String session) {
        TournamentRequest tournamentRequest;

        try {
            User user = sessionService.findBySession(session);
            tournamentRequest = tournamentRequestService.requestTournament(tournamentId, teamId, user);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(tournamentRequest, new HttpHeaders(), HttpStatus.OK);
    }

//    @PostMapping("/api/tournament/{tournamentID}/requests/{requestId}/accept")
//    public ResponseEntity<?> acceptRequest(@PathVariable("tournamentId") long tournamentId,
//                                               @PathVariable("requestId") long requestId,
//                                               @CookieValue("session") String session) {
//
//        try {
//            User user = sessionService.findBySession(session);
//            tournamentRequestService.acceptRequest(tournamentId, requestId, user);
//        } catch (BadRequestException e) {
//            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
//        } catch (InternalErrorException e) {
//            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        return new ResponseEntity<>(Collections.singletonMap("status", "success"), new HttpHeaders(), HttpStatus.OK);
//    }

    @GetMapping("/api/tournament/{tournamentID}/requests")
    public ResponseEntity<?> getRequests(@PathVariable("tournamentId") long tournamentId,
                                               @CookieValue("session") String session) {
        List<TournamentRequest> requests;

        try {
            User user = sessionService.findBySession(session);
            requests = tournamentRequestService.getRequests(tournamentId, user);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(requests, new HttpHeaders(), HttpStatus.OK);
    }
}
