package com.tourneynizer.tourneynizer.service;

import com.tourneynizer.tourneynizer.dao.RosterDao;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.User;

public class RosterService {

    private final RosterDao rosterDao;

    public RosterService(RosterDao rosterDao) {
        this.rosterDao = rosterDao;
    }

    public void addUser(User user, Team team) {
        rosterDao.registerUser(user, team);
    }
}
