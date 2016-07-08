package com.yuyu.android.wct.http;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by bernie.shi on 2016/3/29.
 */
public interface VoteVideoResuleListener {
    void voteVideoResultRequest(JSONObject jsonObject);
    void voteVideoResultRequestError(VolleyError error);
}
