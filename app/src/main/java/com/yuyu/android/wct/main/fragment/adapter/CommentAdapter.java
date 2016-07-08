package com.yuyu.android.wct.main.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuyu.android.wct.R;
import com.yuyu.android.wct.main.fragment.info.NewCommentsInfo;
import com.yuyu.android.wct.tools.InitImageLoader;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by bernie.shi on 2016/3/28.
 */
public class CommentAdapter extends BaseAdapter {
    Context context;
    List<NewCommentsInfo> newCommentsList;
    LayoutInflater mInflater;
    InitImageLoader initImageLoader;

    public CommentAdapter(Context context) {
        this.context = context;
        this.newCommentsList = new ArrayList<>();
        initImageLoader = new InitImageLoader();
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (newCommentsList == null)
            return 0;
        return newCommentsList.size();
    }

    @Override
    public NewCommentsInfo getItem(int position) {
        return newCommentsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final NewCommentsInfo comment = newCommentsList.get(position);
        String displayTime;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.adapter_comment,
                    null);
            viewHolder.head_icon = (ImageView) convertView
                    .findViewById(R.id.commnet_user_icon);
            viewHolder.customername = (TextView) convertView
                    .findViewById(R.id.comment_user_name);
            viewHolder.comment_content = (TextView) convertView
                    .findViewById(R.id.comment_content);
            viewHolder.time_text = (TextView) convertView
                    .findViewById(R.id.comment_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (0 <= comment.createdAt && comment.createdAt < 60) {
            displayTime = comment.createdAt + context.getResources().getString(R.string.seconds);
        } else if (60 * 1 <= comment.createdAt && comment.createdAt < 60 * 60) {
            displayTime = comment.createdAt / 60 + context.getResources().getString(R.string.minutes);
        } else if (60 * 60 * 1 <= comment.createdAt && comment.createdAt < 60 * 60 * 24) {
            displayTime = comment.createdAt / (60 * 60) + context.getResources().getString(R.string.hours);
        } else if (60 * 60 * 1 * 24 <= comment.createdAt && comment.createdAt < 60 * 60 * 24 * 30) {
            displayTime = comment.createdAt / (60 * 60 * 24) + context.getResources().getString(R.string.days);
        } else if (60 * 60 * 1 * 24 * 30 <= comment.createdAt && comment.createdAt < 60 * 60 * 24 * 30 * 12) {
            displayTime = comment.createdAt / (60 * 60 * 24 * 30) + context.getResources().getString(R.string.months);
        } else {
            displayTime = 1 + context.getResources().getString(R.string.years);
        }
//        initImageLoader.getImageLoader().displayImage(comment.icon, viewHolder.head_icon, initImageLoader.getOptions());
        viewHolder.customername.setText(comment.nikeName);
        viewHolder.comment_content.setText(comment.comment);
        viewHolder.time_text.setText(displayTime);
        return convertView;
    }

    public void reFresh(List<NewCommentsInfo> commentsList) {
        newCommentsList.addAll(commentsList);
        this.notifyDataSetChanged();
    }

    public void reFresh(List<NewCommentsInfo> commentsList, boolean isAddComment) {
        this.newCommentsList.clear();
        this.newCommentsList = commentsList;
        this.notifyDataSetChanged();
    }

    class ViewHolder {
        ImageView head_icon;
        TextView customername;
        TextView comment_content;
        TextView time_text;
    }
}
