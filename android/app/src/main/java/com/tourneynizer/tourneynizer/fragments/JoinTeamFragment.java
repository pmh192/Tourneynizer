package com.tourneynizer.tourneynizer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.model.Tournament;

public class JoinTeamFragment extends Fragment {

    private static final String TOURNAMENT = "com.tourneynizer.tourneynizer.data.Tournament";

    private Tournament tournament;

    public JoinTeamFragment() {
        // Required empty public constructor
    }

    public static JoinTeamFragment newInstance(Tournament t) {
        JoinTeamFragment fragment = new JoinTeamFragment();
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
        View view = inflater.inflate(R.layout.fragment_join_team, container, false);
        ListView teamsList = view.findViewById(R.id.teamList);
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
