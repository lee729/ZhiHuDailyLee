package com.example.lee.zhihu.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by lee on 2017/3/13.
 */
public class ZhiHuThemeActivity extends SingleFragmentActivity{

    private static final String EXTRA_THEME_ID = "com.example.lee.zhihu.view.extra_theme_id";

    public static Intent newIntent(Context context, String themeId){
        Intent intent = new Intent(context,ZhiHuThemeActivity.class);
        intent.putExtra(EXTRA_THEME_ID,themeId);
        return intent;
    }
    @Override
    protected Fragment creatFragment() {
        String themeId = getIntent().getStringExtra(EXTRA_THEME_ID);
        return ZhiHuThemeFragment.newInstance(themeId);
    }
}
