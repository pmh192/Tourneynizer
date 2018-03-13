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
import java.util.Objects;

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

        return new TeamRequest(keyHolder.getKey().longValue(), team_id, user_id, requester_id, null, now);
    }

    public TeamRequest requestTeam(User user, Team team) {
        if (team.getCreatorId() == user.getId()) {
            throw new IllegalArgumentException("You can't request to be on a team you've created.");
        }
        return insert(team.getId(), user.getId(), user.getId());
    }

    public TeamRequest requestUser(User requested, Team team, User requester) {
        if (team.getCreatorId() != requester.getId()) {
            throw new IllegalArgumentException("Only the creator of a team can request other users");
        }

        if (Objects.equals(requested.getId(), requester.getId())) {
            throw new IllegalArgumentException("You can't request yourself to join the team.");
        }

        return insert(team.getId(), requested.getId(), requester.getId());
    }

    private final RowMapper<Long> idMapper = (resultSet, i) -> resultSet.getLong(1);

    private <T> List<T> getRequestsHelper(RowMapper<T> mapper, String sql, long param1) {
        return getRequestsHelper(mapper, sql, new Long[]{param1});
    }

    private <T> List<T> getRequestsHelper(RowMapper<T> mapper, String sql, Long[] params) {
        return this.jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setLong(i+1, params[i]);
            }
            return preparedStatement;
        }, mapper);
    }

    public List<Long> getRequestIds(Team team) {
        String sql = "SELECT user_id FROM teamRequest WHERE team_id=?;";
        return getRequestsHelper(idMapper, sql, team.getId());
    }

    public List<Long> getRequestIds(User user) {
        String sql = "SELECT team_id FROM teamRequest WHERE user_id=?;";
        return getRequestsHelper(idMapper, sql, user.getId());
    }

    public List<TeamRequest> getRequestsForTeam(Team team) {
        String sql = "SELECT * FROM teamRequest WHERE team_id=? AND requester_id<>? AND accepted IS NULL;";
        return getRequestsHelper(teamRequestRowMapper, sql, new Long[]{team.getId(), team.getCreatorId()});
    }

    public List<TeamRequest> getRequestsByTeam(Team team) {
        String sql = "SELECT * FROM teamRequest WHERE team_id=? AND requester_id=? AND accepted IS NULL;;";
        return getRequestsHelper(teamRequestRowMapper, sql, new Long[]{team.getId(), team.getCreatorId()});
    }

    public List<TeamRequest> getRequestsForUser(User user) {
        String sql = "SELECT * FROM teamRequest WHERE user_id=? AND requester_id<>user_id AND accepted IS NULL;;";
        return getRequestsHelper(teamRequestRowMapper, sql, user.getId());
    }

    public List<TeamRequest> getRequestsByUser(User user) {
        String sql = "SELECT * FROM teamRequest WHERE user_id=? AND requester_id=user_id AND accepted IS NULL;;";
        return getRequestsHelper(teamRequestRowMapper, sql, user.getId());
    }

    private final RowMapper<TeamRequest> teamRequestRowMapper = ((resultSet, i) -> {
        long id = resultSet.getLong(1);
        long teamId = resultSet.getLong(2);
        long userId = resultSet.getLong(3);
        Timestamp timeRequested = resultSet.getTimestamp(4);
        long requesterId = resultSet.getLong(5);
        Boolean accepted = resultSet.getBoolean(6);
        if (resultSet.wasNull()) { accepted = null; }

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

    public int removeRequest(TeamRequest teamRequest) {
        String sql = "DELETE FROM teamRequest WHERE id=?";
        return this.jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, teamRequest.getId());
            return preparedStatement;
        });
    }

    public int declineRequest(TeamRequest teamRequest) throws IllegalArgumentException {
        if (teamRequest.isAccepted() != null) {
            String happened = teamRequest.isAccepted() ? "accepted" : "declined";
            throw new IllegalArgumentException("That request has already been " + happened);
        }

        String sql = "UPDATE teamRequest SET accepted=False WHERE id=?";
        int updated = this.jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, teamRequest.getId());
            return preparedStatement;
        });

        teamRequest.setAccepted(false);
        return updated;
    }
}
