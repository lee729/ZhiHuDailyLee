package com.example.lee.zhihu.Modle;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lee on 2017/3/12.
 */
public class ZhiHuCommentItem {
    private long mUserId;
    private String mIconUrl;
    private String mName;
    private String mPopularity;
    private String mCommentContent;
    private long mDate;
    private long mReplyUserId;

    public long getUserId() {
        return mUserId;
    }

    public void setUserId(long userId) {
        mUserId = userId;
    }

    public String getIconUrl() {
        return mIconUrl;
    }

    public void setIconUrl(String iconUrl) {
        mIconUrl = iconUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getCommentContent() {
        return mCommentContent;
    }

    public void setCommentContent(String commentContent) {
        mCommentContent = commentContent;
    }

    public String getPopularity() {
        return mPopularity;
    }

    public void setPopularity(String popularity) {
        mPopularity = popularity;
    }

    public String getDate() {
        Date date = new Date(mDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = simpleDateFormat.format(date);

        return formatDate;
    }

    public void setDate(long date) {
        mDate = date;
    }

    public long getReplyUserId() {
        return mReplyUserId;
    }

    public void setReplyUserId(long replyUserId) {
        mReplyUserId = replyUserId;
    }
}
