package com.tourneynizer.tourneynizer.service;

import com.tourneynizer.tourneynizer.dao.MatchDao;
import com.tourneynizer.tourneynizer.dao.TeamDao;
import com.tourneynizer.tourneynizer.dao.TournamentDao;
import com.tourneynizer.tourneynizer.error.BadRequestException;
import com.tourneynizer.tourneynizer.error.InternalErrorException;
import com.tourneynizer.tourneynizer.model.*;

import java.sql.SQLException;
import java.util.*;

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

    private Match getMatch(long matchId, long tournamentId) throws BadRequestException, InternalErrorException{
        Match match = getMatch(matchId);
        if (match.getTournamentId() != tournamentId) {
            throw new BadRequestException("That match isn't part of this tournament");
        }

        return match;
    }

    public List<Match> findByTournament(long tournamentId) throws BadRequestException, InternalErrorException{
        return matchDao.findByTournament(getTournament(tournamentId));
    }

    public List<Match> getAllCompleted(long tournamentId) throws BadRequestException, InternalErrorException {
        return matchDao.getCompleted(getTournament(tournamentId));
    }

    private void mustBeRef(Match match, User user) throws BadRequestException, InternalErrorException {
        Team team;
        try { team = teamDao.findById(match.getRefId()); }
        catch (SQLException e) { throw new InternalErrorException(e); }
        if (team == null) { throw new InternalErrorException("Couldn't find team that refId references"); }

        if (team.getCreatorId() != user.getId()) {
            throw new BadRequestException("You must be a the creator of the team refereeing to do that");
        }
    }

    public void startMatch(long tournamentId, long matchId, User user) throws BadRequestException, InternalErrorException{
        Match match = getMatch(matchId, tournamentId);

        mustBeRef(match, user);

        try { matchDao.startMatch(match); }
        catch (IllegalArgumentException e) { throw new BadRequestException(e.getMessage()); }
    }

    private static final String score1Key = "score1";
    private static final String score2Key = "score2";
    private static final Set<String> expectedParams = new HashSet<>(Arrays.asList(score1Key, score2Key));

    public void updateScore(long tournamentId, long matchId, Map<String, String> body, User user) throws
            BadRequestException, InternalErrorException  {

        if (!body.keySet().containsAll(expectedParams)) {
            throw new BadRequestException("Expected body to have values: " + expectedParams);
        }

        long score1, score2;
        try {
            score1 = Long.parseLong(body.get(score1Key));
            score2 = Long.parseLong(body.get(score2Key));
        } catch (NumberFormatException e) {
            throw new BadRequestException(e.getMessage());
        }

        Match match = getMatch(matchId, tournamentId);

        mustBeRef(match, user);

        matchDao.updateScore(match, score1, score2);
    }

    public void endMatch(long tournamentId, long matchId, Map<String, String> body, User user)
            throws BadRequestException, InternalErrorException {

        if (!body.keySet().containsAll(expectedParams)) {
            throw new BadRequestException("Expected body to have values: " + expectedParams);
        }

        long score1, score2;
        try {
            score1 = Long.parseLong(body.get(score1Key));
            score2 = Long.parseLong(body.get(score2Key));
        } catch (NumberFormatException e) {
            throw new BadRequestException(e.getMessage());
        }

        if (score1 == score2) {
            throw new BadRequestException("Game's cannot end in a tie");
        }


        Match match = getMatch(matchId, tournamentId);
        MatchChildren children = match.getMatchChildren();
        long winnerId = score1 > score2 ? children.getTeamChild1() : children.getTeamChild2();

        mustBeRef(match, user);

        matchDao.endMatch(match, winnerId, score1, score2);
    }

    public Long[] getScore(long tournamentId, long matchId) throws BadRequestException, InternalErrorException {
        Match match = getMatch(matchId, tournamentId);

        return matchDao.getScore(match);

    }

    public List<Match> getValid(long tournamentId) throws BadRequestException, InternalErrorException {
        Tournament tournament = getTournament(tournamentId);

        return matchDao.getValidMatches(tournament);
    }

    public Match getRefereeMatch(long tournamentId, User user) throws BadRequestException, InternalErrorException {
        Team team = teamDao.getTeamCreated(tournamentId, user);
        if (team == null) { throw new BadRequestException("You are not the creator of any teams in this tournament"); }

        return matchDao.getMatchToReferee(team);
    }
}
