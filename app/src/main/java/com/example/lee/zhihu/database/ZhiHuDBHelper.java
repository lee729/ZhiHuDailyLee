package com.example.lee.zhihu.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lee.zhihu.database.ZhiHuDbSchema.ZhiHuNewsTable;
import com.example.lee.zhihu.database.ZhiHuDbSchema.ZhiHuThemeTable;

/**
 * Created by lee on 2017/3/9.
 */
public class ZhiHuDBHelper extends SQLiteOpenHelper{

    private static final int VERSION = 1;
    private static final String DATABASE_NAME ="ZhiHuDaily.db";

    public ZhiHuDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //建表，知乎news
        db.execSQL("create table "+ ZhiHuNewsTable.NAME+" ("
                +"_id integer primary key autoincrement, "
                + ZhiHuNewsTable.Cols.DATE+", "
                + ZhiHuNewsTable.Cols.THEME_ID+", "
                + ZhiHuNewsTable.Cols.IS_TOP_STORY+", "

                + ZhiHuNewsTable.Cols.NEWSID+", "
                + ZhiHuNewsTable.Cols.TITLE+", "
                + ZhiHuNewsTable.Cols.CONTENT+", "
                + ZhiHuNewsTable.Cols.SHARE_URL+", "
                + ZhiHuNewsTable.Cols.COMMENT+", "
                + ZhiHuNewsTable.Cols.LONG_COMMENT+", "
                + ZhiHuNewsTable.Cols.SHORT_COMMENT+", "
                + ZhiHuNewsTable.Cols.POPULARITY
                        +")");
        //建表，知乎themelist
        db.execSQL("create table "+ ZhiHuThemeTable.NAME+" ("
                +"_id integer primary key autoincrement, "
                + ZhiHuThemeTable.Cols.THEME_ID+", "
                + ZhiHuThemeTable.Cols.THEME_NAME+", "
                + ZhiHuThemeTable.Cols.DESCRIPTION
                +")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //删除表，重建
        db.execSQL("drop table if exists "+ZhiHuNewsTable.NAME);
        db.execSQL("drop table if exists "+ZhiHuThemeTable.NAME);
        onCreate(db);
    }
}
