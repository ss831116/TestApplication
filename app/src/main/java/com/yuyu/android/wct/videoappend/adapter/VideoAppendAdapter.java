package com.yuyu.android.wct.videoappend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yuyu.android.wct.R;
import com.yuyu.android.wct.videoappend.activity.VideoListActivity;
import com.yuyu.android.wct.videoappend.utils.VideoInfo;
import com.yuyu.android.wct.videoappend.utils.VideoList;
import com.yuyu.android.wct.widget.CircleImageView;

import java.util.List;

/**
 * Created by bernie.shi on 2016/3/18.
 */
public class VideoAppendAdapter extends BaseAdapter {
    List<VideoInfo> list;
    Context context;
    LayoutInflater mInflater;

    public VideoAppendAdapter(Context context, List<VideoInfo> list) {
        this.list = list;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        final ViewHolder viewHolder;
        final VideoInfo comment = list.get(position);
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.video_append_adapter,
                    null);
            viewHolder.userIcon = (CircleImageView) convertView
                    .findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.userIcon.setImageBitmap(comment.bitmap);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i< VideoList.videoList.size(); i++) {
                    if(VideoList.appendVideo.get(position).path.equals(VideoList.videoList.get(i).path)){
                        VideoList.videoList.get(i).isSelect = false;
                    }
                }
                VideoList.appendVideo.remove(position);
                VideoListActivity.instance.displayGridView();
            }
        });
        return convertView;
    }

    class ViewHolder {
        CircleImageView userIcon;
    }
}