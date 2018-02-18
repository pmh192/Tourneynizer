package com.tourneynizer.tourneynizer.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.model.TournamentDef;
import com.tourneynizer.tourneynizer.model.TournamentType;
import com.tourneynizer.tourneynizer.requesters.TournamentRequester;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class CreateTournamentFragment extends Fragment {

    private static final int PLACE_PICKER_REQUEST = 1;

    private Place place;

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
                presentPlacePicker();
                //TournamentRequester.createTournament(getContext());
            }
        });
        Spinner tournamentTypes = view.findViewById(R.id.tournamentTypeSpinner);
        ArrayAdapter<TournamentType> spinnerAdapter = new ArrayAdapter<TournamentType>(getContext(), android.R.layout.simple_spinner_item, TournamentType.values());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tournamentTypes.setAdapter(spinnerAdapter);
        View dateButton = view.findViewById(R.id.showDatePicker);
        final TextView dateLabel = view.findViewById(R.id.startDate);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        dateLabel.setText(String.format(Locale.getDefault(), "%d/%d/%d", month, day, year));
                    }
                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                datePicker.show();
            }
        });
        View timeButton = view.findViewById(R.id.showTimePicker);
        final TextView timeLabel = view.findViewById(R.id.startTime);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        if (minute < 10) {
                            timeLabel.setText(String.format(Locale.getDefault(), "%d:0%d", hour, minute));
                        } else {
                            timeLabel.setText(String.format(Locale.getDefault(), "%d:%d", hour, minute));
                        }
                    }
                }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false);
                timePicker.show();
            }
        });
        View createButton = view.findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TournamentDef tDef = new TournamentDef();
                TextView nameField = getView().findViewById(R.id.tournamentName);
                tDef.setName(nameField.getText().toString());
                TextView description = getView().findViewById(R.id.tournamentDescription);
                Spinner tournamentTypeSelector = getView().findViewById(R.id.tournamentTypeSpinner);
                tDef.setTournamentType(TournamentType.values()[tournamentTypeSelector.getSelectedItemPosition()]);
                tDef.setAddress(place);
                TextView dateText = getView().findViewById(R.id.startDate);
                TextView timeText = getView().findViewById(R.id.startTime);
                String[] dates = dateText.getText().toString().split("/");
                String[] times = timeText.getText().toString().split(":");
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(Calendar.YEAR, Integer.parseInt(dates[0]));
                calendar.set(Calendar.MONTH, Integer.parseInt(dates[1]));
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dates[2]));
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(times[1]));
                tDef.setStartTime(new Time(calendar.getTimeInMillis()));
                TextView teamSize = getView().findViewById(R.id.teamSize);
                tDef.setTeamSize(Integer.parseInt(teamSize.getText().toString()));
                TextView maxTeams = getView().findViewById(R.id.maxTeams);
                tDef.setMaxTeams(Integer.parseInt(maxTeams.getText().toString()));
                TournamentRequester.createTournament(getContext(), tDef);
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
                Place p = PlacePicker.getPlace(getContext(), data);
                place = p;
                TextView location = getView().findViewById(R.id.locationText);
                location.setText(place.getName());
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
