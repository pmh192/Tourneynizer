package com.tourneynizer.tourneynizer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.adapters.TeamListAdapter;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.services.TeamService;

import java.util.List;

public class TeamListFragment extends UIQueueFragment {

    private static final String TEAMS = "com.tourneynizer.tourneynizer.model.Team[]";
    private static final String TOURNAMENT = "com.tourneynizer.tourneynizer.model.Tournament";
    private static final String COMPLETE = "java.boolean";

    private Tournament tournament;
    private TeamService teamService;
    private TeamListAdapter listAdapter;
    private SwipeRefreshLayout swipeRefresher;
    private boolean complete;

    public TeamListFragment() {
        // Required empty public constructor
    }

    public static TeamListFragment newInstance(Tournament t, boolean c) {
        TeamListFragment fragment = new TeamListFragment();
        Bundle args = new Bundle();
        args.putParcelable(TOURNAMENT, t);
        args.putBoolean(COMPLETE, c);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tournament = getArguments().getParcelable(TOURNAMENT);
            complete = getArguments().getBoolean(COMPLETE);
        }
        teamService = new TeamService();
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
        View view = inflater.inflate(R.layout.fragment_list_team, container, false);
        ListView teamsList = view.findViewById(R.id.teamList);
        swipeRefresher = view.findViewById(R.id.swipeRefresher);
        swipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        teamsList.setAdapter(listAdapter);
        teamsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToTeamInfo(listAdapter.getItem(i));
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
        listAdapter.clear();
        if (swipeRefresher != null) {
            swipeRefresher.setRefreshing(true);
        }
        TeamService.OnTeamsLoadedListener listener = new TeamService.OnTeamsLoadedListener() {
            @Override
            public void onTeamsLoaded(final Team[] teams) {
                performUITask(new Runnable() {
                    @Override
                    public void run() {
                        if (teams != null) {
                            listAdapter.addAll(teams);
                        }
                        if (swipeRefresher != null) {
                            swipeRefresher.setRefreshing(false);
                        }
                    }
                });
            }
        };
        if (complete) {
            teamService.getCompleteTeams(tournament, listener);
        } else {
            teamService.getPendingTeams(tournament, listener);
        }
    }

    public void goToTeamInfo(Team t) {
        Fragment fragment = TeamInfoFragment.newInstance(t);
        ((RootFragment) getParentFragment()).pushFragment(fragment);
    }
}
