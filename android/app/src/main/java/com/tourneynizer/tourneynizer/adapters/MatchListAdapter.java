package com.tourneynizer.tourneynizer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.model.Match;
import com.tourneynizer.tourneynizer.model.Tournament;

/**
 * Created by ryanl on 2/3/2018.
 */

public class MatchListAdapter extends ListAdapter<Match> {

    public MatchListAdapter(Context c) {
        super(c, R.layout.match_list_item_layout);
    }

    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Match match = getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.match_list_item_layout, parent, false);
        }
        TextView title = convertView.findViewById(R.id.title);
        title.setText(match.getTeam1ID() + " vs. " + match.getTeam2ID());
        return convertView;
    }
}
