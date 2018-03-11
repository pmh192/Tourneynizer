package com.tourneynizer.tourneynizer.services;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.model.TournamentDef;
import com.tourneynizer.tourneynizer.util.CookieRequestFactory;
import com.tourneynizer.tourneynizer.util.JSONConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ryanwiener on 2/16/18.
 */

public class TournamentService {

    public TournamentService() {}

    public interface OnTournamentLoadedListener {
        void onTournamentLoaded(Tournament tournament);
    }

    public interface OnTournamentsLoadedListener {
        void onTournamentsLoaded(Tournament[] tournaments);
    }

    public interface OnErrorListener {
        void onError(VolleyError error);
    }

    public void getFromId(long id, final OnTournamentLoadedListener listener) {
        String url = HTTPService.DOMAIN + "tournament/" + id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //parse response and return tournament
                Log.d("Response", response.toString());
                listener.onTournamentLoaded(JSONConverter.getInstance().convertJSONToTournament(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTTPService.errorPrinterHelper(error);
                listener.onTournamentLoaded(null);
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }

    public void getAllTournaments(final OnTournamentsLoadedListener listener) {
        String url = HTTPService.DOMAIN + "tournament/getAll";
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
                // Do on a seperate thread since the geocoder in the parse function takes a long time
                Thread parser = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Tournament[] tournaments = new Tournament[responses.length];
                        for (int i = 0; i < responses.length; i++) {
                            tournaments[i] = JSONConverter.getInstance().convertJSONToTournament(responses[i]);
                        }
                        listener.onTournamentsLoaded(tournaments);
                    }
                });
                parser.start();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTTPService.errorPrinterHelper(error);
                listener.onTournamentsLoaded(null);
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }

    public void getAllCreatedTournaments(final OnTournamentsLoadedListener listener) {
        String url = HTTPService.DOMAIN + "tournament/getAllCreated";
        CookieRequestFactory cookieRequestFactory = new CookieRequestFactory();
        Request request = cookieRequestFactory.makeJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
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
                // Do on a seperate thread since the geocoder in the parse function takes a long time
                Thread parser = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Tournament[] tournaments = new Tournament[responses.length];
                        for (int i = 0; i < responses.length; i++) {
                            tournaments[i] = JSONConverter.getInstance().convertJSONToTournament(responses[i]);
                        }
                        listener.onTournamentsLoaded(tournaments);
                    }
                });
                parser.start();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTTPService.errorPrinterHelper(error);
                listener.onTournamentsLoaded(null);
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }

    public void startTournament(Tournament t, final OnErrorListener listener) {
        String url = HTTPService.DOMAIN + "tournament/" + t.getID() + "/start";
        CookieRequestFactory cookieRequestFactory = new CookieRequestFactory();
        Request request = cookieRequestFactory.makeStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Success", "Tournament has been started");
                listener.onError(null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTTPService.errorPrinterHelper(error);
                listener.onError(error);
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }

    public void createTournament(TournamentDef tDef, final OnTournamentLoadedListener listener) {
        String url = HTTPService.DOMAIN + "tournament/create";
        JSONObject tournamentJSON = JSONConverter.getInstance().convertTournamentDefToJSON(tDef);
        JsonObjectRequest request = new CookieRequestFactory().makeJsonObjectRequest(Request.Method.POST, url, tournamentJSON , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //parse response and return tournament
                Log.d("Response", response.toString());
                listener.onTournamentLoaded(JSONConverter.getInstance().convertJSONToTournament(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTTPService.errorPrinterHelper(error);
                listener.onTournamentLoaded(null);
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }
}
