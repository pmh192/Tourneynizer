package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.helper.TestWithContext;
import com.tourneynizer.tourneynizer.model.*;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TournamentDaoTest extends TestWithContext {

    private final UserDao userDao;
    private final TournamentDao tournamentDao;
    private final TeamDao teamDao;

    public TournamentDaoTest() {
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
        Tournament tournament = new Tournament("name", 3.4, 2.3, null, 1, 1, TournamentType.VOLLEYBALL_BRACKET,
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
    public void insert() throws Exception {
        User user = getUser(0);

        Timestamp beforeInsert = new Timestamp(System.currentTimeMillis());
        Tournament tournament = new Tournament("name", 5.4, 3.4, null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, user.getId(), TournamentStatus.CREATED);
        tournamentDao.insert(tournament, user);

        assertTrue(tournament.isPersisted());
        assertFalse(tournament.getTimeCreated().before(beforeInsert)); // timeCreated >= beforeInsert
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertBadUserId() throws Exception {
        User user = getUser(0);

        Tournament tournament = new Tournament("name", 5.4, 3.4, null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, user.getId()-1, TournamentStatus.CREATED);
        tournamentDao.insert(tournament, user);
    }

    @Test
    public void insertEquality() throws Exception {
        User user = getUser(0);
        Tournament tournament1 = new Tournament("name", 5.4, 3.4, null, 1, 1,
                TournamentType.VOLLEYBALL_BRACKET, user.getId(), TournamentStatus.CREATED);

        tournamentDao.insert(tournament1, user);
        Tournament tournament2 = new Tournament(tournament1.getId(),"name", 5.4, 3.4, tournament1.getTimeCreated(),null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, user.getId(), TournamentStatus.CREATED);

        assertEquals(tournament2, tournament1);
    }

    @Test
    public void retrieve() throws Exception {
        User user = getUser(0);
        Tournament tournament1 = new Tournament("name", 5.4, 3.4, null, 1, 1,
                TournamentType.VOLLEYBALL_BRACKET, user.getId(), TournamentStatus.CREATED);

        tournamentDao.insert(tournament1, user);
        Tournament tournament2 = new Tournament(tournament1.getId(),"name", 5.4, 3.4, tournament1.getTimeCreated(),null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, user.getId(), TournamentStatus.CREATED);

        assertEquals(tournament2, tournamentDao.findById(tournament1.getId()));
    }

    @Test
    public void retrieveNull() throws Exception {
        assertNull(tournamentDao.findById(-1L));
    }

    @Test
    public void getAll() throws Exception {
        User user = getUser(0);

        Tournament tournament1 = new Tournament("name1", 5.4, 3.4, null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, user.getId(), TournamentStatus.CREATED);
        Tournament tournament2 = new Tournament("name2", 5.4, 3.4, null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, user.getId(), TournamentStatus.CREATED);
        Tournament tournament3 = new Tournament("name3", 5.4, 3.4, null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, user.getId(), TournamentStatus.CREATED);
        tournamentDao.insert(tournament1, user);
        tournamentDao.insert(tournament2, user);
        tournamentDao.insert(tournament3, user);

        List<Tournament> expected = Arrays.asList(tournament3, tournament2, tournament1);
        List<Tournament> actual = tournamentDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    public void ownedBy() throws Exception {
        User user = getUser(0);
        User user2 = getUser(1);

        Tournament tournament1 = new Tournament("name1", 2.3, 4.5, null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, user.getId(), TournamentStatus.CREATED);
        Tournament tournament2 = new Tournament("name2", 2.3, 4.5, null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, user.getId(), TournamentStatus.CREATED);
        Tournament tournament3 = new Tournament("name3", 2.3, 4.5, null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, user2.getId(), TournamentStatus.CREATED);
        tournamentDao.insert(tournament1, user);
        tournamentDao.insert(tournament2, user);
        tournamentDao.insert(tournament3, user2);

        List<Tournament> expected = Arrays.asList(tournament1, tournament2);
        List<Tournament> expected2 = Arrays.asList(tournament3);
        List<Tournament> actual = tournamentDao.ownedBy(user);
        List<Tournament> actual2 = tournamentDao.ownedBy(user2);

        assertEquals(expected, actual);
        assertEquals(expected2, actual2);
    }

    @Test
    public void startTournament() throws Exception {
        User user = new User("person@place.com", "Name", "");
        user.setPlaintextPassword("HI");
        userDao.insert(user);

        Tournament tournament1 = new Tournament("name1", 2.3, 4.5, null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, user.getId(), TournamentStatus.CREATED);
        Tournament tournament2 = new Tournament("name2", 2.3, 4.5, null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, user.getId(), TournamentStatus.CREATED);

        tournamentDao.insert(tournament1, user);
        tournamentDao.insert(tournament2, user);

        tournamentDao.startTournament(tournament1);

        assertEquals(tournament1.getStatus(), TournamentStatus.STARTED);
        assertEquals(tournament2.getStatus(), TournamentStatus.CREATED);

        assertEquals(tournamentDao.findById(tournament1.getId()).getStatus(), TournamentStatus.STARTED);
        assertEquals(tournamentDao.findById(tournament2.getId()).getStatus(), TournamentStatus.CREATED);
    }

    @Test
    public void startTournament2() throws Exception {
        User user = getUser(0);
        Tournament tournament = getTournament(user);
        getTeam(user, tournament, 0);

        tournamentDao.startTournament(tournament);

        user = userDao.findById(user.getId());
        assertEquals(1, user.getUserInfo().tournaments);
    }
}