package com.yuyu.android.wct.main.fragment.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bernie.shi on 2016/3/28.
 */
public class EventVideoInfo implements Parcelable {
    public String lottoStartTime;
    public String vote_end_time;
    public String eName;
    public String videoName;
    public String name;
    public String videoPicture;
    public int type;
    public int lottoType;
    public String aid;
    public String cid;
    public String lottoEndTime;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.lottoStartTime);
        dest.writeString(this.vote_end_time);
        dest.writeString(this.eName);
        dest.writeString(this.videoName);
        dest.writeString(this.name);
        dest.writeString(this.videoPicture);
        dest.writeInt(this.type);
        dest.writeInt(this.lottoType);
        dest.writeString(this.aid);
        dest.writeString(this.cid);
        dest.writeString(this.lottoEndTime);
    }

    public EventVideoInfo() {
    }

    protected EventVideoInfo(Parcel in) {
        this.lottoStartTime = in.readString();
        this.vote_end_time = in.readString();
        this.eName = in.readString();
        this.videoName = in.readString();
        this.name = in.readString();
        this.videoPicture = in.readString();
        this.type = in.readInt();
        this.lottoType = in.readInt();
        this.aid = in.readString();
        this.cid = in.readString();
        this.lottoEndTime = in.readString();
    }

    public static final Parcelable.Creator<EventVideoInfo> CREATOR = new Parcelable.Creator<EventVideoInfo>() {
        public EventVideoInfo createFromParcel(Parcel source) {
            return new EventVideoInfo(source);
        }

        public EventVideoInfo[] newArray(int size) {
            return new EventVideoInfo[size];
        }
    };
}
