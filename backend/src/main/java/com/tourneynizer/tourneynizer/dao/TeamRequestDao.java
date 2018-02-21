package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.TeamRequest;
import com.tourneynizer.tourneynizer.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

public class TeamRequestDao {
    private final JdbcTemplate jdbcTemplate;

    public TeamRequestDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private TeamRequest insert(long team_id, long user_id, long requester_id) {
        String sql = "INSERT INTO teamRequest (team_id, user_id, timeRequested, requester_id)" +
                " VALUES (?, ?, ?, ?);";

        Timestamp now = new Timestamp(System.currentTimeMillis());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {

            this.jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
                preparedStatement.setLong(1, team_id);
                preparedStatement.setLong(2, user_id);
                preparedStatement.setTimestamp(3, now);
                preparedStatement.setLong(4, requester_id);

                return preparedStatement;
            }, keyHolder);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("duplicate key value violates unique constraint \"unique_team_user_request\"")) {
                throw new IllegalArgumentException("That request has already been made");
            }
            throw e;
        }

        return new TeamRequest(keyHolder.getKey().longValue(), team_id, user_id, requester_id, false, now);
    }

    public TeamRequest requestTeam(User user, Team team) {
        if (team.getCreatorId() == user.getId()) {
            throw new IllegalArgumentException("You can't request to be on a team you've created.");
        }
        return insert(team.getId(), user.getId(), user.getId());
    }

    public void requestUser(User requested, Team team, User requester) {
        if (team.getCreatorId() != requester.getId()) {
            throw new IllegalArgumentException("Only the creator of a team can request other users");
        }
        insert(team.getId(), requested.getId(), requester.getId());
    }

    private final RowMapper<Long> idMapper = (resultSet, i) -> resultSet.getLong(1);

    public List<Long> getRequests(Team team) {
        String sql = "SELECT user_id FROM teamRequest WHERE team_id=?;";
        return this.jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, team.getId());
            return preparedStatement;
        }, idMapper);
    }

    public List<Long> getRequests(User user) {
        String sql = "SELECT team_id FROM teamRequest WHERE user_id=?;";
        return this.jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, user.getId());
            return preparedStatement;
        }, idMapper);
    }

    private final RowMapper<TeamRequest> teamRequestRowMapper = ((resultSet, i) -> {
        long id = resultSet.getLong(1);
        long teamId = resultSet.getLong(2);
        long userId = resultSet.getLong(3);
        Timestamp timeRequested = resultSet.getTimestamp(4);
        long requesterId = resultSet.getLong(5);
        boolean accepted = resultSet.getBoolean(6);

        return new TeamRequest(id, teamId, userId, requesterId, accepted, timeRequested);
    });

    public TeamRequest findById(long requestId) {
        String sql = "SELECT * FROM teamRequest WHERE id=?;";
        try {
            return this.jdbcTemplate.queryForObject(sql, new Object[]{requestId}, teamRequestRowMapper);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void removeRequest(TeamRequest teamRequest) {
        String sql = "DELETE FROM teamRequest WHERE id=?";
        this.jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, teamRequest.getId());
            return preparedStatement;
        });
    }
}
