package com.tourneynizer.tourneynizer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.adapters.TournamentListAdapter;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.requesters.TournamentRequester;

public class TournamentListFragment extends Fragment {

	private TournamentListAdapter listAdapter;

	public TournamentListFragment() {
		// Required empty public constructor
	}

	public static TournamentListFragment newInstance() {
		TournamentListFragment fragment = new TournamentListFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_tournament_list, container, false);
		ListView listView = view.findViewById(R.id.tournamentList);

		// sets progress bar to fill list view when empty
		ProgressBar progressBar = new ProgressBar(getContext());
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
		layoutParams.gravity = Gravity.CENTER;
		progressBar.setLayoutParams(layoutParams);
		progressBar.setIndeterminate(true);
		((ViewGroup) listView.getParent()).addView(progressBar);
		listView.setEmptyView(progressBar);

		listAdapter = new TournamentListAdapter(getContext());
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				goToInfo(listAdapter.getItem(i));
			}
		});
		// when available, request tournament info from back end and add Tournament objects to listAdapter
		// must add to listAdapter on UI thread, if having trouble use runOnUiThread(Runnable)
		// all lines in this function will be deleted after back end is exposed so you can disregard
        TournamentRequester.getAllTournaments(getContext(), new TournamentRequester.OnTournamentLoadedListener() {
            @Override
            public void onTournamentLoaded(Tournament t) {
                listAdapter.add(t);
            }
        });
        /*
		Address a = new Address(Locale.getDefault());
		a.setLongitude(45);
		a.setLatitude(45);
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
		listAdapter.add(new Tournament(1, "Tournament 1", "A really cool test tournament", a, new Time(new java.util.Date().getTime()), null, 50, 0, new Time(new java.util.Date().getTime()), TournamentType.VOLLEYBALL_POOLED, null, 1, 1, false));
		listAdapter.add(new Tournament(1, "Tournament 2", "A really cool test tournament with a logo", a, new Time(new java.util.Date().getTime()), null, 50, 0, new Time(new java.util.Date().getTime()), TournamentType.VOLLEYBALL_POOLED, logo, 1, 1, false));
		*/
		return view;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}

	private void goToInfo(Tournament tournament) {
		TournamentInfoFragment tournamentInfoFragment = TournamentInfoFragment.newInstance(tournament);
		((RootFragment) getParentFragment()).pushFragment(tournamentInfoFragment);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}
}
