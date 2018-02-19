package com.tourneynizer.tourneynizer.requesters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.TournamentDef;
import com.tourneynizer.tourneynizer.model.TournamentType;
import com.tourneynizer.tourneynizer.util.HTTPRequester;
import com.tourneynizer.tourneynizer.util.JSONConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by ryanwiener on 2/16/18.
 */

public class TournamentRequester {

    public interface OnTournamentLoadedListener {
        void onTournamentLoaded(Tournament tournament);
    }

    public interface OnTournamentsLoadedListener {
        void onTournamentsLoaded(Tournament[] tournaments);
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

    public static void getAllTournaments(final Context c, final OnTournamentsLoadedListener listener) {
        String url = HTTPRequester.DOMAIN + "tournament/getAll";
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //parse response and return tournament
                Log.d("Response", response.toString());
                final JSONObject[] responses = new JSONObject[response.length()];
                for (int i = 0; i < response.length(); i++) {
                    try {
                        responses[i] = response.getJSONObject(i);
                    } catch (JSONException e) {
                        responses[i] = null;
                    }
                }
                Thread parser = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Tournament[] tournaments = new Tournament[responses.length];
                        for (int i = 0; i < responses.length; i++) {
                            tournaments[i] = JSONConverter.convertJSONToTournament(c, responses[i]);
                        }
                        listener.onTournamentsLoaded(tournaments);
                    }
                });
                parser.start();
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

    public static void createTournament(final Context c, TournamentDef tDef) {
        String url = HTTPRequester.DOMAIN + "tournament/create";
        JSONObject tournamentJSON = JSONConverter.convertTournamentDefToJSON(tDef);
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Cookie", HTTPRequester.getInstance(c).getCookieManager().getCookieStore().getCookies().get(0).toString());
                return headers;
            }
        };
        HTTPRequester.getInstance(c).getRequestQueue().add(request);
    }
}
