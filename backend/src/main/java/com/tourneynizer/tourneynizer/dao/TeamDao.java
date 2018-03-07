package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.model.Team;
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
import java.util.List;

public class TeamDao {
    private final JdbcTemplate jdbcTemplate;

    public TeamDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insert(Team team, User user) throws SQLException {
        if (team.isPersisted()) {
            throw new IllegalArgumentException("Team is already persisted");
        }
        if (team.getCreatorId() != user.getId()) {
            throw new IllegalArgumentException("tournament creator id and user id do no match: " +
                    team.getCreatorId() + ", " + user.getId());
        }

        String sql = "INSERT INTO teams (name, timeCreated, creator_id, tournament_id, checkedIn)" +
                " VALUES (?, ?, ?, ?, ?);";

        Timestamp now = new Timestamp(System.currentTimeMillis());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            this.jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
                preparedStatement.setString(1, team.getName());
                preparedStatement.setTimestamp(2, now);
                preparedStatement.setLong(3, team.getCreatorId());
                preparedStatement.setLong(4, team.getTournamentId());
                preparedStatement.setBoolean(5, team.isCheckedIn());

                return preparedStatement;
            }, keyHolder);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("unique_team_name_request")) {
                throw new IllegalArgumentException("That name is already taken");
            }
            if (e.getMessage().contains("teams_tournament_id_fkey")) {
                throw new IllegalArgumentException("Can't find tournament with id " + team.getTournamentId());
            }
            throw e;
        }

        team.persist(keyHolder.getKey().longValue(), now);
    }

    static final RowMapper<Team> rowMapper = (resultSet, rowNum) -> new Team(
            resultSet.getLong(1),
            resultSet.getString(2),
            resultSet.getTimestamp(3),
            resultSet.getLong(6),
            resultSet.getLong(4),
            resultSet.getBoolean(5)
    );

    public Team findById(Long id) {
        String sql = "SELECT * FROM teams WHERE id=" + id + ";";
        try {
            return this.jdbcTemplate.queryForObject(sql, rowMapper);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Team> findByTournament(Tournament tournament) {
        String sql = "SELECT * FROM teams WHERE tournament_id=?;";
        return this.jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, tournament.getId());
            return preparedStatement;
        }, rowMapper);
    }

    public List<Team> findByTournament(Tournament tournament, boolean complete) {
        String op = complete ? ">=" : "<";
        String sql = "SELECT * FROM teams WHERE tournament_id=? AND " +
                "(SELECT COUNT(id) FROM roster WHERE team_id=teams.id)" + op + "?;";

        return this.jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, tournament.getId());
            preparedStatement.setInt(2, tournament.getTeamSize());
            return preparedStatement;
        }, rowMapper);
    }
}
