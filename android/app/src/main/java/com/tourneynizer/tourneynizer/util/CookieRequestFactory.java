package com.tourneynizer.tourneynizer.util;

import android.content.Context;
import android.text.Editable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.tourneynizer.tourneynizer.services.HTTPService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ryanwiener on 2/20/18.
 */

public class CookieRequestFactory {

    private final String COOKIE = "Cookie";

    public CookieRequestFactory() {}

    private Map<String, String> getCookies() {
        Map<String, String> headers = new HashMap<>();
        List<HttpCookie> cookies = HTTPService.getInstance().getCookieManager().getCookieStore().getCookies();
        if (cookies.size() > 0) {
            Editable cookieString = new Editable.Factory().newEditable(cookies.get(0).toString());
            for (int i = 1; i < cookies.size(); i++) {
                cookieString.append("; ").append(cookies.get(i).toString());
            }
            headers.put(COOKIE, cookieString.toString());
        }
        return headers;
    }

    public StringRequest makeStringRequest(int method, String url, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        return new StringRequest(method, url, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getCookies();
            }
        };
    }

    public JsonObjectRequest makeJsonObjectRequest(int method, String url, JSONObject body, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        return new JsonObjectRequest(method, url, body, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getCookies();
            }
        };
    }

    public JsonArrayRequest makeJsonArrayRequest(int method, String url, JSONArray body, Response.Listener<JSONArray> responseListener, Response.ErrorListener errorListener) {
        return new JsonArrayRequest(method, url, body, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getCookies();
            }
        };
    }
}
