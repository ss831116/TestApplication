package com.yuyu.android.wct.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.umeng.analytics.MobclickAgent;
import com.yuyu.android.wct.R;
import com.yuyu.android.wct.http.HttpSite;
import com.yuyu.android.wct.main.OnTitleClickListener;
import com.yuyu.android.wct.main.activity.TrailerDetailActivity_;
import com.yuyu.android.wct.theme.CopyWriter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bernie.shi on 2016/4/5.
 */
public class TrailerFragment extends BaseFragment {
    WebView webView;

    @Override
    public void onCreate(Bundle onSaveInstanceState) {
        super.onCreate(onSaveInstanceState);
    }

    private static TrailerFragment instance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fra_trailer_layout, container, false);
        initHead(v,
                0,
                CopyWriter.thirdPageTitle,
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
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("TrailerFragment");
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("TrailerFragment");
        MobclickAgent.onPause(getActivity());
    }

    public static TrailerFragment getInstance() {
        if (null == instance) {
            instance = new TrailerFragment();
        }
        return instance;
    }

    @Override
    protected void initBody(View v) {
        webView = (WebView) v.findViewById(R.id.webView);
        initPlayAwardsResults();
    }

    public void initPlayAwardsResults() {
        webView.loadUrl(HttpSite.trailerPageH5);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.addJavascriptInterface(new JsTrailerInteration(), "Winner");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                // TODO Auto-generated method stub
                return super.onJsAlert(view, url, message, result);
            }

        });
    }

    public class JsTrailerInteration {
        @JavascriptInterface
        public void toastMessage(String message) {
            Intent intent;
            try {
                JSONObject jsonObject = new JSONObject(message);
                intent = new Intent(getActivity().getApplicationContext(), TrailerDetailActivity_.class);
                intent.putExtra("aid", jsonObject.optString("id"));
                intent.putExtra("titleName",
                        jsonObject.optString("live_start_time").substring(jsonObject.optString("live_start_time").length() - 9, jsonObject.optString("live_start_time").length() - 3) +
                                " -" +
                                jsonObject.optString("live_end_time").substring(jsonObject.optString("live_end_time").length() - 9, jsonObject.optString("live_end_time").length() - 3) +
                                getString(R.string.date));
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
