package com.dreamteam.tourneynizer.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dreamteam.tourneynizer.R;
import com.dreamteam.tourneynizer.adapters.TournamentListAdapter;
import com.dreamteam.tourneynizer.data.Tournament;
import com.dreamteam.tourneynizer.data.TournamentType;

import java.sql.Time;
import java.util.Locale;

public class TournamentListActivity extends AppCompatActivity {

    public static final String INTENT_TOURNAMENT = "com.dreamteam.tournament.data.Tournament";
    private TournamentListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_list);
        ListView listView = findViewById(R.id.tournamentList);

        // sets progress bar to fill list view when empty
        ProgressBar progressBar = new ProgressBar(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        progressBar.setLayoutParams(layoutParams);
        progressBar.setIndeterminate(true);
        ((ViewGroup) listView.getParent()).addView(progressBar);
        listView.setEmptyView(progressBar);

        listAdapter = new TournamentListAdapter(this);
        listView.setAdapter(listAdapter);
        // when available, request tournament info from back end and add Tournament objects to listAdapter
        // must add to listAdapter on UI thread, if having trouble use runOnUiThread(Runnable)
        Address address = new Address(Locale.getDefault());
        address.setAddressLine(0, "5880 W 75th St");
        address.setAddressLine(1, "Los Angeles, CA 90045");
        listAdapter.add(new Tournament(1, "Tournament 1", "A really cool test tournament", address, new Time(new java.util.Date().getTime()), null, 50, new Time(new java.util.Date().getTime()), TournamentType.VOLLEYBALL_POOLED, null, 1, 1, false));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(TournamentInfoActivity.class, listAdapter.getItem(i));
            }
        });
    }

    private void startActivity(Class<?> c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }


    private void startActivity(Class<?> c, Tournament data) {
        Intent intent = new Intent(this, c);
        intent.putExtra(INTENT_TOURNAMENT, data);
        startActivity(intent);
    }
}
