package com.tourneynizer.tourneynizer.service;

import com.tourneynizer.tourneynizer.dao.RosterDao;
import com.tourneynizer.tourneynizer.dao.TeamDao;
import com.tourneynizer.tourneynizer.dao.TournamentDao;
import com.tourneynizer.tourneynizer.error.BadRequestException;
import com.tourneynizer.tourneynizer.error.InternalErrorException;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class TeamService {

    private final TeamDao teamDao;
    private final TournamentDao tournamentDao;
    private final RosterDao rosterDao;

    public TeamService(TeamDao teamDao, TournamentDao tournamentDao, RosterDao rosterDao) {
        this.teamDao = teamDao;
        this.tournamentDao = tournamentDao;
        this.rosterDao = rosterDao;
    }

    public Team createTeam(User user, Tournament tournament, Map<String, String> values) throws BadRequestException, InternalErrorException {
        String teamName = values.get("name");

        try {
            Team team = new Team(teamName, user.getId(), tournament.getId());
            teamDao.insert(team, user);
            return team;
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        }
    }

    public Team findById(long id) throws InternalErrorException {
        try {
            return teamDao.findById(id);
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        }
    }

    public List<Team> findByTournament(long id) throws BadRequestException, InternalErrorException {
        Tournament tournament;
        try {
            tournament = tournamentDao.findById(id);
        } catch (SQLException e) { throw new InternalErrorException(e); }

        if (tournament == null) {
            throw new BadRequestException("Couldn't find tournament with id " + id);
        }

        return teamDao.findByTournament(tournament);

    }

    public List<Team> findByTournament(long id, boolean complete) throws BadRequestException, InternalErrorException {
        Tournament tournament;
        try { tournament = tournamentDao.findById(id); }
        catch (SQLException e) { throw new InternalErrorException(e); }

        if (tournament == null) { throw new BadRequestException("Couldn't find tournament with id " + id); }

        return teamDao.findByTournament(tournament, complete);
    }

    public List<Team> getAllWith(User user) {
        return rosterDao.findByUser(user);
    }

    public Team getTeamForTournament(Tournament tournament, User user) throws BadRequestException {
        Team team = teamDao.getTeamForTournament(tournament, user);

        if(team == null) {
            throw new BadRequestException("The user isn't part of any team for this tournament.");
        }

        return team;
    }
}
