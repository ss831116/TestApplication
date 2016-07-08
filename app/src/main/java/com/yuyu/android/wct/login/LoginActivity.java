package com.yuyu.android.wct.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;
import com.ypy.eventbus.EventBus;
import com.yuyu.android.wct.DreamAchieveApplication;
import com.yuyu.android.wct.R;
import com.yuyu.android.wct.http.GetCookieRequest;
import com.yuyu.android.wct.http.HttpSite;
import com.yuyu.android.wct.log.DreamAchieveLog;
import com.yuyu.android.wct.main.HomeActivity;
import com.yuyu.android.wct.utils.PointUtil;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bernie.shi on 2016/3/22.
 */
@EActivity
public class LoginActivity extends Activity implements OnClickListener {
    public static final String TAG = "LoginActivity";
    public static final String TAG_FROM = "from";
    private String mActivityFrom = "";
    EditText input_phone;
    EditText input_psw;
    RelativeLayout login_image;
    RelativeLayout login_layout;
    LinearLayout vote_linearLayout;
    RelativeLayout register_image;

    protected void onCreate(Bundle onSaveInstanceState) {
        super.onCreate(onSaveInstanceState);
        setContentView(R.layout.login_activity);
        initIntent();
        initView();
    }

    private void initIntent() {
        mActivityFrom = getIntent().getStringExtra(TAG_FROM);
    }

    public void initView() {
        input_phone = (EditText) findViewById(R.id.input_phone);
        input_psw = (EditText) findViewById(R.id.input_psw);
        register_image = (RelativeLayout) findViewById(R.id.register_image);
        login_image = (RelativeLayout) findViewById(R.id.login_image);
        login_layout = (RelativeLayout) findViewById(R.id.login_layout);
        vote_linearLayout = (LinearLayout) findViewById(R.id.vote_linearLayout);
        initListener();
    }

    public void initListener() {
        login_image.setOnClickListener(this);
        login_layout.setOnClickListener(this);
        register_image.setOnClickListener(this);
        vote_linearLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_image:
                if(input_phone.getText() != null && !"".equals(input_phone.getText().toString())) {
                    if(input_psw.getText() != null && !"".equals(input_psw.getText().toString())) {
                        loginWCT();
                    } else {
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.psw_error),Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.phone_error),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.login_layout:
                finish();
                HomeActivity.instance.setBackMainPage();
                break;
            case R.id.vote_linearLayout:
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(input_phone.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(input_psw.getWindowToken(), 0);
                break;
            case R.id.register_image:
                gotoRegister();
                break;
        }
    }

    public void gotoRegister(){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(HttpSite.registerSite);
        intent.setData(content_url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent,1);
    }
    @Background
    public void loginWCT() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("accound",input_phone.getText().toString());
        map.put("password", input_psw.getText().toString());
        GetCookieRequest newMissRequest = new GetCookieRequest(
                Request.Method.POST, HttpSite.loginSite,
                new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonobj) {
                insertDataToSharedPreference(jsonobj);
                PointUtil.logForSpec(PointUtil.APP_EVENT_FOR_LOGIN, PointUtil.TAG_LOGIN_STATE, "true");
                mobClickState("true");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_login),Toast.LENGTH_SHORT).show();
                PointUtil.logForSpec(PointUtil.APP_EVENT_FOR_LOGIN,PointUtil.TAG_LOGIN_STATE,"false");
                mobClickState("false");
            }
        });
        newMissRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        newMissRequest.setTag(TAG);
        DreamAchieveApplication.getHttpQueues().add(newMissRequest);
    }

    private void mobClickState(String state){
        Map<String,String> maps = new HashMap<>();
        maps.put(PointUtil.TAG_LOGIN_STATE,state);
        MobclickAgent.onEvent(LoginActivity.this,PointUtil.APP_EVENT_FOR_LOGIN,maps);
    }

    public void insertDataToSharedPreference(JSONObject jsonObject) {
        if (jsonObject.optInt("retCode") == 1) {
            try {
                JSONObject userInofo = new JSONObject(jsonObject.optString("userInfo"));
                DreamAchieveApplication.editor.putString("cookies", "?wct_session_id=" + GetCookieRequest.cookies);
                DreamAchieveApplication.editor.putString("username", userInofo.optString("username"));
                DreamAchieveApplication.editor.putInt("countryid", userInofo.optInt("countryid"));
                DreamAchieveApplication.editor.putString("phone", userInofo.optString("phone"));
                DreamAchieveApplication.editor.putString("createtime", userInofo.optString("createtime"));
                DreamAchieveApplication.editor.putInt("goldpayid", userInofo.optInt("goldpayid"));
                DreamAchieveApplication.editor.putString("goldpayusername", userInofo.optString("goldpayusername"));
                DreamAchieveApplication.editor.putString("goldpayemail", userInofo.optString("goldpayemail"));
                DreamAchieveApplication.editor.putString("goldpayaccountnum", userInofo.optString("goldpayaccountnum"));
                DreamAchieveApplication.editor.putString("userid", userInofo.optString("userid"));
                DreamAchieveApplication.editor.commit();
                gotoVoteActivity();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_login),Toast.LENGTH_SHORT).show();
        }
    }

    @UiThread
    public void gotoVoteActivity() {
        Toast.makeText(getApplicationContext(), getString(R.string.login_success), Toast.LENGTH_SHORT).show();
        if ("vote".equals(mActivityFrom)) {
            EventBus.getDefault().post("refresh_vote");
        }
//        else if ("user".equals(mActivityFrom)) {
        EventBus.getDefault().post("refresh_user_video");
//        }
        DreamAchieveApplication.instance.playFloatWindow();
        finish();
        //Intent intent = new Intent(LoginActivity.this, PlayClientVideoActivity_.class);
        //startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // do something what you want
        super.onBackPressed();
        HomeActivity.instance.setBackMainPage();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("Login");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("Login");
        MobclickAgent.onPause(this);
    }
}
