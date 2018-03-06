package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.helper.TestWithContext;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.TournamentStatus;
import com.tourneynizer.tourneynizer.model.TournamentType;
import com.tourneynizer.tourneynizer.model.User;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TournamentDaoTest extends TestWithContext {

    private final UserDao userDao;
    private final TournamentDao tournamentDao;

    public TournamentDaoTest() {
        userDao = super.context.getBean("UserDao", UserDao.class);
        tournamentDao = super.context.getBean("TournamentDao", TournamentDao.class);
    }

    @Before
    public void clearDB() {
        super.clearDB();
    }

    @Test
    public void insert() throws Exception {
        User user = new User("person@place.com", "Name", "");
        user.setPlaintextPassword("HI");
        userDao.insert(user);

        Timestamp beforeInsert = new Timestamp(System.currentTimeMillis());
        Tournament tournament = new Tournament("name", "address", null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 1, user.getId(), TournamentStatus.CREATED);
        tournamentDao.insert(tournament, user);

        assertTrue(tournament.isPersisted());
        assertFalse(tournament.getTimeCreated().before(beforeInsert)); // timeCreated >= beforeInsert
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertBadUserId() throws Exception {
        User user = new User("person@place.com", "Name", "");
        user.setPlaintextPassword("HI");
        userDao.insert(user);

        Tournament tournament = new Tournament("name", "address", null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 1, user.getId()-1, TournamentStatus.CREATED);
        tournamentDao.insert(tournament, user);
    }

    @Test
    public void insertEquality() throws Exception {
        User user = new User("person@place.com", "Name", "");
        userDao.insert(user);
        Tournament tournament1 = new Tournament("name", "address", null, 1, 1,
                TournamentType.VOLLEYBALL_BRACKET, 1, user.getId(), TournamentStatus.CREATED);

        tournamentDao.insert(tournament1, user);
        Tournament tournament2 = new Tournament(tournament1.getId(),"name", "address", tournament1.getTimeCreated(),null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 1, user.getId(), TournamentStatus.CREATED);

        assertEquals(tournament2, tournament1);
    }

    @Test
    public void retrieve() throws Exception {
        User user = new User("person@place.com", "Name", "");
        userDao.insert(user);
        Tournament tournament1 = new Tournament("name", "address", null, 1, 1,
                TournamentType.VOLLEYBALL_BRACKET, 1, user.getId(), TournamentStatus.CREATED);

        tournamentDao.insert(tournament1, user);
        Tournament tournament2 = new Tournament(tournament1.getId(),"name", "address", tournament1.getTimeCreated(),null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 1, user.getId(), TournamentStatus.CREATED);

        assertEquals(tournament2, tournamentDao.findById(tournament1.getId()));
    }

    @Test
    public void retrieveNull() throws Exception {
        assertNull(tournamentDao.findById(-1L));
    }

    @Test
    public void getAll() throws Exception {
        User user = new User("person@place.com", "Name", "");
        user.setPlaintextPassword("HI");
        userDao.insert(user);

        Tournament tournament1 = new Tournament("name1", "address", null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 1, user.getId(), TournamentStatus.CREATED);
        Tournament tournament2 = new Tournament("name2", "address", null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 1, user.getId(), TournamentStatus.CREATED);
        Tournament tournament3 = new Tournament("name3", "address", null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 1, user.getId(), TournamentStatus.CREATED);
        tournamentDao.insert(tournament1, user);
        tournamentDao.insert(tournament2, user);
        tournamentDao.insert(tournament3, user);

        List<Tournament> expected = Arrays.asList(tournament1, tournament2, tournament3);
        List<Tournament> actual = tournamentDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    public void ownedBy() throws Exception {
        User user = new User("person@place.com", "Name", "");
        user.setPlaintextPassword("HI");
        userDao.insert(user);

        User user2 = new User("person2@place.com", "Name", "");
        user2.setPlaintextPassword("HI");
        userDao.insert(user2);

        Tournament tournament1 = new Tournament("name1", "address", null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 1, user.getId(), TournamentStatus.CREATED);
        Tournament tournament2 = new Tournament("name2", "address", null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 1, user.getId(), TournamentStatus.CREATED);
        Tournament tournament3 = new Tournament("name3", "address", null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 1, user2.getId(), TournamentStatus.CREATED);
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

        Tournament tournament1 = new Tournament("name1", "address", null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 1, user.getId(), TournamentStatus.CREATED);
        Tournament tournament2 = new Tournament("name2", "address", null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 1, user.getId(), TournamentStatus.CREATED);

        tournamentDao.insert(tournament1, user);
        tournamentDao.insert(tournament2, user);

        tournamentDao.startTournament(tournament1);

        assertEquals(tournament1.getStatus(), TournamentStatus.STARTED);
        assertEquals(tournament2.getStatus(), TournamentStatus.CREATED);

        assertEquals(tournamentDao.findById(tournament1.getId()).getStatus(), TournamentStatus.STARTED);
        assertEquals(tournamentDao.findById(tournament2.getId()).getStatus(), TournamentStatus.CREATED);
    }
}