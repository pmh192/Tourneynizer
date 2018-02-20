package com.tourneynizer.tourneynizer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.model.User;

import java.util.Locale;

public class UserProfileFragment extends Fragment {

    private static final String USER = "com.tourneynizer.tourneynizer.data.User";

    private User user;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance(User user) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(USER);
        }
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
