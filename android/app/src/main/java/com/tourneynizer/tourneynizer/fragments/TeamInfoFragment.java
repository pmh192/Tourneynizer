package com.tourneynizer.tourneynizer.fragments;


import android.media.MediaRouter;
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
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.services.TeamRequestService;
import com.tourneynizer.tourneynizer.services.TournamentService;
import com.tourneynizer.tourneynizer.services.UserService;

import java.util.List;

public class TeamInfoFragment extends UIQueueFragment {

    private static final String TEAM = "com.tourneynizer.tourneynizer.model.Team";
    private static final String USERS = "com.tourneynizer.tourneynizer.model.User[]";

    private Team team;
    private Tournament tournament;
    private User creator;
    private ListView listView;
    private UserService userService;
    private TournamentService tournamentService;
    private TextView tournamentLabel;
    private TextView creatorLabel;
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
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            team = getArguments().getParcelable(TEAM);
        }
        teamRequestService = new TeamRequestService();
        userService = new UserService();
        tournamentService = new TournamentService();
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
        creatorLabel = view.findViewById(R.id.creatorName);
        userService.getUserFromID(team.getCreatorID(), new UserService.OnUserLoadedListener() {
            @Override
            public void onUserLoaded(final User user) {
                performUITask(new Runnable() {
                    @Override
                    public void run() {
                        creatorLabel.setText("Created by " + user.getName());
                    }
                });
                creatorLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToUserInfo(user);
                    }
                });
            }
        });
        tournamentLabel = view.findViewById(R.id.tournamentName);
        tournamentService.getFromId(team.getTournamentID(), new TournamentService.OnTournamentLoadedListener() {
            @Override
            public void onTournamentLoaded(final Tournament tournament) {
                performUITask(new Runnable() {
                    @Override
                    public void run() {
                        tournamentLabel.setText(tournament.getName());

                    }
                });
                tournamentLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToTournamentInfo(tournament);
                    }
                });
            }
        });
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

    public void goToUserInfo(User u) {
        Fragment fragment = UserProfileFragment.newInstance(u);
        ((RootFragment) getParentFragment()).pushFragment(fragment);
    }

    public void goToTournamentInfo(Tournament t) {
        Fragment fragment = TournamentInfoFragment.newInstance(t);
        ((RootFragment) getParentFragment()).pushFragment(fragment);
    }
}
