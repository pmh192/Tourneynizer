package com.tourneynizer.tourneynizer.services;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;

/**
 * Created by ryanwiener on 2/16/18.
 */

public class HTTPService {

    public static final String DOMAIN = "http://169.231.234.195:8080/api/";

    private static HTTPService requester;
    private RequestQueue requestQueue;
    private CookieManager cm;

    private HTTPService(@NonNull Context c) {
        requestQueue = Volley.newRequestQueue(c.getApplicationContext());
        cm = new CookieManager();
        CookieHandler.setDefault(cm);
    }

    public static void init(@NonNull Context c) {
        requester = new HTTPService(c);
    }

    public static synchronized HTTPService getInstance() throws NullPointerException {
        if (requester == null) {
            throw new NullPointerException();
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
