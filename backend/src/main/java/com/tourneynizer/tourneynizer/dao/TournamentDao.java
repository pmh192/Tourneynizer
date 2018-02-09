package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.error.EmailTakenException;
import com.tourneynizer.tourneynizer.model.Tournament;
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

public class TournamentDao {
    private final JdbcTemplate jdbcTemplate;

    public TournamentDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insert(Tournament tournament) throws EmailTakenException, SQLException {
        if (tournament.isPersisted()) {
            throw new IllegalArgumentException("Tournament is already persisted");
        }

        String sql = "INSERT INTO tournaments (name, address, startTime, teamSize, maxTeams, timeCreated, type, numCourts, creator_id)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        Timestamp now = new Timestamp(System.currentTimeMillis());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            this.jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
                preparedStatement.setString(1, tournament.getName());
                preparedStatement.setString(2, tournament.getAddress());
                preparedStatement.setTimestamp(3, tournament.getStartTime());
                preparedStatement.setInt(4, tournament.getTeamSize());
                preparedStatement.setInt(5, tournament.getMaxTeams());
                preparedStatement.setTimestamp(6, now);
                preparedStatement.setInt(7, tournament.getType().ordinal());
                preparedStatement.setInt(8, tournament.getNumCourts());
                preparedStatement.setLong(9, tournament.getCreatorId());

                return preparedStatement;
            }, keyHolder);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(e);
        }

        tournament.persist((Long) keyHolder.getKey(), now);
    }

    private final RowMapper<User> rowMapper = (resultSet, rowNum) -> new User(
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
}
