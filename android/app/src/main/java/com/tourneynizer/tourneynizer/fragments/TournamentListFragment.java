package com.tourneynizer.tourneynizer.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
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

import java.util.List;

public class TournamentListFragment extends UIQueueFragment {

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
			List<Tournament> tournaments = savedInstanceState.getParcelableArrayList(TOURNAMENTS);
			if (tournaments != null) {
				listAdapter.addAll(tournaments);
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
		if (getView() != null) {
			return getView();
		}
		View view = inflater.inflate(R.layout.fragment_tournament_list, container, false);
		swipeRefresher = view.findViewById(R.id.swipeRefresher);
		swipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				refresh();
			}
		});
		ListView listView = view.findViewById(R.id.tournamentList);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				goToInfo(listAdapter.getItem(i));
			}
		});
        listView.setAdapter(listAdapter);
		return view;
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
		outState.putParcelableArrayList(TOURNAMENTS, listAdapter.getAll());
	}

	public void refresh() {
        listAdapter.clear();
        if (swipeRefresher != null) {
			swipeRefresher.setRefreshing(true);
		}
		tournamentService.getAllTournaments(new TournamentService.OnTournamentsLoadedListener() {
			@Override
			public void onTournamentsLoaded(final Tournament[] tournaments) {
			    performUITask(new Runnable() {
                    @Override
                    public void run() {
                        if (tournaments != null) {
                            listAdapter.addAll(tournaments);
                        }
                        if (swipeRefresher != null) {
							swipeRefresher.setRefreshing(false);
						}
                    }
                });
			}
		});
	}
}
