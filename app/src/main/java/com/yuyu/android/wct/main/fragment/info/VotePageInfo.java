package com.yuyu.android.wct.main.fragment.info;

/**
 * Created by jackie.sun on 2016/3/22.
 */
public class VotePageInfo {
    public final static int VOTING_FOR_SELF = 1;
    public final static int VOTING_FOR_COMMON = 2;
    public final static int VOTED_VIDEO = 3;
    private String title;
    private String userName;
    private float rank;
    private int state;
    public String videoPath;
    public String imagePath;
    public String aid;
    public int videoId;

    public VotePageInfo(String title, String userName, float rank, int state,String videoPath,String imagePath,String aid,int videoId) {
        this.title = title;
        this.userName = userName;
        this.rank = rank;
        this.state = state;
        this.videoPath =videoPath;
        this.imagePath = imagePath;
        this.aid =  aid;
        this.videoId = videoId;
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
    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public float getRank() {
        return rank;
    }

    public void setRank(float rank) {
        this.rank = rank;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
