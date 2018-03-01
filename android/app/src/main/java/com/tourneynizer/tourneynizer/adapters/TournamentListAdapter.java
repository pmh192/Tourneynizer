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

public class TournamentListAdapter extends UIListAdapter<Tournament> {

    private Activity activity;

    public TournamentListAdapter(Activity a) {
        super(a, R.layout.tournament_list_item_layout);
        activity = a;
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
}
