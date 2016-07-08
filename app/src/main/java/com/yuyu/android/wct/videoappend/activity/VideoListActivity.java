package com.yuyu.android.wct.videoappend.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import com.yuyu.android.wct.R;
import com.yuyu.android.wct.tools.GetVideoListFromSD;
import com.yuyu.android.wct.videoappend.adapter.VideoAppendAdapter;
import com.yuyu.android.wct.videoappend.adapter.VideoListAdapter;
import com.yuyu.android.wct.videoappend.listener.AppendVideoListener;
import com.yuyu.android.wct.videoappend.tools.VideoAppend;
import com.yuyu.android.wct.videoappend.utils.VideoList;
import com.yuyu.android.wct.view.DreamAchieveBaseActivity;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.io.File;
import java.io.IOException;

/**
 * Created by bernie.shi on 2016/3/18.
 */
@EActivity
public class VideoListActivity extends DreamAchieveBaseActivity implements OnClickListener{
    GridView append_video_gridView;
    ListView video_listView;
    VideoListAdapter videoListAdapter;
    VideoAppendAdapter videoAppendAdapter;
    Context context;
    public static VideoListActivity instance;
    Button button;
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.video_list_activity);
        context = getApplicationContext();
        instance = this;
        initView();
    }

    @Background
    public void getVideoListFromSD(){
        GetVideoListFromSD getVideoListFromSD = new GetVideoListFromSD(getApplicationContext());
        VideoList.videoList = getVideoListFromSD.getList();
        displayGridView();
    }
    @UiThread
    public void displayGridView(){
        videoListAdapter.notifyDataSetChanged();
        videoAppendAdapter.notifyDataSetChanged();
    }
    public void initView(){
        append_video_gridView = (GridView)findViewById(R.id.append_video_gridView);
        video_listView = (ListView)findViewById(R.id.video_listView);
        videoListAdapter = new VideoListAdapter(context);
        videoAppendAdapter =  new VideoAppendAdapter(context,VideoList.appendVideo);
        button = (Button)findViewById(R.id.button);
        initOnClickListener();
        setAdapter();
    }
    public void initOnClickListener(){
        button.setOnClickListener(this);
    }
    public void setAdapter(){
        video_listView.setAdapter(videoListAdapter);
        append_video_gridView.setAdapter(videoAppendAdapter);
        getVideoListFromSD();
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
        switch(v.getId()){
            case R.id.button:
                try {
                    VideoAppend.appendVideo(VideoList.appendVideo, new AppendVideoListener() {
                        @Override
                        public void append(String videoPath) {
                            Intent intent = new Intent(VideoListActivity.this,PlayAppendVideoActivity.class);
                            intent.putExtra("videoPath",videoPath);
                            intent.putExtra("isLocalVideo",true);
                            startActivity(intent);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}
