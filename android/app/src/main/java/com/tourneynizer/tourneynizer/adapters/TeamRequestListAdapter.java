package com.tourneynizer.tourneynizer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.model.TeamRequest;
import com.tourneynizer.tourneynizer.services.TeamRequestService;

/**
 * Created by ryanwiener on 3/6/18.
 */

public class TeamRequestListAdapter extends ListAdapter<TeamRequest> {

    private TeamRequestService teamRequestService;

    public TeamRequestListAdapter(Context c) {
        super(c, R.layout.team_request_list_item_layout);
        teamRequestService = new TeamRequestService();
    }

    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final TeamRequest teamRequest = getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tournament_list_item_layout, parent, false);
        }
        TextView description = convertView.findViewById(R.id.description);
        description.setText(teamRequest.getRequesterID() + " requested you to join " + teamRequest.getTeamID());
        View accept = convertView.findViewById(R.id.accept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamRequestService.acceptRequestForTeam(teamRequest);
            }
        });
        View decline = convertView.findViewById(R.id.decline);
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamRequestService.declineRequest(teamRequest);
            }
        });
        return convertView;
    }
}