package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.helper.TestWithContext;
import com.tourneynizer.tourneynizer.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TournamentRequestDaoTest extends TestWithContext {
    private final UserDao userDao;
    private final TournamentDao tournamentDao;
    private final TeamDao teamDao;
    private final TournamentRequestDao tournamentRequestDao;

    public TournamentRequestDaoTest() {
        super();
        userDao = super.context.getBean("UserDao", UserDao.class);
        tournamentDao = super.context.getBean("TournamentDao", TournamentDao.class);
        teamDao = super.context.getBean("TeamDao", TeamDao.class);
        tournamentRequestDao = super.context.getBean("TournamentRequestDao", TournamentRequestDao.class);
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

    private Tournament getTournament(User user, int i) throws Exception {
        Tournament tournament = new Tournament("name" + i, 5.4, 3.4, null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 1, user.getId());
        tournamentDao.insert(tournament, user);
        return tournament;
    }

    private Team getTeam(User user, Tournament tournament, int i) throws Exception {
        Team team = new Team("name" + i, user.getId(), tournament.getId());
        teamDao.insert(team, user);
        return team;
    }

    @Test
    public void insertEquality() throws Exception {
        User user = getUser(0);
        Tournament tournament = getTournament(user, 0);
        Team team = getTeam(user, tournament, 0);

        TournamentRequest request = tournamentRequestDao.requestTournament(tournament, team, user);
        TournamentRequest back = tournamentRequestDao.findById(request.getId());

        assertEquals(back, request);
    }

    @Test
    public void findByTournament() throws Exception {
        User creator = getUser(0);
        User user1 = getUser(1);
        User user2 = getUser(2);
        Tournament tournament = getTournament(creator, 0);
        Team team1 = getTeam(user1, tournament, 0);
        Team team2 = getTeam(user2, tournament, 1);

        TournamentRequest request1 = tournamentRequestDao.requestTournament(tournament, team1, user1);
        TournamentRequest request2 = tournamentRequestDao.requestTournament(tournament, team2, user2);
        List<TournamentRequest> requests = tournamentRequestDao.findByTournament(tournament);

        List<TournamentRequest> expected = Arrays.asList(request1, request2);

        assertEquals(expected, requests);
    }
}