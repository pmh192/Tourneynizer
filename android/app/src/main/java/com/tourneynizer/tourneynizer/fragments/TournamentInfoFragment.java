package com.tourneynizer.tourneynizer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.services.TournamentService;
import com.tourneynizer.tourneynizer.services.UserService;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TournamentInfoFragment extends UIQueueFragment implements OnMapReadyCallback {

	private final int MAP_ZOOM = 15;
	private static final String TOURNAMENT = "com.tourneynizer.tourneynizer.Tournament";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm aa", Locale.getDefault());

	private Tournament tournament;
	private User creator;
	private UserService userService;
	private TextView creatorLabel;
	private MapView map;

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
        userService = new UserService();
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
		map.addMarker(new MarkerOptions().position(coordinates).title(tournament.getName()));
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, MAP_ZOOM)); // used magic number for map zoom, can change if needed
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tournament_info, container, false);
		((TextView) view.findViewById(R.id.tournamentName)).setText(tournament.getName());
        ((TextView) view.findViewById(R.id.tournamentType)).setText(tournament.getTournamentType().toString());
        ((TextView) view.findViewById(R.id.description)).setText(tournament.getDescription());
		map = view.findViewById(R.id.map);
		map.onCreate(savedInstanceState);
		map.getMapAsync(this);
		// replace with name of user later
		creatorLabel = view.findViewById(R.id.creatorName);
		userService.getUserFromID(tournament.getCreatorUserID(), new UserService.OnUserLoadedListener() {
            @Override
            public void onUserLoaded(final User user) {
                performUITask(new Runnable() {
                    @Override
                    public void run() {
                        creatorLabel.setText(String.format(Locale.getDefault(), "Created by %s at %s", user.getName(), DATE_FORMAT.format(tournament.getTimeCreated())));
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
		((TextView) view.findViewById(R.id.timeRange)).setText(String.format(Locale.getDefault(), "There are %d spots left and will start at %s", tournament.getMaxTeams() - tournament.getCurrentTeams(), DATE_FORMAT.format(tournament.getStartTime())));
		if (tournament.getLogo() != null) {
			((ImageView) view.findViewById(R.id.logo)).setImageBitmap(tournament.getLogo());
		}
		View joinTeam = view.findViewById(R.id.joinTeam);
		joinTeam.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				goToJoinTeam();
			}
		});
		View createTeam = view.findViewById(R.id.createTeam);
		createTeam.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				goToCreateTeam();
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

	public void goToJoinTeam() {
	    Fragment joinTeamFragment = JoinTeamFragment.newInstance(tournament);
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
}
