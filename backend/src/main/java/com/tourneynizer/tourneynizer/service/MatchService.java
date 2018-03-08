package com.tourneynizer.tourneynizer.service;

import com.tourneynizer.tourneynizer.dao.MatchDao;
import com.tourneynizer.tourneynizer.dao.TournamentDao;
import com.tourneynizer.tourneynizer.error.BadRequestException;
import com.tourneynizer.tourneynizer.error.InternalErrorException;
import com.tourneynizer.tourneynizer.model.Match;
import com.tourneynizer.tourneynizer.model.Tournament;

import java.sql.SQLException;
import java.util.List;

public class MatchService {

    private final MatchDao matchDao;
    private final TournamentDao tournamentDao;

    public MatchService(MatchDao matchDao, TournamentDao tournamentDao) {
        this.matchDao = matchDao;
        this.tournamentDao = tournamentDao;
    }

    public List<Match> findByTournament(long tournamentId) throws BadRequestException, InternalErrorException{
        Tournament tournament;
        try {
            tournament = tournamentDao.findById(tournamentId);
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        }

        if (tournament == null) {
            throw new BadRequestException("No tournament found with id: " + tournamentId);
        }

        return matchDao.findByTournament(tournament);
    }
}
