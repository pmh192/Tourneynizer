package com.tourneynizer.tourneynizer.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.data.Tournament;

public class CreateTeamFragment extends Fragment {

    private static final String TOURNAMENT = "com.tourneynizer.tourneynizer.data.Tournament";

    private Tournament tournament;

    public CreateTeamFragment() {
        // Required empty public constructor
    }

    public static CreateTeamFragment newInstance(Tournament t) {
        CreateTeamFragment fragment = new CreateTeamFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_team, container, false);
        View createButton = view.findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUserRequest();
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

    public void goToUserRequest() {

    }
}
