package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.helper.TestWithContext;
import com.tourneynizer.tourneynizer.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
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
        Tournament tournament = new Tournament("name", 3.4, 2.3, null, 1, 1, TournamentType.VOLLEYBALL_BRACKET,
                user.getId(), TournamentStatus.CREATED);
        tournamentDao.insert(tournament, user);
        return tournament;
    }

    private Team getTeam(User user, Tournament tournament) throws Exception {
        return getTeam(user, tournament, 0);
    }

    private Team getTeam(User user, Tournament tournament, int i) throws Exception {
        Team team = new Team("name" + i, user.getId(), tournament.getId());
        teamDao.insert(team, user);
        return team;
    }

    @Test
    public void registerUser() throws Exception {
        User creator = getUser(0);
        User user1 = getUser(1);
        User user2 = getUser(2);
        User user3 = getUser(3);
        Tournament tournament = getTournament(creator);
        Team team1 = getTeam(creator, tournament);

        rosterDao.registerUser(user1, team1);

        List<Long> ids = rosterDao.teamRoster(team1);

        List<Long> expected = Arrays.asList(creator.getId(), user1.getId());

        assertEquals(expected, ids);

        rosterDao.registerUser(user2, team1);
        rosterDao.registerUser(user3, team1);

        expected = Arrays.asList(creator.getId(), user1.getId(), user2.getId(), user3.getId());

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

    @Test
    public void findByUser() throws Exception {
        User creator = getUser(1);
        User creator2 = getUser(7);
        User t1 = getUser(2);
        User t2 = getUser(3);
        User user1 = getUser(4);
        User user2 = getUser(5);

        Tournament tournament1 = getTournament(creator);
        Tournament tournament2 = getTournament(creator2);
        Team team1 = getTeam(t1, tournament1, 0);
        Team team2 = getTeam(t2, tournament1, 1);

        Team team4 = getTeam(t1, tournament2, 0);
        Team team5 = getTeam(t2, tournament2, 1);

        rosterDao.registerUser(user1, team1);
        rosterDao.registerUser(user1, team4);
        rosterDao.registerUser(user2, team2);

        List<Team> expected1 = Arrays.asList(team1, team4);
        List<Team> expected2 = Arrays.asList(team2);

        List<Team> actual1 = rosterDao.findByUser(user1);
        List<Team> actual2 = rosterDao.findByUser(user2);

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);

    }
}