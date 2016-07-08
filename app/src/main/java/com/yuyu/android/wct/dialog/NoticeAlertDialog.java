package com.yuyu.android.wct.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuyu.android.wct.R;


/**
 * Created by jackie.sun on 2016/3/24.
 */
public class NoticeAlertDialog extends Dialog {
    private OnConfirmListener listener;
    private Context mContext;
    private int mNoticeTextId;
    private int mBtnTextId;

    public NoticeAlertDialog(Context context, int noticeId, int btnId, OnConfirmListener listener) {
        super(context, R.style.dialog);
        this.mContext = context;
        this.mNoticeTextId = noticeId;
        this.mBtnTextId = btnId;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_dialog_layout);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.dialog_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        TextView notice = (TextView) findViewById(R.id.dialog_notice_text);
        notice.setText(mNoticeTextId);
        Button confirm = (Button) findViewById(R.id.confirm_button);
        confirm.setText(mBtnTextId);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.confirm();
                dismiss();
            }
        });
    }

    public interface OnConfirmListener {
        void confirm();
    }
}
