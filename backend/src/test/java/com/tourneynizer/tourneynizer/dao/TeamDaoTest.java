package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.helper.TestWithContext;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.TournamentType;
import com.tourneynizer.tourneynizer.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.sql.Timestamp;

import static org.junit.Assert.*;

public class TeamDaoTest extends TestWithContext {

    private final UserDao userDao;
    private final TournamentDao tournamentDao;
    private final TeamDao teamDao;

    public TeamDaoTest() {
        userDao = super.context.getBean("UserDao", UserDao.class);
        tournamentDao = super.context.getBean("TournamentDao", TournamentDao.class);
        teamDao = super.context.getBean("TeamDao", TeamDao.class);
    }

    @Before
    public void clearDB() {
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "teamRequest");
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "sessions");
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "roster");
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "matches");
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "teams");
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "tournaments");
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "users");
    }

    private User getUser(int i) throws Exception {
        User user = new User("person" + i + "@place.com", "Name" + i, "");
        user.setPlaintextPassword("HI" + i);
        userDao.insert(user);
        return user;
    }

    private Tournament getTournament(User user) throws Exception {
        Tournament tournament = new Tournament("name", "address", null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 1, user.getId());
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
}