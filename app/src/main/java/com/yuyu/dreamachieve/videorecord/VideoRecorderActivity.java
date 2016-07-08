package com.yuyu.dreamachieve.videorecord;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.yuyu.baseactivity.BaseActivity;
import com.yuyu.dreamachieve.R;

import autoload.socks.com.videoaar.PlayVideoListener;
import autoload.socks.com.videoaar.VideoRecorderView;

/**
 * Created by bernie.shi on 2016/3/15.
 */
public class VideoRecorderActivity extends BaseActivity implements OnClickListener {
    VideoRecorderView videoRecorderView;
    ImageView falsh_light;
    ImageView videoController;
    ImageView camera_location;
    Animation mBigAnimation = null;
    long startRecording;
    long endRecording;
    int timeCount = 10;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_recorder_activity);
        initView();
    }

    public void initView() {
        videoRecorderView = (VideoRecorderView) findViewById(R.id.videoRecorderView);
        videoController = (ImageView) findViewById(R.id.videoController);
        falsh_light = (ImageView) findViewById(R.id.falsh_light);
        camera_location = (ImageView) findViewById(R.id.camera_location);
        mBigAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.record_image_anim);
        mBigAnimation.setFillAfter(true);
        initOnClickListener();
    }

    public void initOnClickListener() {
        falsh_light.setOnClickListener(this);
        camera_location.setOnClickListener(this);
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onResume() {
        super.onResume();
        videoOnTouchEvent();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (timeCount > 0) {
                timeCount--;
                handler.sendEmptyMessageDelayed(0, 1000);
            } else if (timeCount == 0) {
                videoRecorderView.endRecord(new PlayVideoListener() {
                    @Override
                    public void callBackToPlayVideo(String videoPath) {
                        Toast.makeText(getApplicationContext(), videoPath, Toast.LENGTH_LONG).show();
                    }
                }, "auto");
                timeCount = 10;
            }
        }
    };

    public void videoOnTouchEvent() {
        videoController.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        handler.sendEmptyMessageDelayed(0, 0);
                        startRecording = System.currentTimeMillis();
                        videoRecorderView.startRecord();
                        falsh_light.setVisibility(View.GONE);
                        camera_location.setVisibility(View.GONE);
                        videoRecorderView.startRecord();
                        videoController.startAnimation(mBigAnimation);
                        mBigAnimation.startNow();
                        break;
                    case MotionEvent.ACTION_UP:
                        mBigAnimation.cancel();
                        endRecording = System.currentTimeMillis();
                        handler.removeMessages(0);
                        timeCount = 10;
                        falsh_light.setVisibility(View.VISIBLE);
                        camera_location.setVisibility(View.VISIBLE);
                        if(endRecording - startRecording > 1000) {
                            videoRecorderView.endRecord(new PlayVideoListener() {
                                @Override
                                public void callBackToPlayVideo(String videoPath) {
                                    Toast.makeText(getApplicationContext(),videoPath,Toast.LENGTH_LONG).show();
                                }
                            },"auto");
                        }else{
                            Toast.makeText(getApplicationContext(),"not enough one second",Toast.LENGTH_SHORT).show();
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

    protected void onStop() {
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
        }
    }
}
