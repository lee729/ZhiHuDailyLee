package com.example.lee.zhihu.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.lee.zhihu.Modle.ZhiHuNewsItem;
import com.example.lee.zhihu.R;
import com.example.lee.zhihu.database.ZhiHuLab;

import java.util.List;

/**
 * Created by lee on 2017/3/9.
 */
public class ZhiHuPagerActivity extends AppCompatActivity{

    private static final String TAG = "ZhiHuPagerActivity";
    private static final String EXTRA_ZHI_HU_NEWSID  =
            "com.example.lee.zhihu.EXTRA_ZHI_HU_NEWSID";

    private ViewPager mViewPager;
    private List<ZhiHuNewsItem> mZhiHuNewsItems;
    private String mThemeid;

    public static Intent newIntent(Context context,String newsId){
        Intent intent = new Intent(context,ZhiHuPagerActivity.class);
        intent.putExtra(EXTRA_ZHI_HU_NEWSID,newsId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.i(TAG,"onCreate===");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhi_hu_pager);

        String newsId = getIntent()
                .getStringExtra(EXTRA_ZHI_HU_NEWSID);
        ZhiHuNewsItem newsItem = ZhiHuLab.get(this).getItem(newsId);
        if (newsItem.getThemeId()!=null){
            mThemeid = newsItem.getThemeId();
        }


//        Log.i(TAG,"newsId==="+newsId);

        mViewPager = (ViewPager) findViewById(R.id.activity_zhi_hu_view_pager);

        if (mThemeid==ZhiHuNewsItem.NO_THEME_ID){
            mZhiHuNewsItems = ZhiHuLab.get(this).getList();
        }else {
            mZhiHuNewsItems = ZhiHuLab.get(this).getList(mThemeid);
        }

        FragmentManager fm = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                ZhiHuNewsItem item = mZhiHuNewsItems.get(position);
                return ZhiHuPagerFragment.newInstance(item.getId());
            }

            @Override
            public int getCount() {
                return mZhiHuNewsItems.size();
            }
        });

        for (int i = 0; i<mZhiHuNewsItems.size(); i++){
            if (newsId.equals(mZhiHuNewsItems.get(i).getId())){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
