package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.error.EmailTakenException;
import com.tourneynizer.tourneynizer.model.*;
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

public class MatchDao {
    private final JdbcTemplate jdbcTemplate;

    public MatchDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insert(Match match, User user) throws EmailTakenException, SQLException {
        if (match.isPersisted()) {
            throw new IllegalArgumentException("Match is already persisted");
        }

        String sql = "INSERT INTO matches (tournament, team1_id, team2_id, score1, score2, scoreType, timeStart, timeEnd, refTeam_id, matchOrder, courtNumber)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            this.jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
                preparedStatement.setLong(1, match.getTournamentId());
                preparedStatement.setLong(2, match.getTeam1_id());
                preparedStatement.setLong(3, match.getTeam2_id());
                preparedStatement.setLong(4, match.getScore1());
                preparedStatement.setLong(5, match.getScore2());
                preparedStatement.setInt(6, match.getScoreType().ordinal());
                preparedStatement.setTimestamp(7, match.getTimeStart());
                preparedStatement.setTimestamp(8, match.getTimeEnd());
                preparedStatement.setLong(9, match.getRefTeam_id());
                preparedStatement.setInt(10, match.getMatchOrder());
                preparedStatement.setInt(11, match.getCourtNumber());

                return preparedStatement;
            }, keyHolder);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(e);
        }

        match.persist((Long) keyHolder.getKey());
    }

    private final RowMapper<Match> rowMapper = (resultSet, rowNum) -> new Match(
            //long id, long tournament_id, long team1_id, long team2_id, long refTeam_id, long score1, long score2,
   // int order, int courtNumber, Timestamp timeStart, Timestamp timeEnd, ScoreType scoreType
            resultSet.getLong(1),
            resultSet.getLong(2),
            resultSet.getLong(3),
            resultSet.getLong(4),
            resultSet.getLong(12),
            resultSet.getLong(7),
            resultSet.getLong(8),
            resultSet.getInt(13),
            resultSet.getInt(14),
            resultSet.getTimestamp(10),
            resultSet.getTimestamp(11),
            ScoreType.values()[resultSet.getInt(9)]
    );

    public Match findById(Long id) {
        String sql = "SELECT * FROM matches WHERE id=" + id + ";";
        try {
            return this.jdbcTemplate.queryForObject(sql, rowMapper);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
