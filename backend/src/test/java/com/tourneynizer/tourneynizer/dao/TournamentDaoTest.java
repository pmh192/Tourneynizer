package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.error.EmailTakenException;
import com.tourneynizer.tourneynizer.helper.TestWithContext;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.TournamentType;
import com.tourneynizer.tourneynizer.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.sql.Timestamp;

import static org.junit.Assert.*;

public class TournamentDaoTest extends TestWithContext {

    private final UserDao userDao;
    private final TournamentDao touramentDao;

    public TournamentDaoTest() {
        userDao = super.context.getBean("UserDao", UserDao.class);
        touramentDao = super.context.getBean("TournamentDao", TournamentDao.class);
    }

    @Before
    public void clearDB() {
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "tournaments");
        JdbcTestUtils.deleteFromTables(super.jdbcTemplate, "users");
    }

    @Test
    public void insert() throws Exception {
        User user = new User("person@place.com", "Name", "");
        user.setPlaintextPassword("HI");
        userDao.insert(user);

        Timestamp beforeInsert = new Timestamp(System.currentTimeMillis());
        Tournament tournament = new Tournament("name", "address", null, 1, 1, TournamentType.BRACKET, 1, user.getId());
        touramentDao.insert(tournament);

        assertTrue(tournament.isPersisted());
        assertTrue(tournament.getTimeCreated().after(beforeInsert));
    }
}