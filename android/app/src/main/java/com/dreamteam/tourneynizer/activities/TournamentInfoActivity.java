package com.dreamteam.tourneynizer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamteam.tourneynizer.R;
import com.dreamteam.tourneynizer.data.Tournament;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

public class TournamentInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Tournament tournament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_info);
        tournament = getIntent().getParcelableExtra(TournamentListActivity.INTENT_TOURNAMENT);
        ((TextView) findViewById(R.id.tournamentName)).setText(tournament.getName());
        if (tournament.getDescription() != null) {
            ((TextView) findViewById(R.id.description)).setText(tournament.getDescription());
        }
        MapView map = findViewById(R.id.map);
        map.onCreate(savedInstanceState);
        map.getMapAsync(this);
        // replace with name of user later
        ((TextView) findViewById(R.id.creatorName)).setText(String.format(Locale.getDefault(), "Created by %d at " + tournament.getTimeCreated().toString(), tournament.getCreatorUserID()));
        ((TextView) findViewById(R.id.timeRange)).setText(String.format(Locale.getDefault(), "There are %d spots left and will start at " + tournament.getStartTime().toString(), tournament.getMaxTeams() - tournament.getCurrentTeams()));
        if (tournament.getLogo() != null) {
            ((ImageView) findViewById(R.id.logo)).setImageBitmap(tournament.getLogo());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        MapView map = findViewById(R.id.map);
        map.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        MapView map = findViewById(R.id.map);
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        MapView map = findViewById(R.id.map);
        map.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        MapView map = findViewById(R.id.map);
        map.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MapView map = findViewById(R.id.map);
        map.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);
        MapView map = findViewById(R.id.map);
        map.onSaveInstanceState(saveInstanceState);
    }

    @Override
    public void onLowMemory() {
        MapView map = findViewById(R.id.map);
        map.onStart();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng coordinates = new LatLng(tournament.getAddress().getLatitude(), tournament.getAddress().getLongitude());
        map.addMarker(new MarkerOptions().position(coordinates).title(tournament.getName()));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15)); // used magic number for map zoom, can change if needed
    }
}
