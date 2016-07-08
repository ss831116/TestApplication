package com.yuyu.android.wct.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuyu.android.wct.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bernie.shi on 2016/3/30.
 */
public class RushBuyCountDownTimerView extends LinearLayout {
    private TextView tv_min_decade;
    private TextView tv_min_unit;
    private TextView tv_sec_decade;
    private TextView tv_sec_unit;
    private int msecond = 10;
    private long count;
    public static RushBuyCountDownTimerView instance;
    private Timer timer;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            countDown();
        }
    };

    public RushBuyCountDownTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        instance = this;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_countdowntimer, this);
        tv_min_decade = (TextView) view.findViewById(R.id.tv_min_decade);
        tv_min_unit = (TextView) view.findViewById(R.id.tv_min_unit);
        tv_sec_decade = (TextView) view.findViewById(R.id.tv_sec_decade);
        tv_sec_unit = (TextView) view.findViewById(R.id.tv_sec_unit);
    }

    public void start() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            }, 0, 10);
        }
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            count = 0;
            tv_min_decade.setText(count+"");
            tv_min_unit.setText(count+"");
            tv_sec_decade.setText(count+"");
            tv_sec_unit.setText(count+"");
            timer = null;
        }
    }


    private void countDown() {
        tv_min_decade.setText((count / 1000) % msecond + "");
        tv_min_unit.setText((count / 100) % msecond + "");
        tv_sec_decade.setText((count / 10) % msecond + "");
        tv_sec_unit.setText(count % msecond + "");
        count++;
    }
}