package com.yuyu.android.wct.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.yuyu.android.wct.R;
import com.yuyu.android.wct.http.HttpSite;
import com.yuyu.android.wct.http.VolleyHttpRequest;
import com.yuyu.android.wct.http.VolleyHttpRequestListener;
import com.yuyu.android.wct.main.BaseActivity2;
import com.yuyu.android.wct.main.OnTitleClickListener;
import com.yuyu.android.wct.main.fragment.adapter.TrailerDetailAdapter;
import com.yuyu.android.wct.main.fragment.info.VotePageInfo;
import com.yuyu.android.wct.main.fragment.util.HorizontalDividerItemDecoration;
import com.yuyu.android.wct.widget.PullToRefreshBase;
import com.yuyu.android.wct.widget.WanRecycleView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bernie.shi on 2016/4/7.
 */
@EActivity
public class TrailerDetailActivity extends BaseActivity2 implements VolleyHttpRequestListener {
    String aidName;
    String titleName;
    private static final String TAG = "TrailerDetailActivity";
    private WanRecycleView mWanRecycle;
    private TrailerDetailAdapter mAdapter;
    private RecyclerView mRecycleView;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x01:
                    mHandler.removeMessages(0x01);
                    mHandler.sendEmptyMessageDelayed(0x01, 5000);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle onSaveInstance) {
        super.onCreate(onSaveInstance);
        initData(getIntent());
        initView();
    }

    public void initData(Intent intent) {
        aidName = intent.getStringExtra("aid");
        titleName = intent.getStringExtra("titleName");
    }

    public void initView() {
        init(R.layout.trailer_detail_activity,
                R.drawable.back_btn,
                titleName,
                0,
                new OnTitleClickListener() {
                    @Override
                    public void onLeftClick() {
                        TrailerDetailActivity.this.finish();
                    }

                    @Override
                    public void onRightClick() {

                    }
                });
    }

    @Override
    public void initBody() {
        mWanRecycle = (WanRecycleView) findViewById(R.id.vote_recycleview);
        mWanRecycle.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mWanRecycle.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mWanRecycle.onRefreshComplete();
                        getTrailerDetail();
                    }
                }, 1000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                //NONE
            }
        });
        mAdapter = new TrailerDetailAdapter(this);
        mRecycleView = mWanRecycle.getRefreshableView();
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getApplicationContext()).
                color(getResources().getColor(R.color.listview_divider_color)).
                size(getResources().getDimensionPixelSize(R.dimen.vote_divider_height)).
                showLastDivider().
                build());
        getTrailerDetail();
    }

    /**
     * 获得预告视频详情
     *
     * @author Bernie
     * create at 2016/4/7 16:30
     */
    @Background
    public void getTrailerDetail() {
        Map<Object, Object> map = new HashMap<>();
        map.put("aid", "");
        VolleyHttpRequest volleyHttpRequest = new VolleyHttpRequest();
        volleyHttpRequest.setVolleyHttpRequestListener(this);
        volleyHttpRequest.volleyHttpRequest(map, HttpSite.videoResultSite, TAG);
    }

    /**
     * 获得预告视频详情回调
     * @author Bernie
     * create at 2016/4/7 16:30
     */
    @Override
    public void volleyHttpRequestSuccess(JSONObject jsonObject) {
        if (jsonObject.optInt("retCode") == 1) {
            JSONArray ja = jsonObject.optJSONArray("list");
            jsonDataPull(ja);
        }
    }

    @Override
    public void volleyHttpRequestError(VolleyError error) {

    }

    @UiThread
    public void jsonDataPull(JSONArray ja) {
        mAdapter.refreshData();
        List<VotePageInfo> mData = new ArrayList<>();
        for (int i = 0; i < ja.length(); i++) {
            JSONObject jsonObject = ja.optJSONObject(i);
            String videoPath;
            String videoPicPath;
            if (jsonObject.optInt("fromType") == 1) {
                videoPath = HttpSite.videoPath_qiniu + jsonObject.optString("converUrl");
                videoPicPath = HttpSite.imagePath_qiniu + jsonObject.optString("picture");
            } else {
                videoPath = jsonObject.optString("converUrl");
                videoPicPath = jsonObject.optString("picture");
            }
            mData.add(new VotePageInfo("编号:" + (i + 1), jsonObject.optString("name"),
                    (float) 0.0, VotePageInfo.VOTED_VIDEO, videoPath, videoPicPath,
                    jsonObject.optString("aid"), jsonObject.optInt("id")));
        }
        mAdapter.refreshData(mData);
        mHandler.sendEmptyMessageDelayed(0x01, 5000);
    }

}
