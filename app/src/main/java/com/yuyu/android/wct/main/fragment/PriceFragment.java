package com.yuyu.android.wct.main.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;
import com.yuyu.android.wct.DreamAchieveApplication;
import com.yuyu.android.wct.R;
import com.yuyu.android.wct.http.HttpSite;
import com.yuyu.android.wct.http.LoginError;
import com.yuyu.android.wct.http.VolleyHttpRequest;
import com.yuyu.android.wct.http.VolleyHttpRequestListener;
import com.yuyu.android.wct.login.LoginActivity_;
import com.yuyu.android.wct.main.AwardsResultsActivity;
import com.yuyu.android.wct.main.OnTitleClickListener;
import com.yuyu.android.wct.main.VideoRuleActivity_;
import com.yuyu.android.wct.main.fragment.util.EventVideoInfo;
import com.yuyu.android.wct.theme.CopyWriter;
import com.yuyu.android.wct.utils.HaveLogin;
import com.yuyu.android.wct.utils.PointUtil;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jackie.sun on 2016/3/17.
 */
@EFragment
public class PriceFragment extends BaseFragment implements VolleyHttpRequestListener {
    private static PriceFragment instance;
    public static String TAG_INTENT = "BroadcastReceiver.Third";
    WebView webView;
    Context context;

    public static PriceFragment getInstance() {
        if (null == instance) {
            instance = new PriceFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        View v = inflater.inflate(R.layout.fra_price_layout, null);

        initHead(v,
                0,
                CopyWriter.fistPageTitle,
                R.drawable.navigation_camera_btn,
                new OnTitleClickListener() {
                    @Override
                    public void onLeftClick() {
                        //TODO
                    }

                    @Override
                    public void onRightClick() {
                        //TODO
                        haveLogin();
                    }
                });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("PriceFragment");
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("PriceFragment");
        MobclickAgent.onPause(getActivity());
    }

    @UiThread
    public void startApplication() {
        startThirdActivity();
        sendBroadToThird();
        MobclickAgent.onEvent(getActivity(), PointUtil.APP_EVENT_FOR_FILM);
    }

    public void sendBroadToThird() {
        Intent intent = new Intent();
        intent.putExtra("defaultTitle", "愿望成真");
        intent.putExtra("defaultTileId", 157);
        intent.putExtra("isOtherApp", true);
        intent.putExtra("account", DreamAchieveApplication.sharedPreferences.getString("phone", ""));
        intent.setAction(TAG_INTENT);
        getActivity().getApplicationContext().sendBroadcast(intent);
    }

    public void startThirdActivity() {
        Intent mIntent = new Intent();
        ComponentName comp = new ComponentName("com.yuyu.android.pcam", "com.yuyu.android.pcam.main.activity.OverActivity");
        mIntent.setComponent(comp);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.putExtra("defaultTitle", "愿望成真");
        mIntent.putExtra("defaultTileId", 157);
        mIntent.putExtra("isOtherApp", true);
        mIntent.putExtra("account", DreamAchieveApplication.sharedPreferences.getString("phone", ""));
        context.startActivity(mIntent);
    }

    public boolean haveApplication() {
        boolean hasadvpackage = false;
        PackageManager packageManager = this.getActivity().getPackageManager();
        List<PackageInfo> list = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : list) {
            if (packageInfo.packageName.equals("com.yuyu.android.pcam") && packageInfo.versionCode > 10) {
                hasadvpackage = true;
                break;
            }
        }
        return hasadvpackage;
    }

    @Background
    public void haveLogin() {
        Map<Object, Object> map = new HashMap<>();
        map.put("pageNum", 0);
        map.put("pageSize", 10);
        VolleyHttpRequest volleyHttpRequest = new VolleyHttpRequest();
        volleyHttpRequest.setVolleyHttpRequestListener(this);
        volleyHttpRequest.volleyHttpRequest(map, HttpSite.userVideoList +
                DreamAchieveApplication.sharedPreferences.getString("cookies", ""), HaveLogin.HAVE_LOGIN);
    }

    @UiThread
    public void gotoInstallApplication() {
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("前去下载应用吗")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    protected void initBody(View v) {
        webView = (WebView) v.findViewById(R.id.webView);
        webView.loadUrl(HttpSite.homePageH5);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.addJavascriptInterface(new JsInteration(), "Winner");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                // TODO Auto-generated method stub
                return super.onJsAlert(view, url, message, result);
            }

        });
    }

    @Override
    public void volleyHttpRequestSuccess(JSONObject jsonObject) {
        if (!LoginError.noLogin(jsonObject)) {
            if (haveApplication()) {
                startApplication();
            } else {
                gotoInstallApplication();
            }
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity_.class);
            startActivity(intent);
        }
    }


    @Override
    public void volleyHttpRequestError(VolleyError error) {

    }

    /**
     * H5调用源生方法启动获奖详情页或调用原生拍照功能
     *
     * @author Bernie
     *         create at 2016/4/7 16:33
     */
    public class JsInteration {
        @JavascriptInterface
        public void toastMessage(String message) {
            Intent intent;
            if ("ruleBtnClick".equals(message.toString())) {
                intent = new Intent(getActivity().getApplicationContext(), VideoRuleActivity_.class);
                startActivity(intent);
            } else if ("filmBtnClick".equals(message.toString())) {
                haveLogin();
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    if (jsonObject.optString("aid") != null && !jsonObject.optString("aid").equals("") && !jsonObject.optString("aid").equals("null")) {
                        EventVideoInfo eventVideoInfo = new EventVideoInfo();
                        eventVideoInfo.lottoStartTime = jsonObject.optString("lottoStartTime");
                        eventVideoInfo.vote_end_time = jsonObject.optString("vote_end_time");
                        eventVideoInfo.eName = jsonObject.optString("eName");
                        eventVideoInfo.videoName = jsonObject.optString("videoName");
                        eventVideoInfo.name = jsonObject.optString("name");
                        eventVideoInfo.videoPicture = jsonObject.optString("videoPicture");
                        eventVideoInfo.type = jsonObject.optInt("type");
                        eventVideoInfo.lottoType = jsonObject.optInt("lottoType");
                        eventVideoInfo.aid = jsonObject.optString("aid");
                        eventVideoInfo.cid = jsonObject.optString("cid");
                        eventVideoInfo.lottoEndTime = jsonObject.optString("lottoEndTime");
                        intent = new Intent(getActivity().getApplicationContext(), AwardsResultsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("eventVideoInfo", eventVideoInfo);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
