package com.example.lee.zhihu.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lee.zhihu.Modle.ZhiHuNewsItem;
import com.example.lee.zhihu.R;
import com.example.lee.zhihu.database.ZhiHuLab;
import com.example.lee.zhihu.util.ZhiHuConnect;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by lee on 2017/3/9.
 */
public class ZhiHuPagerFragment extends Fragment{
    private static final String TAG  ="ZhiHuPagerFragment";
    private static final String ARG_ZHIHU_ID = "arg_zhihu_id";
    /**
     * 网络请求成功
     */
    public static final int RECEIVE_SUCCESS = 1;
    /**
     * 网络请求失败
     */
    public static final int RECEIVER_FAILED = 0;
    /**
     * 网络请求超时
     */
    public static final int RECEIVER_TIMEOUT = -1;

    private Intent intent;
    private String idOfArticle;
    private RequestQueue mQueue;
    private String cssUrl;
    private String htmlString;
    private Toolbar mToolbar;
    private WebView article;
    private ImageView mImageView;
    private TextView mImageResourceTextView;
    private Document document;
    private ZhiHuNewsItem mZhiHuNewsItem;
    private String mPageUri;
    private String mPageShareUrl;
    private String mPageUrlString;
    private String mImageUrl;
    private RequestQueue mRequestQueue;

    public static ZhiHuPagerFragment newInstance(String newsId){
        Bundle args = new Bundle();
        args.putString(ARG_ZHIHU_ID, newsId);
        ZhiHuPagerFragment fragment = new ZhiHuPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        idOfArticle = getArguments()
                .getString(ARG_ZHIHU_ID);

        mZhiHuNewsItem = ZhiHuLab.get(getActivity()).getItem(idOfArticle);
        mPageUri = ZhiHuConnect.ZhiHuUrl
                .getOneNewsUrl(mZhiHuNewsItem.getId());
    }
    /**
     * 创建菜单
     * */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_toolbar_pager, menu);
    }
    /**
     * 响应菜单点击事件
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item__pager_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                        mZhiHuNewsItem.getTitle() + "（分享自@知乎日报 APP）"
                                 + mZhiHuNewsItem.getShareUrl());
                startActivity(shareIntent);
                break;
            case R.id.menu_item__pager_comment:
                Intent commentIntent = ZhiHuCommentActivity
                        .newIntent(
                                getActivity(),
                                mZhiHuNewsItem.getId());
                startActivity(commentIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zhi_hu_pager,container,false);
        mImageView = (ImageView) view.findViewById(R.id.fragment_zhi_hu_pager_image_view);
        mImageResourceTextView = (TextView) view.findViewById(R.id.fragment_zhi_hu_pager_image_resource);
        initWidget(view);
        getArticle();
        return view;
    }
    /**
     * 发起网络请求 得到文章内容html 然后再进行图片大小处理使之适配屏幕大小
     */
    private void getArticle(){
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message message){
                if (message.what == RECEIVE_SUCCESS){
                    zoomPicture();
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                JsonObjectRequest jsonObjectRequest =
                        new JsonObjectRequest(
                                ZhiHuConnect.ZhiHuUrl.getOneNewsUrl(idOfArticle),
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try
                                        {
                                            //有的没有image，那就不显示图片
                                            if (!response.has("image")){
                                                mImageView.setVisibility(View.GONE);
                                            }else {
                                                //加载图片
                                                mImageUrl = response.getString("image");
                                                Picasso.with(getActivity())
                                                        .load(mImageUrl)
                                                        .into(mImageView);
                                                //加载文字
                                                String image_souce = response.getString("image_source");
                                                mImageResourceTextView.setText(image_souce);
                                            }
                                            //shareurl赋值
                                            mPageShareUrl = response.getString("share_url");
                                            mZhiHuNewsItem.setShareUrl(mPageShareUrl);
                                            htmlString = response.getString("body");
                                            cssUrl = response.getString("css");
                                            Message message = new Message();
                                            message.what = RECEIVE_SUCCESS;
                                            handler.sendMessage(message);
                                        } catch (JSONException e) {
                                             //有时候会出现 没有body只有一个sharedURL的情况
                                            try {
                                                htmlString = response.getString("share_url");
                                                getQuotedArticle(htmlString);
                                            } catch (JSONException e1) {
                                                e1.printStackTrace();
                                            }
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            });
                    mQueue.add(jsonObjectRequest);
                    mQueue.start();
            }
        }).start();
    }
    /**
     * 初始化控件
     */
    private void initWidget(View view){

        article = (WebView) view.findViewById(R.id.fragment_zhi_hu_page_web_view);
        article.getSettings().setAppCacheEnabled(true);// 设置启动缓存
        article.getSettings().getDomStorageEnabled();
        article.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        article.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//适应屏幕，内容将自动缩
        article.getSettings().setJavaScriptEnabled(true);
        article.getSettings().setLoadsImagesAutomatically(true);
        article.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (!article.getSettings().getLoadsImagesAutomatically()) {
                    article.getSettings().setLoadsImagesAutomatically(true);
                }
            }
        });
        mQueue = Volley.newRequestQueue(getActivity());
    }
    /**
     * 将过大的图片适应屏幕
     */
    private void zoomPicture(){
        document = Jsoup.parse(htmlString);
        Elements imgs = document.getElementsByTag("img");
        for (Element img : imgs){
            if (img.attr("class").equals("content-image") || !img.hasAttr("class")){
                Log.d("img","zoomed");
                img.attr("width","100%").attr("height","auto");
            }
        }
        htmlString = document.toString();
        htmlString = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />" + htmlString;
        article.loadDataWithBaseURL("file:///android_asset/", htmlString, "text/html; charset=UTF-8", null, null);
    }
    /**
     * @param html  link to the site
     */
    private void getQuotedArticle(final String html){
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message message){
                if (message.what == RECEIVE_SUCCESS){
                    zoomPicture();
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document document = null;
                try {
                    document = Jsoup.connect(html).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                htmlString = document.toString();
                Message message = new Message();
                message.what = RECEIVE_SUCCESS;
                handler.sendMessage(message);
            }
        }).start();
    }
}
