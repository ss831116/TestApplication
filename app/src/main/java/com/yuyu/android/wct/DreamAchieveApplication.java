package com.yuyu.android.wct;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;
import com.yuyu.android.wct.http.HttpSite;
import com.yuyu.android.wct.http.VolleyHttpRequest;
import com.yuyu.android.wct.http.VolleyHttpRequestListener;
import com.yuyu.android.wct.theme.CopyWriter;
import com.yuyu.android.wct.utils.PointUtil;
import com.yuyu.android.wct.winning.view.WindowUtils;

import org.apkplug.app.FrameworkInstance;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by bernie.shi on 2016/3/15.
 */
public class DreamAchieveApplication extends Application  implements VolleyHttpRequestListener {
    public static RequestQueue queues;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static DreamAchieveApplication instance;
    public static boolean isUploadVideo = false;
    public static int count = 0;
    int[] animationBitmaps;
    WindowUtils windowUtils;
    private FrameworkInstance frame = null;
    private static final String TAG = "DreamAchieveApplication";

    public FrameworkInstance getFrame() {
        return frame;
    }
   
    public void onCreate() {
        super.onCreate();
        queues = Volley.newRequestQueue(getApplicationContext());
        instance = this;
        animationBitmaps = new int[]{R.drawable.kickin_001, R.drawable.kickin_002,
                R.drawable.kickin_003, R.drawable.kickin_004,
                R.drawable.kickin_005, R.drawable.kickin_006,
                R.drawable.kickin_007, R.drawable.kickin_008,
                R.drawable.kickin_009, R.drawable.kickin_010,
                R.drawable.kickin_011, R.drawable.kickin_012,
                R.drawable.kickin_013, R.drawable.kickin_014,
                R.drawable.kickin_015, R.drawable.kickin_016,
                R.drawable.kickin_017, R.drawable.kickin_018,
                R.drawable.kickin_019, R.drawable.kickin_020,
                R.drawable.kickin_021, R.drawable.kickin_022,
                R.drawable.kickin_023, R.drawable.kickin_024,
                R.drawable.kickin_025, R.drawable.kickin_026,
                R.drawable.kickin_027, R.drawable.kickin_028,
                R.drawable.kickin_029, R.drawable.kickin_030,
                R.drawable.kickin_031, R.drawable.kickin_032,
                R.drawable.kickin_033, R.drawable.kickin_034,
                R.drawable.kickin_035, R.drawable.kickin_036,
                R.drawable.kickin_037, R.drawable.kickin_038,
                R.drawable.kickin_039, R.drawable.kickin_040,
                R.drawable.kickin_041, R.drawable.kickin_042,
                R.drawable.kickin_043, R.drawable.kickin_044,
                R.drawable.kickin_045, R.drawable.kickin_046,
                R.drawable.kickin_047, R.drawable.kickin_048,
                R.drawable.kickin_049, R.drawable.kickin_050,
                R.drawable.kickin_051, R.drawable.kickin_052,
                R.drawable.kickin_053, R.drawable.kickin_054,
                R.drawable.kickin_055, R.drawable.kickin_056,
                R.drawable.kickin_057, R.drawable.kickin_058,
                R.drawable.kickin_059, R.drawable.kickin_060,
                R.drawable.kickin_061, R.drawable.kickin_062,
                R.drawable.kickin_063};

        sharedPreferences = getSharedPreferences("user_info",
                Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE);
        editor = sharedPreferences.edit();
        initImageLoader(getApplicationContext());
        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.updateOnlineConfig(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        initWeChatLoad();
        new CopyWriter(getApplicationContext());
        getCopyWriterFromClient();
        PointUtil.setRootServerUrl(HttpSite.httpPoint);
        PointUtil.logForEvent(PointUtil.APP_EVENT_FOR_LAUNCH);
    }

    public void getCopyWriterFromClient(){
        Map<Object, Object> map = new HashMap<>();
        VolleyHttpRequest volleyHttpRequest = new VolleyHttpRequest();
        volleyHttpRequest.setVolleyHttpRequestListener(this);
        volleyHttpRequest.volleyHttpRequest(map, HttpSite.copyWriter, TAG);
    }

    public void playFloatWindow() {
        windowUtils = new WindowUtils(getApplicationContext());
        handler.sendEmptyMessage(0);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (count <= animationBitmaps.length) {
                WindowUtils.instance.playAnimationBitmaps(animationBitmaps[count % animationBitmaps.length]);
                count++;
                handler.sendEmptyMessageDelayed(0, 50);
            } else {
                WindowUtils.instance.hidePopupWindow();
                count = 0;
                handler.removeMessages(0);
            }
            super.handleMessage(msg);
        }
    };

    public void removeHandler() {
        handler.removeMessages(0);
        windowUtils = null;
    }

    private void initWeChatLoad() {
        PlatformConfig.setWeixin("wx719b9b81ec3b8a3c", "831f37b64cc781d7a606cc0d019f3ef4");
    }

    public static void initImageLoader(Context context) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc().build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    public static RequestQueue getHttpQueues() {
        return queues;
    }

    @Override
    public void volleyHttpRequestSuccess(JSONObject jsonObject) {
        pullJsonDataFromClient(jsonObject);
    }

    public void pullJsonDataFromClient(JSONObject jsonObject){

    }
    @Override
    public void volleyHttpRequestError(VolleyError error) {

    }
}
