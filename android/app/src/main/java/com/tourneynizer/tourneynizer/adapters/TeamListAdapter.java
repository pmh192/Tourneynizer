package com.tourneynizer.tourneynizer.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.model.Team;

/**
 * Created by ryanl on 2/3/2018.
 */

public class TeamListAdapter extends ListAdapter<Team> {

    private Activity activity;

    public TeamListAdapter(Activity a) {
        super(a, R.layout.team_list_item_layout);
        activity = a;
    }

    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Team team = getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.team_list_item_layout, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.name)).setText(team.getName());
        ((ImageView) convertView.findViewById(R.id.logo)).setImageBitmap(team.getLogo());
        return convertView;
    }
}
