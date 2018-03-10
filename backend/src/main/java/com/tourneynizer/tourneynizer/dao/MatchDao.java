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

    public void insert(Match match) throws SQLException {
        if (match.isPersisted()) {
            throw new IllegalArgumentException("Match is already persisted");
        }

        String sql = "INSERT INTO matches (tournament, team1_id, team2_id, match_child1, match_child2, " +
                "score1, score2, scoreType, timeStart, timeEnd, refTeam_id, matchOrder, courtNumber, status)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            this.jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
                preparedStatement.setLong(1, match.getTournamentId());

                Long team1Id = match.getMatchChildren().getTeamChild1();
                Long team2Id = match.getMatchChildren().getTeamChild2();
                Long match1Id = match.getMatchChildren().getMatchChild1();
                Long match2Id = match.getMatchChildren().getMatchChild2();
                setNullable(preparedStatement, 2, team1Id);
                setNullable(preparedStatement, 3, team2Id);
                setNullable(preparedStatement, 4, match1Id);
                setNullable(preparedStatement, 5, match2Id);

                setNullable(preparedStatement, 6, match.getScore1());
                setNullable(preparedStatement, 7, match.getScore2());
                preparedStatement.setInt(8, match.getScoreType().ordinal());
                preparedStatement.setTimestamp(9, match.getTimeStart());
                preparedStatement.setTimestamp(10, match.getTimeEnd());
                setNullable(preparedStatement, 11, match.getRefId());
                preparedStatement.setInt(12, match.getMatchOrder());
                preparedStatement.setInt(13, match.getCourtNumber());
                preparedStatement.setShort(14, (short) match.getMatchStatus().ordinal());

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
            new MatchChildren(
                    getNullableLong(resultSet, 3),
                    getNullableLong(resultSet, 4),
                    getNullableLong(resultSet, 16),
                    getNullableLong(resultSet, 17)
                    ),
            getNullableLong(resultSet, 12),
            getNullableLong(resultSet, 7),
            getNullableLong(resultSet, 8),
            resultSet.getInt(13),
            resultSet.getInt(14),
            resultSet.getTimestamp(10),
            resultSet.getTimestamp(11),
            ScoreType.values()[resultSet.getInt(9)],
            MatchStatus.values()[resultSet.getShort(18)]
    );

    public Match findById(Long id) throws SQLException {
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

//    public List<Match> getCompletedMatches(Tournament tournament) {
//        String sql = "SELECT * FROM matches WHERE tournament=? AND finished=True;";
//        return this.jdbcTemplate.query(sql, new Object[]{tournament.getId()}, new int[]{Types.BIGINT}, rowMapper);
//    }

    public void startMatch(Match match) {
        String sql = "UPDATE matches SET status=? WHERE id=?";
        int updated = jdbcTemplate.update(sql, new Object[]{MatchStatus.STARTED.ordinal(), match.getId()},
                new int[] {Types.SMALLINT, Types.BIGINT});
        if (updated == 1) {
            match.setMatchStatus(MatchStatus.STARTED);
        }
    }

    public void endMatch(Match match, long score1, long score2) {
        String sql = "UPDATE matches SET status=?, score1=?, score2=? WHERE id=?;";
        int updated = jdbcTemplate.update(sql, new Object[]{MatchStatus.COMPLETED.ordinal(), score1, score2, match.getId()},
                new int[] {Types.SMALLINT, Types.BIGINT, Types.BIGINT, Types.BIGINT});

        if (updated == 1) {
            match.setMatchStatus(MatchStatus.COMPLETED);
            match.setScore1(score1);
            match.setScore2(score2);
        }
    }

    public void updateScore(Match match, long score1, long score2) {
        String sql = "UPDATE matches SET score1=?, score2=? WHERE id=?;";
        int updated = jdbcTemplate.update(sql, new Object[]{score1, score2, match.getId()},
                new int[] {Types.BIGINT, Types.BIGINT, Types.BIGINT});

        if (updated == 1) {
            match.setScore1(score1);
            match.setScore2(score2);
        }
    }

    private List<Match> getByStatus(Tournament tournament, MatchStatus matchStatus) {
        String sql = "SELECT * FROM matches WHERE tournament=? AND status=?;";
        return this.jdbcTemplate.query(sql, new Object[]{tournament.getId(), (short) matchStatus.ordinal()},
                new int[]{Types.BIGINT, Types.SMALLINT}, rowMapper);
    }

    public List<Match> getCompleted(Tournament tournament) {
        return getByStatus(tournament, MatchStatus.COMPLETED);
    }

    public List<Match> getUnstarted(Tournament tournament) {
        return getByStatus(tournament, MatchStatus.CREATED);
    }

    public List<Match> getInProgress(Tournament tournament) {
        return getByStatus(tournament, MatchStatus.STARTED);
    }
}
