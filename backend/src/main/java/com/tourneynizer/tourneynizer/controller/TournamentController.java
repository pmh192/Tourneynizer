package com.tourneynizer.tourneynizer.controller;

import com.tourneynizer.tourneynizer.error.BadRequestException;
import com.tourneynizer.tourneynizer.error.InternalErrorException;
import com.tourneynizer.tourneynizer.model.ErrorMessage;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.TournamentType;
import com.tourneynizer.tourneynizer.model.TournamentStatus;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.service.SessionService;
import com.tourneynizer.tourneynizer.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
        Tournament tournament;
        try {
            User user = sessionService.findBySession(session);
            tournament = tournamentService.createTournament(values, user);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(tournament, new HttpHeaders(), HttpStatus.OK);
    }

    private static String[] typeValues;

    // made public so I can test it
    public synchronized static String[] getTournamentTypeValues() {
        if (typeValues == null) {
            TournamentType[] types = TournamentType.values();
            typeValues = new String[types.length];
            for (int i = 0; i < types.length; i++) {
                typeValues[i] = types[i].toString();
            }
        }
        return typeValues;
    }

    @GetMapping("/api/enum/tournament/type")
    public ResponseEntity<?> getTournamentTypes() {
        return new ResponseEntity<Object>(getTournamentTypeValues(), new HttpHeaders(), HttpStatus.OK);
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

    @GetMapping("/api/tournament/getAllCreated")
    public ResponseEntity<?> getAllCreated(@CookieValue("session") String session) {
        try {
            User user = sessionService.findBySession(session);
            List<Tournament> tournaments = tournamentService.ownedBy(user);
            return new ResponseEntity<Object>(tournaments, new HttpHeaders(), HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/tournament/{id}/start")
    public ResponseEntity<?> startTournament(@CookieValue("session") String session, @PathVariable("id") long id) {
        try {
            User user = sessionService.findBySession(session);
            tournamentService.startTournament(id, user);

            Map<String, String> status = Collections.singletonMap("status", "success");
            return new ResponseEntity<Object>(status, new HttpHeaders(), HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static TournamentStatus[] values;
    private synchronized static TournamentStatus[] getValues() {
        if (values== null) {
            values = TournamentStatus.values();
        }
        return values;
    }

    @GetMapping("/api/enum/tournament/status")
    public ResponseEntity<?> tournamentStatus() {
        return new ResponseEntity<Object>(getValues(), new HttpHeaders(), HttpStatus.OK);
    }
}
