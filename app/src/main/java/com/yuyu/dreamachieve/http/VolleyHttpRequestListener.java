package com.yuyu.dreamachieve.http;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by bernie.shi on 2016/3/15.
 */
public interface VolleyHttpRequestListener {
    void volleyHttpRequestSuccess(JSONObject jsonObject);
    void volleyHttpRequestReeor(VolleyError error);
}
