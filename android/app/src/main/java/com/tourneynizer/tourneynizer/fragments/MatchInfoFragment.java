package com.tourneynizer.tourneynizer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.model.Match;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.services.MatchService;
import com.tourneynizer.tourneynizer.services.TeamService;
import com.tourneynizer.tourneynizer.services.TournamentService;
import com.tourneynizer.tourneynizer.services.UserService;

import java.util.Locale;

public class MatchInfoFragment extends UIQueueFragment {

    private static final String MATCH = "com.tourneynizer.tourneynizer.model.Match";

    private Match match;
    private Team team1;
    private Team team2;
    private User self;
    private Team refTeam;
    private View upArrow1;
    private View upArrow2;
    private View downArrow1;
    private View downArrow2;
    private TextView statusButton;
    private TextView teamName1;
    private TextView teamName2;
    private TextView scoreField1;
    private TextView scoreField2;
    private MatchService matchService;
    private TeamService teamService;
    private UserService userService;

    public MatchInfoFragment() {
        // Required empty public constructor
    }

    public static MatchInfoFragment newInstance(Match m) {
        MatchInfoFragment fragment = new MatchInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable(MATCH, m);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            match = getArguments().getParcelable(MATCH);
        }
        matchService = new MatchService();
        teamService = new TeamService();
        userService = new UserService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_match_info, container, false);
        scoreField1 = view.findViewById(R.id.score1);
        scoreField2 = view.findViewById(R.id.score2);
        scoreField1.setText(String.format(Locale.getDefault(), "%d", match.getScore1()));
        scoreField2.setText(String.format(Locale.getDefault(), "%d", match.getScore2()));
        upArrow1 = view.findViewById(R.id.arrowUp1);
        upArrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreField1.setText(String.format(Locale.getDefault(), "%d", Long.parseLong(scoreField1.getText().toString()) + 1));
                updateScore();
            }
        });
        upArrow2 = view.findViewById(R.id.arrowUp2);
        upArrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreField2.setText(String.format(Locale.getDefault(), "%d", Long.parseLong(scoreField2.getText().toString()) + 1));
                updateScore();
            }
        });
        downArrow1 = view.findViewById(R.id.arrowDown1);
        downArrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreField1.setText(String.format(Locale.getDefault(), "%d", Long.parseLong(scoreField1.getText().toString()) - 1));
                updateScore();
            }
        });
        downArrow2 = view.findViewById(R.id.arrowDown2);
        downArrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreField2.setText(String.format(Locale.getDefault(), "%d", Long.parseLong(scoreField2.getText().toString()) - 1));
                updateScore();
            }
        });
        statusButton = view.findViewById(R.id.statusButton);
        if (self == null) {
            userService.getSelf(new UserService.OnUserLoadedListener() {
                @Override
                public void onUserLoaded(User user) {
                    self = user;
                    setRefereeFields();
                }
            });
        } else {
            setRefereeFields();
        }
        if (refTeam == null) {
            teamService.getTeamFromID(match.getRefereeID(), new TeamService.OnTeamLoadedListener() {
                @Override
                public void onTeamLoaded(Team team) {
                    refTeam = team;
                    setRefereeFields();
                }
            });
        } else {
            setRefereeFields();
        }
        teamName1 = view.findViewById(R.id.team1Name);
        teamName2 = view.findViewById(R.id.team2Name);
        if (team1 == null) {
            teamService.getTeamFromID(match.getTeam1ID(), new TeamService.OnTeamLoadedListener() {
                @Override
                public void onTeamLoaded(Team team) {
                    team1 = team;
                    performUITask(new Runnable() {
                        @Override
                        public void run() {
                            teamName1.setText(team1.getName());
                        }
                    });
                    teamName1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            goToTeamInfo(team1);
                        }
                    });
                }
            });
        } else {
            teamName1.setText(team1.getName());
            teamName1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToTeamInfo(team1);
                }
            });
        }
        if (team2 == null) {
            teamService.getTeamFromID(match.getTeam2ID(), new TeamService.OnTeamLoadedListener() {
                @Override
                public void onTeamLoaded(Team team) {
                    team2 = team;
                    performUITask(new Runnable() {
                        @Override
                        public void run() {
                            teamName2.setText(team2.getName());
                        }
                    });
                    teamName2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            goToTeamInfo(team2);
                        }
                    });
                }
            });
        } else {
            teamName2.setText(team2.getName());
            teamName2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToTeamInfo(team2);
                }
            });
        }
        return view;
    }

    private void startMatch() {
        matchService.startMatch(match, new MatchService.OnErrorListener() {
            @Override
            public void onError(VolleyError error) {
                if (error == null) {
                    match.start();
                    Toast.makeText(getContext(), "The match has started!", Toast.LENGTH_SHORT).show();
                    setRefereeFields();
                } else {

                }
            }
        });
    }

    private void getScores() {
        matchService.getScores(match, new MatchService.OnScoresLoadedListener() {
            @Override
            public void onScoresLoaded(Long[] scores) {
                if (scores != null) {
                    match.setScore1(scores[0]);
                    match.setScore2(scores[1]);
                    performUITask(new Runnable() {
                        @Override
                        public void run() {
                            scoreField1.setText(String.format(Locale.getDefault(), "%d", match.getScore1()));
                            scoreField2.setText(String.format(Locale.getDefault(), "%d", match.getScore2()));
                        }
                    });
                }
            }
        });
    }

    private void updateScore() {
        upArrow1.setEnabled(false);
        upArrow2.setEnabled(false);
        downArrow1.setEnabled(false);
        downArrow2.setEnabled(false);
        final long score1 = Long.parseLong(scoreField1.getText().toString());
        final long score2 = Long.parseLong(scoreField2.getText().toString());
        matchService.updateScore(match, score1, score2, new MatchService.OnErrorListener() {
            @Override
            public void onError(VolleyError error) {
                if (error == null) {
                    match.setScore1(score1);
                    match.setScore2(score2);
                    performUITask(new Runnable() {
                        @Override
                        public void run() {
                            upArrow1.setEnabled(true);
                            upArrow2.setEnabled(true);
                            downArrow1.setEnabled(true);
                            downArrow2.setEnabled(true);
                        }
                    });
                }
            }
        });
    }

    private void endMatch() {
        matchService.endMatch(match, new MatchService.OnErrorListener() {
            @Override
            public void onError(VolleyError error) {
                if (error == null) {
                    match.end();
                    setRefereeFields();
                }
            }
        });
    }

    private void setRefereeFields() {
        if (self == null || refTeam == null) {
            return;
        }
        if (self.getID() == refTeam.getCreatorID()) {
            if (match.hasStarted() && !match.hasFinished()) {
                upArrow1.setVisibility(View.VISIBLE);
                upArrow2.setVisibility(View.VISIBLE);
                downArrow1.setVisibility(View.VISIBLE);
                downArrow2.setVisibility(View.VISIBLE);
                statusButton.setVisibility(View.VISIBLE);
                statusButton.setText(R.string.endMatch);
                statusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        endMatch();
                    }
                });
            } else {
                upArrow1.setVisibility(View.GONE);
                upArrow2.setVisibility(View.GONE);
                downArrow1.setVisibility(View.GONE);
                downArrow2.setVisibility(View.GONE);
                if (match.hasFinished()) {
                    statusButton.setVisibility(View.GONE);
                } else {
                    statusButton.setVisibility(View.VISIBLE);
                    statusButton.setText(R.string.startMatch);
                    statusButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startMatch();
                        }
                    });
                }
            }
        } else {
            upArrow1.setVisibility(View.GONE);
            upArrow2.setVisibility(View.GONE);
            downArrow1.setVisibility(View.GONE);
            downArrow2.setVisibility(View.GONE);
            statusButton.setVisibility(View.VISIBLE);
            if (match.hasStarted() && !match.hasFinished()) {
                statusButton.setText(R.string.refreshScores);
                statusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getScores();
                    }
                });
            } else if (match.hasFinished()) {
                statusButton.setVisibility(View.GONE);
            } else {
                statusButton.setText(String.format(Locale.getDefault(), "Waiting for the team creator of %s to start the match", refTeam.getName()));
                statusButton.setOnClickListener(null);
            }
        }
    }

    private void goToTeamInfo(Team t) {
        Fragment fragment = TeamInfoFragment.newInstance(t);
        ((RootFragment)getParentFragment()).pushFragment(fragment);
    }
}
