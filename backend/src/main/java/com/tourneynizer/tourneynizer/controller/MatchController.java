package com.tourneynizer.tourneynizer.controller;

import com.tourneynizer.tourneynizer.service.SessionService;
import com.tourneynizer.tourneynizer.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller("MatchController")
public class MatchController {

    private final TournamentService tournamentService;
    private final SessionService sessionService;

    @Autowired
    public MatchController(TournamentService tournamentService, SessionService sessionService) {
        this.tournamentService = tournamentService;
        this.sessionService = sessionService;
    }

    @GetMapping("/api/tournament/{id}/match/getAll")
    public ResponseEntity<?> getAll(@CookieValue("session") String session, @PathVariable("id") long id) {
        return null;
    }
}
