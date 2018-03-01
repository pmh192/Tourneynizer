package com.tourneynizer.tourneynizer.services;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.TeamDef;
import com.tourneynizer.tourneynizer.util.CookieRequestFactory;
import com.tourneynizer.tourneynizer.util.JSONConverter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ryanwiener on 2/28/18.
 */

public class TeamService {

    public TeamService() {}

    public interface OnTeamLoadedListener {
        public void onTeamLoaded(Team team);
    }

    public interface OnTeamsLoadedListener {
        public void onTeamsLoaded(Team[] team);
    }

    public void createTeam(TeamDef teamDef, final OnTeamLoadedListener listener) {
        String url = HTTPService.DOMAIN + "tournament/" + teamDef.getTournamentID() + "/team/create";
        CookieRequestFactory cookieRequestFactory = new CookieRequestFactory();
        JSONObject body = new JSONObject();
        try {
            body.put("name", teamDef.getName());
        } catch (JSONException e) {
            body = null;
        }
        JsonObjectRequest request = cookieRequestFactory.makeJsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Team team = JSONConverter.getInstance().convertJSONToTeam(response);
                listener.onTeamLoaded(team);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }
}
