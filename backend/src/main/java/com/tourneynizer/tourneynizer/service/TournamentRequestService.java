package com.tourneynizer.tourneynizer.service;

import com.tourneynizer.tourneynizer.dao.RosterDao;
import com.tourneynizer.tourneynizer.dao.TeamDao;
import com.tourneynizer.tourneynizer.dao.TournamentDao;
import com.tourneynizer.tourneynizer.dao.TournamentRequestDao;
import com.tourneynizer.tourneynizer.error.BadRequestException;
import com.tourneynizer.tourneynizer.error.InternalErrorException;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.TournamentRequest;
import com.tourneynizer.tourneynizer.model.User;

import java.sql.SQLException;

public class TournamentRequestService {

    private final TournamentRequestDao tournamentRequestDao;
    private final TeamDao teamDao;
    private final RosterDao rosterDao;
    private final TournamentDao tournamentDao;

    public TournamentRequestService(TournamentRequestDao tournamentRequestDao, TeamDao teamDao, RosterDao rosterDao,
                                    TournamentDao tournamentDao) {
        this.tournamentRequestDao = tournamentRequestDao;
        this.teamDao = teamDao;
        this.rosterDao = rosterDao;
        this.tournamentDao = tournamentDao;
    }


    public TournamentRequest requestTournament(long tournamentId, long teamId, User user) throws BadRequestException,
            InternalErrorException {
        Tournament tournament;
        Team team;
        try {
            tournament = tournamentDao.findById(tournamentId);
            team = teamDao.findById(teamId);
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        }

        if (tournament == null) {
            throw new BadRequestException("Couldn't find tournament with id " + tournamentId);
        }
        if (team == null) {
            throw new BadRequestException("Couldn't find team with id " + teamId);
        }
        if (team.getTournamentId() != tournament.getId()) {
            throw new BadRequestException("That team is not playing in that tournament");
        }
        if (team.getCreatorId() != user.getId()) {
            throw new BadRequestException("You do not have permission to make requests for this team");
        }

        return tournamentRequestDao.requestTournament(tournament, team, user);
    }
}
