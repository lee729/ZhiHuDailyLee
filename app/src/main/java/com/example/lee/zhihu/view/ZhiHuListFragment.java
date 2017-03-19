package com.example.lee.zhihu.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.lee.zhihu.Modle.ZhiHuNewsItem;
import com.example.lee.zhihu.Modle.ZhiHuThemeItem;
import com.example.lee.zhihu.R;
import com.example.lee.zhihu.adapter.ZhiHuNewsListAdapter;
import com.example.lee.zhihu.adapter.ZhiHuThemeListViewAdapter;
import com.example.lee.zhihu.database.ZhiHuLab;
import com.example.lee.zhihu.service.ZhiHuLatestNewsService;
import com.example.lee.zhihu.util.ZhiHuConnect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lee on 2017/3/8.
 */
public class ZhiHuListFragment extends Fragment{

    private static final String TAG  = "ZhiHuListFragment";

    private List<ZhiHuNewsItem> mZhiHuNewsItems = new ArrayList<>();
    private List<ZhiHuNewsItem> mBeforeNewsItems = new ArrayList<>();
    private List<ZhiHuThemeItem> mThemeList = new ArrayList<>();
    private String mDate;
    //数据库中存储的第一条news的id
    private String mOldNewsId;

    //抽屉
    private DrawerLayout mLeftDrawerLayout;
    private ListView mLeftListview;
    //主页面
    private RecyclerView mRecyclerView;
    private ZhiHuNewsListAdapter mAdapter;// = new ZhiHuNewsListAdapter(mZhiHuNewsItems);
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static ZhiHuListFragment newInstance(){
        return new ZhiHuListFragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //启动servie
        boolean shouldStartService = ZhiHuLatestNewsService.isServiceAlarmOn(getActivity(),mOldNewsId);
        ZhiHuLatestNewsService.setServiceAlarm(getActivity(), shouldStartService, mOldNewsId);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_toolbar_list, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
                    case R.id.menu_item_list_about:
                        Toast.makeText(getActivity(),"about",Toast.LENGTH_SHORT).show();
                        break;
                }
        return true;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_zhi_hu_left_drawer_layout, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_zhi_hu_left_drawer_layout_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //获取数据,更新界面
        new ZhiHuAsyncTask().execute();//bug定位
        new ZhiHuThemeAsyncTask().execute();
        //抽屉
        mLeftDrawerLayout = (DrawerLayout) view.findViewById(
                R.id.fragment_zi_hu_left_drawer_layout);
        //设置抽屉
        mLeftDrawerLayout.addDrawerListener(
                new DrawerLayout.SimpleDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
//                Toast.makeText(getActivity(), "抽屉被打开了", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
//                Toast.makeText(getActivity(),"抽屉被关闭了",Toast.LENGTH_SHORT).show();
                    }
                });
        mLeftListview = (ListView) view.findViewById(R.id.fragment_zhi_hu_left_drawer_layout__linear_layout_list_view);
        //设置抽屉里的listview监听器
        mLeftListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ZhiHuThemeItem item = mThemeList.get(position);
//                Toast.makeText(getActivity(), item.getThemeName() + item.getThemeId(), Toast.LENGTH_SHORT).show();
                String themeId = item.getThemeId();
                Intent themeIntent = ZhiHuThemeActivity.newIntent(getActivity(), themeId);
                startActivity(themeIntent);
            }
        });
        setUpThemeAdapter(mThemeList);
        //下拉刷新
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(
                R.id.fragment_zi_hu_left_drawer_layout_swipeRefreshLayout);
        //设置swipeRefreshLayout，下拉刷新
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
                        //重新获取数据，在asynctask里。
                        new ZhiHuAsyncTask().execute();
                        Log.i(TAG, mZhiHuNewsItems.size() + "测试3333");
                        String newId = mZhiHuNewsItems.get(0).getId();
                        //判断是否获取了最新消息
                        if (mOldNewsId.equals(newId)) {
                            mAdapter.setNewData(mZhiHuNewsItems);
                            Toast.makeText(getActivity(), "当前已是最新消息了！", Toast.LENGTH_SHORT).show();
                        } else {
                            mAdapter.setNewData(mZhiHuNewsItems);
//                            mRecyclerView.setAdapter(mAdapter);
                            Toast.makeText(getActivity(), "刷新完毕！", Toast.LENGTH_SHORT).show();
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 5000);
            }
        });
        //recycler的item点击事件
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(getActivity(),
                        mZhiHuNewsItems.get(position).getId()
                                + mZhiHuNewsItems.get(position).getTitle(),
                        Toast.LENGTH_SHORT).show();
//                跳转到详情页面
                Intent pageIntent = ZhiHuPagerActivity
                        .newIntent(getActivity(),
                                mZhiHuNewsItems.get(position).getId());
                startActivity(pageIntent);
            }
        });

        return view;
    }
    /**
     * 上拉加载更多内容
     * */
    private void installView(){
        //上拉加载更多内容
        //滑动到最后一个item的时候,自动调用监听器，加载更多内容
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //获取前一天的news的list
                        mDate = mZhiHuNewsItems.get(mZhiHuNewsItems.size()-1).getDate();
                        new ZhiHuBeforeAsyncTask().execute(mDate);
//                        for (int i = 0;i<6;i++){
//                            ZhiHuNewsItem item = new ZhiHuNewsItem(ZhiHuNewsItem.NO_THEME_ID);
//                            item.setId(i+"");
//                            item.setTitle(i + "titel");
//                            mBeforeNewsItems.add(item);
//                        }
                        if (mBeforeNewsItems !=null && mBeforeNewsItems.size()!=0){
                            //list拼接在现有listview下面，显示出来
                            mAdapter.addData(mBeforeNewsItems);
                            mZhiHuNewsItems.addAll(mBeforeNewsItems);
                            //成功获取,加载完成
                            mAdapter.loadMoreComplete();
                        }else {
                            //获取数据失败
                            Toast.makeText(getActivity(),
                                    "获取更多内容失败",
                                    Toast.LENGTH_SHORT).show();
                            mAdapter.loadMoreFail();
                        }
                    }
                }, 3000);
            }
        }, mRecyclerView);
    }
    /**
     * 后台线程访问网络，获取某一天之前的消息，不存入服务器
     * */
    public class ZhiHuBeforeAsyncTask extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... params) {
            String date = params[0];
            mBeforeNewsItems = new ZhiHuConnect().sendUrl(getActivity(),
                    ZhiHuConnect.ZhiHuUrl.getNewsBeforeDateUrl(date),
                    ZhiHuConnect.KIND_NEWS_BEFORE_ONE_DAY,
                    null);
            return null;
        }
    }
    /**
     * handler
     * */
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET_LATEST_NEWS_SUSCESS:
                    mZhiHuNewsItems = (List<ZhiHuNewsItem>) msg.obj;
                    break;
            }
        }
    };
    /**
     * 开启后台线程，获取最新消息
     * */
    public static final int GET_LATEST_NEWS_SUSCESS = 1;
    public void getLatestNews(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ZhiHuNewsItem> list = new ZhiHuConnect().sendUrl(getActivity(),
                        ZhiHuConnect.ZhiHuUrl.URL_NEWS_LATEST,
                        ZhiHuConnect.KIND_NEWS_LATEST,null);
                //把list发送给主线程去更新ui
                Message message = new Message();
                message.what = GET_LATEST_NEWS_SUSCESS;
                message.obj = list;
                mHandler.sendMessage(message);
            }
        });
    }
    public class ZhiHuAsyncTask extends AsyncTask<Void,Void,List<ZhiHuNewsItem>> {

        protected List<ZhiHuNewsItem> doInBackground(Void... params) {
            List<ZhiHuNewsItem> list =
                    new ZhiHuConnect().sendUrl(getActivity(),
                            ZhiHuConnect.ZhiHuUrl.URL_NEWS_LATEST,
                            ZhiHuConnect.KIND_NEWS_LATEST,null);
            Log.i(TAG,list.size()+"测试doInBackground");
            saveZhiHuList(list);
            return list;
        }
        @Override
        protected void onPostExecute(List<ZhiHuNewsItem> list) {
            super.onPostExecute(list);
            mZhiHuNewsItems = list;
            Log.i(TAG,mZhiHuNewsItems.size()+"测试");
            setUpAdapter(list);
        }
    }
    private void saveZhiHuList(List<ZhiHuNewsItem> list){
        //从数据库取出第一条news的id
        mOldNewsId = list.get(0).getId();
        for (int i = 0;i<list.size();i++){
            ZhiHuNewsItem item = list.get(i);
            ZhiHuLab.get(getActivity()).addZhiHuItem(item);
        }
    }
    /**
     * 后台线程访问网络,获取theme数据,更新ui，存入数据库
     * */
    private class ZhiHuThemeAsyncTask extends AsyncTask<Void,Void,List<ZhiHuThemeItem>>{
        @Override
        protected List<ZhiHuThemeItem> doInBackground(Void... params) {
            List<ZhiHuThemeItem> list =
                    new ZhiHuConnect().sendUrl(getActivity(),
                    ZhiHuConnect.ZhiHuUrl.URL_THEME_LIST,
                    ZhiHuConnect.KIND_NEWS_THEME,null);
            saveToDB(list);
            return list;
        }
        @Override
        protected void onPostExecute(List<ZhiHuThemeItem> list) {
            super.onPostExecute(list);
            mThemeList = list;
            setUpThemeAdapter(list);
        }
        public void saveToDB(List<ZhiHuThemeItem> list){
            for (ZhiHuThemeItem item:list){
//                Log.i(TAG, "saveToDB"+item.getThemeId() + item.getThemeName() + item.getDescription());
                ZhiHuLab.get(getActivity()).addThemeItem(item);
            }
        }
    }
    /**
     * 配置themeListview的adapter
     * */
    private void setUpThemeAdapter(List<ZhiHuThemeItem> list){
        ZhiHuThemeListViewAdapter adapter = new ZhiHuThemeListViewAdapter(
                getActivity(),
                R.layout.zhi_hu_theme_list_item,
                list);
        mLeftListview.setAdapter(adapter);
    }
    /**
     * 配置recyclerview的adapter
     * */
    private void setUpAdapter(List<ZhiHuNewsItem> list){
        if (mAdapter==null){
            mAdapter = new ZhiHuNewsListAdapter(list);
            installView();
            mRecyclerView.setAdapter(mAdapter);
//            mAdapter.setNewData(list);
        }else {
            mAdapter.notifyDataSetChanged();
        }

    }
}
