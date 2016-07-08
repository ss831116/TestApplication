package com.yuyu.android.wct.main.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.umeng.analytics.MobclickAgent;
import com.yuyu.android.wct.R;
import com.yuyu.android.wct.http.HttpSite;
import com.yuyu.android.wct.main.BaseActivity2;
import com.yuyu.android.wct.main.OnTitleClickListener;

import org.androidannotations.annotations.EActivity;

/**
 * Created by bernie.shi on 2016/5/6.
 */
@EActivity
public class DreamAchieveMainActivity extends BaseActivity2 {
    WebView webView;

    protected void onCreate(Bundle onSaveInstanceState) {
        super.onCreate(onSaveInstanceState);
        setContentView(R.layout.home_webview_activity);
        initView();
    }

    public void initView() {
        init(R.layout.awards_results_activity,
                0,
                getString(R.string.price_notice),
                0,
                new OnTitleClickListener() {
                    @Override
                    public void onLeftClick() {
                    }

                    @Override
                    public void onRightClick() {
                    }
                });
    }

    @Override
    public void initBody() {
        webView = (WebView) findViewById(R.id.webView);
        initOnClickListener();
    }

    public void initOnClickListener() {
        initPlayAwardsResults();
    }

    public void initPlayAwardsResults() {
        webView.loadUrl(HttpSite.homePageWebViewH5);
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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
