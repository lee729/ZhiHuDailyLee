package com.example.lee.zhihu.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.example.lee.zhihu.Modle.ZhiHuCommentItem;
import com.example.lee.zhihu.Modle.ZhiHuNewsItem;
import com.example.lee.zhihu.Modle.ZhiHuThemeItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lee on 2017/3/8.
 * 从服务器获取byte[]数据。
 * 把baye[]数据转换为string类型
 * 解析string
 * 把数据存入list数组中
 */
public class ZhiHuConnect {
    private static final String TAG = "ZhiHuConnect";

    public static final int KIND_NEWS_LATEST = 0;
    public static final int KIND_NEWS_COMMENT = 1;
    public static final int KIND_NEWS_THEME = 2;
    public static final int KIND_NEWS_THEME_CONTENT = 3;
    public static final int KIND_NEWS_BEFORE_ONE_DAY = 5;
    /**
     * 检查网络是否可用
     * */
    public static boolean isNetwordAvailableAndConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = cm.getActiveNetworkInfo()!=null;
        boolean isNetworkConnected = isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }
    public static final class ZhiHuUrl {
        /*
        * 查看最新消息
        * */
        public static final String URL_NEWS_LATEST = "http://news-at.zhihu.com/api/4/news/latest";
        /*
        * 查看某个消息的具体内容，需要id
        * */
        public static String getOneNewsUrl(String newsId){
            return "http://news-at.zhihu.com/api/4/news/"+newsId;
        }
        /*
        * 查看某日之前的消息列表，需要日期作为后缀，查询某日前一日的消息（格式：20170101）
        * */
        public static String getNewsBeforeDateUrl(String yyyymmdd){
            return "http://news-at.zhihu.com/api/4/news/before/"+yyyymmdd;
        }
        /*
        * 查看某个消息的额外信息，如赞的数量，评论的数量。需要id作为后缀
        * */
        public static String getNewsExtraUrl(String newsId){
            return "http://news-at.zhihu.com/api/4/story-extra/"+newsId;
        }
        /*
        * 查看长评论，需要id作为后缀，注意，id后跟着“/long-comments”
        * */
        public static String getLongCommentUrl(String newsId){
            return "http://news-at.zhihu.com/api/4/story/"+newsId+"/long-comments";
        }
        /*
        * 查看短评论，//需要id作为后缀，注意，id后跟着“/short-comments”
        * */
        public static String getShoreCommentUrl(String newId){
            return "http://news-at.zhihu.com/api/4/story/"+newId+"/short-comments";
        }
        /*
        * 查看主题日报
        * */
        public static final String URL_THEME_LIST = "http://news-at.zhihu.com/api/4/themes";
        /*
        * 查看主题日报的具体内容，需要以主题日报的id作为后缀（注意不是某个news的id）
        * */
        public static String getThemtContentUrl(String themeId){
            return "http://news-at.zhihu.com/api/4/theme/"+themeId;
        }
    }

    public byte[] getUrlBytes(String urlString) throws IOException{
        URL url = new URL(urlString);
        HttpURLConnection httpURLConnection =
                (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        try {
            InputStream inputStream = httpURLConnection.getInputStream();

            byte[] buffer = new byte[1024];
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int len = 0;
            while ((len = inputStream.read(buffer))>0){
                out.write(buffer,0,len);
            }
            out.close();
            return out.toByteArray();
        }finally {
            httpURLConnection.disconnect();
        }
    }
    public String getUrlString(String urlSring) throws IOException {
        return new String(getUrlBytes(urlSring));
    }
    /**
     * 根据url类型不同，有不同的解析方法
     * */
    public List sendUrl(Context context,String url,int kind,Integer themeId){
        switch (kind){
            case KIND_NEWS_LATEST:
                List<ZhiHuNewsItem> items = new ArrayList<>();
                try {
                    String result = getUrlString(url);
//                  Log.i(TAG, "获取知乎返回的数据：：" + result);
                    JSONObject jsonObject = new JSONObject(result);
                    parseNewsItemLatest(items,jsonObject);
                } catch (IOException e) {
                    Log.e(TAG, "连接知乎失败", e);
                    Toast.makeText(context, "连接知乎失败", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Log.e(TAG,"解析知乎数据失败",e);
                    Toast.makeText(context,"解析知乎数据失败",Toast.LENGTH_SHORT).show();
                }
                return items;

            case KIND_NEWS_COMMENT:
                List<ZhiHuCommentItem> commentItems = new ArrayList<>();
                try {
                    String result = getUrlString(url);
                    JSONObject jsonObject = new JSONObject(result);
                    parseNewsComment(commentItems,jsonObject);
                } catch (IOException e) {
                    Log.e(TAG,"获取评论失败",e);
                    Toast.makeText(context,"获取评论失败",Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Log.e(TAG,"解析评论失败",e);
                    Toast.makeText(context,"解析评论失败",Toast.LENGTH_SHORT).show();
                }
                return commentItems;

            case KIND_NEWS_THEME:
                List<ZhiHuThemeItem> themeItems = new ArrayList<>();
                try {
                    String result = getUrlString(url);
                    JSONObject resultObject = new JSONObject(result);
                    parseNewsTheme(themeItems, resultObject);
                } catch (IOException e) {
                    Log.e(TAG,"获取主题日报失败",e);
                    Toast.makeText(context,"获取主题日报失败",Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Log.e(TAG,"解析主题日报失败",e);
                    Toast.makeText(context,"解析主题日报失败",Toast.LENGTH_SHORT).show();
                }
                return themeItems;

            case KIND_NEWS_THEME_CONTENT:
                List<ZhiHuNewsItem> themeStoryItems = new ArrayList<>();
                try {
                    String result = getUrlString(url);
                    JSONObject resultObject = new JSONObject(result);
                    parseNewsThemeStory(themeStoryItems,resultObject,themeId);
                } catch (IOException e) {
                    Log.e(TAG,"获取主题日报内容失败",e);
                    Toast.makeText(context,"获取主题日报内容失败",Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Log.e(TAG,"解析主题日报内容失败",e);
                    Toast.makeText(context,"解析主题日报内容失败",Toast.LENGTH_SHORT).show();
                }
                return themeStoryItems;

            case KIND_NEWS_BEFORE_ONE_DAY:
                List<ZhiHuNewsItem> oldItems = new ArrayList<>();
                try {
                    String result = getUrlString(url);
                    JSONObject resultObject = new JSONObject(result);
                    parseOldNewsItem(oldItems,resultObject);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return oldItems;
        }
        return null;
    }
    /**
     * 解析主题日报所含内容的方法
     * */
    private void parseNewsThemeStory(List<ZhiHuNewsItem> list, JSONObject resultObject,Integer themeId) throws JSONException {
        String largeImageUrl = resultObject.getString("background");

        JSONArray sotryArray = resultObject.getJSONArray("stories");
        for (int i = 0;i<sotryArray.length();i++){
            ZhiHuNewsItem item = new ZhiHuNewsItem(themeId.toString());
            JSONObject storyObject = sotryArray.getJSONObject(i);

            if (storyObject.has("images")){
                JSONArray imagesArray = storyObject.getJSONArray("images");
                if (imagesArray.length()!=0){
                    String storyImage = imagesArray.getString(0);
                    item.setImageUrl(storyImage);
                }
            }
            String storyId = storyObject.getString("id");
            String storyTitle = storyObject.getString("title");

            item.setId(storyId);
            item.setTitle(storyTitle);
            item.setLargeImageUrl(largeImageUrl);

            list.add(item);
        }
    }
    /**
     * 解析主题日报的方法
     * */
    private void parseNewsTheme(List<ZhiHuThemeItem> list,JSONObject jsonObject) throws JSONException {
        JSONArray themeArray = jsonObject.getJSONArray("others");

        for (int i = 0;i<themeArray.length();i++){
            ZhiHuThemeItem themeItem = new ZhiHuThemeItem();
            JSONObject themeObject = themeArray.getJSONObject(i);
            String themeId = themeObject.getString("id");
            String themeName = themeObject.getString("name");
            String themeDescription = themeObject.getString("description");

            themeItem.setThemeId(themeId);
            themeItem.setThemeName(themeName);
            themeItem.setDescription(themeDescription);
            list.add(themeItem);
        }
    }
    /**
     * 解析评论的方法
     * */
    private void parseNewsComment(List<ZhiHuCommentItem> list,JSONObject jsonObject) throws JSONException {
        JSONArray commentArray = jsonObject.getJSONArray("comments");

        for (int i = 0;i<commentArray.length();i++){
            JSONObject commentObject = commentArray.getJSONObject(i);
            ZhiHuCommentItem commentItem = new ZhiHuCommentItem();

            commentItem.setUserId(commentObject.getLong("id"));
            commentItem.setName(commentObject.getString("author"));
            commentItem.setIconUrl(commentObject.getString("avatar"));
            commentItem.setPopularity(commentObject.getString("likes"));
            commentItem.setDate(commentObject.getLong("time"));
            commentItem.setCommentContent(commentObject.getString("content"));

            if (commentObject.has("reply_to")){
                JSONObject replyObject = commentObject.getJSONObject("reply_to");
                commentItem.setReplyUserId(replyObject.getLong("id"));
            }
            list.add(commentItem);
        }
    }
    /**
     * 解析过往消息的方法
     * */
    private void parseOldNewsItem(List<ZhiHuNewsItem> list, JSONObject jsonObject) throws JSONException {
        parseNewsItemCommon(list, jsonObject);
    }
    /**
     * 通用方法，解析过往消息和最新普通消息都要用的
     * */
    private void parseNewsItemCommon(List<ZhiHuNewsItem> list,JSONObject jsonObject) throws JSONException {
        String date = jsonObject.getString("date");

        JSONArray storiesArray = jsonObject.getJSONArray("stories");
        for (int i = 0;i<storiesArray.length();i++){
            JSONObject storyObject = storiesArray.getJSONObject(i);
            ZhiHuNewsItem item = new ZhiHuNewsItem(ZhiHuNewsItem.NO_THEME_ID);

            item.setIsTopNews(ZhiHuNewsItem.NEWS_ITEM_NOT_TOP);
            item.setDate(date);
            item.setId(storyObject.getString("id"));
            item.setTitle(storyObject.getString("title"));

            if (!storyObject.has("images")){
                continue;
            }
            JSONArray imageArray = storyObject.getJSONArray("images");
            item.setImageUrl(imageArray.getString(0));
//                Log.i(TAG, "获取到item信息：：：" + "date=" + item.getDate() + "  id=" + item.getId() + "  title=" + item.getTitle() + "  imageUrl=" + item.getImageUrl());
            list.add(item);
        }
    }
    /**
     * 解析最新消息的方法
     * */
    private void parseNewsItemLatest(List<ZhiHuNewsItem> items,JSONObject jsonObject) throws JSONException {

        parseNewsItemCommon(items, jsonObject);
        //给top news做标记
        JSONArray topStoriesArray = jsonObject.getJSONArray("top_stories");
        for (int i = 0; i < topStoriesArray.length(); i++) {
            JSONObject topStoryObject = topStoriesArray.getJSONObject(i);
            for (ZhiHuNewsItem item : items) {
                if (item.getId().equals(topStoryObject.getString("id"))) {
                    item.setIsTopNews(ZhiHuNewsItem.NEWS_ITEM_IS_TOP);
                } else {
                    item.setIsTopNews(ZhiHuNewsItem.NEWS_ITEM_NOT_TOP);
                }
            }
        }
    }
}
