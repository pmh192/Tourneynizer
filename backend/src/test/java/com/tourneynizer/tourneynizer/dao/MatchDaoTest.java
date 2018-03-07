package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.helper.TestWithContext;
import com.tourneynizer.tourneynizer.model.*;
import org.junit.Before;
import org.junit.Test;

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
        super.clearDB();
    }

    private User getUser() throws Exception {
        User user = new User("person@place.com", "Name", "");
        user.setPlaintextPassword("HI");
        userDao.insert(user);
        return user;
    }

    private Tournament getTournament(User user) throws Exception {
        Tournament tournament = new Tournament("name", "address", null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 1,
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
    public void insert() throws Exception {
        User user = getUser();
        Tournament tournament = getTournament(user);
        Team team1 = getTeam(user, tournament, 0);
        Team team2 = getTeam(user, tournament, 1);

        Match match = new Match(tournament.getId(), team1.getId(), team2.getId(), 0, null, ScoreType.ONE_SET);
        matchDao.insert(match, user);
        assertTrue(match.isPersisted());
    }

    @Test
    public void insert2() throws Exception {
        User user = getUser();
        Tournament tournament = getTournament(user);
        Team team1 = getTeam(user, tournament, 0);
        Team team2 = getTeam(user, tournament, 1);

        Match match = new Match(tournament.getId(), 0, team1.getId(), team2.getId(), null, ScoreType.ONE_SET);
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

        Match match1 = new Match(-99L, team1.getId(), team2.getId(), 0, null, ScoreType.ONE_SET);
        Match match2 = new Match(tournament.getId(), -99L, team2.getId(), 0, null, ScoreType.ONE_SET);
        Match match3 = new Match(tournament.getId(), team1.getId(),-99L, 0, null, ScoreType.ONE_SET);
        try { matchDao.insert(match1, user); fail(); } catch (IllegalArgumentException e ) { }
        try { matchDao.insert(match2, user); fail(); } catch (IllegalArgumentException e ) { }
        try { matchDao.insert(match3, user); fail(); } catch (IllegalArgumentException e ) { }
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertSameTeams_12() throws Exception {
        User user = getUser();
        Tournament tournament = getTournament(user);
        Team team = getTeam(user, tournament, 1);
        new Match(tournament.getId(), team.getId(), team.getId(), 0, null, ScoreType.ONE_SET);
    }

    @Test
    public void insertEquality() throws Exception {
        User user = getUser();
        Tournament tournament = getTournament(user);
        Team team1 = getTeam(user, tournament, 1);
        Team team2 = getTeam(user, tournament, 2);

        Match match = new Match(tournament.getId(), team1.getId(), team2.getId(), 0, null, ScoreType.ONE_SET);
        matchDao.insert(match, user);

        Match expected = matchDao.findById(match.getId());

        assertEquals(expected, match);
    }

    @Test
    public void insertEquality2() throws Exception {
        User user = getUser();
        Tournament tournament = getTournament(user);
        Team team1 = getTeam(user, tournament, 1);
        Team team2 = getTeam(user, tournament, 2);

        Match match = new Match(tournament.getId(), 0, team1.getId(), team2.getId(), null, ScoreType.ONE_SET);
        matchDao.insert(match, user);

        Match expected = matchDao.findById(match.getId());

        assertEquals(expected, match);
    }

    @Test
    public void retrieve() throws Exception {
        User user = getUser();
        Tournament tournament = getTournament(user);
        Team team1 = getTeam(user, tournament, 1);
        Team team2 = getTeam(user, tournament, 2);
        Team team3 = getTeam(user, tournament, 3);

        Match match = new Match(tournament.getId(), team1.getId(), team2.getId(), 0, null, ScoreType.ONE_SET);
        matchDao.insert(match, user);

        Match expected = new Match(match.getId(), tournament.getId(), team1.getId(), team2.getId(), null,null, null, 0, 0, null, null, ScoreType.ONE_SET);

        assertEquals(expected, matchDao.findById(match.getId()));
    }

    @Test
    public void retrieveNull() throws Exception {
        assertNull(matchDao.findById(-1L));
    }
}