package com.yuyu.android.wct.main.fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.yuyu.android.wct.R;
import com.yuyu.android.wct.http.LoginError;
import com.yuyu.android.wct.http.VolleyHttpRequestListener;
import com.yuyu.android.wct.login.LoginActivity_;
import com.yuyu.android.wct.main.HomeActivity;
import com.yuyu.android.wct.main.PlayClientVideoActivity_;
import com.yuyu.android.wct.main.fragment.info.VotePageInfo;
import com.yuyu.android.wct.main.fragment.progressbar.RoundCornerProgressBar;
import com.yuyu.android.wct.sharedout.activity.SharedToOutActivity_;
import com.yuyu.android.wct.tools.InitImageLoader;
import com.yuyu.android.wct.wxapi.WXEntryActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by jackie.sun on 2016/3/22.
 */
public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.ViewHolder> implements VolleyHttpRequestListener {
    private static String TAG = "VoteAdapter";
    private LayoutInflater mInflater;
    private Context mContext;
    private List<VotePageInfo> mData;
    InitImageLoader initImageLoader;
    Random random;

    public VoteAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        initImageLoader = new InitImageLoader();
        mData = new ArrayList<>();
        random = new Random();
    }

    public void changrRank() {
        for (int i = 0; i < mData.size(); i++) {
            VotePageInfo info = mData.get(i);
            float a = random.nextFloat() + (float) Math.random();
            a = a * 30;
            info.setRank(getRankData(a));
        }
        notifyDataSetChanged();
    }

    private float getRankData(float data) {
        float result = 0;
        try {
            String a = data + "";
            a = a.substring(0, a.indexOf(".") + 3);
            result = Float.parseFloat(a);
        }catch(Exception e){

        }
        return result;
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
        View view = mInflater.inflate(R.layout.adapter_vote_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final VotePageInfo info = mData.get(position);
        holder.title.setText(info.getTitle());
        holder.userName.setText(mContext.getString(R.string.vote_username) + info.getUserName());
        holder.progressBar.setProgress(info.getRank());
        holder.rankProgress.setText(info.getRank() + "%");
        initImageLoader.getImageLoader().displayImage(info.imagePath, holder.previewImg, initImageLoader.getOptions());
        int state = info.getState();
        if (state == VotePageInfo.VOTING_FOR_SELF) {
            holder.voteBtnLayout.setVisibility(View.VISIBLE);
            holder.voteSelfLayout.setVisibility(View.VISIBLE);
            holder.commonVote.setVisibility(View.GONE);
            holder.voteRankLayout.setVisibility(View.GONE);
        } else if (state == VotePageInfo.VOTING_FOR_COMMON) {
            holder.voteBtnLayout.setVisibility(View.VISIBLE);
            holder.commonVote.setVisibility(View.VISIBLE);
            holder.voteSelfLayout.setVisibility(View.GONE);
            holder.voteRankLayout.setVisibility(View.GONE);
        } else {
            holder.voteBtnLayout.setVisibility(View.GONE);
            holder.voteSelfLayout.setVisibility(View.GONE);
            holder.commonVote.setVisibility(View.GONE);
            holder.voteRankLayout.setVisibility(View.VISIBLE);
            if (position == 0) {
                holder.voteRankBg.setImageResource(R.drawable.ranking1);
            } else if (position > 0 && position < 3) {
                holder.voteRankBg.setImageResource(R.drawable.ranking2_3);
            } else {
                holder.voteRankBg.setImageResource(R.drawable.ranking4_9);
            }
            holder.voteRankNum.setText((position + 1) + "");
        }
        holder.commonVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoVote(info);
            }
        });
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PlayClientVideoActivity_.class);
                intent.putExtra("videoPath", info.videoPath);
                intent.putExtra("videoId", info.videoId);
                intent.putExtra("aid", info.aid);
                intent.putExtra("isShow", true);
                intent.putExtra("isLocalVideo", false);
                mContext.startActivity(intent);
            }
        });
        holder.commonVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoVote(info);
            }
        });
        holder.voteSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoVote(info);
            }
        });
        holder.voteHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SharedToOutActivity_.class);
                intent.putExtra("imagePath",info.imagePath);
                intent.putExtra("mvId", info.videoId);
                mContext.startActivity(intent);
            }
        });
    }

    public void gotoVote(VotePageInfo info) {
        HomeActivity.instance.voteVideo(info.aid, info.videoId);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void volleyHttpRequestSuccess(JSONObject jsonObject) {
        if (LoginError.noLogin(jsonObject)) {
            Intent intent = new Intent(mContext, LoginActivity_.class);
            mContext.startActivity(intent);
        } else {

        }
    }

    @Override
    public void volleyHttpRequestError(VolleyError error) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView play;
        ImageView previewImg;
        TextView userName;
        TextView rankProgress;
        RoundCornerProgressBar progressBar;
        //投票按钮布局
        RelativeLayout voteBtnLayout;
        Button commonVote;
        //投票按钮布局--自己的
        LinearLayout voteSelfLayout;
        Button voteSelf;
        Button voteHelp;
        //左上角排名布局
        RelativeLayout voteRankLayout;
        ImageView voteRankBg;
        TextView voteRankNum;

        public ViewHolder(View itemView) {
            super(itemView);
            voteRankLayout = (RelativeLayout) itemView.findViewById(R.id.vote_rank_number_layout);
            voteRankBg = (ImageView) itemView.findViewById(R.id.vote_video_rank_img);
            voteRankNum = (TextView) itemView.findViewById(R.id.vote_rank_text);
            title = (TextView) itemView.findViewById(R.id.item_title);
            play = (ImageView) itemView.findViewById(R.id.vote_display_video);
            previewImg = (ImageView) itemView.findViewById(R.id.vote_preview_img);
            userName = (TextView) itemView.findViewById(R.id.vote_user_name);
            progressBar = (RoundCornerProgressBar) itemView.findViewById(R.id.vote_progress_item);
            rankProgress = (TextView) itemView.findViewById(R.id.vote_progress_rank);
            voteBtnLayout = (RelativeLayout) itemView.findViewById(R.id.vote_button_layout);
            voteSelfLayout = (LinearLayout) itemView.findViewById(R.id.vote_self_layout);
            commonVote = (Button) itemView.findViewById(R.id.vote_for_others);
            voteSelf = (Button) itemView.findViewById(R.id.vote_for_self);
            voteHelp = (Button) itemView.findViewById(R.id.vote_for_help);
        }
    }
}
