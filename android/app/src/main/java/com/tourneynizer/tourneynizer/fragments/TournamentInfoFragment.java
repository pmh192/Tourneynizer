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

import java.util.Locale;

public class TournamentInfoFragment extends Fragment implements OnMapReadyCallback {

	private final int MAP_ZOOM = 15;
	private static final String TOURNAMENT = "com.tourneynizer.tourneynizer.Tournament";

	private Tournament tournament;
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
        ((TextView) view.findViewById(R.id.description)).setText(tournament.getDescription());
		map = view.findViewById(R.id.map);
		map.onCreate(savedInstanceState);
		map.getMapAsync(this);
		// replace with name of user later
		((TextView) view.findViewById(R.id.creatorName)).setText(String.format(Locale.getDefault(), "Created by %d at " + tournament.getTimeCreated().toString(), tournament.getCreatorUserID()));
		((TextView) view.findViewById(R.id.timeRange)).setText(String.format(Locale.getDefault(), "There are %d spots left and will start at " + tournament.getStartTime().toString(), tournament.getMaxTeams() - tournament.getCurrentTeams()));
		if (tournament.getLogo() != null) {
			((ImageView) view.findViewById(R.id.logo)).setImageBitmap(tournament.getLogo());
		}
		View requestTournament = view.findViewById(R.id.requestButton);
		requestTournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToTournamentRequest();
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

	public void goToTournamentRequest() {
	    TournamentRequestFragment tournamentRequestFragment = TournamentRequestFragment.newInstance(tournament);
        ((RootFragment) getParentFragment()).pushFragment(tournamentRequestFragment);
    }
}
