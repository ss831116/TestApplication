package com.yuyu.android.wct.videoappend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.yuyu.android.wct.R;
import com.yuyu.android.wct.log.DreamAchieveLog;
import com.yuyu.android.wct.videoappend.activity.VideoListActivity;
import com.yuyu.android.wct.videoappend.utils.VideoInfo;
import com.yuyu.android.wct.videoappend.utils.VideoList;
import com.yuyu.android.wct.widget.CircleImageView;

/**
 * Created by bernie.shi on 2016/3/18.
 */
public class VideoListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater mInflater;

    public VideoListAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return VideoList.videoList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return VideoList.videoList.get(position);
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
        final VideoInfo comment = VideoList.videoList.get(position);
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.videolist_adapter,
                    null);
            viewHolder.userIcon = (CircleImageView) convertView
                    .findViewById(R.id.imageView);
            viewHolder.userName = (TextView) convertView
                    .findViewById(R.id.userName);
            viewHolder.cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.userIcon.setImageBitmap(comment.bitmap);
        viewHolder.userName.setText(comment.title);
        /*convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(context, DisplayAndUploadActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //intent.putExtra("videopath", comment.path);
                //context.startActivity(intent);
            }
        });*/
        viewHolder.cb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(VideoList.appendVideo.size() < 6) {
                    VideoList.appendVideo.clear();
                    if (VideoList.videoList.get(position).isSelect) {
                        VideoList.videoList.get(position).isSelect = false;
                    } else {
                        VideoList.videoList.get(position).isSelect = true;
                    }
                    for (int i = 0; i < VideoList.videoList.size(); i++) {
                        if (VideoList.videoList.get(i).isSelect == true) {
                            VideoList.appendVideo.add(VideoList.videoList.get(i));
                        }
                    }
                }else if(VideoList.appendVideo.size() == 6){
                    if (VideoList.videoList.get(position).isSelect) {
                        VideoList.videoList.get(position).isSelect = false;
                        VideoList.appendVideo.clear();
                        for (int i = 0; i < VideoList.videoList.size(); i++) {
                            if (VideoList.videoList.get(i).isSelect == true) {
                                VideoList.appendVideo.add(VideoList.videoList.get(i));
                            }
                        }
                    }else{
                        Toast.makeText(context,"最多选择6个视频进行合成",Toast.LENGTH_SHORT).show();
                    }

                }
                VideoListActivity.instance.displayGridView();
            }
        });
        // 根据isSelected来设置checkbox的选中状况
        viewHolder.cb.setChecked(comment.isSelect);
        return convertView;
    }

    class ViewHolder {
        CircleImageView userIcon;
        TextView userName;
        CheckBox cb;
    }
}

