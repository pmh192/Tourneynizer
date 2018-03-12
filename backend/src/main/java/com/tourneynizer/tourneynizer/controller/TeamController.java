package com.tourneynizer.tourneynizer.controller;

import com.tourneynizer.tourneynizer.error.BadRequestException;
import com.tourneynizer.tourneynizer.error.InternalErrorException;
import com.tourneynizer.tourneynizer.model.*;
import com.tourneynizer.tourneynizer.service.RosterService;
import com.tourneynizer.tourneynizer.service.SessionService;
import com.tourneynizer.tourneynizer.service.TeamService;
import com.tourneynizer.tourneynizer.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller("TeamController")
public class TeamController {

    private final TeamService teamService;
    private final TournamentService tournamentService;
    private final SessionService sessionService;
    private final RosterService rosterService;

    @Autowired
    public TeamController(TeamService teamService, TournamentService tournamentService, SessionService sessionService, RosterService rosterService) {
        this.teamService = teamService;
        this.tournamentService = tournamentService;
        this.sessionService = sessionService;
        this.rosterService = rosterService;
    }

    @PostMapping("/api/tournament/{id}/team/create")
    public ResponseEntity<?> findById(@PathVariable("id") long id,
                                      @CookieValue("session") String session,
                                      @RequestBody Map<String, String> values) {
        try {
            Tournament tournament = tournamentService.findById(id);
            User user = sessionService.findBySession(session);
            Team team = teamService.createTeam(user, tournament, values);

            return new ResponseEntity<>(team, new HttpHeaders(), HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/team/getAll")
    public ResponseEntity<?> userIn(@CookieValue("session") String session) {
        try {
            User user = sessionService.findBySession(session);
            List<Team> teams = teamService.getAllWith(user);
            return new ResponseEntity<Object>(teams, new HttpHeaders(), HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/team/{id}")
    public ResponseEntity<?> getTeam(@PathVariable("id") long id) {
        try {
            Team team = teamService.findById(id);
            return new ResponseEntity<Object>(team, new HttpHeaders(), HttpStatus.OK);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/team/{id}/getMembers")
    public ResponseEntity<?> getMembers(@PathVariable("id") long id) {
        try {
            Team team = teamService.findById(id);
            List<User> members = rosterService.getTeamMembers(team);
            return new ResponseEntity<Object>(members, new HttpHeaders(), HttpStatus.OK);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
