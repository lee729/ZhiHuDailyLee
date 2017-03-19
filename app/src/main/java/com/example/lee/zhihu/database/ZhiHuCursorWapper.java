package com.example.lee.zhihu.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.lee.zhihu.Modle.ZhiHuNewsItem;
import com.example.lee.zhihu.Modle.ZhiHuThemeItem;
import com.example.lee.zhihu.database.ZhiHuDbSchema.ZhiHuNewsTable;
import com.example.lee.zhihu.database.ZhiHuDbSchema.ZhiHuThemeTable;

/**
 * Created by lee on 2017/3/9.
 * 根据cursor，取出item
 */
public class ZhiHuCursorWapper extends CursorWrapper{

    public ZhiHuCursorWapper(Cursor cursor) {
        super(cursor);
    }

    /**
     * 从数据库取出知乎newsitem
     * */
    public ZhiHuNewsItem getNewsItem(){
        ZhiHuNewsItem item = new ZhiHuNewsItem(
                getString(getColumnIndex(ZhiHuNewsTable.Cols.THEME_ID)));

        String date = getString(getColumnIndex(ZhiHuNewsTable.Cols.DATE));
        int isTopStory = getInt(getColumnIndex(ZhiHuNewsTable.Cols.IS_TOP_STORY));

        String newsId = getString(getColumnIndex(ZhiHuNewsTable.Cols.NEWSID));
        String title = getString(getColumnIndex(ZhiHuNewsTable.Cols.TITLE));
        String content = getString(getColumnIndex(ZhiHuNewsTable.Cols.CONTENT));
        String shareUrl = getString(getColumnIndex(ZhiHuNewsTable.Cols.SHARE_URL));
        String comment = getString(getColumnIndex(ZhiHuNewsTable.Cols.COMMENT));
        String longComment = getString(getColumnIndex(ZhiHuNewsTable.Cols.LONG_COMMENT));
        String shortComment = getString(getColumnIndex(ZhiHuNewsTable.Cols.SHORT_COMMENT));
        String popularity = getString(getColumnIndex(ZhiHuNewsTable.Cols.POPULARITY));

        item.setDate(date);
        item.setIsTopNews(isTopStory);

        item.setId(newsId);
        item.setTitle(title);
        item.setContent(content);
        item.setShareUrl(shareUrl);
        item.setComments(comment);
        item.setLongComments(longComment);
        item.setShortCommets(shortComment);
        item.setPopularity(popularity);

        return item;
    }
    /**
     * 从数据库取出知乎themelistitem
     * */
    public ZhiHuThemeItem getThemeItem(){
        ZhiHuThemeItem item = new ZhiHuThemeItem();

        String themeId = getString(getColumnIndex(ZhiHuThemeTable.Cols.THEME_ID));
        String themeDescription = getString(getColumnIndex(ZhiHuThemeTable.Cols.DESCRIPTION));
        String themeName = getString(getColumnIndex(ZhiHuThemeTable.Cols.THEME_NAME));

        item.setThemeId(themeId);
        item.setDescription(themeDescription);
        item.setThemeName(themeName);

        return item;
    }
}
