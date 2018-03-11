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

/**
 * Created by ryanl on 3/11/2018.
 */

public class MatchService {

    public interface OnRequestCompletedListener {
        void onRequestCompleted(VolleyError error);
    }

    public interface OnMatchesLoadedListener {
        void onMatchesLoaded(Match[] matches);
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
}
