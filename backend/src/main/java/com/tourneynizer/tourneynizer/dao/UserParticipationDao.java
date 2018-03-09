package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

public class UserParticipationDao {
    private final JdbcTemplate jdbcTemplate;

    public UserParticipationDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private final RowMapper<Long> idMapper = (resultSet, i) -> resultSet.getLong(1);

    static void registerUser(User user, Tournament tournament, JdbcTemplate jdbcTemplate) {
        registerUser(user.getId(), tournament.getId(), jdbcTemplate);
    }
    static void registerUser(long userId, long tournamentId, JdbcTemplate jdbcTemplate) {
        String sql = "INSERT INTO user_participation (user_id, tournament_id)" +
                " VALUES (?, ?);";

        Timestamp now = new Timestamp(System.currentTimeMillis());
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setLong(1, userId);
                preparedStatement.setLong(2, tournamentId);

                return preparedStatement;
            });
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("duplicate key value violates unique constraint \"user_participation_tournament_id_user_id_key\"")) {
                throw new IllegalArgumentException("That user is already participating in that tournament");
            }
            throw e;
        }
    }

    public List<Long> usersParticipatingIn(Tournament tournament)  {
        String sql = "SELECT user_id FROM user_participation WHERE tournament_id=?;";
        return this.jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, tournament.getId());
            return preparedStatement;
        }, idMapper);
    }

    public List<Long> tournamentsParticipatedBy(User user)  {
        String sql = "SELECT tournament_id FROM user_participation WHERE tournament_id=?;";
        return this.jdbcTemplate.query(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, user.getId());
            return preparedStatement;
        }, idMapper);
    }

    public void registerUser(long userId, long tournamentId) {
        registerUser(userId, tournamentId, jdbcTemplate);
    }

    public void registerUser(User user, Tournament tournament) {
        registerUser(user, tournament, jdbcTemplate);
    }
}
