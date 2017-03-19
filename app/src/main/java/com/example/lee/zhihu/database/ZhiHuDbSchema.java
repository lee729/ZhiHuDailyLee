package com.example.lee.zhihu.database;

/**
 * Created by lee on 2017/3/9.
 */
public class ZhiHuDbSchema {
    /**
     * 知乎新闻表常量
     * */
    public static final class ZhiHuNewsTable {
        public static final String NAME = "zhihuNews";

        public static final class Cols{

            public static final String DATE = "date";
            public static final String THEME_ID = "theme_id";
            public static final String IS_TOP_STORY = "is_top_story";

            public static final String TITLE = "title";
            public static final String NEWSID = "newsid";
            public static final String CONTENT = "content";
            public static final String SHARE_URL = "share_url";
            public static final String COMMENT = "comment";
            public static final String LONG_COMMENT= "long_comment";
            public static final String SHORT_COMMENT = "short_comment";
            public static final String POPULARITY = "popularity";
        }
    }
    /**
     * 知乎主题日报列表常量
     * */
    public static final class ZhiHuThemeTable{
        public static final String NAME = "zhihuTheme";
        public static final class Cols{
            public static final String THEME_ID="theme_id";
            public static final String THEME_NAME = "theme_name";
            public static final String DESCRIPTION = "description";
        }
    }
}
