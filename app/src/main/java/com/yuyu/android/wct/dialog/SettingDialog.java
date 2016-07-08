package com.yuyu.android.wct.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yuyu.android.wct.R;


/**
 * Created by jackie.sun on 2016/3/24.
 */
public class SettingDialog extends Dialog {
    private OnConfirmListener listener;
    private Context mContext;
    private int mBtnTextId;

    public SettingDialog(Context context, int btnId, OnConfirmListener listener) {
        super(context, R.style.dialog);
        this.mContext = context;
        this.mBtnTextId = btnId;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_dialog_layout);
        final EditText notice = (EditText) findViewById(R.id.dialog_notice_text);
        Button confirm = (Button) findViewById(R.id.confirm_button);
        confirm.setText(mBtnTextId);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = notice.getText().toString();
                if (text.matches("^[a-zA-Z0-9\u4E00-\u9FA5_]+$")) {
                    listener.confirm(text);
                    dismiss();
                } else {
                    Toast.makeText(mContext, R.string.input_type_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public interface OnConfirmListener {
        void confirm(String data);
    }
}
