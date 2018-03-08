package com.tourneynizer.tourneynizer.dao;

import com.tourneynizer.tourneynizer.model.Match;
import com.tourneynizer.tourneynizer.model.ScoreType;
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
import java.sql.Types;
import java.util.List;

import static com.tourneynizer.tourneynizer.helper.JDBCHelper.getNullableLong;
import static com.tourneynizer.tourneynizer.helper.JDBCHelper.setNullable;

public class MatchDao {
    private final JdbcTemplate jdbcTemplate;

    public MatchDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insert(Match match, User user) throws SQLException {
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
                setNullable(preparedStatement, 2, match.getTeam1Id());
                setNullable(preparedStatement, 3, match.getTeam2Id());
                setNullable(preparedStatement, 4, match.getScore1());
                setNullable(preparedStatement, 5, match.getScore2());
                preparedStatement.setInt(6, match.getScoreType().ordinal());
                preparedStatement.setTimestamp(7, match.getTimeStart());
                preparedStatement.setTimestamp(8, match.getTimeEnd());
                setNullable(preparedStatement, 9, match.getRefId());
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
            resultSet.getLong(1),
            resultSet.getLong(2),
            getNullableLong(resultSet, 3),
            getNullableLong(resultSet, 4),
            getNullableLong(resultSet, 12),
            getNullableLong(resultSet, 7),
            getNullableLong(resultSet, 8),
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

    public List<Match> findByTournament(Tournament tournament) {
        String sql = "SELECT * FROM matches WHERE tournament=?;";
        return this.jdbcTemplate.query(sql, new Object[]{tournament.getId()}, new int[]{Types.BIGINT}, rowMapper);
    }
}
