package com.yuyu.android.wct.utils;

import android.graphics.Bitmap;

/**
 * Created by bernie.shi on 2016/3/15.
 */
public class Videos {
    public String title;
    public String path;
    public int duration;
    public String resolution;
    public Bitmap bitmap;
    public long size;

    public Videos(String title, String path, int duration,
                     String resolution, long size, Bitmap bitmap) {
        this.title = title;
        this.path = path;
        this.duration = duration;
        this.resolution = resolution;
        this.bitmap = bitmap;
        this.size =  size;
    }
}
