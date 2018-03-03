package com.tourneynizer.tourneynizer.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.TournamentDef;
import com.tourneynizer.tourneynizer.model.TournamentType;
import com.tourneynizer.tourneynizer.services.TournamentService;

import java.sql.Time;
import java.util.Calendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class CreateTournamentFragment extends Fragment {

	private static final int PLACE_PICKER_REQUEST = 1;

	private Place place;
	private View goToMapButton;
	private TournamentService tournamentService;
	private TextView nameField;
	private TextView description;
	private Spinner tournamentTypes;
	private TextView locationLabel;
	private TextView dateLabel;
	private TextView timeLabel;
	private TextView teamSize;
	private TextView maxTeams;

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
		tournamentService = new TournamentService();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_create_tournament, container, false);
		goToMapButton = view.findViewById(R.id.goToMap);
		goToMapButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				presentPlacePicker();
				//TournamentService.createTournament(getContext());
			}
		});
		tournamentTypes = view.findViewById(R.id.tournamentTypeSpinner);
		ArrayAdapter<TournamentType> spinnerAdapter = new ArrayAdapter<TournamentType>(getContext(), R.layout.spinner_tournament_type_item, R.id.title, TournamentType.values());
		tournamentTypes.setAdapter(spinnerAdapter);
		View dateButton = view.findViewById(R.id.showDatePicker);
		dateLabel = view.findViewById(R.id.startDate);
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
		timeLabel = view.findViewById(R.id.startTime);
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
        nameField = view.findViewById(R.id.tournamentName);
        description = view.findViewById(R.id.tournamentDescription);
        locationLabel = view.findViewById(R.id.locationText);
        teamSize = view.findViewById(R.id.teamSize);
        maxTeams = view.findViewById(R.id.maxTeams);
		View createButton = view.findViewById(R.id.createButton);
		createButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				boolean ready = true;
				if (nameField.getText().toString().equals("")) {
					nameField.setError("Make a name");
					ready = false;
				}
				if (place == null) {
					locationLabel.setError("Select a location");
				}
				String[] dates = dateLabel.getText().toString().split("/");
				String[] indicators = timeLabel.getText().toString().split(" ");
				if (dates.length < 3) {
					dateLabel.setError("Choose a date");
					ready = false;
				}
				if (indicators.length < 2) {
					timeLabel.setError("Select a time");
					ready = false;
				}
				String[] times = indicators[0].split(":");
				if (!teamSize.getText().toString().matches("\\d+")) {
					teamSize.setError("Select a team size");
					ready = false;
				}
				if (!maxTeams.getText().toString().matches("\\d+")) {
					maxTeams.setError("Choose the max num of teams");
					ready = false;
				}
				if (ready) {
					TournamentDef tDef = new TournamentDef();
					tDef.setName(nameField.getText().toString());
					tDef.setDescription(description.getText().toString());
					tDef.setTournamentType(TournamentType.values()[tournamentTypes.getSelectedItemPosition()]);
					tDef.setAddress(place);
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
					tournamentService.createTournament(tDef, new TournamentService.OnTournamentLoadedListener() {
                        @Override
                        public void onTournamentLoaded(Tournament tournament) {
                            if (tournament != null) {
                                Toast.makeText(getContext(), "Tournament was created successfully", Toast.LENGTH_SHORT).show();
                                clearFields();
                            } else {
                                showErrorMessage();
                            }
                        }
                    });
				}
			}
		});
		return view;
	}

    private void showErrorMessage() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Couldn't Create Tournament");
        alertDialog.setMessage("The tournament you tried to make couldn't be created");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

	public void presentPlacePicker() {
		PlacePicker.IntentBuilder tDef = new PlacePicker.IntentBuilder();
		try {
			startActivityForResult(tDef.build(getActivity()), PLACE_PICKER_REQUEST);
			Toast.makeText(getContext(), "Loading...", Toast.LENGTH_SHORT).show();
			goToMapButton.setEnabled(false);
		} catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
            Toast.makeText(getContext(), "Couldn't load place picker", Toast.LENGTH_SHORT).show();
        }
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PLACE_PICKER_REQUEST) {
			goToMapButton.setEnabled(true);
			if (resultCode == RESULT_OK) {
				Place p = PlacePicker.getPlace(getContext(), data);
				place = p;
				locationLabel.setText(place.getName());
				locationLabel.setError(null);
			}
		}
	}

	private void clearFields() {
        nameField.setText(null);
        description.setText(null);
        tournamentTypes.setSelection(0);
        locationLabel.setText(null);
        place = null;
        dateLabel.setText(null);
        timeLabel.setText(null);
        teamSize.setText(null);
        maxTeams.setText(null);
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
