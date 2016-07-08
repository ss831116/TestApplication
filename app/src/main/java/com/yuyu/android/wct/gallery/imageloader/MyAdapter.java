package com.yuyu.android.wct.gallery.imageloader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.yuyu.android.wct.R;
import com.yuyu.android.wct.gallery.utils.CommonAdapter;
import com.yuyu.android.wct.gallery.utils.ViewHolder;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by bernie.shi on 2016/4/13.
 */
public class MyAdapter extends CommonAdapter<String> {

    Activity activity;
    /**
     * 用户选择的图片，存储为图片的完整路径
     */
    public static List<String> mSelectedImage = new LinkedList<String>();

    /**
     * 文件夹路径
     */
    private String mDirPath;

    public MyAdapter(Context context, List<String> mDatas, int itemLayoutId,
                     String dirPath, Activity activity)
    {
        super(context, mDatas, itemLayoutId);
        this.mDirPath = dirPath;
        this.activity = activity;
    }

    @Override
    public void convert(final ViewHolder helper, final String item)
    {
        helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);
        final ImageView mImageView = helper.getView(R.id.id_item_image);
        mImageView.setColorFilter(null);
        mImageView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.putExtra("picPath", mDirPath + "/" + item);
                activity.setResult(2, intent);
                activity.finish();
            }
        });
    }
}

