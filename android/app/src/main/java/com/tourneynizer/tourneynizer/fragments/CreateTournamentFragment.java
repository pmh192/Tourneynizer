package com.tourneynizer.tourneynizer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.requesters.TournamentRequester;

public class CreateTournamentFragment extends Fragment {

    public CreateTournamentFragment() {
        // Required empty public constructor
    }

    public static CreateTournamentFragment newInstance() {
        CreateTournamentFragment fragment = new CreateTournamentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_tournament, container, false);
        View goToMapButton = view.findViewById(R.id.goToMap);
        goToMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TournamentRequester.createTournament(getContext());
                //((RootFragment) getParentFragment()).pushFragment(TournamentMapSelectorFragment.newInstance());
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
}
