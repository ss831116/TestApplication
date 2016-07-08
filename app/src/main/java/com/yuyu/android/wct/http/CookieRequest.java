package com.yuyu.android.wct.http;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by bernie.shi on 2016/3/15.
 */
public class CookieRequest extends JsonObjectRequest {
    private Map mHeaders=new HashMap(1);

    public CookieRequest(String url, JSONObject jsonRequest, Listener listener,
                         ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    public CookieRequest(int method, String url, JSONObject jsonRequest, Listener listener,
                         ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public void setCookie(String cookie){
        mHeaders.put("Cookie", cookie);
    }

    @Override
    public Map getHeaders() throws AuthFailureError {
        return mHeaders;
    }

}
