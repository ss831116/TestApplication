package com.yuyu.android.wct.http;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by bernie.shi on 2016/3/15.
 */
public interface VolleyHttpRequestListener {
    void volleyHttpRequestSuccess(JSONObject jsonObject);
    void volleyHttpRequestError(VolleyError error);
}
