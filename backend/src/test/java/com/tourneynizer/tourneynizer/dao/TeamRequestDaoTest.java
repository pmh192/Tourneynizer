package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.helper.TestWithContext;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.TournamentType;
import com.tourneynizer.tourneynizer.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TeamRequestDaoTest extends TestWithContext {
    private final UserDao userDao;
    private final TournamentDao tournamentDao;
    private final TeamDao teamDao;
    private final TeamRequestDao teamRequestDao;

    public TeamRequestDaoTest() {
        super();
        userDao = super.context.getBean("UserDao", UserDao.class);
        tournamentDao = super.context.getBean("TournamentDao", TournamentDao.class);
        teamDao = super.context.getBean("TeamDao", TeamDao.class);
        teamRequestDao = super.context.getBean("TeamRequestDao", TeamRequestDao.class);
    }

    @Before
    public void clearDB() {
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "teamRequest");
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "roster");
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "matches");
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "teams");
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "tournaments");
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "users");
    }

    private User getUser(int i) throws Exception {
        User user = new User("person" + i + "@place.com", "Name", "");
        user.setPlaintextPassword("HI");
        userDao.insert(user);
        return user;
    }

    private Tournament getTournament(User user) throws Exception {
        Tournament tournament = new Tournament("name", "address", null, 1, 1, TournamentType.BRACKET, 1, user.getId());
        tournamentDao.insert(tournament, user);
        return tournament;
    }

    private Team getTeam(User user, Tournament tournament) throws Exception {
        Team team = new Team("name", user.getId(), tournament.getId());
        teamDao.insert(team, user);
        return team;
    }

    @Test
    public void requestUser() throws Exception {
        User creator = getUser(0);
        User user = getUser(1);
        User user2 = getUser(2);
        Tournament tournament = getTournament(creator);
        Team team = getTeam(creator, tournament);

        teamRequestDao.requestUser(user2, team, creator);
        teamRequestDao.requestUser(user, team, creator);

        List<Long> requests = teamRequestDao.getRequests(user);
        List<Long> requests2 = teamRequestDao.getRequests(user2);
        List<Long> expected = Collections.singletonList(team.getId());

        assertEquals(expected, requests);
        assertEquals(expected, requests2);
    }

    //TODO Only allow requestUser if requester is creator of team

    @Test
    public void requestTeam() throws Exception {
        User user = getUser(0);
        User user2 = getUser(1);
        Tournament tournament = getTournament(user);
        Team team = getTeam(user, tournament);

        teamRequestDao.requestTeam(user, team);
        teamRequestDao.requestTeam(user2, team);

        List<Long> requests = teamRequestDao.getRequests(team);
        List<Long> expected = Arrays.asList(user.getId(), user2.getId());

        assertEquals(expected, requests);
    }

    @Test(expected = IllegalArgumentException.class)
    public void requestTeamTwice() throws Exception {
        User user = getUser(1);
        Tournament tournament = getTournament(user);
        Team team = getTeam(user, tournament);

        teamRequestDao.requestTeam(user, team);
        teamRequestDao.requestTeam(user, team);
    }

}