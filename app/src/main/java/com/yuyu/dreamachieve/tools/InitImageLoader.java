package com.yuyu.dreamachieve.tools;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yuyu.dreamachieve.R;

/**
 * Created by bernie.shi on 2016/3/15.
 */
public class InitImageLoader {
    DisplayImageOptions options;
    ImageLoader loader;
    public InitImageLoader(){
        loader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.head)
                .showImageForEmptyUri(R.drawable.head)
                .showImageOnFail(R.drawable.head)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }
    public ImageLoader getImageLoader(){
        return this.loader;
    }
    public DisplayImageOptions getOptions(){
        return this.options;
    }
}
