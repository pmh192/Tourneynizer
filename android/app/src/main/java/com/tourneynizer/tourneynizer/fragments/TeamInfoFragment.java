package com.tourneynizer.tourneynizer.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.adapters.UserListAdapter;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.services.TeamRequestService;

import java.util.List;

public class TeamInfoFragment extends Fragment {

    private static final String TEAM = "com.tourneynizer.tourneynizer.model.Team";
    private static final String USERS = "com.tourneynizer.tourneynizer.model.User[]";

    private Team team;
    private ListView listView;
    private UserListAdapter listAdapter;
    private TeamRequestService teamRequestService;

    public TeamInfoFragment() {
        // Required empty public constructor
    }

    public static TeamInfoFragment newInstance(Team t) {
        TeamInfoFragment fragment = new TeamInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable(TEAM, t);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            team = getArguments().getParcelable(TEAM);
        }
        teamRequestService = new TeamRequestService();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_team_info, container, false);
        listView = view.findViewById(R.id.memberList);
        listAdapter = new UserListAdapter(getContext());
        if (savedInstanceState != null) {
            List<User> users = savedInstanceState.getParcelableArrayList(USERS);
            if (users != null) {
                listAdapter.addAll(users);
            } else {
                refresh();
            }
        } else {
            refresh();
        }
        listView.setAdapter(listAdapter);
        ImageView logoView = view.findViewById(R.id.logo);
        logoView.setImageBitmap(team.getLogo());
        TextView teamName = view.findViewById(R.id.teamName);
        teamName.setText(team.getName());
        TextView creatorName = view.findViewById(R.id.creatorName);
        creatorName.setText("Created by " + team.getCreatorID());
        TextView tournamentName = view.findViewById(R.id.tournamentName);
        tournamentName.setText("" + team.getTournamentID());
        View requestButton = view.findViewById(R.id.requestJoin);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamRequestService.sendRequestToTeam(team);
            }
        });
        return view;
    }

    public void refresh() {

    }
}
