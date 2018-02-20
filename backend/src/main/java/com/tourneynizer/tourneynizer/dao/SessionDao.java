package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.keygen.KeyGenerators;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class SessionDao {
    private final JdbcTemplate jdbcTemplate;

    public SessionDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String createSession(User user) throws SQLException {
        String session = KeyGenerators.string().generateKey();
        String sql = "INSERT INTO sessions (user_id, session) VALUES (?, ?);";
        this.jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setString(2, session);

            return preparedStatement;
        });

        return session;
    }

    public User findBySession(String session) {
        if (session == null) { return null; }

        String sql = "SELECT users.* FROM users INNER JOIN sessions ON users.id=sessions.user_id WHERE " +
                "sessions.session=?;";
        try {
            return this.jdbcTemplate.queryForObject(sql, new Object[]{session}, UserDao.rowMapper);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }



    public void destroySession(String session) throws SQLException {
        String sql = "DELETE FROM sessions WHERE session=?;";
        this.jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, session);

            return preparedStatement;
        });
    }
}

