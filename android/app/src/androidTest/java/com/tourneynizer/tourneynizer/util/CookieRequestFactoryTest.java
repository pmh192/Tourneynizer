package com.tourneynizer.tourneynizer.util;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CookieRequestFactoryTest {

    private CookieRequestFactory factory;

    @Before
    public void createFactory() throws Exception {
        // Context of the app under test.
        Context context = InstrumentationRegistry.getTargetContext();
        factory = new CookieRequestFactory(context);
    }

    @Test
    public void testMakeStringRequest() {
        StringRequest request = factory.makeStringRequest(Request.Method.GET, "http://www.testurl.com", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        assertTrue(request != null);
        Map<String, String> fakeHeader = new HashMap<>();
        try {
            assertEquals(fakeHeader, request.getHeaders());
        } catch (AuthFailureError e) {
            assertTrue(false);
        }
    }

    @Test
    public void testMakeJsonObjectRequest() {
        JsonObjectRequest request = factory.makeJsonObjectRequest(Request.Method.GET, "http://www.testurl.com", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        assertTrue(request != null);
        Map<String, String> fakeHeader = new HashMap<>();
        try {
            assertEquals(fakeHeader, request.getHeaders());
        } catch (AuthFailureError e) {
            assertTrue(false);
        }
    }

    @Test
    public void testMakeJsonArrayRequest() {
        JsonArrayRequest request = factory.makeJsonArrayRequest(Request.Method.GET, "http://www.testurl.com", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        assertTrue(request != null);
        Map<String, String> fakeHeader = new HashMap<>();
        try {
            assertEquals(fakeHeader, request.getHeaders());
        } catch (AuthFailureError e) {
            assertTrue(false);
        }
    }
}
