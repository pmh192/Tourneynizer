package com.tourneynizer.tourneynizer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.model.Match;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.services.TeamService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ryanl on 2/3/2018.
 */

public class MatchListAdapter extends ListAdapter<Match> {

    private TeamService teamService;
    private List<Team> teams1;
    private List<Team> teams2;

    public MatchListAdapter(Context c) {
        super(c, R.layout.match_list_item_layout);
        teamService = new TeamService();
        teams1 = new ArrayList<>();
        teams2 = new ArrayList<>();
    }

    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (teams1.size() <= position) {
            teams1.addAll(Collections.nCopies(position - teams1.size() + 1, (Team) null));
            teams2.addAll(Collections.nCopies(position - teams2.size() + 1, (Team) null));
        }
        Match match = getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.match_list_item_layout, parent, false);
        }
        final TextView title = convertView.findViewById(R.id.title);
        loadFields(title, match, position);
        return convertView;
    }

    private void loadFields(final TextView title, Match match, final int i) {
        if (teams1.get(i) == null) {
            teamService.getTeamFromID(match.getTeam1ID(), new TeamService.OnTeamLoadedListener() {
                @Override
                public void onTeamLoaded(Team team) {
                    teams1.set(i, team);
                    setFields(title, i);
                }
            });
        } else {
            setFields(title, i);
        }
        if (teams2.get(i) == null) {
            teamService.getTeamFromID(match.getTeam2ID(), new TeamService.OnTeamLoadedListener() {
                @Override
                public void onTeamLoaded(Team team) {
                    teams2.set(i, team);
                    setFields(title, i);
                }
            });
        } else {
            setFields(title, i);
        }
    }

    private void setFields(TextView title, int i) {
        if (teams1.get(i) == null || teams2.get(i) == null) {
            return;
        }
        title.setText(String.format("%s vs. %s", teams1.get(i).getName(), teams2.get(i).getName()));
    }
}
