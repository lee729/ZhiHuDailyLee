package com.example.lee.zhihu.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.lee.zhihu.Modle.ZhiHuNewsItem;
import com.example.lee.zhihu.Modle.ZhiHuThemeItem;
import com.example.lee.zhihu.R;
import com.example.lee.zhihu.adapter.ZhiHuNewsListAdapter;
import com.example.lee.zhihu.adapter.ZhiHuThemeListViewAdapter;
import com.example.lee.zhihu.database.ZhiHuLab;
import com.example.lee.zhihu.util.ZhiHuConnect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lee on 2017/3/13.
 */
public class ZhiHuThemeFragment extends Fragment{

    private static final String ARG_THEME_ID = "arg_theme_id";

    private String mThemeId;
    private ZhiHuThemeItem mThemeItem;
    private List<ZhiHuNewsItem> mThemeStoryList = new ArrayList<>();

    //抽屉
    private DrawerLayout mLeftDrawerLayout;
    private ListView mLeftListview;
    //主页面
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ZhiHuNewsListAdapter mAdapter = new ZhiHuNewsListAdapter(mThemeStoryList);

    public static ZhiHuThemeFragment newInstance(String themeId){
        Bundle args = new Bundle();
        args.putString(ARG_THEME_ID, themeId);

        ZhiHuThemeFragment fragment = new ZhiHuThemeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThemeId = getArguments().getString(ARG_THEME_ID);
        mThemeItem = ZhiHuLab.get(getActivity()).getThemeItem(mThemeId);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zhi_hu_left_drawer_layout,container,false);
        //recyclerview初始化
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_zhi_hu_left_drawer_layout_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //获取数据
        new ZhiHuThemeStoryAsyncTask().execute();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter = new ZhiHuNewsListAdapter(mThemeStoryList);
                mRecyclerView.setAdapter(mAdapter);
            }
        }, 5000);
        //recyclerview的点击事件
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                //跳转到详情界面
                Intent pageIntent = ZhiHuPagerActivity.newIntent(getActivity(),
                        mThemeStoryList.get(position).getId());
                startActivity(pageIntent);
            }
        });
        initView(view);
        return view;
    }
    /**
     * 控件初始化
     * */
    private void initView(View view){
        //抽屉初始化
        mLeftDrawerLayout = (DrawerLayout) view.findViewById(R.id.fragment_zi_hu_left_drawer_layout);
        mLeftDrawerLayout.addDrawerListener(
                new DrawerLayout.SimpleDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                    }
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                    }
                }
        );
        //抽屉的listview初始化
        mLeftListview = (ListView) view.findViewById(R.id.fragment_zhi_hu_left_drawer_layout__linear_layout_list_view);
        final List<ZhiHuThemeItem> themeList = ZhiHuLab.get(getActivity()).getThemeList();
        ZhiHuThemeListViewAdapter listViewAdapter =
                new ZhiHuThemeListViewAdapter(
                        getActivity(),
                        R.layout.zhi_hu_theme_list_item,
                        themeList);
        mLeftListview.setAdapter(listViewAdapter);
        mLeftListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ZhiHuThemeItem themeItem = themeList.get(position);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, ZhiHuThemeFragment.newInstance(themeItem.getThemeId()))
                        .commit();
            }
        });
        //swiperefreshlayout，下拉刷新
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_zi_hu_left_drawer_layout_swipeRefreshLayout);
        //设置刷新时进度条的颜色，最多设置四个
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //asynctask开启子线程，根据themeid，获取theme的详细界面
                        new ZhiHuThemeStoryAsyncTask().execute();
                        mAdapter.setNewData(mThemeStoryList);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 5000);
            }
        });
    }
    /**
     * 配置recyclerview的adapter
     * */
    private void setUpAdapter(List<ZhiHuNewsItem> list){
        if (mAdapter == null){
            mAdapter = new ZhiHuNewsListAdapter(list);
            mRecyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.notifyDataSetChanged();
        }
    }
    /**
     * 开启线程，根据themeid获取theme的详细内容
     * */
    public class ZhiHuThemeStoryAsyncTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            mThemeStoryList=
                    new ZhiHuConnect().sendUrl(
                            getActivity(),
                            ZhiHuConnect.ZhiHuUrl.getThemtContentUrl(mThemeId),
                            ZhiHuConnect.KIND_NEWS_THEME_CONTENT, Integer.valueOf(mThemeId));
            saveToDB(mThemeStoryList);
            return null;
        }
    }
    private void saveToDB(List<ZhiHuNewsItem> list){
        for (int i=0; i<list.size(); i++){
            ZhiHuNewsItem item = list.get(i);
            ZhiHuLab.get(getActivity()).addZhiHuItem(item);
        }
    }
}
