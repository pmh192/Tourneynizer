package com.tourneynizer.tourneynizer.services;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.tourneynizer.tourneynizer.model.Match;
import com.tourneynizer.tourneynizer.model.Tournament;
import com.tourneynizer.tourneynizer.util.CookieRequestFactory;
import com.tourneynizer.tourneynizer.util.JSONConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ryanl on 3/11/2018.
 */

public class MatchService {

    public interface OnErrorListener {
        void onError(VolleyError error);
    }

    public interface OnMatchesLoadedListener {
        void onMatchesLoaded(Match[] matches);
    }

    public interface OnScoresLoadedListener {
        void onScoresLoaded(Long[] scores);
    }

    private CookieRequestFactory cookieRequestFactory;

    public MatchService() {
        cookieRequestFactory = new CookieRequestFactory();
    }

    public void getAllMatches(Tournament tournament, final OnMatchesLoadedListener listener) {
        String url = HTTPService.DOMAIN + "tournament/" + tournament.getID() + "/match/getAll";
        Request request = cookieRequestFactory.makeJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Response", response.toString());
                Match[] matches = new Match[response.length()];
                for (int i = 0; i < matches.length; i++) {
                    try {
                        matches[i] = JSONConverter.getInstance().convertJSONToMatch(response.getJSONObject(i));
                    } catch (JSONException e) {
                        matches[i] = null;
                    }
                }
                listener.onMatchesLoaded(matches);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTTPService.errorPrinterHelper(error);
                listener.onMatchesLoaded(null);
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }

    public void getAllValidMatches(Tournament tournament, final OnMatchesLoadedListener listener) {
        String url = HTTPService.DOMAIN + "tournament/" + tournament.getID() + "/match/valid";
        Request request = cookieRequestFactory.makeJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Response", response.toString());
                Match[] matches = new Match[response.length()];
                for (int i = 0; i < matches.length; i++) {
                    try {
                        matches[i] = JSONConverter.getInstance().convertJSONToMatch(response.getJSONObject(i));
                    } catch (JSONException e) {
                        matches[i] = null;
                    }
                }
                listener.onMatchesLoaded(matches);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTTPService.errorPrinterHelper(error);
                listener.onMatchesLoaded(null);
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }

    public void getAllCompletedMatches(Tournament tournament, final OnMatchesLoadedListener listener) {
        String url = HTTPService.DOMAIN + "tournament/" + tournament.getID() + "/match/getCompleted";
        Request request = cookieRequestFactory.makeJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Response", response.toString());
                Match[] matches = new Match[response.length()];
                for (int i = 0; i < matches.length; i++) {
                    try {
                        matches[i] = JSONConverter.getInstance().convertJSONToMatch(response.getJSONObject(i));
                    } catch (JSONException e) {
                        matches[i] = null;
                    }
                }
                listener.onMatchesLoaded(matches);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTTPService.errorPrinterHelper(error);
                listener.onMatchesLoaded(null);
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }

    public void startMatch(Match match, final OnErrorListener listener) {
        String url = HTTPService.DOMAIN + "tournament/" + match.getTournamentID() + "/match/" + match.getID() + "/start";
        Request request = cookieRequestFactory.makeStringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Success", "Match has started");
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

    public void updateScore(Match match, long score1, long score2, final OnErrorListener listener) {
        String url = HTTPService.DOMAIN + "tournament/" + match.getTournamentID() + "/match/" + match.getID() + "/updateScore";
        JSONObject body = new JSONObject();
        try {
            body.put("score1", score1);
            body.put("score2", score2);
        } catch (JSONException e) {
            body = null;
        }
        Request request = cookieRequestFactory.makeJsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Success", response.toString());
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

    public void getScores(Match match, final OnScoresLoadedListener listener) {
        String url = HTTPService.DOMAIN + "tournament/" + match.getTournamentID() + "/match/" + match.getID() + "/getScore";
        Request request = cookieRequestFactory.makeJsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Success", response.toString());
                Long[] scores = new Long[2];
                try {
                    for (int i = 0; i < 2; i++) {
                        if (!response.isNull(i)) {
                            scores[i] = response.getLong(i);
                        }
                    }
                } catch (JSONException e) {
                    scores = null;
                }
                listener.onScoresLoaded(scores);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTTPService.errorPrinterHelper(error);
                listener.onScoresLoaded(null);
            }
        });
        HTTPService.getInstance().getRequestQueue().add(request);
    }

    public void endMatch(Match match, final OnErrorListener listener) {
        String url = HTTPService.DOMAIN + "tournament/" + match.getTournamentID() + "/match/" + match.getID() + "/end";
        JSONObject body = new JSONObject();
        try {
            body.put("score1", match.getScore1());
            body.put("score2", match.getScore2());
        } catch (JSONException e) {
            body = null;
        }
        Request request = cookieRequestFactory.makeJsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Success", "Match has ended");
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
}
