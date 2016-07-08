package com.yuyu.android.wct.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuyu.android.wct.R;
import com.yuyu.android.wct.main.OnTitleClickListener;
import com.yuyu.android.wct.utils.SpUtil;

/**
 * Created by jackie.sun on 2016/3/17.
 */
public abstract class BaseFragment extends Fragment{
    protected ImageView mLeftImg;
    protected ImageView mRightImg;
    protected TextView mTitleName;
    protected RelativeLayout mLayout;

    /**
     * 初始化标题头
     * @param v                 页面布局
     * @param leftResId         左图标资源  0==隐藏
     * @param resName           页面名字
     * @param rightResId        右图标名字  0==隐藏
     * @param listener          左右图标点击事件
     */
    protected void initHead(View v,int leftResId, String resName,int rightResId, final OnTitleClickListener listener){
        mLeftImg = (ImageView) v.findViewById(R.id.title_left_icon);
        mRightImg = (ImageView) v.findViewById(R.id.title_right_icon);
        mTitleName = (TextView) v.findViewById(R.id.title_name);
        mLayout = (RelativeLayout) v.findViewById(R.id.title_bar_layout);
        setTitleName(resName);
        if (0 == leftResId) {
            setLeftImgVisibility(false);
        } else {
            setLeftImgVisibility(true);
            mLeftImg.setImageResource(leftResId);

        }
        if (0 == rightResId) {
            setRightImgVisibility(false);
        } else {
            setRightImgVisibility(true);
            mRightImg.setImageResource(rightResId);

        }
        if(listener!=null){
            mLeftImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onLeftClick();
                }
            });
            mRightImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRightClick();
                }
            });
        }
        initBody(v);
    }

    protected void setLeftImgVisibility(boolean isShow) {
        mLeftImg.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    protected void setRightImgVisibility(boolean isShow) {
        mRightImg.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        mLayout.setBackgroundResource(SpUtil.getSelectColor(getActivity()));
    }

    protected void setTitleName(String resName) {
        mTitleName.setText(resName);
    }
    protected  void setmLeftImg(int leftResId){
        mLeftImg.setImageResource(leftResId);
    }
    protected void setmRightImg(int rightResId){
        mRightImg.setImageResource(rightResId);
    }
    protected abstract void initBody(View v);
}
