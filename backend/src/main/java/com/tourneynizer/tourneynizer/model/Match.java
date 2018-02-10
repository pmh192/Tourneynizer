package com.tourneynizer.tourneynizer.model;

import java.sql.Timestamp;

public class Match {

    private Long id, child1_id, child2_id;
    private long tournament_id, team1_id, team2_id, refTeam_id, score1, score2;
    private int order, courtNumber;
    private Timestamp timeStart, timeEnd;
    private ScoreType scoreType;

    public Match(long tournament_id, long team1_id, long team2_id, long refTeam_id, int score1, int score2, int order,
                 int courtNumber, Timestamp timeStart, Timestamp timeEnd, ScoreType scoreType) {
        setTournament_id(tournament_id);
        setTeam1_id(team1_id);
        setTeam2_id(team2_id);
        setRefTeam_id(refTeam_id);
        setScore1(score1);
        setScore2(score2);
        setOrder(order);
        setCourtNumber(courtNumber);
        setTimeStart(timeStart);
        setTimeEnd(timeEnd);
        setScoreType(scoreType);
    }

    public Match(long id, long tournament_id, long team1_id, long team2_id, long refTeam_id, int score1, int score2,
                 int order, int courtNumber, Timestamp timeStart, Timestamp timeEnd, ScoreType scoreType) {
        this(tournament_id, team1_id, team2_id, refTeam_id, score1, score2, order, courtNumber, timeStart, timeEnd,
                scoreType);

        persist(id);
    }

    public void setTournament_id(long tournament_id) {
        this.tournament_id = tournament_id;
    }

    public void setTeam1_id(long team1_id) {
        this.team1_id = team1_id;
    }

    public void setTeam2_id(long team2_id) {
        this.team2_id = team2_id;
    }

    public void setRefTeam_id(long refTeam_id) {
        this.refTeam_id = refTeam_id;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public void setScore2(int score2) {
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

    public boolean isPersisted() {
        return id != null;
    }

    public Long getTournamentId() {
        return tournament_id;
    }

    public long getTeam1_id() {
        return team1_id;
    }

    public long getTeam2_id() {
        return team2_id;
    }

    public Long getChild1_id() {
        return child1_id;
    }

    public Long getChild2_id() {
        return child2_id;
    }

    public long getScore1() {
        return score1;
    }

    public long getScore2() {
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

    public long getRefTeam_id() {
        return refTeam_id;
    }

    public int getMatchOrder() {
        return order;
    }

    public int getCourtNumber() {
        return courtNumber;
    }

    public void persist(long id) {
        this.id = id;
    }
}
