package com.example.lee.zhihu.Modle;

/**
 * Created by lee on 2017/3/13.
 */
public class ZhiHuThemeItem {

    private String mThemeId;
    private String mThemeName;
    private String mThumbnail;
    private String mDescription;

    @Override
    public String toString() {
        return mThemeName;
    }

    public String getThemeId() {
        return mThemeId;
    }

    public void setThemeId(String themeId) {
        mThemeId = themeId;
    }

    public String getThemeName() {
        return mThemeName;
    }

    public void setThemeName(String themeName) {
        mThemeName = themeName;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        mThumbnail = thumbnail;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}
