package com.tourneynizer.tourneynizer.services;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
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

    public static HTTPService getInstance() throws NullPointerException {
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

    public static void errorPrinterHelper(VolleyError e) {
        if (e != null && e.networkResponse != null && e.networkResponse.data != null) {
            try {
                String errorResponse = new String(e.networkResponse.data, "UTF8");
                Log.e("Error", errorResponse);
            } catch (UnsupportedEncodingException error) {
                Log.e("Error", "Can't print network response error");
            }
        }
    }
}
