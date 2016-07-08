package com.yuyu.android.wct.http;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yuyu.android.wct.DreamAchieveApplication;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by bernie.shi on 2016/3/15.
 */
public class VolleyHttpRequest {
    VolleyHttpRequestListener volleyHttpRequestListener;
    JsonObjectRequest jsonObjectRequest;
    VoteVideoResuleListener voteVideoResuleListener;
    GetVoteResultListener getVoteResultListener;
    public VolleyHttpRequest(){

    }
    public void setVolleyHttpRequestListener(VolleyHttpRequestListener volleyHttpRequestListener) {
        this.volleyHttpRequestListener = volleyHttpRequestListener;
    }
    public void setVoteVideoResultVolleyHttpRequestListener(VoteVideoResuleListener voteVideoResuleListener) {
        this.voteVideoResuleListener = voteVideoResuleListener;
    }
    public void setVoteVideoResuleyHttpRequestListener(GetVoteResultListener getVoteResultListener) {
        this.getVoteResultListener = getVoteResultListener;
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
                volleyHttpRequestListener.volleyHttpRequestError(error);
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest.setTag(tag);
        DreamAchieveApplication.getHttpQueues().add(jsonObjectRequest);
    }
    public void volleyVoteVideoResultHttpRequest(Map<Object, Object> params,String webAddress, String tag){
        DreamAchieveApplication.getHttpQueues().cancelAll(tag);
        jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, webAddress,
                new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonobj) {
                voteVideoResuleListener.voteVideoResultRequest(jsonobj);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                voteVideoResuleListener.voteVideoResultRequestError(error);
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest.setTag(tag);
        DreamAchieveApplication.getHttpQueues().add(jsonObjectRequest);
    }
    public void voteResultHttpRequest(Map<Object, Object> params,String webAddress, String tag){
        DreamAchieveApplication.getHttpQueues().cancelAll(tag);
        jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, webAddress,
                new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonobj) {
                getVoteResultListener.getvoteResultRequest(jsonobj);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getVoteResultListener.getvoteResultRequestError(error);
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest.setTag(tag);
        DreamAchieveApplication.getHttpQueues().add(jsonObjectRequest);
    }
}
