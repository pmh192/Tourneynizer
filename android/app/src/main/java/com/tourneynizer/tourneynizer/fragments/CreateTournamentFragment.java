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
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.model.TournamentDef;
import com.tourneynizer.tourneynizer.model.TournamentType;
import com.tourneynizer.tourneynizer.requesters.TournamentRequester;

import java.sql.Time;
import java.util.Calendar;
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
		ArrayAdapter<TournamentType> spinnerAdapter = new ArrayAdapter<TournamentType>(getContext(), R.layout.spinner_tournament_type_item, R.id.title, TournamentType.values());
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
						dateLabel.setError(null);
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
						String indicator = "AM";
						if (hour >= 12) {
							indicator = "PM";
						}
						hour %= 12;
						if (hour <= 0) {
							hour = 12;
						}
						if (minute < 10) {
							timeLabel.setText(String.format(Locale.getDefault(), "%d:0%d " + indicator, hour, minute));
						} else {
							timeLabel.setText(String.format(Locale.getDefault(), "%d:%d " + indicator, hour, minute));
						}
						timeLabel.setError(null);
					}
				}, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false);
				timePicker.show();
			}
		});
		View createButton = view.findViewById(R.id.createButton);
		createButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				boolean ready = true;
				TextView nameField = getView().findViewById(R.id.tournamentName);
				if (nameField.getText().toString().equals("")) {
					nameField.setError("Make a name");
					ready = false;
				}
				TextView description = getView().findViewById(R.id.tournamentDescription);
				Spinner tournamentTypeSelector = getView().findViewById(R.id.tournamentTypeSpinner);
				TextView numCourts = getView().findViewById(R.id.numCourts);
				if (!numCourts.getText().toString().matches("\\d+")) {
					numCourts.setError("Enter the number of Courts");
				}
				if (place == null) {
					TextView locationText = getView().findViewById(R.id.locationText);
					locationText.setError("Select a location");
				}
				TextView dateText = getView().findViewById(R.id.startDate);
				TextView timeText = getView().findViewById(R.id.startTime);
				String[] dates = dateText.getText().toString().split("/");
				String[] indicators = timeText.getText().toString().split(" ");
				if (dates.length < 3) {
					dateText.setError("Choose a date");
					ready = false;
				}
				if (indicators.length < 2) {
					timeText.setError("Select a time");
					ready = false;
				}
				String[] times = indicators[0].split(":");
				TextView teamSize = getView().findViewById(R.id.teamSize);
				if (!teamSize.getText().toString().matches("\\d+")) {
					teamSize.setError("Select a team size");
					ready = false;
				}
				TextView maxTeams = getView().findViewById(R.id.maxTeams);
				if (!maxTeams.getText().toString().matches("\\d+")) {
					maxTeams.setError("Choose the max num of teams");
					ready = false;
				}
				if (ready) {
					TournamentDef tDef = new TournamentDef();
					tDef.setName(nameField.getText().toString());
					tDef.setDescription(description.getText().toString());
					tDef.setTournamentType(TournamentType.values()[tournamentTypeSelector.getSelectedItemPosition()]);
					tDef.setAddress(place);
					tDef.setNumCourts(Integer.parseInt(numCourts.getText().toString()));
					Calendar calendar = Calendar.getInstance();
					calendar.clear();
					calendar.set(Calendar.YEAR, Integer.parseInt(dates[2]));
					calendar.set(Calendar.MONTH, Integer.parseInt(dates[0]));
					calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dates[1]));
					int hour = Integer.parseInt(times[0]);
					hour %= 12;
					if (indicators[1].equals("PM")) {
						hour += 12;
					}
					calendar.set(Calendar.HOUR_OF_DAY, hour);
					calendar.set(Calendar.MINUTE, Integer.parseInt(times[1]));
					tDef.setStartTime(new Time(calendar.getTimeInMillis()));
					tDef.setTeamSize(Integer.parseInt(teamSize.getText().toString()));
					tDef.setMaxTeams(Integer.parseInt(maxTeams.getText().toString()));
					TournamentRequester.createTournament(getContext(), tDef);
				}
			}
		});
		return view;
	}

	public void presentPlacePicker() {
		PlacePicker.IntentBuilder tDef = new PlacePicker.IntentBuilder();
		try {
			startActivityForResult(tDef.build(getActivity()), PLACE_PICKER_REQUEST);
			Toast.makeText(getContext(), "Loading...", Toast.LENGTH_SHORT).show();
			getView().findViewById(R.id.goToMap).setEnabled(false);
		} catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PLACE_PICKER_REQUEST) {
			getView().findViewById(R.id.goToMap).setEnabled(true);
			if (resultCode == RESULT_OK) {
				Place p = PlacePicker.getPlace(getContext(), data);
				place = p;
				TextView location = getView().findViewById(R.id.locationText);
				location.setText(place.getName());
				location.setError(null);
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
