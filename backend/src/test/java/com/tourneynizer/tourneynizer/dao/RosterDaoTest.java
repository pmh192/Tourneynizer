package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.helper.TestWithContext;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.TournamentType;
import com.tourneynizer.tourneynizer.model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RosterDaoTest extends TestWithContext {
    private final UserDao userDao;
    private final TournamentDao tournamentDao;
    private final TeamDao teamDao;
    private final RosterDao rosterDao;

    public RosterDaoTest() {
        super();
        userDao = super.context.getBean("UserDao", UserDao.class);
        tournamentDao = super.context.getBean("TournamentDao", TournamentDao.class);
        teamDao = super.context.getBean("TeamDao", TeamDao.class);
        rosterDao = super.context.getBean("RosterDao", RosterDao.class);
    }

    @Before
    public void clearDB() {
        super.clearDB();
    }

    private User getUser(int i) throws Exception {
        User user = new User("person" + i + "@place.com", "Name", "");
        user.setPlaintextPassword("HI");
        userDao.insert(user);
        return user;
    }

    private Tournament getTournament(User user) throws Exception {
        Tournament tournament = new Tournament("name", "address", null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, user.getId());
        tournamentDao.insert(tournament, user);
        return tournament;
    }

    private Team getTeam(User user, Tournament tournament) throws Exception {
        Team team = new Team("name", user.getId(), tournament.getId());
        teamDao.insert(team, user);
        return team;
    }

    @Test
    public void registerUser() throws Exception {
        User user = getUser(1);
        User user2 = getUser(2);
        User user3 = getUser(3);
        Tournament tournament = getTournament(user);
        Team team1 = getTeam(user, tournament);

        rosterDao.registerUser(user, team1);

        List<Long> ids = rosterDao.teamRoster(team1);

        List<Long> expected = new ArrayList<>();
        expected.add(user.getId());

        assertEquals(expected, ids);

        rosterDao.registerUser(user2, team1);
        rosterDao.registerUser(user3, team1);

        expected.add(user2.getId());
        expected.add(user3.getId());

        assertEquals(expected, rosterDao.teamRoster(team1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerUserTwice() throws Exception {
        User user = getUser(1);
        Tournament tournament = getTournament(user);
        Team team = getTeam(user, tournament);

        rosterDao.registerUser(user, team);
        rosterDao.registerUser(user, team);
    }
}