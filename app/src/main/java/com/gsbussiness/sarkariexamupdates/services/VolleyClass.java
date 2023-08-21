package com.gsbussiness.sarkariexamupdates.services;

import android.content.Context;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class VolleyClass {

    Context context;
    RequestQueue mRequestQueue;

    public VolleyClass(Context context) {
        this.context = context;
    }

    public void doPost(String url, HashMap<String, String> map, VolleyListener listener) {

        try {
            getRequestQueue();

            StringRequest request = new StringRequest(Request.Method.POST, url, response -> {

                if (response != null) {
                    listener.response(response);
                }
            }, listener::failed) {
                @Nullable
                @Override
                protected Map<String, String> getParams() {
                    return map;
                }
            };

            mRequestQueue.add(request);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void doGet(String url, HashMap<String, String> map, VolleyListener listener) {

        try {
            getRequestQueue();

            StringRequest request = new StringRequest(Request.Method.GET, url, response -> {

                if (response != null) {
                    listener.response(response);
                }
            }, listener::failed) {
                @Nullable
                @Override
                protected Map<String, String> getParams() {
                    return map;
                }
            };

            mRequestQueue.add(request);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }
}