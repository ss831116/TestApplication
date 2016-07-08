package com.yuyu.android.wct.main;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.umeng.analytics.MobclickAgent;
import com.yuyu.android.wct.R;
import com.yuyu.android.wct.http.HttpSite;
import com.yuyu.android.wct.utils.PointUtil;

import org.androidannotations.annotations.EActivity;

/**
 * Created by bernie.shi on 2016/3/28.
 */
@EActivity
public class VideoRuleActivity extends BaseActivity2 {
    WebView webView;

    protected void onCreate(Bundle onSaveInstanceState) {
        super.onCreate(onSaveInstanceState);
        PointUtil.logForPage("VideoRuleActivity");
        setContentView(R.layout.video_rule_activity);
        initView();
    }

    public void initView() {
        init(R.layout.awards_results_activity,
                R.drawable.back_btn,
                getString(R.string.rule_info),
                0,
                new OnTitleClickListener() {
                    @Override
                    public void onLeftClick() {
                        VideoRuleActivity.this.finish();
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
        webView.loadUrl(HttpSite.rulePageH5);
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
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("VideoRuleActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("VideoRuleActivity");
        MobclickAgent.onPause(this);
    }

    @Override
    public void initBody() {
        webView = (WebView) findViewById(R.id.webView);
        initOnClickListener();
    }
}
