package com.yuyu.android.wct.videorecord;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yuyu.android.wct.DreamAchieveApplication;
import com.yuyu.android.wct.R;
import com.yuyu.android.wct.view.DreamAchieveBaseActivity;
import com.yuyu.android.wct.widget.RushBuyCountDownTimerView;

import autoload.socks.com.videoaar.PlayVideoListener;
import autoload.socks.com.videoaar.VideoRecorderView;

/**
 * Created by bernie.shi on 2016/3/15.
 */
public class VideoRecorderActivity extends DreamAchieveBaseActivity implements OnClickListener {
    VideoRecorderView videoRecorderView;
    ImageView falsh_light;
    ImageView videoController;
    ImageView camera_location;
    Animation mBigAnimation = null;
    RelativeLayout top_layout;
    LinearLayout backImgBtnLayout;
    long startRecording;
    long endRecording;
    int timeCount = 10;
    RelativeLayout countLayout;
    private RushBuyCountDownTimerView timerView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_recorder_activity);
        initView();
    }

    public void initView() {
        videoRecorderView = (VideoRecorderView) findViewById(R.id.videoRecorderView);
        VideoRecorderView.setrecordMaxTime(10000000);
        videoController = (ImageView) findViewById(R.id.videoController);
        falsh_light = (ImageView) findViewById(R.id.falsh_light);
        camera_location = (ImageView) findViewById(R.id.camera_location);
        mBigAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.record_image_anim);
        top_layout = (RelativeLayout)findViewById(R.id.top_layout);
        backImgBtnLayout = (LinearLayout)findViewById(R.id.backImgBtnLayout);
        mBigAnimation.setFillAfter(true);
        timerView = (RushBuyCountDownTimerView) findViewById(R.id.timerView);
        countLayout = (RelativeLayout) findViewById(R.id.countLayout);
        initOnClickListener();
    }

    public void initOnClickListener() {
        falsh_light.setOnClickListener(this);
        camera_location.setOnClickListener(this);
        backImgBtnLayout.setOnClickListener(this);
    }

    public void onStart() {
        super.onStart();
    }

    protected void onResume() {
        super.onResume();
        falsh_light.setVisibility(View.VISIBLE);
        camera_location.setVisibility(View.VISIBLE);
        top_layout.bringToFront();
        videoOnTouchEvent();
    }



    public void videoOnTouchEvent() {
        videoController.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if(DreamAchieveApplication.isUploadVideo){
                            Toast.makeText(getApplicationContext(),getString(R.string.uploading_video),Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if(countLayout.getVisibility() == View.GONE){
                            countLayout.setVisibility(View.VISIBLE);
                        }
                        timerView.start();
                        startRecording = System.currentTimeMillis();
                        videoRecorderView.startRecord();
                        falsh_light.setVisibility(View.GONE);
                        camera_location.setVisibility(View.GONE);
                        videoRecorderView.startRecord();
                        videoController.startAnimation(mBigAnimation);
                        mBigAnimation.startNow();
                        break;
                    case MotionEvent.ACTION_UP:
                        if(DreamAchieveApplication.isUploadVideo){
                            break;
                        }
                        if(countLayout.getVisibility() == View.VISIBLE){
                            countLayout.setVisibility(View.GONE);
                        }
                        timerView.stop();
                        mBigAnimation.cancel();
                        endRecording = System.currentTimeMillis();
                        timeCount = 10;
                        if(endRecording - startRecording > 1000) {
                            videoRecorderView.endRecord(new PlayVideoListener() {
                                @Override
                                public void callBackToPlayVideo(String videoPath) {
                                    Intent intent = new Intent(VideoRecorderActivity.this,PreviewVideoActivity.class);
                                    intent.putExtra("videoPath",videoPath);
                                    intent.putExtra("isLocalVideo",true);
                                    startActivity(intent);
                                }
                            },"auto");
                        }else{
                            Toast.makeText(getApplicationContext(),"not enough one second",Toast.LENGTH_SHORT).show();
                            falsh_light.setVisibility(View.VISIBLE);
                            camera_location.setVisibility(View.VISIBLE);
                            videoRecorderView.cancelRecord("auto");
                        }
                        break;
                }
                return true;
            }
        });
    }

    protected void onPause() {
        super.onPause();
    }

    public void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.falsh_light:
                videoRecorderView.openflashlight();
                if (VideoRecorderView.isFlashOn) {
                    falsh_light.setBackgroundResource(R.drawable.shoot_btn_flash_pre);
                } else {
                    falsh_light.setBackgroundResource(R.drawable.shoot_btn_flash);
                }
                break;
            case R.id.camera_location:
                if (!VideoRecorderView.isFrontCamera) {
                    videoRecorderView.switchCamera(true, "auto");
                    falsh_light.setBackgroundResource(R.drawable.shoot_btn_flash);
                    falsh_light.setEnabled(false);
                    VideoRecorderView.isFlashOn = false;
                } else {
                    videoRecorderView.switchCamera(false, "auto");
                    falsh_light.setEnabled(true);
                }
                break;
            case R.id.backImgBtnLayout:
                this.finish();
                break;
        }
    }
}
