package com.apeman.homeassistant;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.Volley;


public class RequestWizard {
    private static Context ctx;
    private RequestQueue requestQueue;
    private static RequestWizard instance;

    private RequestWizard(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized RequestWizard getInstance(Context applicationContext) {
        if (instance == null) {
            instance = new RequestWizard(applicationContext);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }

        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
