package com.yuyu.android.wct.main;

import android.os.Bundle;
import android.os.Handler;

import com.umeng.analytics.MobclickAgent;
import com.yuyu.android.wct.R;
import com.yuyu.android.wct.main.fragment.adapter.CommentAdapter;
import com.yuyu.android.wct.main.fragment.info.NewCommentsInfo;
import com.yuyu.android.wct.utils.PointUtil;
import com.yuyu.android.wct.widget.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by jackie.sun on 2016/3/25.
 */
public class CommentActivity extends BaseActivity2 implements XListView.IXListViewListener {
    private XListView mListview;
    private CommentAdapter mAdapter;
    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PointUtil.logForPage("CommentActivity");
        mHandler = new Handler();
        init(R.layout.activity_comment,
                R.drawable.back_btn,
                getString(R.string.comment),
                0,
                new OnTitleClickListener() {
                    @Override
                    public void onLeftClick() {
                        CommentActivity.this.finish();
                    }

                    @Override
                    public void onRightClick() {
                    }
                });
    }

    @Override
    public void initBody() {
        mListview = (XListView) findViewById(R.id.comment_listview);
        mListview.setPullRefreshEnable(true);
        mListview.setPullLoadEnable(true);
        mListview.setAutoLoadEnable(true);
        mListview.setXListViewListener(this);
        mListview.setRefreshTime(getTime());
        mAdapter = new CommentAdapter(CommentActivity.this);
        mListview.setAdapter(mAdapter);
        mAdapter.reFresh(getFake());
    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    private List<NewCommentsInfo> getFake() {
        List<NewCommentsInfo> infos = new ArrayList<>();
        infos.add(new NewCommentsInfo("1", "name one", "neirong1111111111111111111111111111", (long) 400.0, 1));
        infos.add(new NewCommentsInfo("1", "name two", "neirong2", (long) 20.0, 1));
        infos.add(new NewCommentsInfo("1", "name three", "neirong33333333", (long) 690.0, 1));
        infos.add(new NewCommentsInfo("1", "name four", "neirong44444", (long) 110.0, 1));
        return infos;
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListview.stopRefresh();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListview.stopLoadMore();
            }
        }, 1000);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("CommentActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("CommentActivity");
        MobclickAgent.onPause(this);
    }
}
