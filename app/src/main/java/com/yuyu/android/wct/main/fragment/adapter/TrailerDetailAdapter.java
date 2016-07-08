package com.yuyu.android.wct.main.fragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuyu.android.wct.R;
import com.yuyu.android.wct.main.PlayClientVideoActivity_;
import com.yuyu.android.wct.main.fragment.info.VotePageInfo;
import com.yuyu.android.wct.main.fragment.progressbar.RoundCornerProgressBar;
import com.yuyu.android.wct.tools.InitImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by bernie.shi on 2016/4/7.
 */
public class TrailerDetailAdapter extends RecyclerView.Adapter<TrailerDetailAdapter.ViewHolder>{
    private static String TAG = "VoteAdapter";
    private LayoutInflater mInflater;
    private Activity mContext;
    private List<VotePageInfo> mData;
    InitImageLoader initImageLoader;
    Random random;

    public TrailerDetailAdapter(Context context) {
        this.mContext = (Activity)context;
        this.mInflater = LayoutInflater.from(context);
        initImageLoader = new InitImageLoader();
        mData = new ArrayList<>();
        random = new Random();
    }

    public void refreshData(List<VotePageInfo> data) {
        mData.addAll(data);
        notifyItemRangeInserted(getItemCount(), data.size());
    }

    public void refreshData() {
        mData.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.trailer_detail_adapter, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final VotePageInfo info = mData.get(position);
        holder.title.setText(info.getTitle());
        holder.userName.setText(mContext.getString(R.string.vote_username) + info.getUserName());
        holder.progressBar.setProgress(info.getRank());
        initImageLoader.getImageLoader().displayImage(info.imagePath, holder.previewImg, initImageLoader.getOptions());
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PlayClientVideoActivity_.class);
                intent.putExtra("videoPath", info.videoPath);
                intent.putExtra("videoId", info.videoId);
                intent.putExtra("aid", info.aid);
                intent.putExtra("isShow", true);
                intent.putExtra("isLocalVideo", false);
                intent.putExtra("isShow",false);
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView play;
        ImageView previewImg;
        TextView userName;
        TextView rankProgress;
        RoundCornerProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_title);
            play = (ImageView) itemView.findViewById(R.id.vote_display_video);
            previewImg = (ImageView) itemView.findViewById(R.id.vote_preview_img);
            userName = (TextView) itemView.findViewById(R.id.vote_user_name);
            progressBar = (RoundCornerProgressBar) itemView.findViewById(R.id.vote_progress_item);
            rankProgress = (TextView) itemView.findViewById(R.id.vote_progress_rank);
        }
    }
}
