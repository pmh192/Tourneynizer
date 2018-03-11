package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

public class RosterDao {
    private final JdbcTemplate jdbcTemplate;

    public RosterDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    static void registerUser(User user, Team team, boolean isLeader, JdbcTemplate jdbcTemplate) {
        String sql = "INSERT INTO roster (team_id, user_id, tournament_id, timeAdded, isLeader)" +
                " VALUES (?, ?, ?, ?, ?);";

        Timestamp now = new Timestamp(System.currentTimeMillis());
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setLong(1, team.getId());
                preparedStatement.setLong(2, user.getId());
                preparedStatement.setLong(3, team.getTournamentId());
                preparedStatement.setTimestamp(4, now);
                preparedStatement.setBoolean(5, isLeader);

                return preparedStatement;
            });
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("duplicate key value violates unique constraint \"unique_team_user\"")) {
                throw new IllegalArgumentException(e);
            }
            throw e;
        }
    }

    static void registerUser(User user, Team team, JdbcTemplate jdbcTemplate) {
        registerUser(user, team, false, jdbcTemplate);
    }

    private final RowMapper<Long> idMapper = (resultSet, i) -> resultSet.getLong(1);

    public List<Long> teamRoster(Team team)  {
        String sql = "SELECT user_id FROM roster WHERE team_id=?;";
        return this.jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, team.getId());
            return preparedStatement;
        }, idMapper);
    }

    public List<Team> findByUser(User user) {
        String sql = "SELECT teams.* FROM teams INNER JOIN roster ON roster.team_id=teams.id WHERE roster.user_id=?;";

        return this.jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, user.getId());
            return preparedStatement;
        }, TeamDao.rowMapper);
    }

    public void registerUser(User user, Team team) {
        registerUser(user, team, jdbcTemplate);
    }
}
