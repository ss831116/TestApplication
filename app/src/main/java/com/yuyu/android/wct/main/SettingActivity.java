package com.yuyu.android.wct.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;
import com.ypy.eventbus.EventBus;
import com.yuyu.android.wct.DreamAchieveApplication;
import com.yuyu.android.wct.R;
import com.yuyu.android.wct.dialog.ChooseThemeDialog;
import com.yuyu.android.wct.dialog.NoticeAlertDialog;
import com.yuyu.android.wct.dialog.SettingDialog;
import com.yuyu.android.wct.http.HttpSite;
import com.yuyu.android.wct.http.VolleyHttpRequest;
import com.yuyu.android.wct.http.VolleyHttpRequestListener;
import com.yuyu.android.wct.utils.PointUtil;
import com.yuyu.android.wct.utils.SpUtil;
import com.yuyu.android.wct.utils.StringUtil;
import com.yuyu.android.wct.widget.CircleImageView;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jackie.sun on 2016/4/5.
 */
public class SettingActivity extends BaseActivity2 implements View.OnClickListener {
    private static final String UPDATE_USER_NAME = "user_name";
    private CircleImageView mUserIcon;
    private ImageView mSelectColor;
    private TextView mSelectText;
    private TextView mUserName;
    private TextView mUserTel;
    private TextView mUserMail;
    private TextView mVersion;
    private int crop = 320;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PointUtil.logForPage("SettingActivity");
        init(R.layout.activity_setting_layout,
                R.drawable.back_btn,
                getString(R.string.setting),
                0,
                new OnTitleClickListener() {
                    @Override
                    public void onLeftClick() {
                        SettingActivity.this.finish();
                    }

                    @Override
                    public void onRightClick() {

                    }
                });
    }

    @Override
    public void initBody() {
        initItem();
        initClickListener();
    }

    private void initItem() {
        mUserIcon = (CircleImageView) findViewById(R.id.setting_user_icon);
        mSelectColor = (ImageView) findViewById(R.id.setting_theme_color);
        mSelectText = (TextView) findViewById(R.id.setting_theme_text);
        mUserName = (TextView) findViewById(R.id.setting_user_name);
        mUserTel = (TextView) findViewById(R.id.setting_user_tel);
        mUserMail = (TextView) findViewById(R.id.setting_user_mail);
        mVersion = (TextView) findViewById(R.id.setting_version);
        setData();
    }

    private void setData() {
        mSelectColor.setImageResource(SpUtil.getSettingDrawable(SettingActivity.this));
        mSelectText.setText(SpUtil.getSelectText(SettingActivity.this));
        mUserName.setText(DreamAchieveApplication.sharedPreferences.getString("username", ""));
        mUserTel.setText(DreamAchieveApplication.sharedPreferences.getString("phone", ""));
        String mail = DreamAchieveApplication.sharedPreferences.getString("goldpayemail", "");
        if (!StringUtil.isEmpty(mail)) {
            mUserMail.setText(mail);
        } else {
            mUserMail.setText(R.string.not_bind);
        }
        mVersion.setText(SpUtil.getVersion(SettingActivity.this));
    }

    private void initClickListener() {
        findViewById(R.id.setting_name_layout).setOnClickListener(this);
        findViewById(R.id.setting_theme_layout).setOnClickListener(this);
        mUserIcon.setOnClickListener(this);
        findViewById(R.id.setting_login_out).setOnClickListener(this);
    }

    public void updateUserName(final String name) {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("phone", "");
        map.put("countryId", "");
        map.put("userName", name);
        VolleyHttpRequest volleyHttpRequest = new VolleyHttpRequest();
        volleyHttpRequest.setVolleyHttpRequestListener(new VolleyHttpRequestListener() {
            @Override
            public void volleyHttpRequestSuccess(JSONObject jsonObject) {
                Toast.makeText(SettingActivity.this, R.string.confirm_success, Toast.LENGTH_SHORT).show();
                DreamAchieveApplication.editor.putString("username", name);
                DreamAchieveApplication.editor.commit();
                EventBus.getDefault().post("uploadUserName");
                mUserName.setText(name);
            }

            @Override
            public void volleyHttpRequestError(VolleyError error) {
                Toast.makeText(SettingActivity.this, R.string.confirm_fail, Toast.LENGTH_SHORT).show();
            }
        });
        volleyHttpRequest.volleyHttpRequest(map, HttpSite.updateUserInfo +
                DreamAchieveApplication.sharedPreferences.getString("cookies", ""), UPDATE_USER_NAME);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_name_layout:
                SettingDialog dialog1 = new SettingDialog(SettingActivity.this, R.string.confirm, new SettingDialog.OnConfirmListener() {
                    @Override
                    public void confirm(String data) {
                        updateUserName(data);
                    }
                });
                dialog1.show();
                break;
            /*case R.id.setting_phone_layout:
                SettingDialog dialog2 = new SettingDialog(SettingActivity.this, R.string.confirm, new SettingDialog.OnConfirmListener() {
                    @Override
                    public void confirm(String data) {

                    }
                });
                dialog2.show();
                break;*/
            case R.id.setting_theme_layout:
                ChooseThemeDialog dialog3 = new ChooseThemeDialog(SettingActivity.this, new ChooseThemeDialog.OnChooseListener() {
                    @Override
                    public void choose() {
                        mSelectColor.setImageResource(SpUtil.getSettingDrawable(SettingActivity.this));
                        mSelectText.setText(SpUtil.getSelectText(SettingActivity.this));
                        mLayout.setBackgroundResource(SpUtil.getSelectColor(SettingActivity.this));
                    }
                });
                dialog3.show();
                break;
            case R.id.setting_user_icon:
                //Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                //startActivityForResult(intent, 1);
                break;
            case R.id.setting_login_out:
                NoticeAlertDialog dialog4 = new NoticeAlertDialog(SettingActivity.this, R.string.confirm_to_login_out, R.string.confirm, new NoticeAlertDialog.OnConfirmListener() {
                    @Override
                    public void confirm() {
                        DreamAchieveApplication.editor.clear();
                        DreamAchieveApplication.editor.commit();
                        EventBus.getDefault().post("logout");
                        SettingActivity.this.finish();
                    }
                });
                dialog4.show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            Intent mintent = getCropImageIntent(Uri.fromFile(new File(data.getStringExtra("picPath"))));
            if (mintent != null) {
                startActivityForResult(mintent, 101);
            }
        } else if (requestCode == 101 && data != null) {
            Bitmap bitmap = data.getParcelableExtra("data");
            if (bitmap != null)
                mUserIcon.setImageBitmap(bitmap);
        }
    }

    private Intent getCropImageIntent(Uri photoUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", crop);
        intent.putExtra("outputY", crop);
        intent.putExtra("return-data", true);
        return intent;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("Setting");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("Setting");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
