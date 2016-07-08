package com.yuyu.android.wct.sharedout.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
//import com.umeng.socialize.controller.UMServiceFactory;
//import com.umeng.socialize.controller.UMSocialService;
//import com.umeng.socialize.sso.UMSsoHandler;
import com.yuyu.android.wct.DreamAchieveApplication;
import com.yuyu.android.wct.R;
import com.yuyu.android.wct.http.GetVoteResultListener;
import com.yuyu.android.wct.http.HttpSite;
import com.yuyu.android.wct.http.VolleyHttpRequest;
import com.yuyu.android.wct.theme.CopyWriter;
import com.yuyu.android.wct.view.DreamAchieveBaseActivity;

import org.androidannotations.annotations.EActivity;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bernie.shi on 2016/3/21.
 */
@EActivity
public class SharedToOutActivity extends DreamAchieveBaseActivity implements OnClickListener, GetVoteResultListener {
    ImageView share_image;
    RelativeLayout share_relativeLayout;
    LinearLayout share_linearLayout;
    TextView upload_success_text;
    TextView get_vote;
    TextView sent_friends;
    String imagePath = "";
    int mvId;
    private String shareUrl;
    private final String SHARED_TAG = "shared_tag";

    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.shared_out_activity);
        initVideoInfo(getIntent());
        initView();
    }
    public void initVideoInfo(Intent intent){
        if(intent != null) {
            imagePath = intent.getStringExtra("imagePath");
            mvId = intent.getIntExtra("mvId",0);
            getUrlFromClient(mvId);
        }
    }
    public void getUrlFromClient(int mvId){
        Map<Object, Object> map = new HashMap<>();
        map.put("mvId", mvId);
        map.put("shareWhere", "WeChat");
        map.put("url", "");
        map.put("type", 0);
        VolleyHttpRequest volleyHttpRequest = new VolleyHttpRequest();
        volleyHttpRequest.setVoteVideoResuleyHttpRequestListener(this);
        volleyHttpRequest.voteResultHttpRequest(map, HttpSite.getShareUrl +
                DreamAchieveApplication.sharedPreferences.getString("cookies", ""), SHARED_TAG);
    }
    protected void onResume(){
        super.onResume();
        upload_success_text.setText(CopyWriter.dialogSuccessUploadDes);
        get_vote.setText(CopyWriter.dialogSuccessShareDes);
        sent_friends.setText(CopyWriter.dialogSuccessShareOut);
    }
    public void initView(){
        share_image = (ImageView)findViewById(R.id.share_image);
        share_relativeLayout = (RelativeLayout)findViewById(R.id.share_relativeLayout);
        share_linearLayout = (LinearLayout)findViewById(R.id.share_linearLayout);
        upload_success_text = (TextView)findViewById(R.id.upload_success_text);
        get_vote  = (TextView)findViewById(R.id.get_vote);
        sent_friends = (TextView)findViewById(R.id.sent_friends);
        initOnClickListener();
    }
    public void initOnClickListener(){
        share_image.setOnClickListener(this);
        share_relativeLayout.setOnClickListener(this);
        share_linearLayout.setOnClickListener(this);
        share_image.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.share_image:
                UMImage image = new UMImage(SharedToOutActivity.this, imagePath);
                new ShareAction(this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                        .withMedia(image)
                        .withTargetUrl(shareUrl)
                        .withText("#愿望成真# 创业视频大赛\n" +
                                  "还在为创业没有资金而犯愁吗？\n" +
                                  "快用Rollit拍视频赢取创业金吧！")
                        .share();
                break;
            case R.id.share_linearLayout:
                break;
            case R.id.share_relativeLayout:
                this.finish();
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        this.finish();
    }
//    public void sharedVideoToThird(String sharePageUrl, String shareType) {
//        sharedManager.shareToWeChatFriends(this, sharePageUrl,
//                "my name",
//                "my video",
//                "http://file3.u148.net/2011/4/images/1302139153715.jpg");
//    }
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {

            Toast.makeText(SharedToOutActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(SharedToOutActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(SharedToOutActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void getvoteResultRequest(JSONObject jsonObject) {
        if(jsonObject.optInt("retCode") == 1){
            shareUrl = jsonObject.optString("sharePageUrl");
        }
    }

    @Override
    public void getvoteResultRequestError(VolleyError volleyError) {

    }
}
