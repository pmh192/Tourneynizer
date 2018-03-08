package com.tourneynizer.tourneynizer.model;

import com.tourneynizer.tourneynizer.dao.MatchDao;
import com.tourneynizer.tourneynizer.dao.TeamDao;
import com.tourneynizer.tourneynizer.dao.TournamentDao;
import com.tourneynizer.tourneynizer.dao.UserDao;
import com.tourneynizer.tourneynizer.helper.TestWithContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MatchGeneratorTest extends TestWithContext{

    private final MatchDao matchDao;
    private final UserDao userDao;
    private final TournamentDao tournamentDao;
    private final TeamDao teamDao;

    public MatchGeneratorTest() {
        matchDao = super.context.getBean("MatchDao", MatchDao.class);
        userDao = super.context.getBean("UserDao", UserDao.class);
        tournamentDao = super.context.getBean("TournamentDao", TournamentDao.class);
        teamDao = super.context.getBean("TeamDao", TeamDao.class);
    }

    private User getUser(int i) throws Exception {
        User user = new User("person" + i + "@place.com", "Name", "");
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

    @Before
    public void clearDB() {
        super.clearDB();
    }

    @Test
    public void createBracket2() throws Exception {
        MatchGenerator generator = new MatchGenerator(matchDao);
        User user = getUser(0);
        Tournament tournament = getTournament(user);

        Team team1 = getTeam(user, tournament, 0);
        Team team2 = getTeam(user, tournament, 1);

        List<Team> teams = Arrays.asList(team1, team2);

        List<Match> actual = generator.createTournamentMatches(teams, user, tournament);

        assertEquals(1, actual.size());
        Match match = actual.get(0);
        Set<Long> children = Sets.newSet(match.getChild1(), match.getChild2());
        Set<Long> expected = Sets.newSet(team1.getId(), team2.getId());

        assertEquals(expected, children);
    }

    @Test
    public void createBracket4() throws Exception {
        MatchGenerator generator = new MatchGenerator(matchDao);
        User user = getUser(0);
        Tournament tournament = getTournament(user);

        Team team1 = getTeam(user, tournament, 0);
        Team team2 = getTeam(user, tournament, 1);
        Team team3 = getTeam(user, tournament, 2);
        Team team4 = getTeam(user, tournament, 3);

        List<Team> teams = Arrays.asList(team1, team2, team3, team4);

        List<Match> actual = generator.createTournamentMatches(teams, user, tournament);

        assertEquals(3, actual.size());

        Match match1 = actual.get(0);
        Match match2 = actual.get(1);
        Match match3 = actual.get(2);
        Set<Long> children = Sets.newSet(match1.getChild1(), match1.getChild2(), match2.getChild1(), match2.getChild2());
        Set<Long> expected = Sets.newSet(team1.getId(), team2.getId(), team3.getId(), team4.getId());
        assertEquals(expected, children);

        children = Sets.newSet(match3.getChild1(), match3.getChild2());
        expected = Sets.newSet(match1.getId(), match2.getId());
        assertEquals(expected, children);

    }

    @Test
    public void createBracket3() throws Exception {
        MatchGenerator generator = new MatchGenerator(matchDao);
        User user = getUser(0);
        Tournament tournament = getTournament(user);

        Team team1 = getTeam(user, tournament, 0);
        Team team2 = getTeam(user, tournament, 1);
        Team team3 = getTeam(user, tournament, 2);

        List<Team> teams = Arrays.asList(team1, team2, team3);

        List<Match> actual = generator.createTournamentMatches(teams, user, tournament);

        assertEquals(2, actual.size());
        Match finalMatch = actual.get(1);
        assertTrue(finalMatch.getChild1().equals(team3.getId()) || finalMatch.getChild2().equals(team3.getId()));
    }

}