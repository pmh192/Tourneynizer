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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by ryanwiener on 3/6/18.
 */

public class TeamRequestListAdapter extends ListAdapter<TeamRequest> {

    private TeamRequestService teamRequestService;
    private UserService userService;
    private TeamService teamService;
    private User self;
    private final Queue<Runnable> selfLoadedTasks;
    private List<Team> teams;
    private List<User> users;

    public TeamRequestListAdapter(Context c) {
        super(c, R.layout.team_request_list_item_layout);
        selfLoadedTasks = new LinkedList<>();
        teamRequestService = new TeamRequestService();
        userService = new UserService();
        teamService = new TeamService();
        userService.getSelf(new UserService.OnUserLoadedListener() {
            @Override
            public void onUserLoaded(User user) {
                self = user;
                synchronized (selfLoadedTasks) {
                    while (!selfLoadedTasks.isEmpty()) {
                        selfLoadedTasks.poll().run();
                    }
                }
            }
        });
        users = new ArrayList<>();
        teams = new ArrayList<>();
    }

    @Override
    public @NonNull View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        if (users.size() <= position) {
            users.addAll(Collections.nCopies(position - users.size() + 1, (User) null));
            teams.addAll(Collections.nCopies(position - teams.size() + 1, (Team) null));
        }
        final TeamRequest teamRequest = getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.team_request_list_item_layout, parent, false);
        }
        final TextView descriptionField = convertView.findViewById(R.id.description);
        final ImageView accept = convertView.findViewById(R.id.accept);
        final ImageView decline = convertView.findViewById(R.id.decline);
        if (self == null) {
            synchronized (selfLoadedTasks) {
                selfLoadedTasks.add(new Runnable() {
                    @Override
                    public void run() {
                        loadFields(descriptionField, accept, decline, teamRequest, position);
                    }
                });
            }
        } else {
            loadFields(descriptionField, accept, decline, teamRequest, position);
        }
        accept.setImageResource(R.drawable.green_check_mark);
        decline.setImageResource(R.drawable.red_x);
        return convertView;
    }

    private void loadFields(final TextView descriptionField, final ImageView accept, final ImageView decline, final TeamRequest teamRequest, final int i) {
        if (users.get(i) == null) {
            userService.getUserFromID(teamRequest.getRequesterID(), new UserService.OnUserLoadedListener() {
                @Override
                public void onUserLoaded(User user) {
                    users.set(i, user);
                    setFields(descriptionField, accept, decline, teamRequest, i);
                }
            });
        } else {
            setFields(descriptionField, accept, decline, teamRequest, i);
        }
        if (teams.get(i) == null) {
            teamService.getTeamFromID(teamRequest.getTeamID(), new TeamService.OnTeamLoadedListener() {
                @Override
                public void onTeamLoaded(Team team) {
                    teams.set(i, team);
                    setFields(descriptionField, accept, decline, teamRequest, i);
                }
            });
        } else {
            setFields(descriptionField, accept, decline, teamRequest, i);
        }
    }

    private void setFields(final TextView descriptionField, ImageView accept, ImageView decline, final TeamRequest teamRequest, final int i) {
        if (users.get(i) == null || teams.get(i) == null) {
            return;
        }
        String text = null;
        if (self.getID() == users.get(i).getID()) {
            text = String.format("%s requested you to join %s", users.get(i).getName(), teams.get(i).getName());
        } else {
            text = String.format("%s requested to join %s", users.get(i).getName(), teams.get(i).getName());
        }
        final CharSequence charSequence = text;
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                descriptionField.setText(charSequence);
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamRequestService.acceptRequest(teamRequest, new TeamRequestService.OnRequestCompletedListener() {
                    @Override
                    public void onRequestCompleted(VolleyError error) {
                        if (error == null) {
                            onAccept(teamRequest, i);
                        } else {
                            showAcceptErrorDialogue();
                        }
                    }
                });
            }
        });
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamRequestService.declineRequest(teamRequest, new TeamRequestService.OnRequestCompletedListener() {
                    @Override
                    public void onRequestCompleted(VolleyError error) {
                        if (error == null) {
                            onDecline(teamRequest, i);
                        } else {
                            showDeclineErrorDialogue();
                        }
                    }
                });
            }
        });
    }

    private void onAccept(final TeamRequest teamRequest, int i) {
        String message = String.format("You are now a member of %s!", teams.get(i).getName());
        if (teamRequest.getRequesterID() == teamRequest.getUserID()) {
            message = String.format("%s is now a member of your team!", users.get(i).getName());
        }
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        if (getContext() instanceof Activity) {
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    remove(teamRequest);
                }
            });
        }
    }

    private void onDecline(final TeamRequest teamRequest, int i) {
        Toast.makeText(getContext(), String.format("You declined %s's request", users.get(i).getName()), Toast.LENGTH_SHORT).show();
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
