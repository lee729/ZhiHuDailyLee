package com.example.lee.zhihu.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by lee on 2017/3/12.
 */
public class ZhiHuCommentActivity extends SingleFragmentActivity{

    private static final String EXTRA_NEWS_ID ="com.example.lee.zhihu.view.extra_news_id";

    public static Intent newIntent(Context context,String newsId){
        Intent intent = new Intent(context,ZhiHuCommentActivity.class);
        intent.putExtra(EXTRA_NEWS_ID,newsId);
        return intent;
    }

    @Override
    protected Fragment creatFragment() {
        String newsId = getIntent().getStringExtra(EXTRA_NEWS_ID);

        return ZhiHuCommentFragment.newInstance(newsId);
    }
}
