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

    private final RowMapper<Tournament> rowMapper = (resultSet, rowNum) -> new Tournament(
            resultSet.getLong(1),
            resultSet.getString(2),
            resultSet.getString(3),
            resultSet.getTimestamp(7),
            resultSet.getTimestamp(4),
            resultSet.getInt(5),
            resultSet.getInt(6),
            TournamentType.values()[resultSet.getInt(8)],
            resultSet.getInt(9),
            resultSet.getLong(10)
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
