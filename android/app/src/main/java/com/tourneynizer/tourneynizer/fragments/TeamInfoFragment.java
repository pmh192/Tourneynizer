package com.tourneynizer.tourneynizer.fragments;


import android.content.DialogInterface;
import android.media.MediaRouter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.adapters.TeamRequestListAdapter;
import com.tourneynizer.tourneynizer.adapters.UserListAdapter;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.services.TeamRequestService;
import com.tourneynizer.tourneynizer.services.TeamService;
import com.tourneynizer.tourneynizer.services.TournamentService;
import com.tourneynizer.tourneynizer.services.UserService;

import java.util.List;
import java.util.Locale;

public class TeamInfoFragment extends UIQueueFragment {

    private static final String TEAM = "com.tourneynizer.tourneynizer.model.Team";
    private static final String USERS = "com.tourneynizer.tourneynizer.model.User[]";

    private Team team;
    private Tournament tournament;
    private User creator;
    private ListView listView;
    private UserService userService;
    private TournamentService tournamentService;
    private TeamService teamService;
    private TextView tournamentLabel;
    private TextView creatorLabel;
    private TextView requestButton1;
    private TextView requestButton2;
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
        teamService = new TeamService();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_team_info, container, false);
        listView = view.findViewById(R.id.memberList);
        listView.setAdapter(listAdapter);
        ImageView logoView = view.findViewById(R.id.logo);
        logoView.setImageBitmap(team.getLogo());
        TextView teamName = view.findViewById(R.id.teamName);
        teamName.setText(team.getName());
        requestButton1 = view.findViewById(R.id.requestButton1);
        requestButton2 = view.findViewById(R.id.requestButton2);
        creatorLabel = view.findViewById(R.id.creatorName);
        userService.getUserFromID(team.getCreatorID(), new UserService.OnUserLoadedListener() {
            @Override
            public void onUserLoaded(final User user) {
                userService.getSelf(new UserService.OnUserLoadedListener() {
                    @Override
                    public void onUserLoaded(final User self) {
                        if (self.equals(user)) {
                            requestButton1.setText(R.string.viewRequests);
                            requestButton1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    goToTeamRequests();
                                }
                            });
                            requestButton2.setVisibility(View.VISIBLE);
                            requestButton2.setText(R.string.requestPlayers);
                            requestButton2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    goToUserList();
                                }
                            });
                        } else {
                            requestButton1.setText(R.string.requestToJoin);
                            requestButton1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    teamRequestService.sendRequestToTeam(team, new TeamRequestService.OnRequestCompletedListener() {
                                        @Override
                                        public void onRequestCompleted(VolleyError error) {
                                            if (error == null) {
                                                Toast.makeText(getContext(), "Request sent to " + team.getName(), Toast.LENGTH_SHORT).show();
                                                requestButton1.setVisibility(View.GONE);
                                            } else {
                                                showErrorDialogue();
                                            }
                                        }
                                    });
                                }
                            });
                            requestButton2.setVisibility(View.GONE);
                        }
                    }
                });
                performUITask(new Runnable() {
                    @Override
                    public void run() {
                        creatorLabel.setText(String.format(Locale.getDefault(), "Created by %s", user.getName()));
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

    public void goToTeamRequests() {
        Fragment fragment = TeamRequestListFragment.newInstance(team);
        ((RootFragment) getParentFragment()).pushFragment(fragment);
    }

    public void goToUserList() {
        Fragment fragment = SearchFragment.newInstance(team);
        ((RootFragment) getParentFragment()).pushFragment(fragment);
    }

    private void showErrorDialogue() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Couldn't Send Request");
        alertDialog.setMessage("Can't send request. Make sure that you haven't already sent a request to this team");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
}
