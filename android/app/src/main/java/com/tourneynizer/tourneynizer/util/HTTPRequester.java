package com.tourneynizer.tourneynizer.util;

import android.content.Context;

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

    private HTTPRequester(Context c) {
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        requestQueue = Volley.newRequestQueue(c.getApplicationContext());
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
}
