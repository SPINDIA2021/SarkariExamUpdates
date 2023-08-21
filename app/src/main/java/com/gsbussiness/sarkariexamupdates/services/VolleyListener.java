package com.gsbussiness.sarkariexamupdates.services;

import com.android.volley.VolleyError;

public interface VolleyListener {

    void response(String result);

    void failed(VolleyError error);
}
