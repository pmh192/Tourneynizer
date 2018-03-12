package com.tourneynizer.tourneynizer.service;

import com.tourneynizer.tourneynizer.dao.MatchDao;
import com.tourneynizer.tourneynizer.dao.TeamDao;
import com.tourneynizer.tourneynizer.dao.TournamentDao;
import com.tourneynizer.tourneynizer.error.BadRequestException;
import com.tourneynizer.tourneynizer.error.InternalErrorException;
import com.tourneynizer.tourneynizer.model.Match;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.User;

import java.sql.SQLException;
import java.util.List;

public class MatchService {

    private final MatchDao matchDao;
    private final TournamentDao tournamentDao;
    private final TeamDao teamDao;

    public MatchService(MatchDao matchDao, TournamentDao tournamentDao, TeamDao teamDao) {
        this.matchDao = matchDao;
        this.tournamentDao = tournamentDao;
        this.teamDao = teamDao;
    }

    private Tournament getTournament(long tournamentId) throws BadRequestException, InternalErrorException {
        Tournament tournament;
        try {
            tournament = tournamentDao.findById(tournamentId);
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        }

        if (tournament == null) {
            throw new BadRequestException("No tournament found with id: " + tournamentId);
        }

        return tournament;
    }

    private Match getMatch(long matchId) throws BadRequestException, InternalErrorException {
        Match match;
        try {
            match = matchDao.findById(matchId);
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        }

        if (match == null) {
            throw new BadRequestException("No match found with id: " + matchId);
        }

        return match;
    }

    public List<Match> findByTournament(long tournamentId) throws BadRequestException, InternalErrorException{
        return matchDao.findByTournament(getTournament(tournamentId));
    }

    public List<Match> getAllCompleted(long tournamentId) throws BadRequestException, InternalErrorException {
        return matchDao.getCompleted(getTournament(tournamentId));
    }

    public void startMatch(long tournamentId, long matchId, User user) throws BadRequestException, InternalErrorException{
        Match match = getMatch(matchId);

        if (match.getTournamentId() != tournamentId) {
            throw new BadRequestException("That match isn't part of this tournament");
        }

        Team team;
        try { team = teamDao.findById(match.getRefId()); }
        catch (SQLException e) { throw new InternalErrorException(e); }
        if (team == null) { throw new InternalErrorException("Couldn't find team that refId references"); }

        if (team.getCreatorId() != user.getId()) {
            throw new BadRequestException("Only the creator of the referee team can start the match");
        }

        try { matchDao.startMatch(match); }
        catch (IllegalArgumentException e) { throw new BadRequestException(e.getMessage()); }
    }
}
