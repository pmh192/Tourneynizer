package com.tourneynizer.tourneynizer.controller;

import com.tourneynizer.tourneynizer.error.BadRequestException;
import com.tourneynizer.tourneynizer.error.InternalErrorException;
import com.tourneynizer.tourneynizer.model.ErrorMessage;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.service.SessionService;
import com.tourneynizer.tourneynizer.service.TeamService;
import com.tourneynizer.tourneynizer.service.TournamentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller("TeamAcceptanceController")
public class TeamAcceptanceController {

    private final TournamentService tournamentService;
    private final SessionService sessionService;
    private final TeamService teamService;

    public TeamAcceptanceController(TournamentService tournamentService, SessionService sessionService, TeamService teamService) {
        this.tournamentService = tournamentService;
        this.sessionService = sessionService;
        this.teamService = teamService;
    }

    @GetMapping("/api/tournament/{tournamentId}/team/all")
    public ResponseEntity<?> getTeams(@PathVariable("tournamentId") long tournamentId) {
        List<Team> teams;

        try {
            teams = teamService.findByTournament(tournamentId);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(teams, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/api/tournament/{tournamentId}/team/complete")
    public ResponseEntity<?> getCompleteTeams(@PathVariable("tournamentId") long tournamentId) {
        List<Team> teams;

        try {
            teams = teamService.findByTournament(tournamentId, true);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(teams, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/api/tournament/{tournamentId}/team/incomplete")
    public ResponseEntity<?> getIncompleteTeams(@PathVariable("tournamentId") long tournamentId) {
        List<Team> teams;

        try {
            teams = teamService.findByTournament(tournamentId, false);
        } catch (BadRequestException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        } catch (InternalErrorException e) {
            return new ResponseEntity<Object>(new ErrorMessage(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(teams, new HttpHeaders(), HttpStatus.OK);
    }
}
