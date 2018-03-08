package com.tourneynizer.tourneynizer.model;

import java.sql.Timestamp;
import java.util.Objects;

public class Match {

    private Long id, child1, child2, team1Id, team2Id, refId, score1, score2;
    private long tournament_id;
    private int order, courtNumber;
    private Timestamp timeStart, timeEnd;
    private ScoreType scoreType;


    public Match(long tournamentId, long team1Id, long team2Id, int order, Timestamp timeStart, ScoreType type) {
        setTournamentId(tournamentId);
        setteam1Id(team1Id);
        setteam2Id(team2Id);
        setOrder(order);
        setTimeStart(timeStart);
        setScoreType(type);
    }

    public Match(long tournamentId, int order, long child1, long child2, Timestamp timeStart, ScoreType type) {
        setTournamentId(tournamentId);
        setOrder(order);
        setChildren(child1, child2);
        setTimeStart(timeStart);
        setScoreType(type);
    }

    public Match(long id, long tournament_id, Long team1Id, Long team2Id, Long refId, Long score1, Long score2, int order,
                 int courtNumber, Timestamp timeStart, Timestamp timeEnd, ScoreType scoreType) {
        persist(id);
        setTournamentId(tournament_id);
        setteam1Id(team1Id);
        setteam2Id(team2Id);
        setRefId(refId);
        setScore1(score1);
        setScore2(score2);
        setOrder(order);
        setCourtNumber(courtNumber);
        setTimeStart(timeStart);
        setTimeEnd(timeEnd);
        setScoreType(scoreType);
    }

    public void setTournamentId(long tournament_id) {
        this.tournament_id = tournament_id;
    }

    public void setteam1Id(Long team1Id) {
        if (this.team2Id != null && this.team2Id.equals(team1Id)) {
            throw new IllegalArgumentException("Teams cannot play themselves");
        }
        if (this.refId != null && this.refId.equals(team1Id)) {
            throw new IllegalArgumentException("Teams cannot ref their own game");
        }
        this.team1Id = team1Id;
    }

    public void setteam2Id(Long team2Id) {
        if (this.team1Id != null && this.team1Id.equals(team2Id)) {
            throw new IllegalArgumentException("Teams cannot play themselves");
        }
        if (this.refId != null && this.refId.equals(team2Id)) {
            throw new IllegalArgumentException("Teams cannot ref their own game");
        }
        this.team2Id = team2Id;
    }

    public void setRefId(Long refId) {
        if (refId != null && (Objects.equals(refId, team1Id) || Objects.equals(refId, team2Id))) {
            throw new IllegalArgumentException("Teams cannot ref their own game");
        }
        this.refId = refId;
    }

    public void setScore1(Long score1) {
        this.score1 = score1;
    }

    public void setScore2(Long score2) {
        this.score2 = score2;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setCourtNumber(int courtNumber) {
        this.courtNumber = courtNumber;
    }

    public void setTimeStart(Timestamp timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeEnd(Timestamp timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setScoreType(ScoreType scoreType) {
        if (scoreType == null) {
            throw new IllegalArgumentException("ScoreType cannot be null");
        }
        this.scoreType = scoreType;
    }

    public void setChildren(long child1, long child2) {
        this.child1 = child1;
        this.child2 = child2;
    }

    public void unsetChildren() {
        this.child1 = this.child2 = null;
    }

    public boolean isPersisted() {
        return id != null;
    }

    public Long getTournamentId() {
        return tournament_id;
    }

    public Long getTeam1Id() {
        return team1Id;
    }

    public Long getTeam2Id() {
        return team2Id;
    }

    public Long getScore1() {
        return score1;
    }

    public Long getScore2() {
        return score2;
    }

    public ScoreType getScoreType() {
        return scoreType;
    }

    public Timestamp getTimeStart() {
        return timeStart;
    }

    public Timestamp getTimeEnd() {
        return timeEnd;
    }

    public Long getRefId() {
        return refId;
    }

    public int getMatchOrder() {
        return order;
    }

    public int getCourtNumber() {
        return courtNumber;
    }

    public Long getChild1() {
        return child1;
    }

    public Long getChild2() {
        return child2;
    }

    public void persist(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Match match = (Match) o;

        if (tournament_id != match.tournament_id) return false;
        if (!Objects.equals(team1Id, match.team1Id)) return false;
        if (!Objects.equals(team2Id, match.team2Id)) return false;
        if (!Objects.equals(refId, match.refId)) return false;
        if (!Objects.equals(score1, match.score1)) return false;
        if (!Objects.equals(score2, match.score2)) return false;
        if (order != match.order) return false;
        if (courtNumber != match.courtNumber) return false;
        if (id != null ? !id.equals(match.id) : match.id != null) return false;
        if (timeStart != null ? !timeStart.equals(match.timeStart) : match.timeStart != null) return false;
        if (timeEnd != null ? !timeEnd.equals(match.timeEnd) : match.timeEnd != null) return false;
        return scoreType == match.scoreType;
    }
}
