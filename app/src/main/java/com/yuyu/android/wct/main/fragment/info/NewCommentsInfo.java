package com.yuyu.android.wct.main.fragment.info;

public class NewCommentsInfo {
    public String icon;
    public String nikeName;
    public String comment;
    public Long createdAt;
    public int playerId;

    public NewCommentsInfo(String icon, String nikeName, String comment, Long createdAt, int playerId) {
        this.icon = icon;
        this.nikeName = nikeName;
        this.comment = comment;
        this.createdAt = createdAt;
        this.playerId = playerId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
