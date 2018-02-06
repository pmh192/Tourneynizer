package com.dreamteam.tourneynizer.adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamteam.tourneynizer.R;
import com.dreamteam.tourneynizer.data.Tournament;

/**
 * Created by ryanl on 2/3/2018.
 */

public class TournamentListAdapter extends ArrayAdapter<Tournament> {

    public TournamentListAdapter(Context context) {
        super(context, R.layout.list_item_layout);
    }

    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Tournament tournament = getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_layout, parent, false);
        }
        // need to reset fields if they are set to null
        ((TextView) convertView.findViewById(R.id.title)).setText(tournament.getName());
        if (tournament.getDescription() != null) {
            ((TextView) convertView.findViewById(R.id.description)).setText(tournament.getDescription());
        } else {
            ((TextView) convertView.findViewById(R.id.description)).setText(null);
        }
        if (tournament.getLogo() != null) {
            ((ImageView) convertView.findViewById(R.id.logo)).setImageBitmap(tournament.getLogo());
        } else {
            ((ImageView) convertView.findViewById(R.id.logo)).setImageBitmap(null);
        }
        return convertView;
    }
}
