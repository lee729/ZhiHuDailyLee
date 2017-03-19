package com.example.lee.zhihu.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class ZhiHuListActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context,ZhiHuListActivity.class);
        return intent;
    }

    @Override
    protected Fragment creatFragment() {

        return ZhiHuListFragment.newInstance();
    }
}
