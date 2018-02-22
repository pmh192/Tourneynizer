package com.tourneynizer.tourneynizer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.tourneynizer.tourneynizer.services.TournamentService;

public class TournamentListFragment extends Fragment {

	private final static String TOURNAMENTS = "com.tourneynizer.tourneynizer.model.Tournament[]";

	private TournamentListAdapter listAdapter;
	private SwipeRefreshLayout swipeRefresher;
	private TournamentService tournamentService;

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
		tournamentService = new TournamentService();
		listAdapter = new TournamentListAdapter(getActivity());
		if (savedInstanceState != null) {
		    Parcelable[] tournaments = savedInstanceState.getParcelableArray(TOURNAMENTS);
		    if (tournaments != null) {
				listAdapter.addAll((Tournament[]) tournaments);
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

		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				goToInfo(listAdapter.getItem(i));
			}
		});
		swipeRefresher = view.findViewById(R.id.swipeRefresher);
		swipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				refresh();
			}
		});
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

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArray(TOURNAMENTS, listAdapter.getAll());
	}

	public void refresh() {
		tournamentService.getAllTournaments(new TournamentService.OnTournamentsLoadedListener() {
			@Override
			public void onTournamentsLoaded(final Tournament[] tournaments) {
				listAdapter.clear();
				listAdapter.addAll(tournaments);
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (swipeRefresher != null) {
							swipeRefresher.setRefreshing(false);
						}
					}
				});
			}
		});
	}
}
