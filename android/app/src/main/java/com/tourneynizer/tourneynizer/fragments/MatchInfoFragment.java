package com.tourneynizer.tourneynizer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.model.Match;

public class MatchInfoFragment extends Fragment {

    private static final String MATCH = "com.tourneynizer.tourneynizer.model.Match";

    private Match match;


    public MatchInfoFragment() {
        // Required empty public constructor
    }

    public static MatchInfoFragment newInstance(Match m) {
        MatchInfoFragment fragment = new MatchInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable(MATCH, m);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            match = getArguments().getParcelable(MATCH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_match_info, container, false);
        return view;
    }

}
