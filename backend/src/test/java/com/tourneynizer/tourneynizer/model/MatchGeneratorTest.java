package com.tourneynizer.tourneynizer.model;

import com.tourneynizer.tourneynizer.dao.MatchDao;
import com.tourneynizer.tourneynizer.dao.TeamDao;
import com.tourneynizer.tourneynizer.dao.TournamentDao;
import com.tourneynizer.tourneynizer.dao.UserDao;
import com.tourneynizer.tourneynizer.helper.TestWithContext;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
        Tournament tournament = new Tournament("name", 3.4, 6.7, null, 1, 1, TournamentType.VOLLEYBALL_BRACKET,
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
        User user2 = getUser(1);
        Tournament tournament = getTournament(user);

        Team team1 = getTeam(user, tournament, 0);
        Team team2 = getTeam(user2, tournament, 1);

        List<Team> teams = Arrays.asList(team1, team2);

        try {
            generator.createTournamentMatches(teams, user, tournament);
            fail();
        }
        catch (IllegalArgumentException e) {
            assertEquals("Teams cannot ref their own game", e.getMessage());
        }
    }

    @Test
    public void createBracket4() throws Exception {
        MatchGenerator generator = new MatchGenerator(matchDao);
        User user = getUser(0);
        User user2 = getUser(1);
        User user3 = getUser(2);
        User user4 = getUser(3);
        Tournament tournament = getTournament(user);

        Team team1 = getTeam(user, tournament, 0);
        Team team2 = getTeam(user2, tournament, 1);
        Team team3 = getTeam(user3, tournament, 2);
        Team team4 = getTeam(user4, tournament, 3);

        List<Team> teams = Arrays.asList(team1, team2, team3, team4);

        List<Match> actual = generator.createTournamentMatches(teams, user, tournament);

        assertEquals(3, actual.size());

        Match match1 = actual.get(0);
        Match match2 = actual.get(1);
        Match match3 = actual.get(2);

        Set<Long> possibleRefs1 = new HashSet<>(Arrays.asList(team3.getId(), team4.getId()));
        Set<Long> possibleRefs2 = new HashSet<>(Arrays.asList(team1.getId(), team2.getId()));

        assertTrue(possibleRefs1.contains(match1.getRefId()));
        assertTrue(possibleRefs2.contains(match2.getRefId()));

        MatchChildren children = match1.getMatchChildren();
        MatchChildren expected = new MatchChildren(team1.getId(), team2.getId(), null, null);
        assertEquals(expected, children);

        children = match2.getMatchChildren();
        expected = new MatchChildren(team3.getId(), team4.getId(), null, null);
        assertEquals(expected, children);

        children = match3.getMatchChildren();
        expected = new MatchChildren(null, null, match1.getId(), match2.getId());
        assertEquals(expected, children);

        List<Short> rounds = matchDao.findByTournament(tournament)
                .stream()
                .map(Match::getRound)
                .collect(Collectors.toList());

        List<Short> expectedRounds = Arrays.asList((short) 1, (short) 1, (short) 2);
        assertEquals(expectedRounds, rounds);

    }

    @Test
    public void createBracket3() throws Exception {
        MatchGenerator generator = new MatchGenerator(matchDao);
        User user = getUser(0);
        User user2 = getUser(1);
        User user3 = getUser(2);
        Tournament tournament = getTournament(user);

        Team team1 = getTeam(user, tournament, 0);
        Team team2 = getTeam(user2, tournament, 1);
        Team team3 = getTeam(user3, tournament, 2);

        List<Team> teams = Arrays.asList(team1, team2, team3);

        List<Match> actual = generator.createTournamentMatches(teams, user, tournament);

        assertEquals(2, actual.size());

        Match match1 = actual.get(0);
        assertEquals(match1.getRefId(), team3.getId());

        Match finalMatch = actual.get(1);
        MatchChildren children = finalMatch.getMatchChildren();
        assertTrue(children.getKnownTeamChildren().contains(team3.getId()));

        List<Short> rounds = matchDao.findByTournament(tournament)
                .stream()
                .map(Match::getRound)
                .collect(Collectors.toList());

        List<Short> expectedRounds = Arrays.asList((short) 1, (short) 2);
        assertEquals(expectedRounds, rounds);
    }

}