package com.yuyu.android.wct.videoappend.activity;

import android.content.Intent;
import android.os.Bundle;

import com.moudle.videoplayer.FullScreenVideoView;
import com.moudle.videoplayer.VideoDisplayManager;
import com.yuyu.android.wct.R;
import com.yuyu.android.wct.view.DreamAchieveBaseActivity;

/**
 * Created by bernie.shi on 2016/3/25.
 */
public class PlayAppendVideoActivity extends DreamAchieveBaseActivity {
    FullScreenVideoView appendVideoView;
    protected void onCreate(Bundle onSaveInstanceState){
        super.onCreate(onSaveInstanceState);
        setContentView(R.layout.play_appendvideo_activity);
        initView();
    }
    public void initView(){
        appendVideoView = (FullScreenVideoView)findViewById(R.id.appendVideoView);
        getDate(appendVideoView,getIntent());
    }
    public void getDate(FullScreenVideoView videoView, Intent intent) {
        if (null != intent) {
            String videoPath = intent.getStringExtra("videoPath");
            boolean isLocalVideo = intent.getBooleanExtra("isLocalVideo", true);
            VideoDisplayManager.getInstance().init(videoPath, videoView, isLocalVideo);
        }
    }
}
