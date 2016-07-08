package com.yuyu.android.wct.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuyu.android.wct.R;


/**
 * Created by jackie.sun on 2016/3/21.
 */
public class UploadFailDialog extends Dialog {
    //    private OnConfirmListener listener;
    private Context mContext;
    String dialogTitle;
    String dialogDelete;
    String dialogReupload;
    TextView upload_video_fail_title;
    TextView failUploadDelete;
    TextView failUploadReUpload;
    ImageView delete_video_image;
    ImageView reUpload_image;
    private OnConfirmListener listener;

    public UploadFailDialog(Context context, String dialogTitle, String dialogDelete, String dialogReupload, OnConfirmListener listener) {
        super(context, R.style.dialog);
        this.mContext = context;
        this.dialogTitle = dialogTitle;
        this.dialogDelete = dialogDelete;
        this.dialogReupload = dialogReupload;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fail_layout);
        upload_video_fail_title = (TextView) findViewById(R.id.upload_video_fail_title);
        failUploadDelete = (TextView) findViewById(R.id.failUploadDelete);
        failUploadReUpload = (TextView) findViewById(R.id.failUploadReUpload);
        delete_video_image = (ImageView) findViewById(R.id.delete_video_image);
        reUpload_image = (ImageView) findViewById(R.id.reUpload_image);
        upload_video_fail_title.setText(dialogTitle);
        failUploadDelete.setText(dialogDelete);
        failUploadReUpload.setText(dialogReupload);
        delete_video_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                listener.deleteVideo();
            }
        });
        reUpload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.reUploadVideo();
                dismiss();
            }
        });
    }

    public interface OnConfirmListener {
        void deleteVideo();

        void reUploadVideo();
    }
}
