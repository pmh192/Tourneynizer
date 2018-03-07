package com.tourneynizer.tourneynizer.fragments;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.adapters.TeamListAdapter;
import com.tourneynizer.tourneynizer.adapters.TeamRequestListAdapter;
import com.tourneynizer.tourneynizer.adapters.TournamentListAdapter;
import com.tourneynizer.tourneynizer.adapters.ListAdapter;
import com.tourneynizer.tourneynizer.adapters.UserListAdapter;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.TeamRequest;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.services.TeamRequestService;
import com.tourneynizer.tourneynizer.services.TeamService;
import com.tourneynizer.tourneynizer.services.TournamentService;

import java.util.ArrayList;


public class HomeFragment extends UIQueueFragment {

    private final int NUM_SEGMENTS = 3;
	private final ListAdapter[] ADAPTERS = new ListAdapter[NUM_SEGMENTS];
	private static final String[] SAVE_KEYS = new String[] {
		"com.tourneynizer.tourneynizer.model.Team[]",
		"com.tourneynizer.tourneynizer.model.Tournament[]",
		"com.tourneynizer.tourneynizer.model.TeamRequest[]"
	};
	private final AdapterView.OnItemClickListener[] CLICK_LISTENERS = new AdapterView.OnItemClickListener[NUM_SEGMENTS];

	private ListView listView;
	private RadioGroup segmentController;
	private SwipeRefreshLayout refresher;
	private SparseArray<ListAdapter> listAdapters;
	private SparseArray<AdapterView.OnItemClickListener> listeners;
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
        ADAPTERS[0] = new TeamListAdapter(getContext());
        ADAPTERS[1] = new TournamentListAdapter(getContext());
        ADAPTERS[2] = new TeamRequestListAdapter(getContext());
        CLICK_LISTENERS[0] = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToTeamInfo((Team) ADAPTERS[0].getItem(i));
            }
        };
        CLICK_LISTENERS[1] = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToTournamentInfo((Tournament) ADAPTERS[1].getItem(i));
            }
        };
        CLICK_LISTENERS[2] = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        };
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_home, container, false);
        final int[][] STATES = new int[][] {
            {-android.R.attr.state_checked},
            {android.R.attr.state_checked}
        };
        final int[] COLORS = new int[] {
            Color.WHITE,
            ResourcesCompat.getColor(getResources(), R.color.colorAccent, null)
        };
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
			for (int i = 0; i < SAVE_KEYS.length; i++) {
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
        ProgressBar progressBar = new ProgressBar(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        progressBar.setLayoutParams(layoutParams);
        progressBar.setIndeterminate(true);
        ((ViewGroup) listView.getParent()).addView(progressBar);
        listView.setEmptyView(progressBar);
		segmentController = view.findViewById(R.id.radioGroup);
		listAdapters = new SparseArray<>();
		listeners = new SparseArray<>();
		for (int i = 0; i < ADAPTERS.length; i++) {
			int id = segmentController.getChildAt(i).getId();
			listAdapters.put(id, ADAPTERS[i]);
			listeners.put(id, CLICK_LISTENERS[i]);
		}
		listView.setAdapter(listAdapters.get(segmentController.getCheckedRadioButtonId()));
        listView.setOnItemClickListener(listeners.get(segmentController.getCheckedRadioButtonId()));
		segmentController.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				listView.setAdapter(listAdapters.get(i));
				listView.setOnItemClickListener(listeners.get(i));
			}
		});
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		for (int i = 0; i < SAVE_KEYS.length; i++) {
			outState.putParcelableArrayList(SAVE_KEYS[i], ADAPTERS[i].getAll());
		}
	}

	public void refreshAll() {
		refreshTeamAdapter();
		refreshTournamentAdapter();
		refreshTeamAdapter();
	}

	public void refreshTeamAdapter() {
        ADAPTERS[0].clear();
		teamService.getMyTeams(new TeamService.OnTeamsLoadedListener() {
			@Override
			public void onTeamsLoaded(final Team[] teams) {
				performUITask(new Runnable() {
					@Override
					public void run() {
						ADAPTERS[0].addAll(teams);
						//refresher.setRefreshing(false);
					}
				});
			}
		});
	}

	public void refreshTournamentAdapter() {
        ADAPTERS[1].clear();
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

    public void refreshTeamRequestAdapter() {
        ADAPTERS[1].clear();
        teamRequestService.getRequestsForSelf(new TeamRequestService.OnTeamRequestsLoadedListener() {
            @Override
            public void onTeamRequestsLoaded(final TeamRequest[] teamRequests) {
                performUITask(new Runnable() {
                    @Override
                    public void run() {
                        ADAPTERS[2].addAll(teamRequests);
                        refresher.setRefreshing(false);
                    }
                });
            }
        });
    }

	public void goToTeamInfo(Team t) {
        Fragment fragment = TeamInfoFragment.newInstance(t);
        ((RootFragment) getParentFragment()).pushFragment(fragment);
    }

    public void goToTournamentInfo(Tournament t) {
        Fragment fragment = TournamentInfoFragment.newInstance(t);
        ((RootFragment) getParentFragment()).pushFragment(fragment);
    }
}
