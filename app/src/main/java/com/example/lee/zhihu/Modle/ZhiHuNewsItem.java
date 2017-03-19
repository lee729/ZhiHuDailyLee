package com.example.lee.zhihu.Modle;

/**
 * Created by lee on 2017/3/8.
 * 模型层对象，保存新闻数据
 */
public class ZhiHuNewsItem {
    public static final int NEWS_ITEM_IS_TOP = 2;
    public static final int NEWS_ITEM_NOT_TOP  = 4;
    public static final String NO_THEME_ID = "-99";

    private String mDate;//最新消息特有的，themestory没有
    private String mThemeId;//themestory特有的，最新消息没有
    private int mIsTopNews;//最新消息里的数据，一个消息是否是top
    private String mLargeImageUrl;//themestory里特有的，一个主题栏目的大图

    private String mId;
    private String mTitle;
    private String mImageUrl;//有的没有图片

    private String mContent;//存储正文内容,现在没有用到
    private String mShareUrl;//存储分享网址
    private String mComments;//总品论数量
    private String mLongComments;//长评数量
    private String mShortCommets;//短评数量
    private String mPopularity;//点赞数量

    @Override
    public String toString() {
        return mTitle;
    }
    //没有themeid的，设置为-99;
    public ZhiHuNewsItem(String themeId){
        mThemeId = themeId;
    }

    public String getLargeImageUrl() {
        return mLargeImageUrl;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        mLargeImageUrl = largeImageUrl;
    }

    public int getIsTopNews() {
        return mIsTopNews;
    }

    public void setIsTopNews(int isTopNews) {
        mIsTopNews = isTopNews;
    }

    public String getThemeId() {
        return mThemeId;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getShareUrl() {
        return mShareUrl;
    }

    public void setShareUrl(String shareUrl) {
        mShareUrl = shareUrl;
    }

    public String getLongComments() {
        return mLongComments;
    }

    public void setLongComments(String longComments) {
        mLongComments = longComments;
    }

    public String getShortCommets() {
        return mShortCommets;
    }

    public void setShortCommets(String shortCommets) {
        mShortCommets = shortCommets;
    }

    public String getPopularity() {
        return mPopularity;
    }

    public void setPopularity(String popularity) {
        mPopularity = popularity;
    }

    public String getComments() {
        return mComments;
    }

    public void setComments(String comments) {
        mComments = comments;
    }
}
