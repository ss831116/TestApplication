package com.yuyu.android.wct.main;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.umeng.analytics.MobclickAgent;
import com.yuyu.android.wct.R;
import com.yuyu.android.wct.http.HttpSite;
import com.yuyu.android.wct.main.fragment.util.EventVideoInfo;
import com.yuyu.android.wct.utils.PointUtil;

/**
 * Created by bernie.shi on 2016/3/25.
 */
public class AwardsResultsActivity extends BaseActivity2 {
    WebView webView;
    public String AwardsResultsUrl;
    String videoName;

    protected void onCreate(Bundle onSaveInstanceState) {
        super.onCreate(onSaveInstanceState);
        PointUtil.logForPage("AwardsResultsActivity");
        // setContentView(R.layout.awards_results_activity);
        getData(getIntent());
        initView();
    }

    public void getData(Intent intent) {
        EventVideoInfo eventVideoInfo = intent.getParcelableExtra("eventVideoInfo");
        videoName = eventVideoInfo.eName;
        AwardsResultsUrl = HttpSite.awardsResultsH5 + "?" + "lottoStartTime=" + eventVideoInfo.lottoStartTime
                + "&vote_end_time=" + eventVideoInfo.vote_end_time
                + "&eName=" + eventVideoInfo.eName
                + "&videoName=" + eventVideoInfo.videoName
                + "&name=" + eventVideoInfo.name
                + "&videoPicture=" + eventVideoInfo.videoPicture
                + "&type=" + eventVideoInfo.type
                + "&lottoType=" + eventVideoInfo.lottoType
                + "&aid=" + eventVideoInfo.aid
                + "&cid=" + eventVideoInfo.cid
                + "&lottoEndTime=" + eventVideoInfo.lottoEndTime;
    }

    public void initView() {
        init(R.layout.awards_results_activity,
                R.drawable.back_btn,
                videoName,
                0,
                new OnTitleClickListener() {
                    @Override
                    public void onLeftClick() {
                        AwardsResultsActivity.this.finish();
                    }

                    @Override
                    public void onRightClick() {

                    }
                });
    }

    public void initOnClickListener() {
        initPlayAwardsResults();
    }

    public void initPlayAwardsResults() {
        webView.loadUrl(AwardsResultsUrl);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
    }

    @Override
    public void initBody() {
        webView = (WebView) findViewById(R.id.webView);
        initOnClickListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AwardsResultsActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AwardsResultsActivity");
        MobclickAgent.onPause(this);
    }
}
