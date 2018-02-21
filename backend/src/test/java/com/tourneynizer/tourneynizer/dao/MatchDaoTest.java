package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.helper.TestWithContext;
import com.tourneynizer.tourneynizer.model.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.sql.Timestamp;

import static org.junit.Assert.*;

public class MatchDaoTest extends TestWithContext {

    private final UserDao userDao;
    private final TournamentDao tournamentDao;
    private final TeamDao teamDao;
    private final MatchDao matchDao;

    public MatchDaoTest() {
        userDao = super.context.getBean("UserDao", UserDao.class);
        tournamentDao = super.context.getBean("TournamentDao", TournamentDao.class);
        teamDao = super.context.getBean("TeamDao", TeamDao.class);
        matchDao = super.context.getBean("MatchDao", MatchDao.class);
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

    private User getUser() throws Exception {
        User user = new User("person@place.com", "Name", "");
        user.setPlaintextPassword("HI");
        userDao.insert(user);
        return user;
    }

    private Tournament getTournament(User user) throws Exception {
        Tournament tournament = new Tournament("name", "address", null, 1, 1, TournamentType.BRACKET, 1, user.getId());
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
        User user = getUser();
        Tournament tournament = getTournament(user);
        Team team1 = getTeam(user, tournament, 0);
        Team team2 = getTeam(user, tournament, 1);
        Team team3 = getTeam(user, tournament, 2);

        Match match = new Match(tournament.getId(), team1.getId(), team2.getId(), team3.getId(), 0, 0, 0, 0, null, null, ScoreType.ONE_SET);
        matchDao.insert(match, user);
        assertTrue(match.isPersisted());
    }

    @Test
    public void insertBadId() throws Exception {
        User user = getUser();
        Tournament tournament = getTournament(user);
        Team team1 = getTeam(user, tournament, 1);
        Team team2 = getTeam(user, tournament, 2);
        Team team3 = getTeam(user, tournament, 3);

        Match match1 = new Match(-99, team1.getId(), team2.getId(), team3.getId(), 0, 0, 0, 0, null, null, ScoreType.ONE_SET);
        Match match2 = new Match(tournament.getId(), -99, team2.getId(), team3.getId(), 0, 0, 0, 0, null, null, ScoreType.ONE_SET);
        Match match3 = new Match(tournament.getId(), team1.getId(), -99, team3.getId(), 0, 0, 0, 0, null, null, ScoreType.ONE_SET);
        try { matchDao.insert(match1, user); fail(); } catch (IllegalArgumentException e ) { }
        try { matchDao.insert(match2, user); fail(); } catch (IllegalArgumentException e ) { }
        try { matchDao.insert(match3, user); fail(); } catch (IllegalArgumentException e ) { }
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertSameTeams_12() throws Exception {
        User user = getUser();
        Tournament tournament = getTournament(user);
        Team team = getTeam(user, tournament, 1);
        Team ref = getTeam(user, tournament, 2);
        new Match(tournament.getId(), team.getId(), team.getId(), ref.getId(), 0, 0, 0, 0, null, null, ScoreType.ONE_SET);
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertSameTeams_ref() throws Exception {
        User user = getUser();
        Tournament tournament = getTournament(user);
        Team team = getTeam(user, tournament, 1);
        Team team1 = getTeam(user, tournament, 2);
        new Match(tournament.getId(), team.getId(), team1.getId(), team.getId(), 0, 0, 0, 0, null, null, ScoreType.ONE_SET);
    }

    @Test
    public void insertEquality() throws Exception {
        User user = getUser();
        Tournament tournament = getTournament(user);
        Team team1 = getTeam(user, tournament, 1);
        Team team2 = getTeam(user, tournament, 2);
        Team team3 = getTeam(user, tournament, 3);

        Match match = new Match(tournament.getId(), team1.getId(), team2.getId(), team3.getId(), 0, 0, 0, 0, null, null, ScoreType.ONE_SET);
        matchDao.insert(match, user);

        Match expected = new Match(match.getId(), tournament.getId(), team1.getId(), team2.getId(), team3.getId(), 0, 0, 0, 0, null, null, ScoreType.ONE_SET);

        assertEquals(expected, match);
    }

    @Test
    public void retrieve() throws Exception {
        User user = getUser();
        Tournament tournament = getTournament(user);
        Team team1 = getTeam(user, tournament, 1);
        Team team2 = getTeam(user, tournament, 2);
        Team team3 = getTeam(user, tournament, 3);

        Match match = new Match(tournament.getId(), team1.getId(), team2.getId(), team3.getId(), 0, 1, 2, 3, null, null, ScoreType.ONE_SET);
        matchDao.insert(match, user);

        Match expected = new Match(match.getId(), tournament.getId(), team1.getId(), team2.getId(), team3.getId(), 0, 1, 2, 3, null, null, ScoreType.ONE_SET);

        assertEquals(expected, matchDao.findById(match.getId()));
    }

    @Test
    public void retrieveNull() throws Exception {
        assertNull(matchDao.findById(-1L));
    }
}