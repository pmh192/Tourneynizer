package com.tourneynizer.tourneynizer.model;

import com.tourneynizer.tourneynizer.dao.MatchDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class MatchGenerator {

    private final MatchDao matchDao;

    public MatchGenerator(MatchDao matchDao) {
        this.matchDao = matchDao;
    }

    public List<Match> createTournamentMatches(List<Team> teams, User user, Tournament tournament) throws SQLException {
        if (TournamentType.VOLLEYBALL_BRACKET.equals(tournament.getType())) {
            return createBracketMatches(teams, tournament, user);
        }
        else {
            throw new IllegalArgumentException(tournament.getType().name() + " is not currently supported");
        }
    }

    private class MatchNode implements Comparable<MatchNode> {
        private Match match;
        private Team team;
        public int orderInserted;

        public MatchNode(Team team, int orderInserted, int height) {
            this.setValue(team);
            this.orderInserted = orderInserted;
            this.height = height;
        }

        public MatchNode(Match match, int orderInserted, int height) {
            this.setValue(match);
            this.orderInserted = orderInserted;
            this.height = height;
        }

        public void setValue(Match match) {
            this.match = match;
            this.team = null;
        }

        public void setValue(Team team) {
            this.team = team;
            this.match = null;
        }

        public Match getMatch() {
            return match;
        }

        public Team getTeam() {
            return team;
        }

        public long getValueId() {
            return match != null ? match.getId() : team.getId();
        }

        public MatchNode child1, child2;
        public int height;

        @Override
        public int compareTo(MatchNode o) {
            int diff = this.height - o.height;
            if (diff != 0) { return diff; }
            return this.orderInserted - o.orderInserted;
        }
    }
    private List<Match> createBracketMatches(List<Team> teams, Tournament tournament, User user) throws SQLException {
        PriorityQueue<MatchNode> tree = new PriorityQueue<>();
        int orderInserted = 0;
        for (Team team : teams) {
            tree.add(new MatchNode(team, orderInserted++, 0));
        }

        int order = 0;
        List<Match> matches = new ArrayList<>();

        while (tree.size() >= 2) {
            MatchNode node1 = tree.poll();
            MatchNode node2 = tree.poll();

            Long team1 = null, team2 = null, match1 = null, match2 = null;
            if (node1.getMatch() != null) { match1 = node1.getValueId(); }
            else {                          team1 = node1.getValueId(); }

            if (node2.getMatch() != null) { match2 = node2.getValueId(); }
            else {                          team2 = node2.getValueId(); }

            MatchChildren children = new MatchChildren(team1, team2, match1, match2);
            int newHeight = Math.max(node1.height, node2.height) + 1;
            Match parent = new Match(tournament.getId(), children, order++, null, ScoreType.ONE_SET, (short) newHeight);
            matchDao.insert(parent);
            tree.add(new MatchNode(parent, orderInserted++, newHeight));
            matches.add(parent);
        }

        return matches;
    }
}
