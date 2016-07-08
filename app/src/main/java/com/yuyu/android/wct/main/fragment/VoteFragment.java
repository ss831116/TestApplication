package com.yuyu.android.wct.main.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;
import com.ypy.eventbus.EventBus;
import com.yuyu.android.wct.DreamAchieveApplication;
import com.yuyu.android.wct.R;
import com.yuyu.android.wct.http.GetVoteResultListener;
import com.yuyu.android.wct.http.HttpSite;
import com.yuyu.android.wct.http.VolleyHttpRequest;
import com.yuyu.android.wct.http.VolleyHttpRequestListener;
import com.yuyu.android.wct.http.VoteVideoResuleListener;
import com.yuyu.android.wct.log.DreamAchieveLog;
import com.yuyu.android.wct.main.OnTitleClickListener;
import com.yuyu.android.wct.main.fragment.adapter.VoteAdapter;
import com.yuyu.android.wct.main.fragment.info.VotePageInfo;
import com.yuyu.android.wct.main.fragment.util.HorizontalDividerItemDecoration;
import com.yuyu.android.wct.theme.CopyWriter;
import com.yuyu.android.wct.widget.PullToRefreshBase;
import com.yuyu.android.wct.widget.WanRecycleView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jackie.sun on 2016/3/17.
 */
@EFragment
public class VoteFragment extends BaseFragment implements VolleyHttpRequestListener, VoteVideoResuleListener, GetVoteResultListener {
    private static VoteFragment instance;
    private WanRecycleView mWanRecycle;
    private RecyclerView mRecycleView;
    private VoteAdapter mAdapter;
    private LinearLayout mRankLayout;
    private TextView mLowPoint;
    private TextView mBusyPoint;
    private LinearLayout mTimeLayout;
    private TextView mLastTime;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x01:
                    mAdapter.changrRank();
                    mHandler.removeMessages(0x01);
                    mHandler.sendEmptyMessageDelayed(0x01, 5000);
                    break;
            }
        }
    };
    private static final String TAG = "VoteFragment";
    private static final String VOTE_TAG = "VOTE_VoteFragment";
    private static final String VOTE_RESULT_TAG = "VOTE_Result_VoteFragment";

    public static VoteFragment getInstance() {
        if (null == instance) {
            instance = new VoteFragment();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle onSaveInstanceState) {
        super.onCreate(onSaveInstanceState);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fra_vote_layout, container, false);
        initHead(v,
                0,
                "name",
                0,
                new OnTitleClickListener() {
                    @Override
                    public void onLeftClick() {
                        //TODO
                    }

                    @Override
                    public void onRightClick() {
                        //TODO
                    }
                });
        return v;
    }

    @Override
    protected void initBody(View v) {
        initPointLayout(v);
        mWanRecycle = (WanRecycleView) v.findViewById(R.id.vote_recycleview);
        mWanRecycle.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mWanRecycle.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mWanRecycle.onRefreshComplete();
                        getVoteState();
                    }
                }, 1000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                //NONE
            }
        });
        mAdapter = new VoteAdapter(getActivity());
        mRecycleView = mWanRecycle.getRefreshableView();
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).
                color(getResources().getColor(R.color.listview_divider_color)).
                size(getResources().getDimensionPixelSize(R.dimen.vote_divider_height)).
                showLastDivider().
                build());
        getVoteState();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("VoteFragment");
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("VoteFragment");
        MobclickAgent.onPause(getActivity());
    }

    /**
     * 获得投票状态
     *
     * @author Bernie
     * create at 2016/4/7 16:21
     */
    public void getVoteState() {
        Map<Object, Object> map = new HashMap<>();
        map.put("aid", "");
        VolleyHttpRequest volleyHttpRequest = new VolleyHttpRequest();
        volleyHttpRequest.setVoteVideoResuleyHttpRequestListener(this);
        volleyHttpRequest.voteResultHttpRequest(map, HttpSite.videoCountDowun, VOTE_RESULT_TAG);
    }

    private void initPointLayout(View v) {
        mRankLayout = (LinearLayout) v.findViewById(R.id.vote_rank_point_layout);
        mLowPoint = (TextView) v.findViewById(R.id.vote_low_point_text);
        mBusyPoint = (TextView) v.findViewById(R.id.vote_busy_point_text);
        mTimeLayout = (LinearLayout) v.findViewById(R.id.vote_last_time_layout);
        mLastTime = (TextView) v.findViewById(R.id.vote_last_time);
        mRankLayout.setVisibility(View.GONE);
        mTimeLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 获得当前可以投票视频详情
     *
     * @author Bernie
     * create at 2016/4/7 16:24
     */
    @Background
    public void getCurrentVoteVideoDetail() {
        Map<Object, Object> map = new HashMap<>();
        map.put("aid", "");
        VolleyHttpRequest volleyHttpRequest = new VolleyHttpRequest();
        volleyHttpRequest.setVolleyHttpRequestListener(this);
        volleyHttpRequest.volleyHttpRequest(map, HttpSite.videoResultSite, TAG);
    }

    /**
     * 获得当前视频投票结果详情
     * @author Bernie
     * create at 2016/4/7 16:27
     */
    @Background
    public void videoVoteResule() {
        Map<Object, Object> map = new HashMap<>();
        map.put("aid", "");
        VolleyHttpRequest volleyHttpRequest = new VolleyHttpRequest();
        volleyHttpRequest.setVoteVideoResultVolleyHttpRequestListener(this);
        volleyHttpRequest.volleyVoteVideoResultHttpRequest(map, HttpSite.videoVoteResult, VOTE_TAG);
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
                    (float) 10.8, videoRelegation(jsonObject.optString("phone")), videoPath, videoPicPath,
                    jsonObject.optString("aid"), jsonObject.optInt("id")));
        }
        mAdapter.refreshData(mData);
        mHandler.removeMessages(0x01);
        mHandler.sendEmptyMessageDelayed(0x01, 5000);
    }

    public int videoRelegation(String name) {
        if (name.equals(DreamAchieveApplication.sharedPreferences.getString("phone", ""))) {
            return VotePageInfo.VOTING_FOR_SELF;
        } else {
            return VotePageInfo.VOTING_FOR_COMMON;
        }
    }

    /**
     * 获得当前可投票视频详情回调函数
     *
     * @author Bernie
     * create at 2016/4/7 16:26
     */
    @Override
    public void volleyHttpRequestSuccess(JSONObject jsonObject) {
        if (jsonObject.optInt("retCode") == 1) {
            JSONArray ja = jsonObject.optJSONArray("list");
            setTitleName(CopyWriter.secondPageTitle);
            jsonDataPull(ja);
        }
    }

    @Override
    public void volleyHttpRequestError(VolleyError error) {

    }
    /**
       * 获得当前投票结果详情回调函数
       * @author Bernie
       * create at 2016/4/7 16:27
    */
    @Override
    public void voteVideoResultRequest(JSONObject jsonObject) {
        if (jsonObject.optInt("retCode") == 1) {
            setTitleName("投票结果");
            JSONArray ja = jsonObject.optJSONArray("list");
            jsonObjectFromVoteVideoResult(ja);
        }
    }

    @Override
    public void voteVideoResultRequestError(VolleyError error) {
    }

    public void jsonObjectFromVoteVideoResult(JSONArray jsObj) {
        mAdapter.refreshData();
        List<VotePageInfo> mData = new ArrayList<>();
        for (int i = 0; i < jsObj.length(); i++) {
            JSONObject jsonObject = jsObj.optJSONObject(i);
            String videoPath;
            String videoPicPath;
            String votePercent = jsonObject.optString("percent");// .substring(0,5);
            if (jsonObject.optInt("fromType") == 1) {
                videoPath = jsonObject.optString("videoPicture");
                videoPath = videoPath.substring(0, videoPath.length() - 4) + ".mp4";
                videoPath = HttpSite.videoPath_qiniu + videoPath;
                videoPicPath = HttpSite.imagePath_qiniu + jsonObject.optString("videoPicture");
            } else {
                videoPath = jsonObject.optString("videoPicture");
                videoPath = videoPath.substring(0, videoPath.length() - 4) + ".mp4";
                videoPicPath = jsonObject.optString("videoPicture");
            }
            votePercent = votePercent.substring(0, votePercent.indexOf(".") + 3);
            mData.add(new VotePageInfo("第" + (i + 1) + "名", jsonObject.optString("videoName"),
                    Float.parseFloat(votePercent), VotePageInfo.VOTED_VIDEO, videoPath, videoPicPath,
                    jsonObject.optString("aid"), jsonObject.optInt("id")));
        }
        mAdapter.refreshData(mData);
        mHandler.removeMessages(0x01);
        mHandler.removeMessages(0x01);
    }


    /**
     * 或得投票状态后的回调处理
     *
     * @author Bernie
     * create at 2016/4/7 16:24
     */
    @Override
    public void getvoteResultRequest(JSONObject jsonObject) {
        DreamAchieveLog.e(VoteFragment.class, "jsonObject = " + jsonObject);
        if (jsonObject.optInt("retCode") == 1) {
            pullVoteResult(jsonObject.optJSONObject("map"));
        }
    }

    @Override
    public void getvoteResultRequestError(VolleyError volleyError) {

    }

    @UiThread
    public void pullVoteResult(JSONObject jsonObject) {
        DreamAchieveLog.e(VoteFragment.class, "pullVoteResult jsonObject = " + jsonObject.toString());
        //jsonObject.optInt("state")中的state代表是否在投票期：1为可以投票
        if (jsonObject.optInt("state") == 1) {
            getCurrentVoteVideoDetail();
            playCountDown(true);
        } else {
            playCountDown(false);
            MyCount mc = new MyCount(jsonObject.optInt("nextActivityBeginCountdown") * 1000, 1000);
            mc.start();
            videoVoteResule();
        }

    }

    public void playCountDown(boolean play) {
        mTimeLayout.setVisibility(play ? View.GONE : View.VISIBLE);
        mRankLayout.setVisibility(play ? View.VISIBLE : View.GONE);
    }

    class MyCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mLastTime.setText(getFormatedDateTime(millisUntilFinished));
        }

        @Override
        public void onFinish() {
            mTimeLayout.setVisibility(View.GONE);
            mRankLayout.setVisibility(View.VISIBLE);
        }

        public String getFormatedDateTime(long dateTime) {
            SimpleDateFormat sDateFormat = new SimpleDateFormat("mm:ss");
            return sDateFormat.format(new Date(dateTime + 0));
        }
    }

    public void onEventMainThread(String refreshPage) {
        if ("refresh_vote".equals(refreshPage)) {
            getCurrentVoteVideoDetail();
        }else if("logout".equals(refreshPage)){
            getVoteState();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}
