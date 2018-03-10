package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.helper.TestWithContext;
import com.tourneynizer.tourneynizer.model.*;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;

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
        super.clearDB();
    }

    private User getUser(int i) throws Exception {
        User user = new User("person" + i + "@place.com", "Name", "");
        user.setPlaintextPassword("HI");
        userDao.insert(user);
        return user;
    }

    private Tournament getTournament(User user) throws Exception {
        Tournament tournament = new Tournament("name", 4.5, 6.7, null, 1, 1, TournamentType.VOLLEYBALL_BRACKET,
                user.getId(), TournamentStatus.CREATED);
        tournamentDao.insert(tournament, user);
        return tournament;
    }

    private Team getTeam(User user, Tournament tournament, int i) throws Exception {
        Team team = new Team("name" + i, user.getId(), tournament.getId());
        teamDao.insert(team, user);
        return team;
    }

    @Test
    public void requestUser() throws Exception {
        User creator = getUser(0);
        User user = getUser(1);
        User user2 = getUser(2);
        Tournament tournament = getTournament(creator);
        Team team = getTeam(creator, tournament, 0);

        teamRequestDao.requestUser(user2, team, creator);
        teamRequestDao.requestUser(user, team, creator);

        List<Long> requests = teamRequestDao.getRequestIds(user);
        List<Long> requests2 = teamRequestDao.getRequestIds(user2);
        List<Long> expected = Collections.singletonList(team.getId());

        assertEquals(expected, requests);
        assertEquals(expected, requests2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void requestUserNotCreator() throws Exception {
        User creator = getUser(0);
        User user = getUser(1);
        User user2 = getUser(2);

        Tournament tournament = getTournament(creator);
        Team team = getTeam(creator, tournament, 0);

        teamRequestDao.requestUser(user, team, user2);
    }

    @Test
    public void requestTeam() throws Exception {
        User creator = getUser(10);
        User user = getUser(0);
        User user2 = getUser(1);
        Tournament tournament = getTournament(user);
        Team team = getTeam(creator, tournament, 0);

        teamRequestDao.requestTeam(user, team);
        teamRequestDao.requestTeam(user2, team);

        List<Long> requests = teamRequestDao.getRequestIds(team);
        List<Long> expected = Arrays.asList(team.getCreatorId(), team.getCreatorId());

        assertEquals(expected, requests);
    }

    @Test
    public void getRequestsForTeam() throws Exception {
        User creator = getUser(10);
        User user = getUser(0);
        User user2 = getUser(1);
        Tournament tournament = getTournament(user);
        Team team = getTeam(creator, tournament, 0);

        TeamRequest r1 = teamRequestDao.requestUser(user, team, creator);
        TeamRequest r2 = teamRequestDao.requestTeam(user2, team);

        List<TeamRequest> requests = teamRequestDao.getRequestsForTeam(team);
        List<TeamRequest> expected = Collections.singletonList(r2);

        assertEquals(expected, requests);
    }

    @Test
    public void getRequestsForUser() throws Exception {
        User creator = getUser(0);
        User creator2 = getUser(1);
        User user = getUser(2);
        Tournament tournament = getTournament(user);
        Team team1 = getTeam(creator, tournament, 0);
        Team team2 = getTeam(creator2, tournament, 1);

        TeamRequest r1 = teamRequestDao.requestUser(user, team1, creator);
        TeamRequest r2 = teamRequestDao.requestUser(creator, team2, creator2);

        List<TeamRequest> requests = teamRequestDao.getRequestsForUser(creator);
        List<TeamRequest> expected = Collections.singletonList(r2);

        assertEquals(expected, requests);
    }

    @Test
    public void getRequestsByUser() throws Exception {
        User creator = getUser(0);
        User creator2 = getUser(1);
        User user = getUser(2);

        Tournament tournament = getTournament(user);
        Team team1 = getTeam(creator, tournament, 0);
        Team team2 = getTeam(creator2, tournament, 1);

        TeamRequest r1 = teamRequestDao.requestUser(user, team1, creator);
        TeamRequest r2 = teamRequestDao.requestTeam(user, team2);

        List<TeamRequest> requests = teamRequestDao.getRequestsByUser(user);
        List<TeamRequest> expected = Collections.singletonList(r2);

        assertEquals(expected, requests);
    }

    @Test
    public void getRequestsByTeam() throws Exception {
        User creator = getUser(0);
        User creator2 = getUser(1);
        User user = getUser(2);
        Tournament tournament = getTournament(user);
        Team team1 = getTeam(creator, tournament, 0);
        Team team2 = getTeam(creator2, tournament, 1);

        TeamRequest r1 = teamRequestDao.requestUser(user, team1, creator);
        TeamRequest r2 = teamRequestDao.requestTeam(user, team2);

        List<TeamRequest> requests = teamRequestDao.getRequestsByTeam(team1);
        List<TeamRequest> expected = Collections.singletonList(r1);

        assertEquals(expected, requests);
    }

    @Test(expected = IllegalArgumentException.class)
    public void creatorRequestOwnTeam() throws Exception {
        User user = getUser(0);
        Tournament tournament = getTournament(user);
        Team team = getTeam(user, tournament, 0);

        teamRequestDao.requestTeam(user, team);
    }

    @Test(expected = IllegalArgumentException.class)
    public void requestTeamTwice() throws Exception {
        User user = getUser(1);
        Tournament tournament = getTournament(user);
        Team team = getTeam(user, tournament, 0);

        teamRequestDao.requestTeam(user, team);
        teamRequestDao.requestTeam(user, team);
    }

    @Test
    public void deleteRequest() throws Exception {
        User user = getUser(0);
        User user1 = getUser(1);
        Tournament tournament = getTournament(user);
        Team team = getTeam(user, tournament, 0);

        TeamRequest teamRequest = teamRequestDao.requestTeam(user1, team);
        int removed = teamRequestDao.removeRequest(teamRequest);

        assertEquals(1, removed);
        assertNull(teamRequestDao.findById(teamRequest.getId()));
    }

    @Test
    public void deleteRequestMissing() throws Exception {
        TeamRequest teamRequest = new TeamRequest(-1, 2, 3, 2, false, new Timestamp(0L));
        int removed = teamRequestDao.removeRequest(teamRequest);

        assertEquals(0, removed);
    }

    @Test
    public void declineRequest() throws Exception {
        User user = getUser(0);
        User user1 = getUser(1);
        Tournament tournament = getTournament(user);
        Team team = getTeam(user, tournament, 0);

        TeamRequest teamRequest = teamRequestDao.requestTeam(user1, team);
        teamRequestDao.declineRequest(teamRequest);

        TeamRequest gotten = teamRequestDao.findById(teamRequest.getId());

        assertFalse(gotten.isAccepted());
    }

    @Test
    public void declineRequestTwice() throws Exception {
        User user = getUser(0);
        User user1 = getUser(1);
        Tournament tournament = getTournament(user);
        Team team = getTeam(user, tournament, 0);

        TeamRequest teamRequest = teamRequestDao.requestTeam(user1, team);
        teamRequestDao.declineRequest(teamRequest);
        try {
            teamRequestDao.declineRequest(teamRequest);
            fail("Should have thrown");
        }
        catch (IllegalArgumentException e) {
            assertEquals("That request has already been declined", e.getMessage());
        }
    }
}