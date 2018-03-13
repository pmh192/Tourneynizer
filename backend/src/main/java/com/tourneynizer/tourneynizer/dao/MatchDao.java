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
                "score1, score2, scoreType, timeStart, timeEnd, refTeam_id, matchOrder, courtNumber, status, round)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

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
                preparedStatement.setShort(15, match.getRound());

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
                    getNullableLong(resultSet, 14),
                    getNullableLong(resultSet, 15)
                    ),
            getNullableLong(resultSet, 10),
            getNullableLong(resultSet, 5),
            getNullableLong(resultSet, 6),
            resultSet.getInt(11),
            resultSet.getInt(12),
            resultSet.getTimestamp(8),
            resultSet.getTimestamp(9),
            ScoreType.values()[resultSet.getInt(7)],
            MatchStatus.values()[resultSet.getShort(16)],
            resultSet.getShort(17)
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

    public void startMatch(Match match) {
        if (!match.getMatchStatus().equals(MatchStatus.CREATED)) {
            throw new IllegalArgumentException("That match has already been started");
        }

        String sql = "UPDATE matches SET status=? WHERE id=?";
        int updated = jdbcTemplate.update(sql, new Object[]{MatchStatus.STARTED.ordinal(), match.getId()},
                new int[] {Types.SMALLINT, Types.BIGINT});
        if (updated == 1) {
            match.setMatchStatus(MatchStatus.STARTED);
        }
    }

    public void endMatch(Match match, Team winner, long score1, long score2) {
        endMatch(match, winner.getId(), score1, score2);
    }

    public void endMatch(Match match, long winnerId, long score1, long score2) {
        if (!match.getMatchChildren().getKnownTeamChildren().contains(winnerId)) {
            throw new IllegalArgumentException("That team isn't playing in this match");
        }

        if (match.getMatchStatus().equals(MatchStatus.COMPLETED)) {
            throw new IllegalArgumentException("That match has already ended");
        }

        if (match.getMatchStatus().equals(MatchStatus.CREATED)) {
            throw new IllegalArgumentException("That match hasn't been started yet");
        }

        Match parentMatch = getParentMatch(match);

        String sql = "UPDATE matches SET status=?, score1=?, score2=? WHERE id=?;";
        int updated = jdbcTemplate.update(sql, new Object[]{MatchStatus.COMPLETED.ordinal(), score1, score2, match.getId()},
                new int[] {Types.SMALLINT, Types.BIGINT, Types.BIGINT, Types.BIGINT});

        if (updated == 1) {
            match.setMatchStatus(MatchStatus.COMPLETED);
            match.setScore1(score1);
            match.setScore2(score2);

            if (parentMatch != null) {
                updateParent(parentMatch, match, winnerId);
            }
        }
    }

    private void updateParent(Match parentMatch, Match childMatch, long winnerId) {
        MatchChildren children = parentMatch.getMatchChildren();

        String toUpdate;
        if (childMatch.getId().equals(children.getMatchChild1())) {
            toUpdate = "team1_id";
            children.setTeamChild1(winnerId);
        }
        else {
            toUpdate = "team2_id";
            children.setTeamChild2(winnerId);
        }
        String sql = "UPDATE matches SET " + toUpdate + "=? WHERE id=?;";
        jdbcTemplate.update(sql, new Object[]{winnerId, parentMatch.getId()},
                new int[] {Types.BIGINT, Types.BIGINT});

        if (children.getKnownTeamChildren().size() == 2) {
            updateParentWithReferee(parentMatch, childMatch, winnerId);
        }
    }

    private void updateParentWithReferee(Match parent, Match childMatch, long winnerId) {
        MatchChildren teamsPlayed = childMatch.getMatchChildren();

        long loserTeamId;
        if (teamsPlayed.getTeamChild1().equals(winnerId)) {
            loserTeamId = teamsPlayed.getTeamChild2();
        }
        else {
            loserTeamId = teamsPlayed.getTeamChild1();
        }

        String sql = "UPDATE matches SET refteam_id=? WHERE id=?;";
        jdbcTemplate.update(sql, new Object[]{loserTeamId, parent.getId()}, new int[] {Types.BIGINT, Types.BIGINT});
    }

    public void updateScore(Match match, long score1, long score2) {
        if (!match.getMatchStatus().equals(MatchStatus.STARTED)) {
            throw new IllegalArgumentException("Only matches in progress can have their scores updated");
        }

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

    public List<Match> getValidMatches(Tournament tournament) {
        String sql = "SELECT * FROM matches WHERE tournament=? AND team1_id IS NOT NULL AND team2_id IS NOT NULL;";
        return this.jdbcTemplate.query(sql, new Object[]{tournament.getId()}, new int[]{Types.BIGINT}, rowMapper);
    }

    public Match getParentMatch(Match match) {
        String sql = "SELECT * FROM matches WHERE match_child1=? OR match_child2=?;";
        try {
            return this.jdbcTemplate.queryForObject(sql, new Object[]{match.getId(), match.getId()},
                    new int[]{Types.BIGINT, Types.BIGINT}, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private final RowMapper<Long[]> scoreMapper = (resultSet, rowNum) -> new Long[] {
            getNullableLong(resultSet, 1),
            getNullableLong(resultSet, 2),
    };

    public Long[] getScore(Match match) {
        String sql = "SELECT score1, score2 FROM matches WHERE id=?";
        try {
            return this.jdbcTemplate.queryForObject(sql, new Object[]{match.getId()}, new int[]{Types.BIGINT},
                    scoreMapper);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Match getMatchToReferee(Team team) {
        String sql = "SELECT * FROM matches WHERE refteam_id=? AND status=? ORDER BY round ASC LIMIT 1";
        try {
            return this.jdbcTemplate.queryForObject(sql,
                    new Object[]{team.getId(), (short) TournamentStatus.CREATED.ordinal()},
                    new int[]{Types.BIGINT, Types.SMALLINT},
                    rowMapper
            );
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
