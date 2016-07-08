package com.yuyu.android.wct.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.umeng.analytics.MobclickAgent;
import com.yuyu.android.wct.R;
import com.yuyu.android.wct.main.HomeActivity;
import com.yuyu.android.wct.utils.PointUtil;
import com.yuyu.android.wct.utils.SpUtil;


/**
 * Created by jackie.sun on 2016/4/5.
 */
public class ChooseThemeDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private ImageView mSelectImg1;
    private ImageView mSelectImg2;
    private ImageView mSelectImg3;
    private ImageView mSelectImg4;
    private OnChooseListener mListener;

    public ChooseThemeDialog(Context context,OnChooseListener listener) {
        super(context, R.style.dialog);
        this.mContext = context;
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.theme_dialog_layout);
        initImage();
        initOnclickListener();
        initSelectImg();
    }

    private void initImage() {
        mSelectImg1 = (ImageView) findViewById(R.id.theme_select1);
        mSelectImg2 = (ImageView) findViewById(R.id.theme_select2);
        mSelectImg3 = (ImageView) findViewById(R.id.theme_select3);
        mSelectImg4 = (ImageView) findViewById(R.id.theme_select4);
    }

    private void initOnclickListener() {
        findViewById(R.id.dialog_layout).setOnClickListener(this);
        findViewById(R.id.theme_color1).setOnClickListener(this);
        findViewById(R.id.theme_color2).setOnClickListener(this);
        findViewById(R.id.theme_color3).setOnClickListener(this);
        findViewById(R.id.theme_color4).setOnClickListener(this);
    }

    private void setHomePageTabBar(String color){
        if(HomeActivity.instance != null){
            HomeActivity.instance.setRadioGroup(color);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.theme_color1:
                setSelect(1);
                setHomePageTabBar("red");
                break;
            case R.id.theme_color2:
                setSelect(2);
                setHomePageTabBar("green");
                break;
            case R.id.theme_color3:
                setSelect(3);
                setHomePageTabBar("yellow");
                break;
            case R.id.theme_color4:
                setSelect(4);
                setHomePageTabBar("purple");
                break;
            case R.id.dialog_layout:
                dismiss();
                break;
        }
    }

    private void initSelectImg() {
        int i = SpUtil.getSelectPosi(mContext);
        mSelectImg1.setVisibility(i == 1 ? View.VISIBLE : View.GONE);
        mSelectImg2.setVisibility(i == 2 ? View.VISIBLE : View.GONE);
        mSelectImg3.setVisibility(i == 3 ? View.VISIBLE : View.GONE);
        mSelectImg4.setVisibility(i == 4 ? View.VISIBLE : View.GONE);
    }

    private void setSelect(int posi) {
        PointUtil.logForEvent(PointUtil.APP_EVENT_FOR_THEME);
        MobclickAgent.onEvent(mContext,PointUtil.APP_EVENT_FOR_THEME);
        SpUtil.setSelectPosi(mContext, posi);
        mListener.choose();
        dismiss();
    }

    public interface OnChooseListener{
        void choose();
    }
}
