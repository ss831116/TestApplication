package com.yuyu.android.wct.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jackie.sun on 2015/12/8.
 */
public class WanRecycleView extends PullToRefreshBase<RecyclerView> {

    public WanRecycleView(Context context) {
        super(context);
    }

    public WanRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WanRecycleView(Context context, Mode mode) {
        super(context, mode);
    }

    public WanRecycleView(Context context, Mode mode, AnimationStyle animStyle) {
        super(context, mode, animStyle);
    }

    @Override
    public Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        RecyclerView view = new RecyclerView(context, attrs);
        return view;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        View view = getRefreshableView().getChildAt(getRefreshableView().getChildCount() - 1);
        if (null != view) {
            return getRefreshableView().getBottom() >= view.getBottom();
        }
        return false;
    }

    @Override
    protected boolean isReadyForPullStart() {
        View view = getRefreshableView().getChildAt(0);

        if (view != null) {
            return view.getTop() >= getRefreshableView().getTop();
        }
        return false;
    }
}
