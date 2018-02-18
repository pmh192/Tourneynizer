package com.tourneynizer.tourneynizer.requesters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.TournamentType;
import com.tourneynizer.tourneynizer.util.HTTPRequester;
import com.tourneynizer.tourneynizer.util.JSONConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

/**
 * Created by ryanwiener on 2/16/18.
 */

public class TournamentRequester {

    public interface OnTournamentLoadedListener {
        void onTournamentLoaded(Tournament tournament);
    }

    public static void getFromId(final Context c, long id, final OnTournamentLoadedListener listener) {
        String url = HTTPRequester.DOMAIN + "tournament/" + id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //parse response and return tournament
                Log.d("Response", response.toString());
                listener.onTournamentLoaded(JSONConverter.convertJSONToTournament(c, response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //awww shucks
                Log.d("Response", error.toString());
            }
        });
        HTTPRequester.getInstance(c).getRequestQueue().add(request);
    }

    public static void getAllTournaments(final Context c, final OnTournamentLoadedListener listener) {
        String url = HTTPRequester.DOMAIN + "tournament/getAll";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //parse response and return tournament
                Log.d("Response", response.toString());
                for (int i = 0; i < response.length(); i++) {
                    JSONObject tJSON = null;
                    try {
                        tJSON = response.getJSONObject(i);
                    } catch (JSONException e) {
                        tJSON = null;
                    }
                    listener.onTournamentLoaded(JSONConverter.convertJSONToTournament(c, tJSON));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //awww shucks
                Log.d("Response", error.toString());
            }
        });
        HTTPRequester.getInstance(c).getRequestQueue().add(request);
    }

    public static void createTournament(Context c) {
        String url = HTTPRequester.DOMAIN + "tournament/create";
        JSONObject tournamentJSON = new JSONObject();
        try {
            tournamentJSON.put("name", "Tournament 1");
            tournamentJSON.put("address", "796 Embarcadero del Norte, Isla Vista, CA 93117");
            tournamentJSON.put("startTime", new Time(0));
            tournamentJSON.put("teamSize", "3");
            tournamentJSON.put("maxTeams", "20");
            tournamentJSON.put("type", TournamentType.VOLLEYBALL_POOLED.ordinal());
            tournamentJSON.put("numCourts", "2");
        } catch (JSONException e) {
            tournamentJSON = null;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, tournamentJSON , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //parse response and return tournament
                Log.d("Response", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //awww shucks
                Log.d("Error", error.toString());
                if (error.networkResponse.data != null) {
                    try {
                        Log.d("Error Message", new String(error.networkResponse.data,"UTF-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        HTTPRequester.getInstance(c).getRequestQueue().add(request);
    }
}
