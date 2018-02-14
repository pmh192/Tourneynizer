package com.tourneynizer.tourneynizer.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tourneynizer.tourneynizer.R;

public class TournamentRequestFragment extends Fragment {

    public TournamentRequestFragment() {
        // Required empty public constructor
    }

    public static TournamentRequestFragment newInstance() {
        TournamentRequestFragment fragment = new TournamentRequestFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        joinTeam.setOnClickListener(new View.OnClickListener() {
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

    }

    public void goToCreateTeams() {

    }
}
