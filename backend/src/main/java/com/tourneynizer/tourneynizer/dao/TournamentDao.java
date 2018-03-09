package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.TournamentType;
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
import java.util.List;

public class TournamentDao {
    private final JdbcTemplate jdbcTemplate;

    public TournamentDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insert(Tournament tournament, User user) throws SQLException {
        if (tournament.isPersisted()) {
            throw new IllegalArgumentException("Tournament is already persisted");
        }
        if (tournament.getCreatorId() != user.getId()) {
            throw new IllegalArgumentException("tournament creator id and user id do no match: " +
                    tournament.getCreatorId() + ", " + user.getId());
        }

        String sql = "INSERT INTO tournaments (name, lat, lng, startTime, teamSize, maxTeams, timeCreated, type, numCourts, creator_id)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        Timestamp now = new Timestamp(System.currentTimeMillis());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            this.jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
                preparedStatement.setString(1, tournament.getName());
                preparedStatement.setDouble(2, tournament.getLat());
                preparedStatement.setDouble(3, tournament.getLng());
                preparedStatement.setTimestamp(4, tournament.getStartTime());
                preparedStatement.setInt(5, tournament.getTeamSize());
                preparedStatement.setInt(6, tournament.getMaxTeams());
                preparedStatement.setTimestamp(7, now);
                preparedStatement.setInt(8, tournament.getType().ordinal());
                preparedStatement.setInt(9, tournament.getNumCourts());
                preparedStatement.setLong(10, tournament.getCreatorId());

                return preparedStatement;
            }, keyHolder);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(e);
        }

        tournament.persist((Long) keyHolder.getKey(), now);
    }

    private final RowMapper<Tournament> rowMapper = (resultSet, rowNum) -> new Tournament(
            resultSet.getLong(1),
            resultSet.getString(2),
            resultSet.getDouble(10),
            resultSet.getDouble(11),//lng
            resultSet.getTimestamp(6),
            resultSet.getTimestamp(3),
            resultSet.getInt(4),
            resultSet.getInt(5),
            TournamentType.values()[resultSet.getInt(7)],
            resultSet.getInt(8),
            resultSet.getLong(9)
    );

    public Tournament findById(Long id) throws SQLException {
        String sql = "SELECT * FROM tournaments WHERE id=" + id + ";";
        try {
            return this.jdbcTemplate.queryForObject(sql, rowMapper);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Tournament> getAll() throws SQLException {
        String sql = "SELECT * FROM tournaments;";
        return this.jdbcTemplate.query(sql, rowMapper);
    }
}
