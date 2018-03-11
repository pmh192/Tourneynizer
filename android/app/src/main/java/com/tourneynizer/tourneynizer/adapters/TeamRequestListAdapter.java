package com.tourneynizer.tourneynizer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.tourneynizer.tourneynizer.R;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.TeamRequest;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.services.TeamRequestService;
import com.tourneynizer.tourneynizer.services.TeamService;
import com.tourneynizer.tourneynizer.services.UserService;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by ryanwiener on 3/6/18.
 */

public class TeamRequestListAdapter extends ListAdapter<TeamRequest> {

    private TeamRequestService teamRequestService;
    private User self;
    Queue<Runnable> selfLoadedTasks;

    public TeamRequestListAdapter(Context c) {
        super(c, R.layout.team_request_list_item_layout);
        selfLoadedTasks = new LinkedList<>();
        teamRequestService = new TeamRequestService();
        new UserService().getSelf(new UserService.OnUserLoadedListener() {
            @Override
            public void onUserLoaded(User user) {
                self = user;
                synchronized (selfLoadedTasks) {
                    while (!selfLoadedTasks.isEmpty()) {
                        if (getContext() instanceof Activity) {
                            ((Activity) getContext()).runOnUiThread(selfLoadedTasks.poll());
                        }
                    }
                }
            }
        });
    }

    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final TeamRequest teamRequest = getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.team_request_list_item_layout, parent, false);
        }
        final TextView description = convertView.findViewById(R.id.description);
        if (self == null) {
            synchronized (selfLoadedTasks) {
                selfLoadedTasks.add(new Runnable() {
                    @Override
                    public void run() {
                        description.setText(getDescriptionField(teamRequest));
                    }
                });
            }
        } else {
            description.setText(getDescriptionField(teamRequest));
        }
        ImageView accept = convertView.findViewById(R.id.accept);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamRequestService.acceptRequest(teamRequest, new TeamRequestService.OnRequestCompletedListener() {
                    @Override
                    public void onRequestCompleted(VolleyError error) {
                        if (error == null) {
                            onAccept(teamRequest);
                        } else {
                            showAcceptErrorDialogue();
                        }
                    }
                });
            }
        });
        accept.setImageResource(R.drawable.green_check_mark);
        ImageView decline = convertView.findViewById(R.id.decline);
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamRequestService.declineRequest(teamRequest, new TeamRequestService.OnRequestCompletedListener() {
                    @Override
                    public void onRequestCompleted(VolleyError error) {
                        if (error == null) {
                            onDecline(teamRequest);
                        } else {
                            showDeclineErrorDialogue();
                        }
                    }
                });
            }
        });
        decline.setImageResource(R.drawable.red_x);
        return convertView;
    }

    private String getDescriptionField(TeamRequest teamRequest) {
        if (self.getId() == teamRequest.getUserID()) {
            return teamRequest.getRequesterID() + " requested you to join " + teamRequest.getTeamID();
        }
        return teamRequest.getRequesterID() + " requested to join " + teamRequest.getTeamID();
    }

    private void onAccept(final TeamRequest teamRequest) {
        Toast.makeText(getContext(), "You are now a member of " + teamRequest.getTeamID() + "!", Toast.LENGTH_SHORT).show();
        if (getContext() instanceof Activity) {
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    remove(teamRequest);
                }
            });
        }
    }

    private void onDecline(final TeamRequest teamRequest) {
        Toast.makeText(getContext(), "You declined " + teamRequest.getUserID() + "'s request", Toast.LENGTH_SHORT).show();
        if (getContext() instanceof Activity) {
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    remove(teamRequest);
                }
            });
        }
    }

    private void showAcceptErrorDialogue() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Couldn't Accept Request");
        alertDialog.setMessage("Can't accept request. Make sure that you aren't already part of another team for this tournament");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void showDeclineErrorDialogue() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Couldn't Decline Request");
        alertDialog.setMessage("Can't decline request. Make sure that you aren't already part of this team");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
}
