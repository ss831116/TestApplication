package com.yuyu.android.wct.http;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by bernie.shi on 2016/3/29.
 */
public interface GetVoteResultListener {
    void getvoteResultRequest(JSONObject jsonObject);
    void getvoteResultRequestError(VolleyError volleyError);
}
