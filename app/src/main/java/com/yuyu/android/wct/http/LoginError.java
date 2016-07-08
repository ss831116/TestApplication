package com.yuyu.android.wct.http;

import org.json.JSONObject;

/**
 * Created by bernie.shi on 2016/3/23.
 */
public class LoginError {
    public static boolean noLogin(JSONObject jsonObject){
        return jsonObject.optString("msg").equals("Hello World");
    }
}
