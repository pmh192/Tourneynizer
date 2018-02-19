package com.tourneynizer.tourneynizer.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

/**
 * Created by ryanwiener on 2/16/18.
 */

public class HTTPRequester {

    public static final String DOMAIN = "http://169.231.234.195:8080/api/";

    private static HTTPRequester requester;
    private RequestQueue requestQueue;
    private CookieManager cm;

    private HTTPRequester(Context c) {
        requestQueue = Volley.newRequestQueue(c.getApplicationContext());
        cm = new CookieManager();
        CookieHandler.setDefault(cm);
    }

    public static synchronized HTTPRequester getInstance(Context c) {
        if (requester == null) {
            requester = new HTTPRequester(c);
        }
        return requester;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public CookieManager getCookieManager() {
        return cm;
    }
}
