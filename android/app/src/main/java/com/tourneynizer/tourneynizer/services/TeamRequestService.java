package com.tourneynizer.tourneynizer.services;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tourneynizer.tourneynizer.model.Team;
import com.tourneynizer.tourneynizer.model.TeamRequest;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.util.CookieRequestFactory;
import com.tourneynizer.tourneynizer.util.JSONConverter;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by ryanl on 2/28/2018.
 */

public class TeamRequestService {

    public interface OnTeamRequestsLoadedListener {
        public void onTeamRequestsLoaded(TeamRequest[] teamRequests);
    }

    public void sendRequestToTeam(Team t) {
        String url = HTTPService.DOMAIN + "team/" + t.getId() + "/request";
        CookieRequestFactory factory = new CookieRequestFactory();
        StringRequest request = factory.makeStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Success", "Request Sent");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }

    public void acceptRequestForTeam(TeamRequest tRequest) {
        String url = HTTPService.DOMAIN + "team/" + tRequest.getTeamID() + "/request/" + tRequest.getID() + "/accept";
        CookieRequestFactory factory = new CookieRequestFactory();
        StringRequest request = factory.makeStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Success", "Request Accepted");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        });
    }

    public void declineRequest(TeamRequest tRequest) {
        String url = HTTPService.DOMAIN + "requests/" + tRequest.getID();
        CookieRequestFactory factory = new CookieRequestFactory();
        StringRequest request = factory.makeStringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Success", "Request declined");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }

    public void getRequestsForSelf(final OnTeamRequestsLoadedListener listener) {
        String url = HTTPService.DOMAIN + "user/requests/team";
        CookieRequestFactory factory = new CookieRequestFactory();
        Request request = factory.makeJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                TeamRequest[] teamRequests = new TeamRequest[response.length()];
                try {
                    for (int i = 0; i < response.length(); i++) {
                        teamRequests[i] = JSONConverter.getInstance().convertJSONToTeamRequest(response.getJSONObject(i));
                    }
                } catch (JSONException e) {
                    teamRequests = null;
                }
                listener.onTeamRequestsLoaded(teamRequests);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }

    public void getRequestsBySelf(final OnTeamRequestsLoadedListener listener) {
        String url = HTTPService.DOMAIN + "user/requests/pending";
        CookieRequestFactory factory = new CookieRequestFactory();
        Request request = factory.makeJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                TeamRequest[] teamRequests = new TeamRequest[response.length()];
                try {
                    for (int i = 0; i < response.length(); i++) {
                        teamRequests[i] = JSONConverter.getInstance().convertJSONToTeamRequest(response.getJSONObject(i));
                    }
                } catch (JSONException e) {
                    teamRequests = null;
                }
                listener.onTeamRequestsLoaded(teamRequests);
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
