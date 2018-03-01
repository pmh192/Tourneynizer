package com.tourneynizer.tourneynizer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.adapters.TeamListAdapter;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.services.TeamService;

import java.util.List;

public class JoinTeamFragment extends Fragment {

    private static final String TEAMS = "com.tourneynizer.tourneynizer.model.Team[]";
    private static final String TOURNAMENT = "com.tourneynizer.tourneynizer.model.Tournament";

    private Tournament tournament;
    private TeamListAdapter listAdapter;

    public JoinTeamFragment() {
        // Required empty public constructor
    }

    public static JoinTeamFragment newInstance(Tournament t) {
        JoinTeamFragment fragment = new JoinTeamFragment();
        Bundle args = new Bundle();
        args.putParcelable(TOURNAMENT, t);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tournament = getArguments().getParcelable(TOURNAMENT);
        }
        listAdapter = new TeamListAdapter(getActivity());
        if (savedInstanceState != null) {
            List<Team> teams = savedInstanceState.getParcelableArrayList(TEAMS);
            if (teams != null) {
                listAdapter.addAll(teams);
            } else {
                refresh();
            }
        } else {
            refresh();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_join_team, container, false);
        ListView teamsList = view.findViewById(R.id.teamList);
        teamsList.setAdapter(listAdapter);
        SwipeRefreshLayout swipeRefresher = view.findViewById(R.id.swipeRefresher);
        swipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TEAMS, listAdapter.getAll());
    }

    public void refresh() {
        TeamService teamService = new TeamService();
        teamService.getPendingTeams(tournament, new TeamService.OnTeamsLoadedListener() {
            @Override
            public void onTeamsLoaded(Team[] teams) {
                listAdapter.clear();
                listAdapter.addAll(teams);
            }
        });
    }
}
