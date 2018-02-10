package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.error.EmailTakenException;
import com.tourneynizer.tourneynizer.model.Match;
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

public class MatchDao {
    private final JdbcTemplate jdbcTemplate;

    public MatchDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insert(Match match, User user) throws EmailTakenException, SQLException {
        if (match.isPersisted()) {
            throw new IllegalArgumentException("Match is already persisted");
        }

        String sql = "INSERT INTO matches (tournament, team1_id, team2_id, child1_id, child2_id, score1, score2, scoreType, timeStart, timeEnd, refTeam_id, matchOrder, courtNumber)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        Timestamp now = new Timestamp(System.currentTimeMillis());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            this.jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
                preparedStatement.setLong(1, match.getTournamentId());
                preparedStatement.setLong(2, match.getTeam1_id());
                preparedStatement.setLong(3, match.getTeam2_id());
                preparedStatement.setLong(4, match.getChild1_id());
                preparedStatement.setLong(5, match.getChild2_id());
                preparedStatement.setLong(6, match.getScore1());
                preparedStatement.setLong(7, match.getScore2());
                preparedStatement.setInt(8, match.getScoreType().ordinal());
                preparedStatement.setTimestamp(9, match.getTimeStart());
                preparedStatement.setTimestamp(10, match.getTimeEnd());
                preparedStatement.setLong(11, match.getRefTeam_id());
                preparedStatement.setInt(12, match.getMatchOrder());
                preparedStatement.setInt(13, match.getCourtNumber());

                return preparedStatement;
            }, keyHolder);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(e);
        }

        match.persist((Long) keyHolder.getKey());
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

    public Tournament findById(Long id) {
        String sql = "SELECT * FROM tournaments WHERE id=" + id + ";";
        try {
            return this.jdbcTemplate.queryForObject(sql, rowMapper);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
