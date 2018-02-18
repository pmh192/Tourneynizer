package com.tourneynizer.tourneynizer.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.activities.MainActivity;
import com.tourneynizer.tourneynizer.requesters.TournamentRequester;

import static android.app.Activity.RESULT_OK;

public class CreateTournamentFragment extends Fragment {

    private static final int PLACE_PICKER_REQUEST = 1;

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
                //presentPlacePicker();
                TournamentRequester.createTournament(getContext());
            }
        });
        return view;
    }

    public void presentPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getContext(), data);
            }
        }
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
