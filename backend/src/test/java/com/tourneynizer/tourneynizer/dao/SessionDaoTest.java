package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.helper.TestWithContext;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.TournamentType;
import com.tourneynizer.tourneynizer.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

public class SessionDaoTest extends TestWithContext {
    private final UserDao userDao;
    private final SessionDao sessionDao;

    public SessionDaoTest() {
        super();
        userDao = super.context.getBean("UserDao", UserDao.class);
        sessionDao = super.context.getBean("SessionDao", SessionDao.class);
    }

    @Before
    public void clearDB() {
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "teamRequest");
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "roster");
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "matches");
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "teams");
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "tournaments");
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "sessions");
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "users");
    }

    private User getUser(int i) throws Exception {
        User user = new User("person" + i + "@place.com", "Name" + i, "");
        user.setPlaintextPassword("HI" + i);
        userDao.insert(user);
        return user;
    }

    @Test
    public void createSession() throws Exception {
        User user = getUser(0);
        String session = sessionDao.createSession(user);

        User auth = sessionDao.findBySession(session);

        assertEquals(user, auth);
    }

    @Test
    public void testNull() throws Exception {
        assertNull(sessionDao.findBySession(""));
    }

    @Test
    public void destroySession() throws Exception {
        User user = getUser(0);
        User user2 = getUser(1);
        String session = sessionDao.createSession(user);
        String session2 = sessionDao.createSession(user2);

        sessionDao.destroySession(session);

        assertEquals(user2, sessionDao.findBySession(session2));
        assertNull(sessionDao.findBySession(session));
    }
}