package com.tourneynizer.tourneynizer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.adapters.TeamRequestListAdapter;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.TeamRequest;
import com.tourneynizer.tourneynizer.services.TeamRequestService;

import java.util.ArrayList;
import java.util.List;


public class TeamRequestListFragment extends UIQueueFragment {

    private static final String TEAM = "com.tourneynizer.tourneynizer.model.Team";
    private static final String TEAM_REQUESTS = "com.tourneynizer.tourneynizer.model.TeamRequest[]";

    private Team team;
    private TeamRequestListAdapter listAdapter;
    private TeamRequestService teamRequestService;
    private SwipeRefreshLayout swipeRefresher;

    public TeamRequestListFragment() {
        // Required empty public constructor
    }

    public static TeamRequestListFragment newInstance(Team t) {
        TeamRequestListFragment fragment = new TeamRequestListFragment();
        Bundle args = new Bundle();
        args.putParcelable(TEAM, t);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            team = getArguments().getParcelable(TEAM);
        }
        teamRequestService = new TeamRequestService();
        listAdapter = new TeamRequestListAdapter(getContext());
        if (savedInstanceState != null) {
            List<TeamRequest> teamRequests = savedInstanceState.getParcelableArrayList(TEAM_REQUESTS);
            if (teamRequests != null) {
                listAdapter.addAll(teamRequests);
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
        View view = inflater.inflate(R.layout.fragment_team_request_list, container, false);
        ListView listView = view.findViewById(R.id.requestsList);
        swipeRefresher = view.findViewById(R.id.swipeRefresher);
        swipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        listView.setAdapter(listAdapter);
        return view;
    }

    public void refresh() {
        listAdapter.clear();
        if (swipeRefresher != null) {
            swipeRefresher.setRefreshing(true);
            teamRequestService.getRequestsForTeam(team, new TeamRequestService.OnTeamRequestsLoadedListener() {
                @Override
                public void onTeamRequestsLoaded(final TeamRequest[] teamRequests) {
                    performUITask(new Runnable() {
                        @Override
                        public void run() {
                            listAdapter.addAll(teamRequests);
                            swipeRefresher.setRefreshing(false);
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TEAM_REQUESTS, listAdapter.getAll());
    }
}
