package com.tourneynizer.tourneynizer.requesters;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.tourneynizer.tourneynizer.model.User;
import com.tourneynizer.tourneynizer.util.HTTPRequester;
import com.tourneynizer.tourneynizer.util.JSONConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ryanwiener on 2/16/18.
 */

public class UserRequester {

    public interface OnUserLoadedListener {
        public void onUserLoaded(User user);
    }

    public static void getUserFromEmail(Context c, String email, final OnUserLoadedListener listener) {
        String url = HTTPRequester.DOMAIN + "user/get";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // parse response
                listener.onUserLoaded(JSONConverter.convertJSONToUser(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //awww shucks
            }
        });
        HTTPRequester.getInstance(c).getRequestQueue().add(request);
    }

    public static void getUsers(Context c, int pageNum, int pageSize, final OnUserLoadedListener listener) {
        String url = HTTPRequester.DOMAIN + "user/getAll";
        JSONArray pageination = new JSONArray();
        pageination.put(pageNum);
        pageination.put(pageSize);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // parse response
                Log.d("Response", response.toString());
                for (int i = 0; i < response.length(); i++) {
                    try {
                        listener.onUserLoaded(JSONConverter.convertJSONToUser(response.getJSONObject(i)));
                    } catch (JSONException e) {

                    }
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

    public static void getUserFromEmailAndPassword(final Context c, String email, String password, final OnUserLoadedListener listener) {
        String url = HTTPRequester.DOMAIN + "auth/login";
        JSONObject loginJSON = new JSONObject();
        try {
            loginJSON.put("email", email);
            loginJSON.put("password", password);
        } catch (JSONException e) {
            loginJSON = null;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, loginJSON, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // parse response and make sure the user was valid
                Log.d("Response", response.toString());
                listener.onUserLoaded(JSONConverter.convertJSONToUser(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //awww shucks
                Log.d("Error", "" + error.getMessage());
                Log.d("Error", "" + error.toString());
                Log.d("Error", "" + error.networkResponse);
                listener.onUserLoaded(null);
            }
        });
        HTTPRequester.getInstance(c).getRequestQueue().add(request);
    }

    public static void createUser(Context c, String name, String email, String password, final OnUserLoadedListener listener) {
        String url = HTTPRequester.DOMAIN + "user/create";
        JSONObject userJSON = new JSONObject();
        try {
            userJSON.put("name", name);
            userJSON.put("email", email);
            userJSON.put("password", password);
        } catch (JSONException e) {
            userJSON = null;
        }
        Log.d("JSON", userJSON.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, userJSON, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                User u = JSONConverter.convertJSONToUser(response);
                listener.onUserLoaded(u);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //awww shucks
                Log.d("Error", error.toString());
                listener.onUserLoaded(null);
            }
        });
        HTTPRequester.getInstance(c).getRequestQueue().add(request);
    }

    public static void logOut(final Context c) {
        String url = HTTPRequester.DOMAIN + "auth/logout";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", "Logged out");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
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
