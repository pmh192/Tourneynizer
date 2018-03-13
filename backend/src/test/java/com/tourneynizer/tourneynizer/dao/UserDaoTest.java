package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.error.EmailTakenException;
import com.tourneynizer.tourneynizer.helper.TestWithContext;
import com.tourneynizer.tourneynizer.model.*;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class UserDaoTest extends TestWithContext {

    private final UserDao userDao;
    private final TournamentDao tournamentDao;
    private final TeamDao teamDao;
    private final RosterDao rosterDao;
    private final UserParticipationDao userParticipationDao;

    public UserDaoTest() {
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

    private User getUser(int i) throws SQLException, EmailTakenException {
        User user = new User("person" + i + "@place.com", "Name", "");
        user.setPlaintextPassword("HI");
        userDao.insert(user);
        return user;
    }

    private Tournament getTournament(User user) throws Exception {
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
        User user = new User("person@place.com", "Name", "");
        user.setPlaintextPassword("HI");

        Timestamp beforeInsert = new Timestamp(System.currentTimeMillis());
        userDao.insert(user);

        assertTrue(user.isPersisted());
        assertFalse(user.getTimeCreated().before(beforeInsert));
    }

    @Test(expected = EmailTakenException.class)
    public void insertDuplicateEmails() throws Exception {
        User user1 = new User("person@place.com", "Name", "");
        User user2 = new User("person@place.com", "Name2", "");

        userDao.insert(user1);
        userDao.insert(user2);
    }

    @Test
    public void retrieve() throws Exception {
        User user = new User("person@place.com", "Name", "");
        user.setPlaintextPassword("HI");
        user.setUserInfo(new UserInfo(5, 4, 6));
        userDao.insert(user);

        User user2 = userDao.findById(user.getId());

        assertEquals(user, user2);
    }

    @Test
    public void getAll() throws Exception {
        User user1 = getUser(1);
        User user2 = getUser(2);

        List<User> users = Arrays.asList(user1, user2);
        List<User> actual = userDao.getAll();

        assertEquals(users, actual);
    }

    @Test
    public void getAllUnregisteredFor() throws Exception {
        User creator = getUser(0);
        User user1 = getUser(1);
        User user2 = getUser(2);
        User user3 = getUser(3);
        User user4 = getUser(4);
        User user5 = getUser(5);

        Tournament tournament = getTournament(user1);
        Team team1 = getTeam(creator, tournament, 0);
        Team team2 = getTeam(user1, tournament, 1);

        userParticipationDao.registerUser(user2.getId(), team1.getTournamentId());
        userParticipationDao.registerUser(user3.getId(), team2.getTournamentId());

        List<User> expected = Arrays.asList(user4, user5);
        List<User> actual = userDao.getAllUnregisteredFor(tournament);

        assertEquals(expected, actual);
    }

    @Test
    public void getAllRegisteredFor() throws Exception {
        User creator = getUser(0);
        User user1 = getUser(1);
        User user2 = getUser(2);
        User user3 = getUser(3);
        User user4 = getUser(4);
        User user5 = getUser(5);

        Tournament tournament = getTournament(user1);
        Team team1 = getTeam(creator, tournament, 0);
        Team team2 = getTeam(user1, tournament, 1);

        userParticipationDao.registerUser(user2.getId(), team1.getTournamentId());
        userParticipationDao.registerUser(user3.getId(), team2.getTournamentId());

        List<User> expected = Arrays.asList(creator, user1, user2, user3);
        List<User> actual = userDao.getAllRegisteredFor(tournament);

        assertEquals(expected, actual);
    }

    @Test
    public void retrieveNull() throws Exception {
        User user = userDao.findById(-1L);
        assertNull(user);
    }

    @Test
    public void findByEmail() throws Exception {
        User user1 = new User("person@place.com", "Name", "");
        userDao.insert(user1);

        User user2 = userDao.findByEmail("person@place.com");

        assertTrue(user1.equals(user2));
    }

    @Test
    public void retrieveNull2() throws Exception {
        User user = userDao.findByEmail("");
        assertNull(user);
    }
}