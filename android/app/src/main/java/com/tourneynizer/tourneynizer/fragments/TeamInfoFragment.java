package com.tourneynizer.tourneynizer.fragments;


import android.content.DialogInterface;
import android.media.MediaRouter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TeamInfoFragment extends UIQueueFragment {

    private static final String TEAM = "com.tourneynizer.tourneynizer.model.Team";
    private static final String USERS = "com.tourneynizer.tourneynizer.model.User[]";

    private Team team;
    private Team selfTeam;
    private Tournament tournament;
    private User creator;
    private User self;
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
    private SwipeRefreshLayout swipeRefresher;

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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToUserInfo(listAdapter.getItem(i));
            }
        });
        ImageView logoView = view.findViewById(R.id.logo);
        logoView.setImageBitmap(team.getLogo());
        TextView teamName = view.findViewById(R.id.teamName);
        teamName.setText(team.getName());
        swipeRefresher = view.findViewById(R.id.swipeRefresher);
        swipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        requestButton1 = view.findViewById(R.id.requestButton1);
        requestButton2 = view.findViewById(R.id.requestButton2);
        creatorLabel = view.findViewById(R.id.creatorName);
        tournamentLabel = view.findViewById(R.id.tournamentName);
        if (creator == null) {
            userService.getUserFromID(team.getCreatorID(), new UserService.OnUserLoadedListener() {
                @Override
                public void onUserLoaded(User user) {
                    creator = user;
                    setCreatorFields();
                    setButtons();
                }
            });
        } else {
            setCreatorFields();
            setButtons();
        }
        if (tournament == null) {
            tournamentService.getFromId(team.getTournamentID(), new TournamentService.OnTournamentLoadedListener() {
                @Override
                public void onTournamentLoaded(Tournament t) {
                    tournament = t;
                    setTournamentFields();
                    if (selfTeam == null) {
                        teamService.getTeamForTournament(tournament, new TeamService.OnTeamLoadedListener() {
                            @Override
                            public void onTeamLoaded(Team otherTeam) {
                                selfTeam = otherTeam;
                                setButtons();
                            }
                        });
                    } else {
                        setButtons();
                    }
                }
            });
        } else {
            setTournamentFields();
            setButtons();
        }
        if (self == null) {
            userService.getSelf(new UserService.OnUserLoadedListener() {
                @Override
                public void onUserLoaded(User user) {
                    self = user;
                    setButtons();
                }
            });
        } else {
            setButtons();
        }
        return view;
    }

    public void refresh() {
        if (swipeRefresher != null) {
            swipeRefresher.setRefreshing(true);
        }
        teamService.getTeamMembers(team, new TeamService.OnUsersLoadedListener() {
            @Override
            public void onUsersLoaded(final User[] users) {
                performUITask(new Runnable() {
                    @Override
                    public void run() {
                        listAdapter.addAll(users);
                        if (swipeRefresher != null) {
                            swipeRefresher.setRefreshing(false);
                        }
                    }
                });
            }
        });
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

    // I don't believe this needs to be synchronized, but double check if you like
    public void setButtons() {
        if (tournament == null) {
            return;
        }
        if (tournament.hasStarted()) {
            requestButton1.setVisibility(View.GONE);
            requestButton2.setVisibility(View.GONE);
        }
        if (self == null) {
            return;
        }
        if (self.equals(creator)) {
            requestButton1.setVisibility(View.VISIBLE);
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
        } else if (selfTeam != null) {
            requestButton1.setVisibility(View.GONE);
            requestButton2.setVisibility(View.GONE);
        } else {
            requestButton1.setVisibility(View.VISIBLE);
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

    public void setCreatorFields() {
        performUITask(new Runnable() {
            @Override
            public void run() {
                creatorLabel.setText(String.format(Locale.getDefault(), "Created by %s", creator.getName()));
            }
        });
        creatorLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUserInfo(creator);
            }
        });
    }

    public void setTournamentFields() {
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
