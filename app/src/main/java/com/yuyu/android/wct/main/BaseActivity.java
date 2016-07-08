package com.yuyu.android.wct.main;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by jackie.sun on 2016/3/17.
 */
public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}
