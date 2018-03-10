package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.helper.TestWithContext;
import com.tourneynizer.tourneynizer.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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

    private User getUser(int i) throws Exception {
        User user = new User("person" + i + "@place.com", "Name", "");
        user.setPlaintextPassword("HI");
        userDao.insert(user);
        return user;
    }

    private Tournament getTournament(User user) throws Exception {
        return getTournament(user, 0);
    }

    private Tournament getTournament(User user, int i) throws Exception {
        Tournament tournament = new Tournament("name", 3.4, 2.3, null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, user.getId(), TournamentStatus.CREATED);
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
        User user2 = getUser(1);
        Tournament tournament = getTournament(user);
        Team team1 = getTeam(user, tournament, 0);
        Team team2 = getTeam(user2, tournament, 1);

        MatchChildren matchChildren = new MatchChildren(team1.getId(), team2.getId(), null, null);
        Match match = new Match(tournament.getId(), matchChildren, 0, null, ScoreType.ONE_SET);
        matchDao.insert(match);
        assertTrue(match.isPersisted());
    }

    @Test
    public void insert2() throws Exception {
        User user = getUser(0);
        User user2 = getUser(1);
        Tournament tournament = getTournament(user);
        Team team1 = getTeam(user, tournament, 0);
        Team team2 = getTeam(user2, tournament, 1);

        MatchChildren matchChildren = new MatchChildren(team1.getId(), team2.getId(), null, null);
        Match match1 = new Match(tournament.getId(), matchChildren, 0, null, ScoreType.ONE_SET);
        Match match2 = new Match(tournament.getId(), matchChildren, 0, null, ScoreType.ONE_SET);
        matchDao.insert(match1);
        matchDao.insert(match2);

        matchChildren = new MatchChildren(null, null, match1.getId(), match2.getId());
        Match match = new Match(tournament.getId(), matchChildren, 0, null, ScoreType.ONE_SET);
        matchDao.insert(match);
    }

    @Test
    public void insertBadId() throws Exception {
        User user = getUser(0);
        User user2 = getUser(1);
        Tournament tournament = getTournament(user);
        Team team1 = getTeam(user, tournament, 1);
        Team team2 = getTeam(user2, tournament, 2);

        MatchChildren children;

        children = new MatchChildren(team1.getId(), team2.getId(), null, null);
        Match match1 = new Match(-99L, children, 0, null, ScoreType.ONE_SET);
        children = new MatchChildren(-99L, team2.getId(), null, null);
        Match match2 = new Match(tournament.getId(), children, 0, null, ScoreType.ONE_SET);
        children = new MatchChildren(team1.getId(), -99L, null, null);
        Match match3 = new Match(tournament.getId(), children, 0, null, ScoreType.ONE_SET);

        try { matchDao.insert(match1 ); fail(); } catch (IllegalArgumentException e ) { }
        try { matchDao.insert(match2); fail(); } catch (IllegalArgumentException e ) { }
        try { matchDao.insert(match3); fail(); } catch (IllegalArgumentException e ) { }
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertSameTeams_12() throws Exception {
        User user = getUser(0);
        Tournament tournament = getTournament(user);
        Team team = getTeam(user, tournament, 1);
        MatchChildren matchChildren = new MatchChildren(team.getId(), team.getId(), null, null);
        new Match(tournament.getId(), matchChildren, 0, null, ScoreType.ONE_SET);
    }

    @Test
    public void insertEquality() throws Exception {
        User user = getUser(0);
        User user2 = getUser(1);
        Tournament tournament = getTournament(user);
        Team team1 = getTeam(user, tournament, 1);
        Team team2 = getTeam(user2, tournament, 2);

        MatchChildren matchChildren = new MatchChildren(team1.getId(), team2.getId(), null, null);
        Match match = new Match(tournament.getId(), matchChildren, 0, null, ScoreType.ONE_SET);
        matchDao.insert(match);

        Match expected = matchDao.findById(match.getId());

        assertEquals(expected, match);
    }

    @Test
    public void insertEquality2() throws Exception {
        User user = getUser(0);
        User user2 = getUser(1);
        Tournament tournament = getTournament(user);
        Team team1 = getTeam(user, tournament, 1);
        Team team2 = getTeam(user2, tournament, 2);

        MatchChildren matchChildren = new MatchChildren(team1.getId(), team2.getId(), null, null);
        Match match1 = new Match(tournament.getId(), matchChildren, 0, null, ScoreType.ONE_SET);
        Match match2 = new Match(tournament.getId(), matchChildren, 0, null, ScoreType.ONE_SET);
        matchDao.insert(match1);
        matchDao.insert(match2);

        matchChildren = new MatchChildren(null, null, match1.getId(), match2.getId());
        Match match = new Match(tournament.getId(), matchChildren, 0, null, ScoreType.ONE_SET);
        matchDao.insert(match);
        Match expected = matchDao.findById(match.getId());

        assertEquals(expected, match);
    }

    @Test
    public void retrieve() throws Exception {
        User user = getUser(0);
        User user2 = getUser(1);
        User user3 = getUser(2);
        Tournament tournament = getTournament(user);
        Team team1 = getTeam(user, tournament, 1);
        Team team2 = getTeam(user2, tournament, 2);
        Team team3 = getTeam(user3, tournament, 3);

        MatchChildren matchChildren = new MatchChildren(team1.getId(), team2.getId(), null, null);
        Match match = new Match(tournament.getId(), matchChildren, 0, null, ScoreType.ONE_SET);
        matchDao.insert(match);

        Match expected = new Match(match.getId(), tournament.getId(), matchChildren, null,null, null, 0, 0, null, null,
                ScoreType.ONE_SET, MatchStatus.CREATED);

        assertEquals(expected, matchDao.findById(match.getId()));
    }

    @Test
    public void retrieveNull() throws Exception {
        assertNull(matchDao.findById(-1L));
    }

    @Test
    public void findByTournament() throws Exception {
        User user = getUser(0);
        User user2 = getUser(1);
        Tournament tournament1 = getTournament(user, 0);
        Tournament tournament2 = getTournament(user2, 1);
        Team team1 = getTeam(user, tournament1, 0);
        Team team2 = getTeam(user2, tournament2, 1);

        MatchChildren matchChildren = new MatchChildren(team1.getId(), team2.getId(), null, null);

        Match match1 = new Match(tournament1.getId(), matchChildren, 0, null, ScoreType.ONE_SET);
        matchDao.insert(match1);

        Match match2 = new Match(tournament1.getId(), matchChildren, 0, null, ScoreType.ONE_SET);
        matchDao.insert(match2);

        Match match3 = new Match(tournament2.getId(), matchChildren, 0, null, ScoreType.ONE_SET);
        matchDao.insert(match3);

        List<Match> expected1 = Arrays.asList(match1, match2);
        List<Match> expected2 = Arrays.asList(match3);

        List<Match> actual1 = matchDao.findByTournament(tournament1);
        List<Match> actual2 = matchDao.findByTournament(tournament2);

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    public void matchLifeCycle() throws Exception {
        User creator = getUser(0);
        User user = getUser(1);
        Tournament tournament = getTournament(creator);
        Team team1 = getTeam(creator, tournament, 0);
        Team team2 = getTeam(user, tournament, 1);

        MatchChildren matchChildren = new MatchChildren(team1.getId(), team2.getId(), null, null);
        Match match1 = new Match(tournament.getId(), matchChildren, 0, null, ScoreType.ONE_SET);
        matchDao.insert(match1);
        assertEquals(match1.getMatchStatus(), MatchStatus.CREATED);
        matchDao.startMatch(match1);
        assertEquals(match1.getMatchStatus(), MatchStatus.STARTED);
        matchDao.endMatch(match1, 5, 3);
        assertEquals(match1.getMatchStatus(), MatchStatus.COMPLETED);
        assertEquals(match1.getScore1().longValue(), 5);
        assertEquals(match1.getScore2().longValue(), 3);
    }

    @Test
    public void getMatchesInStates() throws Exception {

        User creator = getUser(0);
        User user = getUser(1);
        User user2 = getUser(2);
        User user3 = getUser(3);
        User user4 = getUser(4);
        User user5 = getUser(5);
        Tournament tournament = getTournament(creator);
        Team team1 = getTeam(creator, tournament, 0);
        Team team2 = getTeam(user, tournament, 1);
        Team team3 = getTeam(user2, tournament, 2);
        Team team4 = getTeam(user3, tournament, 3);
        Team team5 = getTeam(user4, tournament, 4);
        Team team6 = getTeam(user5, tournament, 5);

        MatchChildren matchChildren = new MatchChildren(team1.getId(), team2.getId(), null, null);
        Match match1 = new Match(tournament.getId(), matchChildren, 0, null, ScoreType.ONE_SET);
        matchDao.insert(match1);

        matchChildren = new MatchChildren(team3.getId(), team4.getId(), null, null);
        Match match2 = new Match(tournament.getId(), matchChildren, 0, null, ScoreType.ONE_SET);
        matchDao.insert(match2);

        matchChildren = new MatchChildren(team5.getId(), team6.getId(), null, null);
        Match match3 = new Match(tournament.getId(), matchChildren, 0, null, ScoreType.ONE_SET);
        matchDao.insert(match3);


        List<Match> expected = Arrays.asList(match1, match2, match3);
        List<Match> actual = matchDao.getUnstarted(tournament);
        assertEquals(expected, actual);

        matchDao.startMatch(match1);

        expected = Arrays.asList(match2, match3);
        actual = matchDao.getUnstarted(tournament);
        assertEquals(expected, actual);

        expected = Arrays.asList(match1);
        actual = matchDao.getInProgress(tournament);
        assertEquals(expected, actual);

        matchDao.endMatch(match1, 4, 5);

        expected = Arrays.asList(match1);
        actual = matchDao.getCompleted(tournament);
        assertEquals(expected, actual);
    }

    @Test
    public void updateScore() throws Exception {
        User creator = getUser(0);
        User creator2 = getUser(1);
        Tournament tournament = getTournament(creator);
        Team team1 = getTeam(creator, tournament, 0);
        Team team2 = getTeam(creator2, tournament, 1);

        MatchChildren matchChildren = new MatchChildren(team1.getId(), team2.getId(), null, null);
        Match match1 = new Match(tournament.getId(), matchChildren, 0, null, ScoreType.ONE_SET);

        matchDao.insert(match1);
        Long score1 = match1.getScore1();
        Long score2 = match1.getScore2();
        assertNull(score1);
        assertNull(score2);

        matchDao.updateScore(match1, 5, 3);
        score1 = match1.getScore1();
        score2 = match1.getScore2();
        assertEquals(score1.longValue(), 5);
        assertEquals(score2.longValue(), 3);
    }
}