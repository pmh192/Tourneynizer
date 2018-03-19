package com.tourneynizer.tourneynizer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.services.TeamService;
import com.tourneynizer.tourneynizer.services.TournamentService;
import com.tourneynizer.tourneynizer.services.UserService;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TournamentInfoFragment extends UIQueueFragment implements OnMapReadyCallback {

	private static final int MAP_ZOOM = 15;
	private static final String TOURNAMENT = "com.tourneynizer.tourneynizer.model.Tournament";
	private static final String USER = "com.tourneynizer.tourneynizer.model.User";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm aa", Locale.getDefault());

	private Tournament tournament;
	private User creator;
	private Team team;
	private UserService userService;
	private TournamentService tournamentService;
	private TeamService teamService;
	private TextView creatorLabel;
	private MapView map;
	private TextView button1;
	private TextView button2;
	private TextView startButton;

	public TournamentInfoFragment() {
		// Required empty public constructor
	}

	public static TournamentInfoFragment newInstance(Tournament t) {
		TournamentInfoFragment fragment = new TournamentInfoFragment();
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
		if (savedInstanceState != null) {
			creator = savedInstanceState.getParcelable(USER);
		}
        userService = new UserService();
		tournamentService = new TournamentService();
		teamService = new TeamService();
	}

	@Override
	public void onStart() {
		super.onStart();
		if (map != null) {
			map.onStart();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (map != null) {
			map.onResume();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (map != null) {
			map.onPause();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (map != null) {
			map.onStop();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (map != null) {
			map.onDestroy();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle saveInstanceState) {
		super.onSaveInstanceState(saveInstanceState);
		if (map != null) {
			map.onSaveInstanceState(saveInstanceState);
		}
		saveInstanceState.putParcelable(USER, creator);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		if (map != null) {
			map.onLowMemory();
		}
	}

	@Override
	public void onMapReady(GoogleMap map) {
		LatLng coordinates = new LatLng(tournament.getAddress().getLatitude(), tournament.getAddress().getLongitude());
		map.addMarker(new MarkerOptions().position(coordinates).title(tournament.getAddress().toString()));
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, MAP_ZOOM));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tournament_info, container, false);
		((TextView) view.findViewById(R.id.tournamentName)).setText(tournament.getName());
        ((TextView) view.findViewById(R.id.tournamentType)).setText(tournament.getTournamentType().toString());
        TextView descriptionField =  view.findViewById(R.id.description);
        descriptionField.setText(tournament.getDescription());
        if (descriptionField.getText().toString().equals("")) {
        	descriptionField.setVisibility(View.GONE);
		}
		map = view.findViewById(R.id.map);
		map.onCreate(savedInstanceState);
		map.getMapAsync(this);
		button1 = view.findViewById(R.id.button1);
		button2 = view.findViewById(R.id.button2);
		if (team == null) {
            teamService.getTeamForTournament(tournament, new TeamService.OnTeamLoadedListener() {
                @Override
                public void onTeamLoaded(Team t) {
                    team = t;
                    setTeamFields();
                }
            });
        } else {
		    setTeamFields();
        }
        startButton = view.findViewById(R.id.startTournament);
		creatorLabel = view.findViewById(R.id.creatorName);
		if (creator == null) {
			userService.getUserFromID(tournament.getCreatorUserID(), new UserService.OnUserLoadedListener() {
				@Override
				public void onUserLoaded(User user) {
					creator = user;
					setCreatorFields();
				}
			});
		} else {
			setCreatorFields();
		}
		((TextView) view.findViewById(R.id.timeRange)).setText(String.format(Locale.getDefault(), "There are %d spots left and will start at %s", tournament.getMaxTeams() - tournament.getCurrentTeams(), DATE_FORMAT.format(tournament.getStartTime())));
		if (tournament.getLogo() != null) {
			((ImageView) view.findViewById(R.id.logo)).setImageBitmap(tournament.getLogo());
		}
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

	public void setCreatorFields() {
		userService.getSelf(new UserService.OnUserLoadedListener() {
			@Override
			public void onUserLoaded(final User self) {
				performUITask(new Runnable() {
					@Override
					public void run() {
						if (creator.equals(self) && !tournament.hasStarted()) {
                            startButton.setVisibility(View.VISIBLE);
						    startButton.setText(R.string.startTournament);
                            startButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startTournament();
                                }
                            });
						} else if (tournament.hasStarted()) {
                            startButton.setVisibility(View.VISIBLE);
						    startButton.setText(R.string.viewMatches);
                            startButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    goToMatchList();
                                }
                            });
						} else {
						    startButton.setVisibility(View.GONE);
                        }
					}
				});
			}
		});
		performUITask(new Runnable() {
			@Override
			public void run() {
				creatorLabel.setText(String.format(Locale.getDefault(), "Created by %s at %s", creator.getName(), DATE_FORMAT.format(tournament.getTimeCreated())));
			}
		});
		creatorLabel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				goToUserInfo(creator);
			}
		});
	}

	public void setTeamFields() {
	    if (team != null) {
            button1.setVisibility(View.VISIBLE);
	        button1.setText(R.string.viewYourTeam);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToTeamInfo();
                }
            });
            button2.setVisibility(View.VISIBLE);
            button2.setText(R.string.viewTeams);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToTeamList(true);
                }
            });
        } else if (!tournament.hasStarted()) {
            button1.setVisibility(View.VISIBLE);
	        button1.setText(R.string.viewPendingTeams);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToTeamList(false);
                }
            });
            button2.setVisibility(View.VISIBLE);
            button2.setText(R.string.createOwnTeam);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToCreateTeam();
                }
            });
        } else {
	        button1.setVisibility(View.GONE);
	        button2.setVisibility(View.VISIBLE);
            button2.setText(R.string.viewTeams);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToTeamList(true);
                }
            });
        }
    }

	public void goToTeamList(boolean complete) {
	    Fragment joinTeamFragment = TeamListFragment.newInstance(tournament, complete);
        ((RootFragment) getParentFragment()).pushFragment(joinTeamFragment);
    }

    public void goToCreateTeam() {
		Fragment createTeamFragment = CreateTeamFragment.newInstance(tournament);
		((RootFragment) getParentFragment()).pushFragment(createTeamFragment);
	}

	public void goToUserInfo(User user) {
	    Fragment fragment = UserProfileFragment.newInstance(user);
        ((RootFragment) getParentFragment()).pushFragment(fragment);
    }

    public void goToTeamInfo() {
	    Fragment fragment = TeamInfoFragment.newInstance(team);
        ((RootFragment) getParentFragment()).pushFragment(fragment);
    }

    public void goToMatchList() {
        Fragment fragment = MatchListFragment.newInstance(tournament);
        ((RootFragment) getParentFragment()).pushFragment(fragment);
    }

    public void startTournament() {
		tournamentService.startTournament(tournament, new TournamentService.OnErrorListener() {
			@Override
			public void onError(VolleyError error) {
				if (error == null) {
					Toast.makeText(getContext(), "The tournament has started!", Toast.LENGTH_SHORT).show();
					goToMatchList();
				} else {
					//TODO: make AlertDialogueFactory in util package to make displaying an alert dialogue much easier
				}
			}
		});
	}
}
