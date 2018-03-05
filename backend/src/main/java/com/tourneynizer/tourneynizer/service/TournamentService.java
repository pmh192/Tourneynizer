package com.tourneynizer.tourneynizer.service;

import com.tourneynizer.tourneynizer.dao.TournamentDao;
import com.tourneynizer.tourneynizer.error.BadRequestException;
import com.tourneynizer.tourneynizer.error.InternalErrorException;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.TournamentType;
import com.tourneynizer.tourneynizer.model.User;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class TournamentService {
    private final TournamentDao tournamentDao;

    public TournamentService(TournamentDao tournamentDao) {
        this.tournamentDao = tournamentDao;
    }

    public Tournament createTournament(Map<String, String> values, User user) throws BadRequestException, InternalErrorException {
        Tournament tournament;
        try {
            tournament = new Tournament(
                    values.get("name"),
                    values.get("address"),
                    new Timestamp(Long.parseLong(values.get("startTime"))),
                    Integer.parseInt(values.get("teamSize")),
                    Integer.parseInt(values.get("maxTeams")),
                    TournamentType.values()[Integer.parseInt(values.get("type"))],
                    Integer.parseInt(values.get("numCourts")),
                    user.getId()
            );
        }
        catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            throw new BadRequestException(e.getMessage(), e);
        }

        try {
            tournamentDao.insert(tournament, user);
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        }

        return tournament;
    }

    public List<Tournament> getAll() throws InternalErrorException {
        try {
            return tournamentDao.getAll();
        } catch (SQLException e ) {
            throw new InternalErrorException(e);
        }
    }

    public Tournament findById(Long id) throws BadRequestException, InternalErrorException {
        Tournament tournament;
        try {
            tournament = tournamentDao.findById(id);
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        }

        if (tournament == null) {
            throw new BadRequestException("Couldn't find tournament with id " + id);
        }

        return tournament;
    }

    public List<Tournament> ownedBy(User user) throws InternalErrorException{
        try {
            return tournamentDao.ownedBy(user);
        }
        catch (SQLException e) {
            throw new InternalErrorException(e);
        }
    }
}
