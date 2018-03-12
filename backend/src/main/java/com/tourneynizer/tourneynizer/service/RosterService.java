package com.tourneynizer.tourneynizer.service;

import com.tourneynizer.tourneynizer.dao.RosterDao;
import com.tourneynizer.tourneynizer.error.InternalErrorException;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.User;

import java.sql.SQLException;
import java.util.List;

public class RosterService {

    private final RosterDao rosterDao;

    public RosterService(RosterDao rosterDao) {
        this.rosterDao = rosterDao;
    }

    public void addUser(User user, Team team) {
        rosterDao.registerUser(user, team);
    }

    public List<User> getTeamMembers(Team team) throws InternalErrorException {
        try {
            return rosterDao.getTeamMembers(team);
        } catch (SQLException e) {
            throw new InternalErrorException(e);
        }
    }
}
