package com.tourneynizer.tourneynizer.util;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by ryanwiener on 2/16/18.
 */

public class HTTPRequester {

    public static final String DOMAIN = "http://169.231.234.195:8080/api/";

    private static HTTPRequester requestor;
    private RequestQueue requestQueue;

    private HTTPRequester(Context c) {
        requestQueue = Volley.newRequestQueue(c.getApplicationContext());
    }

    public static synchronized HTTPRequester getInstance(Context c) {
        if (requestor == null) {
            requestor = new HTTPRequester(c);
        }
        return requestor;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
