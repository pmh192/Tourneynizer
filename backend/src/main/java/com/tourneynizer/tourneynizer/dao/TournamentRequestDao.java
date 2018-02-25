package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.model.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class TournamentRequestDao {
    private final JdbcTemplate jdbcTemplate;

    public TournamentRequestDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private TournamentRequest insert(long team_id, long tournament_id, long requester_id) {
        String sql = "INSERT INTO tournamentRequest (team_id, tournament_id, timeRequested, requester_id)" +
                " VALUES (?, ?, ?, ?);";

        Timestamp now = new Timestamp(System.currentTimeMillis());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {

            this.jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
                preparedStatement.setLong(1, team_id);
                preparedStatement.setLong(2, tournament_id);
                preparedStatement.setTimestamp(3, now);
                preparedStatement.setLong(4, requester_id);

                return preparedStatement;
            }, keyHolder);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("duplicate key value violates unique constraint \"unique_team_tournament_request\"")) {
                throw new IllegalArgumentException("That request has already been made");
            }
            throw e;
        }

        long id = keyHolder.getKey().longValue();
        return new TournamentRequest(id, team_id, tournament_id, requester_id, false, now);
    }

    public TournamentRequest requestTournament(Tournament tournament, Team team, User requester) {
        if (team.getCreatorId() != requester.getId()) {
            throw new IllegalArgumentException("Only the creator of a team can request to join tournaments");
        }
        return insert(team.getId(), tournament.getId(), requester.getId());
    }

    private final RowMapper<TournamentRequest> teamRequestRowMapper = ((resultSet, i) -> {
        long id = resultSet.getLong(1);
        long tournamentId = resultSet.getLong(2);
        long teamId = resultSet.getLong(3);
        Timestamp timeRequested = resultSet.getTimestamp(4);
        boolean accepted = resultSet.getBoolean(5);
        long requesterId = resultSet.getLong(6);

        return new TournamentRequest(id, teamId, tournamentId, requesterId, accepted, timeRequested);
    });

    public TournamentRequest findById(long id) {
        String sql = "SELECT * FROM tournamentRequest WHERE id=?;";
        try {
            return this.jdbcTemplate.queryForObject(sql, new Object[]{id}, teamRequestRowMapper);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
