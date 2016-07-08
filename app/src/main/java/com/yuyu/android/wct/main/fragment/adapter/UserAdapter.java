package com.yuyu.android.wct.main.fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuyu.android.wct.R;
import com.yuyu.android.wct.http.HttpSite;
import com.yuyu.android.wct.main.CommentActivity;
import com.yuyu.android.wct.main.PlayClientVideoActivity_;
import com.yuyu.android.wct.main.SettingActivity;
import com.yuyu.android.wct.sharedout.activity.SharedToOutActivity_;
import com.yuyu.android.wct.tools.InitImageLoader;
import com.yuyu.android.wct.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jackie.sun on 2016/3/17.
 */
public class UserAdapter extends BaseAdapter {
    public static  Context mContext;
    private List<BaseChildItem> mData;
    static InitImageLoader initImageLoader;

    public enum eItemType {
        eCHILD_TYPE_1, eCHILD_TYPE_2,
    }

    public UserAdapter(Context mContext) {
        super();
        this.mContext = mContext;
        mData = new ArrayList<>();
        initImageLoader = new InitImageLoader();
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public BaseChildItem getItem(int position) {
        return mData != null ? mData.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void refreshHeadData(BaseChildItem item) {
        List<BaseChildItem> items = new ArrayList<>();
        items.add(item);
        items.addAll(mData);
        reRreshData(items);
    }

    public void loginOut(BaseChildItem item){
        mData.clear();
        mData.add(item);
        notifyDataSetChanged();
    }

    public void reRreshData(List<BaseChildItem> data) {
        mData.clear();
        addNewData(data);
    }

    public void addNewData(List<BaseChildItem> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseChildItem item = getItem(position);
        return item.getView(mContext, convertView, parent);
    }

    public abstract static class BaseChildItem {

        abstract public View getView(Context context, View convertView, ViewGroup parent);

        protected eItemType mChildType;

        protected BaseChildItem(eItemType type) {
            mChildType = type;
        }

        public eItemType getItemType() {
            return this.mChildType;
        }

        private Object data;

        public Object getData() {
            return this.data;
        }

        public void setData(Object data) {
            this.data = data;
        }

    }

    public static class HeadItem extends BaseChildItem {
        TextView vote;
        TextView point;
        public static HeadItem instance;
        public HeadItem(DataOne data) {
            super(eItemType.eCHILD_TYPE_1);
            setData(data);
            instance =  this;
        }

        public void setVoteCountText(String count,String integral){
            if(vote != null) {
                vote.setText(mContext.getString(R.string.user_votes)+count);
            }
            if(integral != null){
                point.setText(mContext.getString(R.string.user_points)+integral);
            }
        }
        @Override
        public View getView(final Context context, View convertView, ViewGroup parent) {
            DataOne data = (DataOne) getData();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_user_head_layout, parent, false);
            CircleImageView head = (CircleImageView) convertView.findViewById(R.id.user_head_icon);
            vote = (TextView) convertView.findViewById(R.id.user_votes);
            point = (TextView) convertView.findViewById(R.id.user_points);
            vote.setText(context.getString(R.string.user_votes) + data.getVotes());
            point.setText(context.getString(R.string.user_points) + data.getPoints());
            head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SettingActivity.class);
                    context.startActivity(intent);
                }
            });
            return convertView;
        }
    }

    public static class BodyItem extends BaseChildItem {

        public BodyItem(DataTwo data) {
            super(eItemType.eCHILD_TYPE_2);
            setData(data);
        }

        @Override
        public View getView(final Context context, View convertView, ViewGroup parent) {
            final DataTwo data = (DataTwo) getData();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_user_item_layout, parent, false);
            ImageView preImg = (ImageView) convertView.findViewById(R.id.user_preview_img);
            ImageView display = (ImageView) convertView.findViewById(R.id.user_display_video);
            TextView item_title =  (TextView) convertView.findViewById(R.id.item_title);
            TextView get_vote_count = (TextView) convertView.findViewById(R.id.get_vote_count);
            ImageView share = (ImageView) convertView.findViewById(R.id.user_share_layout);
            item_title.setText(data.title + mContext.getResources().getString(R.string.join_match_video));
            get_vote_count.setText(context.getResources().getString(R.string.current_get_vote_count) + data.like);
            initImageLoader.getImageLoader().displayImage(HttpSite.imagePath_qiniu + data.getPreUrl(), preImg, initImageLoader.getOptions());
            display.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlayClientVideoActivity_.class);
                    intent.putExtra("videoPath", data.getMovieUrl());
                    intent.putExtra("videoId", data.getVideoId());
                    intent.putExtra("aid", data.getAid());
                    intent.putExtra("isLocalVideo", false);
                    context.startActivity(intent);
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SharedToOutActivity_.class);
                    intent.putExtra("imagePath", HttpSite.imagePath_qiniu + data.getPreUrl());
                    intent.putExtra("mvId", data.getVideoId());
                    context.startActivity(intent);
                }
            });
            return convertView;
        }
    }

    public static class DataOne {
        private String headUrl;
        private String votes;
        private String friendVotes;
        private String points;

        public DataOne(String headUrl, String votes, String friendVotes, String points) {
            this.headUrl = headUrl;
            this.votes = votes;
            this.friendVotes = friendVotes;
            this.points = points;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getVotes() {
            return votes;
        }

        public void setVotes(String votes) {
            this.votes = votes;
        }

        public String getFriendVotes() {
            return friendVotes;
        }

        public void setFriendVotes(String friendVotes) {
            this.friendVotes = friendVotes;
        }

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }
    }

    public static class DataTwo {
        private String title;
        private String movieUrl;
        private String preUrl;
        private String like;
        private String comment;
        private String aid;
        private int videoId;

        public DataTwo(String title, String movieUrl, String preUrl, String like, String comment, String aid, int videoId) {
            this.title = title;
            this.movieUrl = movieUrl;
            this.preUrl = preUrl;
            this.like = like;
            this.comment = comment;
            this.aid = aid;
            this.videoId = videoId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMovieUrl() {
            return movieUrl;
        }

        public void setMovieUrl(String movieUrl) {
            this.movieUrl = movieUrl;
        }

        public String getPreUrl() {
            return preUrl;
        }

        public void setPreUrl(String preUrl) {
            this.preUrl = preUrl;
        }

        public String getLike() {
            return like;
        }

        public void setLike(String like) {
            this.like = like;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

        public int getVideoId() {
            return videoId;
        }

        public void setVideoId(int videoId) {
            this.videoId = videoId;
        }
    }
}
