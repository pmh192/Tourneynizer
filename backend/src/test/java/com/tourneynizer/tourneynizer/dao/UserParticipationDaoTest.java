package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.helper.TestWithContext;
import com.tourneynizer.tourneynizer.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UserParticipationDaoTest extends TestWithContext {

    private final UserDao userDao;
    private final TournamentDao tournamentDao;
    private final TeamDao teamDao;
    private final RosterDao rosterDao;
    private final UserParticipationDao userParticipationDao;

    public UserParticipationDaoTest() {
        super();
        userDao = super.context.getBean("UserDao", UserDao.class);
        tournamentDao = super.context.getBean("TournamentDao", TournamentDao.class);
        teamDao = super.context.getBean("TeamDao", TeamDao.class);
        rosterDao = super.context.getBean("RosterDao", RosterDao.class);
        userParticipationDao = super.context.getBean("UserParticipationDao", UserParticipationDao.class);
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
        Tournament tournament = new Tournament("name" + i, 3.4, 2.3, null, 1, 1,
                TournamentType.VOLLEYBALL_BRACKET, user.getId(), TournamentStatus.CREATED);
        tournamentDao.insert(tournament, user);
        return tournament;
    }

    private Team getTeam(User user, Tournament tournament, int i) throws Exception {
        Team team = new Team("name" + i, user.getId(), tournament.getId());
        teamDao.insert(team, user);
        return team;
    }

    @Test
    public void test1() throws Exception {
        User user1 = getUser(1);

        // tournamentDao registers creator as member of team
        Tournament tournament = getTournament(user1, 1);
        List<Long> expected = Collections.emptyList();
        List<Long> actual = userParticipationDao.usersParticipatingIn(tournament);
        assertEquals(expected, actual);
    }

    @Test
    public void test2() throws Exception {
        User user1 = getUser(1);
        User user2 = getUser(2);

        Tournament tournament1 = getTournament(user1, 1);
        getTeam(user2, tournament1, 1);



        List<Long> expected = Arrays.asList(user2.getId());
        List<Long> actual = userParticipationDao.usersParticipatingIn(tournament1);
        assertEquals(expected, actual);
    }

    @Test
    public void tournamentsParticipatedBy() throws Exception {
        User user1 = getUser(1);
        User user2 = getUser(2);
        User user3 = getUser(3);
        User user4 = getUser(4);
        User user5 = getUser(5);

        Tournament tournament1 = getTournament(user1, 1);
        Team team1 = getTeam(user1, tournament1, 1);
        rosterDao.registerUser(user2, team1);
        userParticipationDao.registerUser(user2.getId(), team1.getTournamentId());
        rosterDao.registerUser(user4, team1);
        userParticipationDao.registerUser(user4.getId(), team1.getTournamentId());

        Team team2 = getTeam(user3, tournament1, 2);


        List<Long> expected = Arrays.asList(user1.getId(), user2.getId(), user4.getId(), user3.getId());
        List<Long> actual = userParticipationDao.usersParticipatingIn(tournament1);
        assertEquals(expected, actual);
    }

}