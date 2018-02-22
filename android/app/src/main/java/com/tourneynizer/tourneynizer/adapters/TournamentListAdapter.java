package com.tourneynizer.tourneynizer.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.model.Tournament;

import java.util.Collection;

/**
 * Created by ryanl on 2/3/2018.
 */

public class TournamentListAdapter extends ArrayAdapter<Tournament> {

    private Activity activity;

    public TournamentListAdapter(Activity a) {
        super(a, R.layout.tournament_list_item_layout);
        activity = a;
    }

    public Tournament[] getAll() {
        Tournament[] tournaments = new Tournament[getCount()];
        for (int i = 0; i < getCount(); i++) {
            tournaments[i] = getItem(i);
        }
        return tournaments;
    }

    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Tournament tournament = getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tournament_list_item_layout, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.title)).setText(tournament.getName());
        ((TextView) convertView.findViewById(R.id.description)).setText(tournament.getDescription());
        ((ImageView) convertView.findViewById(R.id.logo)).setImageBitmap(tournament.getLogo());
        return convertView;
    }

    @Override
    public void add(@Nullable final Tournament object) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TournamentListAdapter.super.add(object);
            }
        });
    }

    @Override
    public void addAll(final Tournament... items) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TournamentListAdapter.super.addAll(items);
            }
        });
    }

    @Override
    public void addAll(@NonNull final Collection<? extends Tournament> collection) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TournamentListAdapter.super.addAll(collection);
            }
        });
    }

    @Override
    public void clear() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TournamentListAdapter.super.clear();
            }
        });
    }
}
