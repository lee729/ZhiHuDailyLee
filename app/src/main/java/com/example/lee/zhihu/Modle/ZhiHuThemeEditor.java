package com.example.lee.zhihu.Modle;

/**
 * Created by lee on 2017/3/14.
 */
public class ZhiHuThemeEditor {

    private int mThemeId;//与theme关联的id
    private long mThemeEditorId;
    private String mThemeEditorName;
    private String mThemeEditorIconUrl;
    private String mThemeEditorIntro;
    private String mThemeEditorUrl;

    public ZhiHuThemeEditor(int themeId){
        mThemeId = themeId;
    }

    public int getThemeId() {
        return mThemeId;
    }

    @Override
    public String toString() {
        return mThemeEditorName;
    }

    public long getThemeEditorId() {
        return mThemeEditorId;
    }

    public void setThemeEditorId(long themeEditorId) {
        mThemeEditorId = themeEditorId;
    }

    public String getThemeEditorName() {
        return mThemeEditorName;
    }

    public void setThemeEditorName(String themeEditorName) {
        mThemeEditorName = themeEditorName;
    }

    public String getThemeEditorIconUrl() {
        return mThemeEditorIconUrl;
    }

    public void setThemeEditorIconUrl(String themeEditorIconUrl) {
        mThemeEditorIconUrl = themeEditorIconUrl;
    }

    public String getThemeEditorIntro() {
        return mThemeEditorIntro;
    }

    public void setThemeEditorIntro(String themeEditorIntro) {
        mThemeEditorIntro = themeEditorIntro;
    }

    public String getThemeEditorUrl() {
        return mThemeEditorUrl;
    }

    public void setThemeEditorUrl(String themeEditorUrl) {
        mThemeEditorUrl = themeEditorUrl;
    }

}
