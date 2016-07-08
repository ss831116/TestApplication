package com.yuyu.android.wct.tools;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import com.yuyu.android.wct.widget.CircleImageView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by bernie.shi on 2016/3/18.
 */
public class BitmapCache {

    /**
     * 最大缓存数量
     */
    private static HashMap<String, Bitmap> cacheMap = new HashMap<String, Bitmap>();
    private static HashMap<String, Boolean> loadedMap = new HashMap<String, Boolean>();
    private static HashMap<CircleImageView, String> circleImageViewHashMap = new HashMap<>();

    public BitmapCache() {
        // TODO Auto-generated constructor stub
    }

    public static void addBitmapTag(String key) {
        loadedMap.put(key, true);
    }

    public static boolean isLoading(String key) {
        return loadedMap.get(key) != null;
    }

    public static Bitmap getCacheBitmap(String key) {
        Object bmp = cacheMap.get(key);
        if (bmp != null) {
            return (Bitmap) bmp;
        } else {
            return null;
        }
    }

    public static void addCircleImageView(String key, CircleImageView circleImageView) {
        circleImageViewHashMap.put(circleImageView, key);
    }

    public static void addCacheBitmap(String key, Bitmap bmp) {
        cacheMap.put(key, bmp);
        Set<CircleImageView> keys = circleImageViewHashMap.keySet();
        Iterator<CircleImageView> it1 = keys.iterator();
        final Bitmap bitm = bmp;
        while (it1.hasNext()) {
            final CircleImageView circleImageView = it1.next();
            if (circleImageViewHashMap.get(circleImageView).equalsIgnoreCase(key)) {
                (new Handler(Looper.getMainLooper())).post(new Runnable() {
                    @Override
                    public void run() {
                        circleImageView.setImageBitmap(bitm);
                    }
                });
                it1.remove();
            }
        }

        while (cacheMap.size() > 5000) {
            Set<String> set = cacheMap.keySet();
            Iterator<String> it = set.iterator();
            if (it.hasNext()) {
                String removeKey = it.next();
                cacheMap.remove(removeKey);
            }
        }
    }
}
