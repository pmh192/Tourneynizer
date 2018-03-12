package com.tourneynizer.tourneynizer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.*;

@JsonSerialize(using = MatchChildrenSerializer.class)
public class MatchChildren {

    private Long matchChild1, matchChild2;
    private Long teamChild1, teamChild2;
    private Set<Long> knownTeamChildren = new HashSet<>();

    public MatchChildren(Long team1, Long team2, Long match1, Long match2)  {
        if (Objects.equals(team1, team2) && team1 != null) {
            throw new IllegalArgumentException("Two teams cannot play each other");
        }
        if (Objects.equals(match1, match2) && match1 != null) {
            throw new IllegalArgumentException("Two teams cannot play each other");
        }

        this.matchChild1 = match1;
        this.matchChild2 = match2;
        this.teamChild1 = team1;
        this.teamChild2 = team2;

        if (this.teamChild1 != null) { knownTeamChildren.add(this.teamChild1); }
        if (this.teamChild2 != null) { knownTeamChildren.add(this.teamChild2); }
    }

    @JsonIgnore
    public Long getMatchChild1() {
        return matchChild1;
    }

    @JsonIgnore
    public Long getMatchChild2() {
        return matchChild2;
    }

    @JsonIgnore
    public Long getTeamChild1() {
        return teamChild1;
    }

    @JsonIgnore
    public Long getTeamChild2() {
        return teamChild2;
    }

    public void setTeamChild1(long teamChild1) {
        if (this.teamChild1 != null) { knownTeamChildren.remove(teamChild1); }
        this.teamChild1 = teamChild1;
        knownTeamChildren.add(teamChild1);
    }

    public void setTeamChild2(long teamChild2) {
        if (this.teamChild2 != null) { knownTeamChildren.remove(teamChild2); }
        this.teamChild2 = teamChild2;
        knownTeamChildren.add(teamChild2);
    }

    @JsonIgnore
    public Set<Long> getKnownTeamChildren() {
        return knownTeamChildren;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MatchChildren children = (MatchChildren) o;

        if (matchChild1 != null ? !matchChild1.equals(children.matchChild1) : children.matchChild1 != null)
            return false;
        if (matchChild2 != null ? !matchChild2.equals(children.matchChild2) : children.matchChild2 != null)
            return false;
        if (teamChild1 != null ? !teamChild1.equals(children.teamChild1) : children.teamChild1 != null) return false;
        return teamChild2 != null ? teamChild2.equals(children.teamChild2) : children.teamChild2 == null;
    }
}
