package com.dreamteam.tourneynizer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamteam.tourneynizer.R;
import com.dreamteam.tourneynizer.data.Tournament;

import java.util.Locale;

public class TournamentInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_info);
        Tournament tournament = getIntent().getParcelableExtra(TournamentListActivity.INTENT_TOURNAMENT);
        ((TextView) findViewById(R.id.tournamentName)).setText(tournament.getName());
        if (tournament.getDescription() != null) {
            ((TextView) findViewById(R.id.description)).setText(tournament.getDescription());
        }
        StringBuilder address = new StringBuilder("Tournament will be held at:");
        for (int i = 0; i <= tournament.getAddress().getMaxAddressLineIndex(); i++) {
            address.append('\n').append(tournament.getAddress().getAddressLine(i));
        }
        ((TextView) findViewById(R.id.address)).setText(address.toString());
        // replace with name of user later
        ((TextView) findViewById(R.id.creatorName)).setText(String.format(Locale.getDefault(), "Created by: %d", tournament.getCreatorUserID()));
        ((TextView) findViewById(R.id.timeRange)).setText(String.format(Locale.getDefault(), "Tournament will start at " + tournament.getStartTime().toString()));
        if (tournament.getLogo() != null) {
            ((ImageView) findViewById(R.id.logo)).setImageBitmap(tournament.getLogo());
        } else {
            ((ImageView) findViewById(R.id.logo)).setImageResource(R.mipmap.ic_launcher);
        }
    }
}
