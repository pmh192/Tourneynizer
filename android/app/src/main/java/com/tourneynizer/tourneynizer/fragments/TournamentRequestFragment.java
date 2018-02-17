package com.tourneynizer.tourneynizer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.model.Tournament;

public class TournamentRequestFragment extends Fragment {

    private static final String TOURNAMENT = "com.tourneynizer.tourneynizer.data.Tournament";

    private Tournament tournament;

    public TournamentRequestFragment() {
        // Required empty public constructor
    }

    public static TournamentRequestFragment newInstance(Tournament t) {
        TournamentRequestFragment fragment = new TournamentRequestFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tournament_request, container, false);
        View joinTeam = view.findViewById(R.id.joinTeam);
        joinTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPendingTeams();
            }
        });
        View createTeam = view.findViewById(R.id.createTeam);
        createTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCreateTeams();
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

    public void goToPendingTeams() {
        ((RootFragment) getParentFragment()).pushFragment(JoinTeamFragment.newInstance(tournament));
    }

    public void goToCreateTeams() {
        ((RootFragment) getParentFragment()).pushFragment(CreateTeamFragment.newInstance(tournament));
    }
}
