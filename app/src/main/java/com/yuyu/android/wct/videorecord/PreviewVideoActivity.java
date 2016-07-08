package com.yuyu.android.wct.videorecord;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moudle.videoplayer.FullScreenVideoView;
import com.moudle.videoplayer.VideoDisplayManager;
import com.yuyu.android.wct.R;
import com.yuyu.android.wct.dialog.NoticeAlertDialog;
import com.yuyu.android.wct.dialog.ProgressDialog;
import com.yuyu.android.wct.dialog.UploadFailDialog;
import com.yuyu.android.wct.http.FileUpload;
import com.yuyu.android.wct.login.LoginActivity;
import com.yuyu.android.wct.login.LoginActivity_;
import com.yuyu.android.wct.sharedout.activity.SharedToOutActivity_;
import com.yuyu.android.wct.theme.CopyWriter;
import com.yuyu.android.wct.utils.HaveLogin;
import com.yuyu.android.wct.view.DreamAchieveBaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by bernie.shi on 2016/3/16.
 */
public class PreviewVideoActivity extends DreamAchieveBaseActivity implements View.OnClickListener {
    private RelativeLayout mMainLayout;
    private EditText mEditSubText;
    private TextView mSubTextview;
    private String mSubTitleText;
    private FullScreenVideoView mVideoView;
    Context context;
    private String mVideoPath;
    private ImageView mSubmit;

    private int statusBarHeight = 0;
    private int keyboardHeight = 0;
    private boolean isShowKeyboard = false;
    private int navigationHeight = 0;
    public static PreviewVideoActivity instance;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x01:
                    Intent intent = new Intent(PreviewVideoActivity.this, SharedToOutActivity_.class);
                    startActivity(intent);
                    PreviewVideoActivity.this.finish();
                    break;
                case 0x02:
                    UploadFailDialog uploadFailDialog = new UploadFailDialog(PreviewVideoActivity.this, CopyWriter.dialogFailTitle, CopyWriter.dialogFailDelete, CopyWriter.dialogFailReUpload, new UploadFailDialog.OnConfirmListener() {
                        @Override
                        public void deleteVideo() {
                            File f = new File(mVideoPath);
                            if (f != null && f.exists()) {
                                f.delete();
                            }
                            PreviewVideoActivity.this.finish();
                        }

                        @Override
                        public void reUploadVideo() {
                            ProgressDialog.newInstance().show(getFragmentManager(), "dialog");
                            mSubmit.setEnabled(false);
                            submit();
                        }
                    });
                    uploadFailDialog.show();
                    break;
                case 0x03:
                    mSubmit.setEnabled(true);
                    break;
            }
        }
    };

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        instance =  this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.preview_video_activity);
        statusBarHeight = getStatusBarHeight(PreviewVideoActivity.this);
        navigationHeight = getNavigationBarHeight(PreviewVideoActivity.this);
        context = getApplicationContext();
        initView();
    }

    public void initInfo(FullScreenVideoView videoView, Intent intent) {
        if (null != intent) {
            mVideoPath = intent.getStringExtra("videoPath");
            boolean isLocalVideo = intent.getBooleanExtra("isLocalVideo", true);
            VideoDisplayManager.getInstance().init(mVideoPath, videoView, isLocalVideo);
            scanPhotos(mVideoPath, context);
        }
    }

    public static void scanPhotos(String filePath, Context context) {
        Intent intent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    public void initView() {
        mVideoView = (FullScreenVideoView) findViewById(R.id.videoView);
        mMainLayout = (RelativeLayout) findViewById(R.id.edit_video_control_layout);
        mSubTextview = (TextView) findViewById(R.id.edit_video_subtitle_text);
        initButton();
        initEditText();
        mMainLayout.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        initInfo(mVideoView, getIntent());
    }

    private void initEditText() {
        mEditSubText = (EditText) findViewById(R.id.edit_video_edittext1);
        mEditSubText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mSubTitleText = s.toString().trim();
            }
        });
        mEditSubText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                closeInputWindow();
                return true;
            }
        });
    }

    private void initButton() {
        ImageView delete = (ImageView) findViewById(R.id.pre_video_delete);
        ImageView subtitle = (ImageView) findViewById(R.id.pre_video_subtitle);
        mSubmit = (ImageView) findViewById(R.id.pre_video_submit);
        delete.setOnClickListener(this);
        subtitle.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pre_video_delete:
                NoticeAlertDialog dialog = new NoticeAlertDialog(this, R.string.sure_to_delete_video, R.string.delete, new NoticeAlertDialog.OnConfirmListener() {
                    @Override
                    public void confirm() {
                        File f = new File(mVideoPath);
                        if (f != null && f.exists()) {
                            f.delete();
                        }
                        PreviewVideoActivity.this.finish();
                    }
                });
                dialog.show();
                break;
            case R.id.pre_video_subtitle:
                setEditVisible(true);
                editTextGetFocus();
                openInputWindow();
                break;
            case R.id.pre_video_submit:
                mSubmit.setEnabled(false);
                new HaveLogin(getApplicationContext());
                break;
        }
    }

    public void canUploadVideo(boolean can){
        mSubmit.setEnabled(true);
        if(!can){
            submit();
        }else{
            Intent intent = new Intent(PreviewVideoActivity.this, LoginActivity_.class);
            intent.putExtra(LoginActivity.TAG_FROM, "user");
            startActivity(intent);
        }
    }
    private void submit() {
        ProgressDialog.newInstance().show(getFragmentManager(), "dialog");
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String s = "";
                try {
                    s = FileUpload.post(new File(mVideoPath));
                    Log.i("ABC", s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (s.contains("Hello")) {
                    Log.i("ABC", "login");
                    Intent i = new Intent(PreviewVideoActivity.this, LoginActivity_.class);
                    startActivity(i);
                    mHandler.sendEmptyMessage(0x03);
                } else {
                    try {
                        JSONObject jo = new JSONObject(s);
                        if (jo.optInt("retCode") == 1) {
                            mHandler.sendEmptyMessage(0x01);
                        } else {
                            mHandler.sendEmptyMessage(0x02);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        mHandler.sendEmptyMessage(0x03);
                    }
                }
            }
        });
        t.start();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        VideoDisplayManager.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void editTextGetFocus() {
        mEditSubText.setFocusable(true);
        mEditSubText.setFocusableInTouchMode(true);
        mEditSubText.requestFocus();
    }

    public void editTextClearFocus() {
        mEditSubText.setFocusable(false);
        mEditSubText.setFocusableInTouchMode(false);
        mEditSubText.clearFocus();
    }

    private void setEditVisible(boolean state) {
        mEditSubText.setVisibility(state ? View.VISIBLE : View.INVISIBLE);
        mSubTextview.setVisibility(state ? View.INVISIBLE : View.VISIBLE);
    }

    public void openInputWindow() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditSubText, InputMethodManager.SHOW_FORCED);
    }

    public void closeInputWindow() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditSubText.getWindowToken(), 0);
    }

    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {


            Rect r = new Rect();
            mMainLayout.getWindowVisibleDisplayFrame(r);


            int screenHeight = mMainLayout.getRootView().getHeight();

            int heightDiff = screenHeight - (r.bottom - r.top);
            int barHeight = statusBarHeight + navigationHeight;

            if (keyboardHeight == 0 && heightDiff > barHeight) {
                keyboardHeight = heightDiff - statusBarHeight;
            }

            if (isShowKeyboard) {
                if (heightDiff <= barHeight) {
                    isShowKeyboard = false;
                    onHideKeyboard();
                }
            } else {
                if (heightDiff > barHeight) {
                    isShowKeyboard = true;
                    onShowKeyboard();
                }
            }
        }
    };


    @SuppressLint("NewApi")
    public static boolean checkDeviceHasNavigationBar(Context activity) {
        boolean hasMenuKey = false;
        boolean hasBackKey = false;
        try {
            hasMenuKey = ViewConfiguration.get(activity).hasPermanentMenuKey();
        } catch (java.lang.NoSuchMethodError e) {
        }
        try {
            hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        } catch (java.lang.NoSuchMethodError e) {
        }
        if (!hasMenuKey && !hasBackKey) {
            return true;
        }
        return false;
    }


    public int getNavigationBarHeightEx(Context activity) {
        if (checkDeviceHasNavigationBar(activity)) {
            return getNavigationBarHeight(activity);
        }
        return 0;
    }

    public int getNavigationBarHeight(Context activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    private void setTextData() {
        mEditSubText.setText(mSubTitleText);
        mEditSubText.setSelection(mSubTitleText.length());
        mSubTextview.setText(mSubTitleText);
    }

    private void onShowKeyboard() {
        setEditVisible(true);
        setTextData();
    }

    private void onHideKeyboard() {
        setEditVisible(false);
        setTextData();
        editTextClearFocus();
    }


    public int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
