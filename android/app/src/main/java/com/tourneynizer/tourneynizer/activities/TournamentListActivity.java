package com.tourneynizer.tourneynizer.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.adapters.TournamentListAdapter;
import com.tourneynizer.tourneynizer.data.Tournament;
import com.tourneynizer.tourneynizer.data.TournamentType;

import java.io.IOException;
import java.sql.Time;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TournamentListActivity extends AppCompatActivity {

    public static final String INTENT_TOURNAMENT = "com.dreamteam.tournament.data.Tournament";
    private TournamentListAdapter listAdapter;

    // will delete these members when back end is exposed please disregard
    private Geocoder geocoder;
    private List<Address> a1;
    private List<Address> a2;
    private Bitmap logo;

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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(TournamentInfoActivity.class, listAdapter.getItem(i));
            }
        });
        // when available, request tournament info from back end and add Tournament objects to listAdapter
        // must add to listAdapter on UI thread, if having trouble use runOnUiThread(Runnable)
        // all lines in this function will be deleted after back end is exposed so you can disregard
        geocoder = new Geocoder(this);
        Timer pretendLoad = new Timer();
        pretendLoad.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    a1 = geocoder.getFromLocationName("5880 W 75th St, Los Angeles, CA 90045", 1);
                    a2 = geocoder.getFromLocationName("796 Embarcadero del Norte, Isla Vista, CA 93117", 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                logo = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listAdapter.add(new Tournament(1, "Tournament 1", "A really cool test tournament", a1.get(0), new Time(new java.util.Date().getTime()), null, 50, 0, new Time(new java.util.Date().getTime()), TournamentType.VOLLEYBALL_POOLED, null, 1, 1, false));
                        listAdapter.add(new Tournament(1, "Tournament 2", "A really cool test tournament with a logo", a2.get(0), new Time(new java.util.Date().getTime()), null, 50, 0, new Time(new java.util.Date().getTime()), TournamentType.VOLLEYBALL_POOLED, logo, 1, 1, false));
                    }
                });
            }
        }, 1500);
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
