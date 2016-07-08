package com.yuyu.dreamachieve.http;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yuyu.dreamachieve.DreamAchieveApplication;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by bernie.shi on 2016/3/15.
 */
public class VolleyHttpRequest {
    VolleyHttpRequestListener volleyHttpRequestListener;
    JsonObjectRequest jsonObjectRequest;
    public VolleyHttpRequest(){

    }
    public void setVolleyHttpRequestListener(VolleyHttpRequestListener volleyHttpRequestListener) {
        this.volleyHttpRequestListener = volleyHttpRequestListener;
    }
    public void volleyHttpRequest(Map<Object, Object> params,String webAddress, String tag){
        DreamAchieveApplication.getHttpQueues().cancelAll(tag);
        jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, webAddress,
                new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonobj) {
                volleyHttpRequestListener.volleyHttpRequestSuccess(jsonobj);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyHttpRequestListener.volleyHttpRequestReeor(error);
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest.setTag(tag);
        DreamAchieveApplication.getHttpQueues().add(jsonObjectRequest);
    }
}
