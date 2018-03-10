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
        Tournament tournament = new Tournament("name", 2.4, 5.3, null, teamSize, 1, TournamentType.VOLLEYBALL_BRACKET, user.getId(), TournamentStatus.CREATED);
        tournamentDao.insert(tournament, user);
        return tournament;
    }

    private Team getTeam(User user, Tournament tournament, int i) throws Exception {
        Team team = new Team("name" + i, user.getId(), tournament.getId());
        teamDao.insert(team, user);
        return team;
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
        Team team = getTeam(user, tournament, 0);

        Team expected = new Team(team.getId(), "name0", team.getTimeCreated(), user.getId(), tournament.getId(), false);
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
        User user2 = getUser(2);
        Tournament tournament = getTournament(user);
        Tournament tournament2 = getTournament(user);
        Team team1 = getTeam(user, tournament, 0);
        Team team2 = getTeam(user2, tournament, 1);
        Team team3 = getTeam(user, tournament2, 1);

        List<Team> expected = Arrays.asList(team1, team2);
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

        Team team1 = getTeam(user, tournament, 0);
        Team team2 = getTeam(user2, tournament, 1);
        Team team3 = getTeam(user3, tournament, 2);

        rosterDao.registerUser(user4, team1);

        List<Team> expected = Collections.singletonList(team1);
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

        Team team1 = getTeam(user, tournament, 0);
        Team team2 = getTeam(user2, tournament, 1);
        Team team3 = getTeam(user3, tournament, 2);

        rosterDao.registerUser(user4, team1);

        List<Team> expected = Arrays.asList(team2, team3);
        List<Team> found = teamDao.findByTournament(tournament, false);

        assertEquals(expected, found);
    }
}