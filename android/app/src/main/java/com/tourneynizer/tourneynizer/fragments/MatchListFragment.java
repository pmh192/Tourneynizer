package com.tourneynizer.tourneynizer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.adapters.MatchListAdapter;
import com.tourneynizer.tourneynizer.adapters.TournamentListAdapter;
import com.tourneynizer.tourneynizer.model.Match;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.services.MatchService;
import com.tourneynizer.tourneynizer.services.TournamentService;

import java.util.List;

public class MatchListFragment extends UIQueueFragment {

	private final static String TOURNAMENT = "com.tourneynizer.tourneynizer.model.Tournament";
	private final static String MATCHES = "com.tourneynizer.tourneynizer.model.Match[]";

	private MatchListAdapter listAdapter;
	private SwipeRefreshLayout swipeRefresher;
	private MatchService matchService;
	private Tournament tournament;

	public MatchListFragment() {
		// Required empty public constructor
	}

	public static MatchListFragment newInstance(Tournament t) {
		MatchListFragment fragment = new MatchListFragment();
		Bundle args = new Bundle();
		args.putParcelable(TOURNAMENT, t);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		matchService = new MatchService();
		listAdapter = new MatchListAdapter(getActivity());
		if (getArguments() != null) {
			tournament = getArguments().getParcelable(TOURNAMENT);
		}
		if (savedInstanceState != null) {
			List<Match> matches = savedInstanceState.getParcelableArrayList(MATCHES);
			if (matches != null) {
				listAdapter.addAll(matches);
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
		View view = inflater.inflate(R.layout.fragment_match_list, container, false);
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

	private void goToInfo(Match m) {
		Fragment fragment = MatchInfoFragment.newInstance(m);
		((RootFragment) getParentFragment()).pushFragment(fragment);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArrayList(MATCHES, listAdapter.getAll());
	}

	public void refresh() {
        listAdapter.clear();
        if (swipeRefresher != null) {
			swipeRefresher.setRefreshing(true);
		}
		matchService.getAllMatches(tournament, new MatchService.OnMatchesLoadedListener() {
			@Override
			public void onMatchesLoaded(final Match[] matches) {
			    performUITask(new Runnable() {
                    @Override
                    public void run() {
                        if (matches != null) {
                            listAdapter.addAll(matches);
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
