package com.yuyu.android.wct.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.moudle.videoplayer.FullScreenVideoView;
import com.moudle.videoplayer.VideoDisplayManager;
import com.umeng.analytics.MobclickAgent;
import com.yuyu.android.wct.DreamAchieveApplication;
import com.yuyu.android.wct.R;
import com.yuyu.android.wct.http.HttpSite;
import com.yuyu.android.wct.http.LoginError;
import com.yuyu.android.wct.http.VolleyHttpRequest;
import com.yuyu.android.wct.http.VolleyHttpRequestListener;
import com.yuyu.android.wct.login.LoginActivity_;
import com.yuyu.android.wct.utils.PointUtil;
import com.yuyu.android.wct.view.DreamAchieveBaseActivity;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bernie.shi on 2016/3/18.
 */
@EActivity
public class PlayClientVideoActivity extends DreamAchieveBaseActivity implements OnClickListener, VolleyHttpRequestListener {
    FullScreenVideoView videoView;
    Button voteVideo;
    String aid;
    int videoId;
    private boolean isShowVoteBtn = false;
    private static final String TAG = "VOTE";
    LinearLayout backImgBtnLayout;

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        PointUtil.logForEvent(PointUtil.APP_EVENT_FOR_DISPLAY);
        MobclickAgent.onEvent(PlayClientVideoActivity.this, PointUtil.APP_EVENT_FOR_DISPLAY);
        setContentView(R.layout.play_client_video_activity);
        initView();
    }

    public void initView() {
        videoView = (FullScreenVideoView) findViewById(R.id.videoView);
        voteVideo = (Button) findViewById(R.id.voteVideo);
        backImgBtnLayout = (LinearLayout) findViewById(R.id.backImgBtnLayout);
        initInfo(videoView, getIntent());
        voteVideo.setVisibility(isShowVoteBtn ? View.VISIBLE : View.GONE);
        initListener();
    }

    public void initInfo(FullScreenVideoView videoView, Intent intent) {
        if (null != intent) {
            String videoPath = intent.getStringExtra("videoPath");
            boolean isLocalVideo = intent.getBooleanExtra("isLocalVideo", true);
            aid = intent.getStringExtra("aid");
            videoId = intent.getIntExtra("videoId", 0);
            isShowVoteBtn = intent.getBooleanExtra("isShow", false);
            VideoDisplayManager.getInstance().init(videoPath, videoView, isLocalVideo);
        }
    }

    public void initListener() {
        voteVideo.setOnClickListener(this);
        backImgBtnLayout.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        VideoDisplayManager.getInstance().onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.voteVideo:
                voteVideo();
                break;
            case R.id.backImgBtnLayout:
                this.finish();
                break;
        }
    }

    @Background
    public void voteVideo() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("videoId", videoId);
        map.put("aid", aid);
        map.put("voteNum", 1);
        map.put("voteType", 1);
        VolleyHttpRequest volleyHttpRequest = new VolleyHttpRequest();
        volleyHttpRequest.setVolleyHttpRequestListener(this);
        volleyHttpRequest.volleyHttpRequest(map, HttpSite.voteSite + DreamAchieveApplication.sharedPreferences.getString("cookies", ""), TAG);
    }

    @Override
    public void volleyHttpRequestSuccess(JSONObject jsonObject) {
        if (LoginError.noLogin(jsonObject)) {
            Intent intent = new Intent(PlayClientVideoActivity.this, LoginActivity_.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.vote_success), Toast.LENGTH_SHORT).show();
            DreamAchieveApplication.instance.playFloatWindow();
        }
    }

    @Override
    public void volleyHttpRequestError(VolleyError error) {

    }
}
