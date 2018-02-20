package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.error.EmailTakenException;
import com.tourneynizer.tourneynizer.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Precondition: The user to be inserted isn't already persisted.
     * Postcondition: The user has been persisted, meaning it has an id
     *
     * @param user The user to insert
     * @throws EmailTakenException  If the email is taken
     * @throws SQLException If there was a problem persisting the user
     */
    public void insert(User user) throws EmailTakenException, SQLException {
        if (user.isPersisted()) {
            throw new IllegalArgumentException("User is already persisted");
        }

        String sql = "INSERT INTO users (email, name, password, timeCreated)" +
                " VALUES (?, ?, ?, ?);";

        Timestamp now = new Timestamp(System.currentTimeMillis());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            this.jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
                preparedStatement.setString(1, user.getEmail());
                preparedStatement.setString(2, user.getName());
                preparedStatement.setString(3, user.getHashedPassword());
                preparedStatement.setTimestamp(4, now);

                return preparedStatement;
            }, keyHolder);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("duplicate key value violates unique constraint \"users_email_key\"")) {
                throw new EmailTakenException("That email is taken", e);
            }
            throw new IllegalArgumentException(e);
        }

        user.persist((Long) keyHolder.getKey(), now);
    }

    static final RowMapper<User> rowMapper = (resultSet, rowNum) -> new User(
            resultSet.getLong(1),
            resultSet.getString(2),
            resultSet.getString(3),
            resultSet.getString(4),
            resultSet.getTimestamp(5)
    );

    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id=" + id + ";";
        try {
            return this.jdbcTemplate.queryForObject(sql, rowMapper);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email=?;";
        try {
            return this.jdbcTemplate.queryForObject(sql, new Object[]{email}, rowMapper);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}

