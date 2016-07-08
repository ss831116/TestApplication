package com.yuyu.dreamachieve;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by bernie.shi on 2016/3/15.
 */
public class DreamAchieveApplication  extends Application {
    public static RequestQueue queues;
    public void onCreate() {
        super.onCreate();
        queues = Volley.newRequestQueue(getApplicationContext());
        initImageLoader(getApplicationContext());
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
}
