package com.yuyu.android.wct.main;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuyu.android.wct.R;
import com.yuyu.android.wct.utils.SpUtil;

/**
 * Created by jackie.sun on 2016/3/17.
 */
public abstract class BaseActivity2 extends BaseActivity {
    protected ImageView mLeftImg;
    protected ImageView mRightImg;
    protected TextView mTitleName;
    protected RelativeLayout mLayout;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mLeftImg = (ImageView) findViewById(R.id.title_left_icon);
        mRightImg = (ImageView) findViewById(R.id.title_right_icon);
        mTitleName = (TextView) findViewById(R.id.title_name);
        mLayout = (RelativeLayout) findViewById(R.id.title_bar_layout);
    }

    protected void init(int resLayoutId, int resLeftId, String resName, int resRightId, OnTitleClickListener listener) {
        setContentView(resLayoutId);
        initTitle(resLeftId, resName, resRightId, listener);
        initBody();
    }

    protected void initTitle(int resLeftId, String resName, int resRightId, final OnTitleClickListener listener) {
        setTitleName(resName);
        if (0 == resLeftId) {
            setLeftImgVisibility(false);
        } else {
            setLeftImgVisibility(true);
            mLeftImg.setImageResource(resLeftId);
            mLeftImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onLeftClick();
                }
            });
        }
        if (0 == resRightId) {
            setRightImgVisibility(false);
        } else {
            setRightImgVisibility(true);
            mRightImg.setImageResource(resLeftId);
            mRightImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRightClick();
                }
            });
        }
    }

    protected void setLeftImgVisibility(boolean isShow) {
        mLeftImg.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    protected void setRightImgVisibility(boolean isShow) {
        mRightImg.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    protected void setTitleName(String resName) {
        mTitleName.setText(resName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLayout.setBackgroundResource(SpUtil.getSelectColor(getApplicationContext()));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public abstract void initBody();
}
