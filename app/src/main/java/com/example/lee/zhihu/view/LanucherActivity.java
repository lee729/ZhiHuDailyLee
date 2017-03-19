package com.example.lee.zhihu.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.lee.zhihu.R;

/**
 * Created by lee on 2017/3/18.
 */
public class LanucherActivity extends AppCompatActivity{
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.launcher_layout);
        getSupportActionBar().hide();
        mImageView = (ImageView) findViewById(R.id.launcher_layout_image_view);
        mImageView.setImageResource(R.drawable.lancher_image);
        //启动延迟handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //跳转到zhihulistactivity
                Intent intent = ZhiHuListActivity.newIntent(LanucherActivity.this);
                startActivity(intent);
                //结束本activity
                LanucherActivity.this.finish();
            }
        }, 5000);
    }
}
