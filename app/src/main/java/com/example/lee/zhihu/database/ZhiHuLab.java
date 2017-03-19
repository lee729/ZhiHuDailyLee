package com.example.lee.zhihu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lee.zhihu.Modle.ZhiHuNewsItem;
import com.example.lee.zhihu.Modle.ZhiHuThemeItem;
import com.example.lee.zhihu.database.ZhiHuDbSchema.ZhiHuNewsTable;
import com.example.lee.zhihu.database.ZhiHuDbSchema.ZhiHuThemeTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lee on 2017/3/9.
 */
public class ZhiHuLab {

    private static ZhiHuLab sZhiHuLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    /**
     * 构造函数私有化
     * 单例设计模式
     * */
    private ZhiHuLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new ZhiHuDBHelper(mContext)
                .getWritableDatabase();
    }
    public static ZhiHuLab get(Context context){
        if (sZhiHuLab == null){
            sZhiHuLab=new ZhiHuLab(context);
        }
        return sZhiHuLab;
    }
    /**
     * 根据news item的id，从数据库取出某个news
     * */
    public ZhiHuNewsItem getItem(String newsId){
        ZhiHuCursorWapper cursorWapper = queryZhiHuNews(
                ZhiHuNewsTable.Cols.NEWSID + " = ?",
                new String[]{newsId}
        );
        try {
            if (cursorWapper.getCount()==0){
                return null;
            }
            cursorWapper.moveToFirst();
            return cursorWapper.getNewsItem();
        }finally {
            cursorWapper.close();
        }
    }
    /**
     * 从数据库取出没有themeid的news的list集合
     * themeid = -99
     * */
    public List<ZhiHuNewsItem> getList(){
        List<ZhiHuNewsItem> list = new ArrayList<>();
        ZhiHuCursorWapper cursorWapper = queryZhiHuNews(ZhiHuNewsTable.Cols.THEME_ID+"= ?",new String[]{ZhiHuNewsItem.NO_THEME_ID});
        try{
            cursorWapper.moveToFirst();
            while (!cursorWapper.isAfterLast()){
                list.add(cursorWapper.getNewsItem());
                cursorWapper.moveToNext();
            }
        }finally {
            cursorWapper.close();
        }
        return list;
    }
    /**
     * 从数据库取出有themeid的list集合
     * */
    public List<ZhiHuNewsItem> getList(String themeId){
        List<ZhiHuNewsItem> list = new ArrayList<>();
        ZhiHuCursorWapper cursorWapper = queryZhiHuNews(ZhiHuNewsTable.Cols.THEME_ID+"= ?",new String[]{themeId});
        try{
            cursorWapper.moveToFirst();
            while (!cursorWapper.isAfterLast()){
                list.add(cursorWapper.getNewsItem());
                cursorWapper.moveToNext();
            }
        }finally {
            cursorWapper.close();
        }
        return list;
    }
    /**
     * 根据参数isTopNews的值，（在ZhiHuNewsItem中定义了常量）
     * 从数据库取出所有是topnews的list集合.
     * 或者取出所有不是topnews的list集合。
     * */
    public List<ZhiHuNewsItem> getList(int isTopNews){
        List<ZhiHuNewsItem> list = new ArrayList<>();
        ZhiHuCursorWapper cursorWapper = queryZhiHuNews(ZhiHuNewsTable.Cols.IS_TOP_STORY+"= ?",new String[]{isTopNews+""});
        try{
            cursorWapper.moveToFirst();
            while (!cursorWapper.isAfterLast()){
                list.add(cursorWapper.getNewsItem());
                cursorWapper.moveToNext();
            }
        }finally {
            cursorWapper.close();
        }
        return list;
    }
    /**
     * 向数据库添加一个news
     * */
    public void addZhiHuItem(ZhiHuNewsItem item){
        ContentValues contentValues = getContentValues(item);
        mDatabase.insert(ZhiHuNewsTable.NAME, null, contentValues);
    }
    /**
     * 向数据库增加一个theme
     * */
    public void addThemeItem(ZhiHuThemeItem item){
        ContentValues contentValues = getContentValues(item);
        mDatabase.insert(ZhiHuThemeTable.NAME, null, contentValues);
    }
    /**
     * 数据库更新某个news信息
     * */
    public void updateZhiHuItem(ZhiHuNewsItem item){
        ContentValues contentValues = getContentValues(item);
        mDatabase.update(
                ZhiHuNewsTable.NAME,
                contentValues,
                ZhiHuNewsTable.Cols.NEWSID + " = ?",
                new String[]{item.getId()});
    }
    /**
     * 数据库更新某个themeitem
     * */
    public void updateThemeItem(ZhiHuThemeItem item){
        ContentValues contentValues = getContentValues(item);
        mDatabase.update(
                ZhiHuThemeTable.NAME,
                contentValues,
                ZhiHuThemeTable.Cols.THEME_ID + "= ?",
                new String[]{item.getThemeId()});
    }
    /**
     * theme的id，从数据库取出某个theme
     * */
    public ZhiHuThemeItem getThemeItem(String themeId){
        ZhiHuCursorWapper cursorWapper = queryZhiHuTheme(
                ZhiHuThemeTable.Cols.THEME_ID + "= ?",
                new String[]{themeId});
        try {
            cursorWapper.moveToFirst();
            if (cursorWapper.getCount()==0){
                return null;
            }
            return cursorWapper.getThemeItem();
        }finally {
            cursorWapper.close();
        }
    }
    /**
     * 从数据库取出所有theme的集合list
     * */
    public List<ZhiHuThemeItem> getThemeList(){
        List<ZhiHuThemeItem> list = new ArrayList<>();
        ZhiHuCursorWapper cursorWrapper = queryZhiHuTheme(null, null);
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()){
                list.add(cursorWrapper.getThemeItem());
                cursorWrapper.moveToNext();
            }
        }finally {
            cursorWrapper.close();
        }
        return list;
    }
    /**
     * 获取封装有知乎newsitem的contentvaluse
     * */
    private static ContentValues getContentValues(ZhiHuNewsItem item){
        ContentValues contentValues = new ContentValues();

        contentValues.put(ZhiHuNewsTable.Cols.DATE,item.getDate());
        contentValues.put(ZhiHuNewsTable.Cols.THEME_ID,item.getThemeId());
        contentValues.put(ZhiHuNewsTable.Cols.IS_TOP_STORY,item.getIsTopNews());

        contentValues.put(ZhiHuNewsTable.Cols.NEWSID,item.getId());
        contentValues.put(ZhiHuNewsTable.Cols.TITLE,item.getTitle());
        contentValues.put(ZhiHuNewsTable.Cols.CONTENT, item.getContent());
        contentValues.put(ZhiHuNewsTable.Cols.COMMENT,item.getComments());
        contentValues.put(ZhiHuNewsTable.Cols.LONG_COMMENT,item.getLongComments());
        contentValues.put(ZhiHuNewsTable.Cols.SHORT_COMMENT,item.getShortCommets());
        contentValues.put(ZhiHuNewsTable.Cols.POPULARITY, item.getPopularity());

        return contentValues;
    }
    /**
     * 获取封装有知乎themeitem的contentvaluse
     * */
    private static ContentValues getContentValues(ZhiHuThemeItem item){
        ContentValues contentValues = new ContentValues();

        contentValues.put(ZhiHuThemeTable.Cols.THEME_ID,item.getThemeId());
        contentValues.put(ZhiHuThemeTable.Cols.THEME_NAME,item.getThemeName());
        contentValues.put(ZhiHuThemeTable.Cols.DESCRIPTION, item.getDescription());

        return contentValues;
    }
    /**
     * 遍历数据库，返回cursor的包装类wapper
     * */
    private ZhiHuCursorWapper queryZhiHuNews(String whereCaluse,String[] whereArgs){
        Cursor cursor = mDatabase.query(
                ZhiHuNewsTable.NAME,
                null,
                whereCaluse,
                whereArgs,
                null, null, null);
        return new ZhiHuCursorWapper(cursor);
    }
    /**
     * 遍历数据库，返回含有themelist的cursorwapper
     * */
    private ZhiHuCursorWapper queryZhiHuTheme(String whereCaluse,String[] whereArgs){
        Cursor cursor = mDatabase.query(
                ZhiHuThemeTable.NAME,
                null,
                whereCaluse,
                whereArgs,
                null, null, null
        );
        return new ZhiHuCursorWapper(cursor);
    }
}
