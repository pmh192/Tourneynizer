package com.tourneynizer.tourneynizer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.model.Match;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.services.TeamService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ryanwiener on 3/13/18.
 */

public class ExpandableMatchListAdapter extends BaseExpandableListAdapter {

    private List<String> statuses;
    private Map<String, List<Match>> matches;
    private TeamService teamService;
    private Map<String, List<Team>> teams1;
    private Map<String, List<Team>> teams2;
    private Context context;

    public ExpandableMatchListAdapter(Context c) {
        super();
        context = c;
        statuses = Arrays.asList("Waiting for Referee", "In Progress", "Finished");
        matches = new HashMap<>();
        teams1 = new HashMap<>();
        teams2 = new HashMap<>();
        for (String status : statuses) {
            matches.put(status, new ArrayList<Match>());
            teams1.put(status, new ArrayList<Team>());
            teams2.put(status, new ArrayList<Team>());
        }
        teamService = new TeamService();
    }

    @Override
    public int getGroupCount() {
        return statuses.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return matches.get(statuses.get(groupPosition)).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return statuses.get(groupPosition);
    }

    @Override
    public Match getChild(int groupPosition, int childPosition) {
        return matches.get(statuses.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.match_list_group_layout, parent, false);
        }
        TextView title = convertView.findViewById(R.id.title);
        title.setText(statuses.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        List<Team> teams1List = teams1.get(statuses.get(groupPosition));
        List<Team> teams2List = teams2.get(statuses.get(groupPosition));
        if (teams1List.size() <= childPosition) {
            teams1List.addAll(Collections.nCopies(childPosition - teams1List.size() + 1, (Team) null));
            teams2List.addAll(Collections.nCopies(childPosition - teams2List.size() + 1, (Team) null));
        }
        Match match = getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.match_list_item_layout, parent, false);
        }
        final TextView title = convertView.findViewById(R.id.title);
        loadFields(title, match, groupPosition, childPosition);
        return convertView;
    }

    private void loadFields(final TextView title, Match match, final int groupPosition, final int childPosition) {
        final List<Team> teams1List = teams1.get(statuses.get(groupPosition));
        final List<Team> teams2List = teams2.get(statuses.get(groupPosition));
        if (teams1List.get(childPosition) == null) {
            teamService.getTeamFromID(match.getTeam1ID(), new TeamService.OnTeamLoadedListener() {
                @Override
                public void onTeamLoaded(Team team) {
                    teams1List.set(childPosition, team);
                    setFields(title, groupPosition, childPosition);
                }
            });
        } else {
            setFields(title, groupPosition, childPosition);
        }
        if (teams2List.get(childPosition) == null) {
            teamService.getTeamFromID(match.getTeam2ID(), new TeamService.OnTeamLoadedListener() {
                @Override
                public void onTeamLoaded(Team team) {
                    teams2List.set(childPosition, team);
                    setFields(title, groupPosition, childPosition);
                }
            });
        } else {
            setFields(title, groupPosition, childPosition);
        }
    }

    private void setFields(TextView title, int groupPosition, int childPosition) {
        List<Team> teams1List = teams1.get(statuses.get(groupPosition));
        List<Team> teams2List = teams2.get(statuses.get(groupPosition));
        if (teams1List.get(childPosition) == null || teams2List.get(childPosition) == null) {
            return;
        }
        title.setText(String.format("%s vs. %s", teams1List.get(childPosition).getName(), teams2List.get(childPosition).getName()));
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void addAll(Match... ms) {
        for (Match match : ms) {
            if (match.hasFinished()) {
                matches.get(statuses.get(2)).add(match);
            } else if (match.hasStarted()) {
                matches.get(statuses.get(1)).add(match);
            } else {
                matches.get(statuses.get(0)).add(match);
            }
        }
    }

    public void addAll(Collection<? extends Match> ms) {
        for (Match match : ms) {
            if (match.hasFinished()) {
                matches.get(statuses.get(2)).add(match);
            } else if (match.hasStarted()) {
                matches.get(statuses.get(1)).add(match);
            } else {
                matches.get(statuses.get(0)).add(match);
            }
        }
    }

    public void clear() {
        for (List<Match> matches : matches.values()) {
            matches.clear();
        }
    }

    public ArrayList<Match> getAll() {
        ArrayList<Match> allMatches = new ArrayList<>();
        for (List<Match> matchList : matches.values()) {
            allMatches.addAll(matchList);
        }
        return allMatches;
    }
}
