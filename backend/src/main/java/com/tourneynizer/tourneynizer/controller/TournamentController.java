package com.tourneynizer.tourneynizer.controller;

import com.tourneynizer.tourneynizer.dao.SessionDao;
import com.tourneynizer.tourneynizer.error.BadRequestException;
import com.tourneynizer.tourneynizer.error.InternalErrorException;
import com.tourneynizer.tourneynizer.model.ErrorMessage;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.TournamentType;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.service.SessionService;
import com.tourneynizer.tourneynizer.service.TournamentService;
import com.tourneynizer.tourneynizer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Controller("TournamentController")
public class TournamentController {

    private final TournamentService tournamentService;
    private final SessionService sessionService;

    @Autowired
    public TournamentController(TournamentService tournamentService, SessionService sessionService) {
        this.tournamentService = tournamentService;
        this.sessionService = sessionService;
    }

    @PostMapping("/api/tournament/create")
    public ResponseEntity<?> create(@CookieValue("session") String session, @RequestBody Map<String, String> values) {
        User user = sessionService.findBySession(session);
        if (user == null) {
            return new ResponseEntity<Object>(new ErrorMessage("User is not logged in"), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        Tournament tournament;
        try {
            tournament = tournamentService.createTournament(values, user);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(tournament, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/api/tournament/getAll")
    public ResponseEntity<?> getAll() {
        try {
            List<Tournament> tournaments = tournamentService.getAll();
            return new ResponseEntity<>(tournaments, new HttpHeaders(), HttpStatus.OK);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/tournament/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        try {
            Tournament tournament = tournamentService.findById(id);
            return new ResponseEntity<>(tournament, new HttpHeaders(), HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
