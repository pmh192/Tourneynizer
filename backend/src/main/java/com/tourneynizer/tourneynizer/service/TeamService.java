package com.tourneynizer.tourneynizer.service;

import com.tourneynizer.tourneynizer.dao.TeamDao;
import com.tourneynizer.tourneynizer.dao.TournamentDao;
import com.tourneynizer.tourneynizer.error.BadRequestException;
import com.tourneynizer.tourneynizer.error.EmailTakenException;
import com.tourneynizer.tourneynizer.error.InternalErrorException;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.User;

import java.sql.SQLException;
import java.util.Map;

public class TeamService {

    private final TeamDao teamDao;
    private final TournamentDao tournamentDao;

    public TeamService(TeamDao teamDao, TournamentDao tournamentDao) {
        this.teamDao = teamDao;
        this.tournamentDao = tournamentDao;
    }

    public Team createTeam(User user, Map<String, String> values) throws BadRequestException, InternalErrorException {
        String tournamentId = values.get("tournamentId");
        String teamName = values.get("name");

        try {
            Team team = new Team(teamName, user.getId(), Long.parseLong(tournamentId));
            teamDao.insert(team, user);
            return team;
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid tournament id: " + tournamentId);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        }
    }
}
