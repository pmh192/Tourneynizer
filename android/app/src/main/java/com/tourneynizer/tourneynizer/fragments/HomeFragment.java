package com.tourneynizer.tourneynizer.fragments;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.adapters.TeamListAdapter;
import com.tourneynizer.tourneynizer.adapters.TournamentListAdapter;
import com.tourneynizer.tourneynizer.adapters.ListAdapter;
import com.tourneynizer.tourneynizer.adapters.UserListAdapter;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.services.TeamRequestService;
import com.tourneynizer.tourneynizer.services.TeamService;
import com.tourneynizer.tourneynizer.services.TournamentService;

import java.util.ArrayList;


public class HomeFragment extends UIQueueFragment {

	private static final int NUM_SEGMENTS = 3;
	private final ListAdapter[] ADAPTERS = new ListAdapter[NUM_SEGMENTS];
	private static final String[] SAVE_KEYS = new String[] {
		"com.tourneynizer.tourneynizer.model.Team[]",
		"com.tourneynizer.tourneynizer.model.Tournament[]",
		"com.tourneynizer.tourneynizer.model.TeamRequest[]"
	};
    private final int[][] STATES = new int[][] {
        {-android.R.attr.state_checked},
        {android.R.attr.state_checked}
    };
    private final int[] COLORS = new int[] {
        Color.WHITE,
        ResourcesCompat.getColor(getResources(), R.color.colorAccent, null)
    };

	private ListView listView;
	private RadioGroup segmentController;
	private SwipeRefreshLayout refresher;
	private SparseArray<ListAdapter> listAdapters;
	private TeamService teamService;
	private TournamentService tournamentService;
	private TeamRequestService teamRequestService;

	public HomeFragment() {
		// Required empty public constructor
	}

	public static HomeFragment newInstance() {
		HomeFragment fragment = new HomeFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		teamService = new TeamService();
		tournamentService = new TournamentService();
		teamRequestService = new TeamRequestService();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_home, container, false);
        ADAPTERS[0] = new TeamListAdapter(getActivity());
        ADAPTERS[1] = new TournamentListAdapter(getActivity());
        ADAPTERS[2] = new UserListAdapter(getActivity());
		ColorStateList colorStateList = new ColorStateList(STATES, COLORS);
		RadioButton teamSegment = view.findViewById(R.id.myTeams);
		teamSegment.setTextColor(colorStateList);
		RadioButton tournamentSegment = view.findViewById(R.id.myTournaments);
		tournamentSegment.setTextColor(colorStateList);
		RadioButton requestSegment = view.findViewById(R.id.myRequests);
		requestSegment.setTextColor(colorStateList);
		refresher = view.findViewById(R.id.swipeRefresher);
		refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				refreshAll();
			}
		});
		if (savedInstanceState != null) {
			for (int i = 0; i < NUM_SEGMENTS; i++) {
				ArrayList<Parcelable> data = savedInstanceState.getParcelableArrayList(SAVE_KEYS[i]);
				if (data != null) {
					ADAPTERS[i].addAll(data);
				} else {
					refreshAll();
				}
			}
		} else {
			refreshAll();
		}
		listView = view.findViewById(R.id.listView);
		segmentController = view.findViewById(R.id.radioGroup);
		listAdapters = new SparseArray<>();
		for (int i = 0; i < segmentController.getChildCount(); i++) {
			int id = segmentController.getChildAt(i).getId();
			listAdapters.put(id, ADAPTERS[i]);
		}
		listView.setAdapter(listAdapters.get(segmentController.getCheckedRadioButtonId()));
		segmentController.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				listView.setAdapter(listAdapters.get(i));
			}
		});
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		for (int i = 0; i < NUM_SEGMENTS; i++) {
			outState.putParcelableArrayList(SAVE_KEYS[i], ADAPTERS[i].getAll());
		}
	}

	public void refreshAll() {
		refreshTeamAdapter();
		refreshTournamentAdapter();
	}

	public void refreshTeamAdapter() {
		performUITask(new Runnable() {
			@Override
			public void run() {
				ADAPTERS[0].clear();
			}
		});
		teamService.getMyTeams(new TeamService.OnTeamsLoadedListener() {
			@Override
			public void onTeamsLoaded(final Team[] teams) {
				performUITask(new Runnable() {
					@Override
					public void run() {
						ADAPTERS[0].addAll(teams);
						refresher.setRefreshing(false);
					}
				});
			}
		});
	}

	public void refreshTournamentAdapter() {
		performUITask(new Runnable() {
			@Override
			public void run() {
				ADAPTERS[1].clear();
			}
		});
		tournamentService.getAllTournaments(new TournamentService.OnTournamentsLoadedListener() {
			@Override
			public void onTournamentsLoaded(final Tournament[] tournaments) {
				performUITask(new Runnable() {
					@Override
					public void run() {
						ADAPTERS[1].addAll(tournaments);
						refresher.setRefreshing(false);
					}
				});
			}
		});
	}
}
