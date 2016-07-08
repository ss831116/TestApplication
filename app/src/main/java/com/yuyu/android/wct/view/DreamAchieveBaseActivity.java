package com.yuyu.android.wct.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import cn.jpush.android.api.InstrumentedActivity;

/**
 * Created by bernie.shi on 2016/3/15.
 */
public class DreamAchieveBaseActivity extends InstrumentedActivity {
    Context context;
    ConnectivityManager connectivityManager;

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        context = getApplicationContext();
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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

    public void closeActivity() {
        this.finish();
    }

    //判断是否有网络连接
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            NetworkInfo mNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //判断Wifi是否可用
    public boolean isWifiConnected(Context context) {
        if (context != null) {
            NetworkInfo mWiFiNetworkInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //判断移动网络是否可用
    public boolean isMobileConnected(Context context) {
        if (context != null) {
            NetworkInfo mMobileNetworkInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //判断网络连接类型
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }
    public void setImageViewBackground(boolean isColor,int color,Bitmap bitmap,ImageView imageView){
        if(isColor){
            imageView.setBackgroundColor(color);
        }else{
            imageView.setImageBitmap(bitmap);
        }
    }
    public void setLayoutBackground(boolean isColor, int color, Drawable drawable, LinearLayout linearLayout, RelativeLayout relativeLayout){
        if(linearLayout == null && relativeLayout != null){
            if(isColor) {
                relativeLayout.setBackgroundColor(color);
            } else{
                relativeLayout.setBackground(drawable);
            }
        }else if(linearLayout != null && relativeLayout == null){
            if(isColor) {
                linearLayout.setBackgroundColor(color);
            } else{
                linearLayout.setBackground(drawable);
            }
        }
    }
}
