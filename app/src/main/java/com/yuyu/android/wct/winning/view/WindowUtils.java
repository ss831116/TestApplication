package com.yuyu.android.wct.winning.view;

/**
 * Created by bernie.shi on 2016/3/23.
 */

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

import com.yuyu.android.wct.DreamAchieveApplication;
import com.yuyu.android.wct.R;

public class WindowUtils {
    private static final String LOG_TAG = "WindowUtils";
    private static View mView = null;
    private static WindowManager mWindowManager = null;
    private static Context mContext = null;
    public static Boolean isShown = false;
    View view;
    ImageView imageView;
    public static WindowUtils instance;
    public WindowUtils(Context context) {
        instance = this;
        showPopupWindow(context);
    }

    public void showPopupWindow(Context context) {
        if (isShown) {
            return;
        }
        isShown = true;
        mContext = context.getApplicationContext();
        mWindowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        mView = setUpView(context);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        int flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        params.flags = flags;
        params.format = PixelFormat.TRANSLUCENT;
        params.width = LayoutParams.WRAP_CONTENT;
        params.height = LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        mWindowManager.addView(mView, params);
    }

    public void hidePopupWindow() {
        if (isShown && null != mView) {
            mWindowManager.removeView(mView);
            DreamAchieveApplication.count = 0;
            isShown = false;
        }
    }
    public void playAnimationBitmaps(int bitmp){
        if(imageView != null)
        imageView.setImageResource(bitmp);
    }
    private View setUpView(final Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.float_layout,
                null);
        imageView = (ImageView) view.findViewById(R.id.animation_image);
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                WindowUtils.instance.hidePopupWindow();
                DreamAchieveApplication.instance.removeHandler();
                return false;
            }
        });
        return view;
    }
}
