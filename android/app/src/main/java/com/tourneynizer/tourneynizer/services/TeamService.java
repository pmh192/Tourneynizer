package com.tourneynizer.tourneynizer.services;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.TeamRequest;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.util.CookieRequestFactory;
import com.tourneynizer.tourneynizer.util.JSONConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.Time;

/**
 * Created by ryanwiener on 2/28/18.
 */

public class TeamService {

    public TeamService() {}

    public interface OnTeamLoadedListener {
        public void onTeamLoaded(Team team);
    }

    public interface OnTeamsLoadedListener {
        public void onTeamsLoaded(Team[] teams);
    }

    public void createTeam(Tournament t, String teamName, final OnTeamLoadedListener listener) {
        String url = HTTPService.DOMAIN + "tournament/" + t.getID() + "/team/create";
        CookieRequestFactory cookieRequestFactory = new CookieRequestFactory();
        JSONObject body = new JSONObject();
        try {
            body.put("name", teamName);
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
                HTTPService.errorPrinterHelper(error);
                listener.onTeamLoaded(null);
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }

    public void getMyTeams(final OnTeamsLoadedListener listener) {
        String url = HTTPService.DOMAIN + "team/getAll";
        CookieRequestFactory cookieRequestFactory = new CookieRequestFactory();
        Request request = cookieRequestFactory.makeJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Team[] teams = new Team[response.length()];
                try {
                    for (int i = 0; i < response.length(); i++) {
                        teams[i] = JSONConverter.getInstance().convertJSONToTeam(response.getJSONObject(i));
                    }
                } catch (JSONException e) {
                    teams = null;
                }
                listener.onTeamsLoaded(teams);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onTeamsLoaded(null);
                HTTPService.errorPrinterHelper(error);
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }

    public void getTeams(Tournament t, final OnTeamsLoadedListener listener) {
        String url = HTTPService.DOMAIN + "tournament/" + t.getID() + "/team/all";
        CookieRequestFactory cookieRequestFactory = new CookieRequestFactory();
        JsonArrayRequest request = cookieRequestFactory.makeJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Team[] teams = new Team[response.length()];
                try {
                    for (int i = 0; i < response.length(); i++) {
                        teams[i] = JSONConverter.getInstance().convertJSONToTeam(response.getJSONObject(i));
                    }
                } catch (JSONException e) {
                    teams = null;
                }
                listener.onTeamsLoaded(teams);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTTPService.errorPrinterHelper(error);
                listener.onTeamsLoaded(null);
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }

    public void getCompleteTeams(Tournament t, final OnTeamsLoadedListener listener) {
        String url = HTTPService.DOMAIN + "tournament/" + t.getID() + "/team/complete";
        CookieRequestFactory cookieRequestFactory = new CookieRequestFactory();
        JsonArrayRequest request = cookieRequestFactory.makeJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Team[] teams = new Team[response.length()];
                try {
                    for (int i = 0; i < response.length(); i++) {
                        teams[i] = JSONConverter.getInstance().convertJSONToTeam(response.getJSONObject(i));
                    }
                } catch (JSONException e) {
                    teams = null;
                }
                listener.onTeamsLoaded(teams);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTTPService.errorPrinterHelper(error);
                listener.onTeamsLoaded(null);
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }

    public void getPendingTeams(Tournament t, final OnTeamsLoadedListener listener) {
        String url = HTTPService.DOMAIN + "tournament/" + t.getID() + "/team/incomplete";
        CookieRequestFactory cookieRequestFactory = new CookieRequestFactory();
        JsonArrayRequest request = cookieRequestFactory.makeJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Team[] teams = new Team[response.length()];
                try {
                    for (int i = 0; i < response.length(); i++) {
                        teams[i] = JSONConverter.getInstance().convertJSONToTeam(response.getJSONObject(i));
                    }
                } catch (JSONException e) {
                    teams = null;
                }
                listener.onTeamsLoaded(teams);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTTPService.errorPrinterHelper(error);
                listener.onTeamsLoaded(null);
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }

    public void declineRequest(TeamRequest tRequest) {
        String url = HTTPService.DOMAIN + "requests/" + tRequest.getID();
        CookieRequestFactory cookieRequestFactory = new CookieRequestFactory();
        StringRequest request = cookieRequestFactory.makeStringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Success", "Request declined");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTTPService.errorPrinterHelper(error);
            }
        });
    }
}
