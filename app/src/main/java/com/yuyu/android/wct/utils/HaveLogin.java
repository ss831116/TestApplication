package com.yuyu.android.wct.utils;

import android.content.Context;

import com.android.volley.VolleyError;
import com.yuyu.android.wct.DreamAchieveApplication;
import com.yuyu.android.wct.http.HttpSite;
import com.yuyu.android.wct.http.LoginError;
import com.yuyu.android.wct.http.VolleyHttpRequest;
import com.yuyu.android.wct.http.VolleyHttpRequestListener;
import com.yuyu.android.wct.videorecord.PreviewVideoActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bernie.shi on 2016/4/8.
 */
public class HaveLogin implements VolleyHttpRequestListener {
    Context context;
    public static String HAVE_LOGIN = "haveLogin";
    public HaveLogin(Context context){
        this.context = context;
        getUserVideoList();
    }
    private void getUserVideoList() {
        Map<Object, Object> map = new HashMap<>();
        map.put("pageNum", 0);
        map.put("pageSize", 10);
        VolleyHttpRequest volleyHttpRequest = new VolleyHttpRequest();
        volleyHttpRequest.setVolleyHttpRequestListener(this);
        volleyHttpRequest.volleyHttpRequest(map, HttpSite.userVideoList +
                DreamAchieveApplication.sharedPreferences.getString("cookies", ""), HAVE_LOGIN);
    }

    @Override
    public void volleyHttpRequestSuccess(JSONObject jsonObject) {
           PreviewVideoActivity.instance.canUploadVideo(LoginError.noLogin(jsonObject));
    }

    @Override
    public void volleyHttpRequestError(VolleyError error) {

    }
}
