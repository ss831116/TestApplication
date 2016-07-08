package com.yuyu.android.wct.main.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;
import com.ypy.eventbus.EventBus;
import com.yuyu.android.wct.DreamAchieveApplication;
import com.yuyu.android.wct.R;
import com.yuyu.android.wct.http.HttpSite;
import com.yuyu.android.wct.http.VolleyHttpRequest;
import com.yuyu.android.wct.http.VolleyHttpRequestListener;
import com.yuyu.android.wct.http.VoteVideoResuleListener;
import com.yuyu.android.wct.log.DreamAchieveLog;
import com.yuyu.android.wct.main.OnTitleClickListener;
import com.yuyu.android.wct.main.fragment.adapter.UserAdapter;
import com.yuyu.android.wct.main.fragment.adapter.UserAdapter.BaseChildItem;
import com.yuyu.android.wct.widget.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by jackie.sun on 2016/3/17.
 */
public class UserFragment extends BaseFragment implements XListView.IXListViewListener, VolleyHttpRequestListener, VoteVideoResuleListener {
    private static final String VIDEO_LIST_TAG = "video_list";
    private static final String TAG = "UserFragment";
    private static final String VOTE_COUNT_TAG = "voteCount";
    private static UserFragment instance;
    private Handler mHandler;
    private XListView mListView;
    private UserAdapter mAdapter;
    private boolean isShow = false;
    Context context;

    public static UserFragment getInstance() {
        if (null == instance) {
            instance = new UserFragment();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        mHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        View v = inflater.inflate(R.layout.fra_user_layout, null);
        initHead(v,
                0,
                "name",
                R.drawable.tickets,
                new OnTitleClickListener() {
                    @Override
                    public void onLeftClick() {
                    }

                    @Override
                    public void onRightClick() {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(HttpSite.bugTickets);
                        intent.setData(content_url);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivityForResult(intent,1);
                    }
                });
        return v;
    }

    @Override
    protected void initBody(View v) {
        mListView = (XListView) v.findViewById(R.id.user_listview);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setAutoLoadEnable(true);
        mListView.setXListViewListener(this);
        mListView.setRefreshTime(getTime());
        mAdapter = new UserAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mAdapter.refreshHeadData(new UserAdapter.HeadItem(new UserAdapter.DataOne("headUrl", "", "", "")));
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getVoteFromClient(DreamAchieveApplication.sharedPreferences.getString("userid", ""));
                mListView.stopRefresh();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.stopLoadMore();
            }
        }, 1000);
    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    private void getUserVideoList() {
        Map<Object, Object> map = new HashMap<>();
        map.put("pageNum", 0);
        map.put("pageSize", 10);
        VolleyHttpRequest volleyHttpRequest = new VolleyHttpRequest();
        volleyHttpRequest.setVolleyHttpRequestListener(this);
        volleyHttpRequest.volleyHttpRequest(map, HttpSite.userVideoList +
                DreamAchieveApplication.sharedPreferences.getString("cookies", ""), VIDEO_LIST_TAG);
    }

    private BaseChildItem getBaseChildItem(String title, String movieUrl, String preUrl,
                                           String like, String comment, String aid, int videoId) {
        return new UserAdapter.BodyItem(new UserAdapter.DataTwo(title, movieUrl, preUrl, like, comment, aid, videoId));
    }

    public void onEventMainThread(String refreshPage) {
        if ("refresh_user_video".equals(refreshPage)) {
            getUserVideoList();
        } else if ("logout".equals(refreshPage)) {
            mAdapter.loginOut(new UserAdapter.HeadItem(new UserAdapter.DataOne("headUrl", "30", "40", "50")));
            setTitleName("");
        } else if ("uploadUserName".equals(refreshPage)) {
            setTitleName(DreamAchieveApplication.sharedPreferences.getString("username", ""));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserVideoList();
        MobclickAgent.onPageStart("UserFragment");
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("UserFragment");
        MobclickAgent.onPause(getActivity());
    }

    @Override
    public void volleyHttpRequestSuccess(JSONObject jsonObject) {
        if (jsonObject.optInt("retCode") == 1) {
            getVoteFromClient(DreamAchieveApplication.sharedPreferences.getString("userid", ""));
            setTitleName(DreamAchieveApplication.sharedPreferences.getString("username", ""));
            JSONArray ja = jsonObject.optJSONArray("list");
            List<BaseChildItem> items = new ArrayList<>();
            for (int i = 0; i < ja.length(); i++) {
                try {
                    JSONObject jo = ja.getJSONObject(i);
                    String videoPath =jo.optString("converUrl");
                    if (jo.optInt("fromType") == 1) {
                        videoPath = HttpSite.videoPath_qiniu + videoPath;
                    }
                    if(jo.optInt("status") == 4) {
                        items.add(getBaseChildItem(jo.optString("approve_time"), videoPath, jo.optString("picture"), jo.optString("total"), "", "", jo.optInt("id")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            mAdapter.addNewData(items);
        }
    }

    /**
     * 获得投票数量
     *
     * @author Bernie
     * create at 2016/4/8 14:47
     */
    public void getVoteFromClient(String userId) {
        if (userId.equals("")) {
            return;
        } else {
            Map<Object, Object> map = new HashMap<>();
            map.put("userid", userId);
            VolleyHttpRequest volleyHttpRequest = new VolleyHttpRequest();
            volleyHttpRequest.setVoteVideoResultVolleyHttpRequestListener(this);
            volleyHttpRequest.volleyVoteVideoResultHttpRequest(map, HttpSite.getVoteCount +
                    DreamAchieveApplication.sharedPreferences.getString("cookies", ""), VOTE_COUNT_TAG);
        }

    }

    @Override
    public void volleyHttpRequestError(VolleyError error) {
        Log.i(TAG, error.toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    /**
     * 获得投票数回调
     *
     * @author Bernie
     * create at 2016/4/8 14:50
     */
    @Override
    public void voteVideoResultRequest(JSONObject jsonObject) {
        pullVoteCountData(jsonObject.optJSONObject("map"));
    }

    public void pullVoteCountData(JSONObject jsonObject) {
        int voteCount = jsonObject.optInt("total") - jsonObject.optInt("used");
        int industriousPoints = jsonObject.optInt("integralSum");
        if (voteCount >= 1000 * 1000) {
            voteCount = voteCount / 1000 / 1000;
            UserAdapter.HeadItem.instance.setVoteCountText(String.valueOf(voteCount) + "m",String.valueOf(industriousPoints));
        } else if (voteCount >= 1000) {
            voteCount = voteCount / 1000;
            UserAdapter.HeadItem.instance.setVoteCountText(String.valueOf(voteCount) + "k",String.valueOf(industriousPoints));
        } else if (voteCount < 1000) {
            UserAdapter.HeadItem.instance.setVoteCountText(String.valueOf(voteCount),String.valueOf(industriousPoints));
        }
    }

    @Override
    public void voteVideoResultRequestError(VolleyError error) {
    }
}
