package com.yuyu.android.wct.main;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;
import com.ypy.eventbus.EventBus;
import com.yuyu.android.wct.DreamAchieveApplication;
import com.yuyu.android.wct.R;
import com.yuyu.android.wct.http.HttpSite;
import com.yuyu.android.wct.http.LoginError;
import com.yuyu.android.wct.http.VolleyHttpRequest;
import com.yuyu.android.wct.http.VolleyHttpRequestListener;
import com.yuyu.android.wct.login.LoginActivity;
import com.yuyu.android.wct.login.LoginActivity_;
import com.yuyu.android.wct.main.fragment.PriceFragment;
import com.yuyu.android.wct.main.fragment.TrailerFragment;
import com.yuyu.android.wct.main.fragment.UserFragment;
import com.yuyu.android.wct.main.fragment.VoteFragment;
import com.yuyu.android.wct.theme.CopyWriter;
import com.yuyu.android.wct.utils.PointUtil;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by bernie.shi on 2016/3/17.
 */
@EActivity
public class HomeActivity extends BaseActivity implements VolleyHttpRequestListener {
    private static final String VIDEO_LIST_TAG = "video_list";
    public static String TAG = "VOTE";
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    public static HomeActivity instance;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private RadioGroup mRadioGroup;
    private RadioButton mRadioBtn0;
    private RadioButton mRadioBtn1;
    private RadioButton mRadioBtn2;
    private RadioButton mRadioBtn3;
    Long start;
    Long end;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        instance = this;
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_main);
        initBody();
    }

    private void initBody() {
        initViewPage();
        initRadio();
    }

    protected void onResume() {
        super.onResume();
    }

    private void initViewPage() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mFragments.add(PriceFragment.getInstance());
        mFragments.add(VoteFragment.getInstance());
        mFragments.add(TrailerFragment.getInstance());
        mFragments.add(UserFragment.getInstance());
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mFragments.get(arg0);
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        PointUtil.logForPage("PriceFragment");
                        mRadioBtn0.setChecked(true);
                        break;
                    case 1:
                        PointUtil.logForPage("VoteFragment");
                        mRadioBtn1.setChecked(true);
                        break;
                    case 2:
                        PointUtil.logForPage("TrailerFragment");
                        mRadioBtn2.setChecked(true);
                        break;
                    case 3:
                        PointUtil.logForPage("UserFragment");
                        getUserVideoList();
                        mRadioBtn3.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void getUserVideoList() {
        Map<Object, Object> map = new HashMap<>();
        map.put("pageNum", 0);
        map.put("pageSize", 10);
        VolleyHttpRequest volleyHttpRequest = new VolleyHttpRequest();
        volleyHttpRequest.setVolleyHttpRequestListener(new VolleyHttpRequestListener() {
            @Override
            public void volleyHttpRequestSuccess(JSONObject jsonObject) {
                if (LoginError.noLogin(jsonObject)) {
                    Intent intent = new Intent(HomeActivity.this, LoginActivity_.class);
                    intent.putExtra(LoginActivity.TAG_FROM, "user");
                    startActivity(intent);
                }
            }

            @Override
            public void volleyHttpRequestError(VolleyError error) {

            }
        });
        volleyHttpRequest.volleyHttpRequest(map, HttpSite.userVideoList +
                DreamAchieveApplication.sharedPreferences.getString("cookies", ""), VIDEO_LIST_TAG);
    }

    private void initRadio() {
        mRadioGroup = (RadioGroup) findViewById(R.id.home_rad_group);
        mRadioBtn0 = (RadioButton) findViewById(R.id.fragment_price);
        mRadioBtn1 = (RadioButton) findViewById(R.id.fragment_vote);
        mRadioBtn2 = (RadioButton) findViewById(R.id.fragment_trailer);
        mRadioBtn3 = (RadioButton) findViewById(R.id.fragment_user);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.fragment_price) {
                    mViewPager.setCurrentItem(0);
                } else if (checkedId == R.id.fragment_vote) {
                    mViewPager.setCurrentItem(1);
                } else if (checkedId == R.id.fragment_trailer) {
                    mViewPager.setCurrentItem(2);
                } else if (checkedId == R.id.fragment_user) {
                    mViewPager.setCurrentItem(3);
                }
            }
        });
        initDefaultRadioCheck();
        setRadioButtonText();
    }

    public void setRadioButtonText() {
        mRadioBtn0.setText(CopyWriter.firstPageTabBarTitle);
        mRadioBtn1.setText(CopyWriter.secondPageTabBarTitle);
        mRadioBtn2.setText(CopyWriter.thirdPageTabBarTitle);
        mRadioBtn3.setText(CopyWriter.fourthPageTabBarTitle);
    }

    public void onEventMainThread(String refreshPage) {
        if ("logout".equals(refreshPage)) {
            mViewPager.setCurrentItem(0);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void setRadioGroup(String color) {
        Drawable drawableRadioButtonOne = null;
        Drawable drawableRadioButtonTwo = null;
        Drawable drawableRadioButtonThree = null;
        Drawable drawableRadioButtonFour = null;
        ColorStateList csl = null;
        if ("red".equals(color)) {
            drawableRadioButtonOne = this.getResources().getDrawable(R.drawable.rad_btn_award);
            drawableRadioButtonTwo = this.getResources().getDrawable(R.drawable.rad_btn_vote);
            drawableRadioButtonThree = this.getResources().getDrawable(R.drawable.preview_effect);
            drawableRadioButtonFour = this.getResources().getDrawable(R.drawable.rad_btn_person);
            csl = getResources().getColorStateList(R.color.rad_btn,null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                csl = getResources().getColorStateList(R.color.rad_btn,null);
            } else {
                csl = getResources().getColorStateList(R.color.rad_btn);
            }
        } else if ("green".equals(color)) {
            drawableRadioButtonOne = this.getResources().getDrawable(R.drawable.green_rad_btn_award);
            drawableRadioButtonTwo = this.getResources().getDrawable(R.drawable.green_rad_btn_vote);
            drawableRadioButtonThree = this.getResources().getDrawable(R.drawable.green_preview_effect);
            drawableRadioButtonFour = this.getResources().getDrawable(R.drawable.green_rad_btn_person);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                csl = getResources().getColorStateList(R.color.green_rad_btn,null);
            } else {
                csl = getResources().getColorStateList(R.color.green_rad_btn);
            }
        } else if ("yellow".equals(color)) {
            drawableRadioButtonOne = this.getResources().getDrawable(R.drawable.yellow_rad_btn_award);
            drawableRadioButtonTwo = this.getResources().getDrawable(R.drawable.yello_rad_btn_vote);
            drawableRadioButtonThree = this.getResources().getDrawable(R.drawable.yellow_preview_effect);
            drawableRadioButtonFour = this.getResources().getDrawable(R.drawable.yellow_rad_btn_person);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                csl = getResources().getColorStateList(R.color.yellow_rad_btn,null);
            } else {
                csl = getResources().getColorStateList(R.color.yellow_rad_btn);
            }
        } else if ("purple".equals(color)) {
            drawableRadioButtonOne = this.getResources().getDrawable(R.drawable.purple_rad_btn_award);
            drawableRadioButtonTwo = this.getResources().getDrawable(R.drawable.purple_rad_btn_vote);
            drawableRadioButtonThree = this.getResources().getDrawable(R.drawable.purple_preview_effect);
            drawableRadioButtonFour = this.getResources().getDrawable(R.drawable.purple_rad_btn_person);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                csl = getResources().getColorStateList(R.color.yellow_rad_btn,null);
            } else {
                csl = getResources().getColorStateList(R.color.yellow_rad_btn);
            }
        }
        if (drawableRadioButtonOne != null && drawableRadioButtonTwo != null && drawableRadioButtonThree != null && drawableRadioButtonFour != null) {
            mRadioBtn0.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawableRadioButtonOne, null, null);
            mRadioBtn1.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawableRadioButtonTwo, null, null);
            mRadioBtn2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawableRadioButtonThree, null, null);
            mRadioBtn3.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawableRadioButtonFour, null, null);
            mRadioBtn0.setTextColor(csl);
            mRadioBtn1.setTextColor(csl);
            mRadioBtn2.setTextColor(csl);
            mRadioBtn3.setTextColor(csl);
        }
    }

    protected void onStop() {
        super.onStop();
    }

    public void setBackMainPage() {
        mViewPager.setCurrentItem(0);
    }

    private void initDefaultRadioCheck() {
        ((RadioButton) findViewById(R.id.fragment_price)).setChecked(true);
    }

    /**
     * 为视频投票
     *
     * @author Bernie
     * create at 2016/4/7 16:28
     */
    @Background
    public void voteVideo(String aid, int videoId) {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("videoId", videoId);
        map.put("aid", aid);
        map.put("voteNum", 1);
        map.put("voteType", 1);
        VolleyHttpRequest volleyHttpRequest = new VolleyHttpRequest();
        volleyHttpRequest.setVolleyHttpRequestListener(this);
        volleyHttpRequest.volleyHttpRequest(map, HttpSite.voteSite + DreamAchieveApplication.sharedPreferences.getString("cookies", ""), TAG);
    }

    /**
     * 视频投票回调
     *
     * @author Bernie
     * create at 2016/4/7 16:33
     */
    @Override
    public void volleyHttpRequestSuccess(JSONObject jsonObject) {
        if (LoginError.noLogin(jsonObject)) {
            Intent intent = new Intent(HomeActivity.this, LoginActivity_.class);
            intent.putExtra(LoginActivity.TAG_FROM, "vote");
            startActivity(intent);
        } else {
            if(jsonObject.optInt("mesType") == 2) {
                PointUtil.logForEvent(PointUtil.APP_EVENT_FOR_VOTE);
                MobclickAgent.onEvent(HomeActivity.this, PointUtil.APP_EVENT_FOR_VOTE);
                Toast.makeText(getApplicationContext(), getString(R.string.vote_success), Toast.LENGTH_SHORT).show();
                DreamAchieveApplication.instance.playFloatWindow();
            } else{
                Toast.makeText(getApplicationContext(), jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void volleyHttpRequestError(VolleyError error) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}
