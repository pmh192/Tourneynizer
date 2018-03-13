package com.tourneynizer.tourneynizer.controller;

import com.tourneynizer.tourneynizer.error.BadRequestException;
import com.tourneynizer.tourneynizer.error.InternalErrorException;
import com.tourneynizer.tourneynizer.model.ErrorMessage;
import com.tourneynizer.tourneynizer.model.Match;
import com.tourneynizer.tourneynizer.model.MatchStatus;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.service.MatchService;
import com.tourneynizer.tourneynizer.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller("MatchController")
public class MatchController {

    private final MatchService matchService;
    private final SessionService sessionService;

    @Autowired
    public MatchController(MatchService matchService, SessionService sessionService) {
        this.matchService = matchService;
        this.sessionService = sessionService;
    }

    @GetMapping("/api/tournament/{id}/match/getAll")
    public ResponseEntity<?> getAll(@CookieValue("session") String session, @PathVariable("id") long id) {
        try {
            List<Match> matches = matchService.findByTournament(id);
            return new ResponseEntity<Object>(matches, new HttpHeaders(), HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/tournament/{id}/match/getCompleted")
    public ResponseEntity<?> getCompleted(@PathVariable("id") long tournamentId) {
        try {
            List<Match> matches = matchService.getAllCompleted(tournamentId);
            return new ResponseEntity<Object>(matches, new HttpHeaders(), HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/tournament/{tournamentId}/match/{matchID}/start")
    public ResponseEntity<?> startMatch(@CookieValue("session") String session,
                                        @PathVariable("tournamentId") long tournamentId,
                                        @PathVariable("matchID") long matchId) {

        try {
            User user = sessionService.findBySession(session);
            matchService.startMatch(tournamentId, matchId, user);
            return new ResponseEntity<Object>(Collections.singletonMap("status", "success"), new HttpHeaders(),
                    HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/tournament/{tournamentId}/match/{matchID}/updateScore")
    public ResponseEntity<?> updateScore(@CookieValue("session") String session,
                                         @PathVariable("tournamentId") long tournamentId,
                                         @PathVariable("matchID") long matchId,
                                         @RequestBody Map<String, String> body) {

        try {
            User user = sessionService.findBySession(session);
            matchService.updateScore(tournamentId, matchId, body, user);
            return new ResponseEntity<Object>(Collections.singletonMap("status", "success"), new HttpHeaders(),
                    HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/tournament/{tournamentId}/match/{matchID}/getScore")
    public ResponseEntity<?> getScore(@PathVariable("tournamentId") long tournamentId,
                                      @PathVariable("matchID") long matchId) {

        try {
            Long[] score = matchService.getScore(tournamentId, matchId);
            return new ResponseEntity<Object>(score, new HttpHeaders(), HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/tournament/{tournamentId}/match/{matchID}/end")
    public ResponseEntity<?> endMatch(@CookieValue("session") String session,
                                      @PathVariable("tournamentId") long tournamentId,
                                      @PathVariable("matchID") long matchId,
                                      @RequestBody Map<String, String> body) {

        try {
            User user = sessionService.findBySession(session);
            matchService.endMatch(tournamentId, matchId, body, user);
            return new ResponseEntity<Object>(Collections.singletonMap("status", "success"), new HttpHeaders(),
                    HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/tournament/{id}/match/valid")
    public ResponseEntity<?> getValidMatches(@PathVariable("id") long tournamentId) {
        try {
            List<Match> matches = matchService.getValid(tournamentId);
            return new ResponseEntity<Object>(matches, new HttpHeaders(), HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/tournament/{id}/match/getRefereeMatch")
    public ResponseEntity<?> getRefereeMatch(@CookieValue("session") String session, @PathVariable("id") long id) {
        try {
            User user = sessionService.findBySession(session);
            Match match = matchService.getRefereeMatch(id, user);
            return new ResponseEntity<Object>(match, new HttpHeaders(), HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static final Map<String, Object> toReturn = getToReturn();

    private static Map<String, Object> getToReturn() {
        Map<String, Object> map = new HashMap<>();

        map.put("actual", MatchStatus.values());

        Map<MatchStatus, String> converter = MatchStatus.getStringMap();
        map.put("user", Arrays.stream(MatchStatus.values())
                .map(converter::get)
                .collect(Collectors.toList())
        );
        return map;
    }

    @GetMapping("/api/enum/match/status")
    public ResponseEntity<?> getEnum() {
        return new ResponseEntity<Object>(toReturn, new HttpHeaders(), HttpStatus.OK);
    }

}
