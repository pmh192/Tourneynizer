package com.tourneynizer.tourneynizer.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.TeamRequest;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.services.TeamRequestService;

import java.util.Locale;

public class UserProfileFragment extends Fragment {

    private static final String USER = "com.tourneynizer.tourneynizer.data.User";
    private static final String TEAM = "com.tourneynizer.tourneynizer.data.Team";

    private User user;
    private Team team;
    private TeamRequestService teamRequestService;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance(User user) {
        return newInstance(user, null);
    }

    public static UserProfileFragment newInstance(User user, Team t) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(USER, user);
        args.putParcelable(TEAM, t);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(USER);
            team = getArguments().getParcelable(TEAM);
        }
        teamRequestService = new TeamRequestService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        TextView name = view.findViewById(R.id.name);
        name.setText(user.getName());
        TextView email = view.findViewById(R.id.email);
        email.setText(user.getEmail());
        ImageView profilePicture = view.findViewById(R.id.profilePicture);
        profilePicture.setImageBitmap(user.getProfilePicture());
        TextView tournamentsParticipated = view.findViewById(R.id.tournamentsParticipated);
        tournamentsParticipated.setText(String.format(Locale.getDefault(),"%d", user.getTournamentsParticipated()));
        TextView gamesWon = view.findViewById(R.id.gamesWon);
        gamesWon.setText(String.format(Locale.getDefault(), "%d", user.getWins()));
        TextView gamesLost = view.findViewById(R.id.gamesLost);
        gamesLost.setText(String.format(Locale.getDefault(), "%d", user.getLosses()));
        final View requestButton = view.findViewById(R.id.registerButton);
        if (team == null) {
            requestButton.setVisibility(View.GONE);
        } else {
            requestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    teamRequestService.sendRequestToUser(team, user, new TeamRequestService.OnRequestCompletedListener() {
                        @Override
                        public void onRequestCompleted(VolleyError error) {
                            if (error == null) {
                                Toast.makeText(getContext(), "Request was sent to " + user.getName(), Toast.LENGTH_SHORT).show();
                                requestButton.setVisibility(View.GONE);
                            } else {
                                showErrorDialogue();
                            }
                        }
                    });
                }
            });
        }
        return view;
    }

    private void showErrorDialogue() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Couldn't Send Request");
        alertDialog.setMessage("Can't send a request to " + user.getName() + ". Make sure that they aren't already part of another team for this tournament");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
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
