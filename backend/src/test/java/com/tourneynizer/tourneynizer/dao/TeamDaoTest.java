package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.helper.TestWithContext;
import com.tourneynizer.tourneynizer.model.*;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class TeamDaoTest extends TestWithContext {

    private final UserDao userDao;
    private final TournamentDao tournamentDao;
    private final TeamDao teamDao;
    private final RosterDao rosterDao;

    public TeamDaoTest() {
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
        User user = new User("person" + i + "@place.com", "Name" + i, "");
        user.setPlaintextPassword("HI" + i);
        userDao.insert(user);
        return user;
    }

    private Tournament getTournament(User user) throws Exception {
        return getTournament(user, 1);
    }

    private Tournament getTournament(User user, int teamSize) throws Exception {
        Tournament tournament = new Tournament("name", "address", null, teamSize, 1, TournamentType.VOLLEYBALL_BRACKET, 1, user.getId(), TournamentStatus.CREATED);
        tournamentDao.insert(tournament, user);
        return tournament;
    }

    @Test
    public void insert() throws Exception {
        User user = getUser(0);
        Tournament tournament = getTournament(user);

        Timestamp beforeInsert = new Timestamp(System.currentTimeMillis());
        Team team = new Team("name", user.getId(), tournament.getId());
        teamDao.insert(team, user);

        assertTrue(team.isPersisted());
        assertFalse(team.getTimeCreated().before(beforeInsert)); // timeCreated >= beforeInsert
    }

    @Test
    public void insertBadId() throws Exception {
        User user = getUser(1);
        Tournament tournament = getTournament(user);

        Team team1 = new Team("name", -1, tournament.getId());
        Team team2 = new Team("name", user.getId(), -1);
        try { teamDao.insert(team1, user); fail(); } catch (IllegalArgumentException e ) { }
        try { teamDao.insert(team2, user); fail(); } catch (IllegalArgumentException e ) { }
    }

    @Test
    public void insertEquality() throws Exception {
        User user = getUser(1);
        Tournament tournament = getTournament(user);

        Team team = new Team("name", user.getId(), tournament.getId());
        teamDao.insert(team, user);

        Team expected = new Team(team.getId(), "name", team.getTimeCreated(), user.getId(), tournament.getId(), false);
        assertEquals(expected, team);
    }

    // TODO Unique team names per tournament
    @Test(expected = IllegalArgumentException.class)
    public void sameTeamNameSameTournament() throws Exception {
        User user1 = getUser(1);
        User user2 = getUser(2);
        User user3 = getUser(3);
        Tournament tournament = getTournament(user1);

        Team team1 = new Team("name", user2.getId(), tournament.getId());
        teamDao.insert(team1, user2);

        Team team2 = new Team("name", user3.getId(), tournament.getId());
        teamDao.insert(team2, user3);
    }

    @Test
    public void retrieve() throws Exception {
        User user = getUser(1);
        Tournament tournament = getTournament(user);
        Team team = new Team("name", user.getId(), tournament.getId());
        teamDao.insert(team, user);

        Team expected = new Team(team.getId(), "name", team.getTimeCreated(), user.getId(), tournament.getId(), false);
        Team found = teamDao.findById(team.getId());

        assertEquals(expected, found);
    }

    @Test
    public void retrieveNull() throws Exception {
        assertNull(teamDao.findById(-1L));
    }

    @Test
    public void findByTournament() throws Exception {
        User user = getUser(1);
        Tournament tournament = getTournament(user);
        Tournament tournament2 = getTournament(user);
        Team team = new Team("name", user.getId(), tournament.getId());
        Team team2 = new Team("name2", user.getId(), tournament.getId());
        Team team3 = new Team("name2", user.getId(), tournament2.getId());
        teamDao.insert(team, user);
        teamDao.insert(team2, user);
        teamDao.insert(team3, user);

        List<Team> expected = Arrays.asList(team, team2);
        List<Team> found = teamDao.findByTournament(tournament);

        assertEquals(expected, found);
    }

    @Test
    public void findByTournamentComplete() throws Exception {
        User user = getUser(1);
        User user2 = getUser(2);
        User user3 = getUser(3);
        User user4 = getUser(4);
        Tournament tournament = getTournament(user, 2);
        Team team = new Team("name", user.getId(), tournament.getId());
        Team team2 = new Team("name2", user2.getId(), tournament.getId());
        Team team3 = new Team("name3", user3.getId(), tournament.getId());
        teamDao.insert(team, user);
        rosterDao.registerUser(user, team);
        teamDao.insert(team2, user2);
        rosterDao.registerUser(user2, team2);
        teamDao.insert(team3, user3);
        rosterDao.registerUser(user3, team3);

        rosterDao.registerUser(user4, team);

        List<Team> expected = Collections.singletonList(team);
        List<Team> found = teamDao.findByTournament(tournament, true);

        assertEquals(expected, found);
    }

    @Test
    public void findByTournamentIncomplete() throws Exception {
        User user = getUser(1);
        User user2 = getUser(2);
        User user3 = getUser(3);
        User user4 = getUser(4);
        Tournament tournament = getTournament(user, 2);
        assertEquals(tournament.getTeamSize(), 2);

        Team team = new Team("name", user.getId(), tournament.getId());
        Team team2 = new Team("name2", user2.getId(), tournament.getId());
        Team team3 = new Team("name3", user3.getId(), tournament.getId());
        teamDao.insert(team, user);
        rosterDao.registerUser(user, team);
        teamDao.insert(team2, user2);
        rosterDao.registerUser(user2, team2);
        teamDao.insert(team3, user3);
        rosterDao.registerUser(user3, team3);

        rosterDao.registerUser(user4, team);

        List<Team> expected = Arrays.asList(team2, team3);
        List<Team> found = teamDao.findByTournament(tournament, false);

        assertEquals(expected, found);
    }
}