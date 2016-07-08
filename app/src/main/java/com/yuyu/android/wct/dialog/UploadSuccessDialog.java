package com.yuyu.android.wct.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.yuyu.android.wct.R;


/**
 * Created by jackie.sun on 2016/3/21.
 */
public class UploadSuccessDialog extends Dialog {
//    private OnConfirmListener listener;
    private Context mContext;

    public UploadSuccessDialog(Context context) {
        super(context, R.style.dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_suc_layout);
    }

    public interface OnConfirmListener {
        void confirm();
    }
}
