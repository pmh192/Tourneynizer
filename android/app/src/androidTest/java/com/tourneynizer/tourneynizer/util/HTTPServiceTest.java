package com.tourneynizer.tourneynizer.util;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.android.volley.RequestQueue;
import com.tourneynizer.tourneynizer.services.HTTPService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.CookieManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class HTTPServiceTest {

    @Before
    public void getContext() throws Exception {
        // Context of the app under test.
        Context context = InstrumentationRegistry.getTargetContext();
        HTTPService.init(context);
    }

    @Test
    public void testGetInstance() {
        HTTPService instance = HTTPService.getInstance();
        assertTrue(instance != null);
    }

    @Test
    public void testGetRequestQueue() {
        HTTPService instance = HTTPService.getInstance();
        RequestQueue requestQueue = instance.getRequestQueue();
        assertTrue(requestQueue != null);
    }

    @Test
    public void testGetCookieManager() {
        HTTPService instance = HTTPService.getInstance();
        CookieManager cookieManager = instance.getCookieManager();
        assertTrue(cookieManager != null);
    }
}
