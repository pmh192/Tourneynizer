package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.error.EmailTakenException;
import com.tourneynizer.tourneynizer.helper.TestWithContext;
import com.tourneynizer.tourneynizer.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.sql.Timestamp;

import static org.junit.Assert.*;

public class UserDaoTest extends TestWithContext {

    private final UserDao userDao;

    public UserDaoTest() {
        userDao = super.context.getBean("UserDao", UserDao.class);
    }

    @Before
    public void clearDB() {
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "teams");
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "tournaments");
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "users");
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
        User user1 = new User("person@place.com", "Name", "");
        userDao.insert(user1);

        User user2 = userDao.findById(user1.getId());

        assertTrue(user1.equals(user2));
    }

    @Test
    public void retrieveNull() throws Exception {
        User user = userDao.findById(-1L);
        assertNull(user);
    }

}