package com.example.lee.zhihu.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lee.zhihu.Modle.ZhiHuCommentItem;
import com.example.lee.zhihu.database.ZhiHuLab;
import com.example.lee.zhihu.Modle.ZhiHuNewsItem;
import com.example.lee.zhihu.R;
import com.example.lee.zhihu.adapter.ZhiHuCommentAdapter;
import com.example.lee.zhihu.util.ZhiHuConnect;

import java.util.List;

/**
 * Created by lee on 2017/3/12.
 */
public class ZhiHuCommentFragment extends Fragment{

    private static final String TAG  ="ZhiHuCommentFragment";
    private static final String ARG = "arg_news_id";

    private RecyclerView mCommentRecyclerView;
    private ZhiHuCommentAdapter mCommentAdapter;

    private List<ZhiHuCommentItem> mZhihuLongCommentList;
    private ZhiHuNewsItem mNewsItem;

    public static ZhiHuCommentFragment newInstance(String newsId){
        Bundle args = new Bundle();
        args.putString(ARG,newsId);
        ZhiHuCommentFragment commentFragment = new ZhiHuCommentFragment();
        commentFragment.setArguments(args);

        return commentFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String newsId = getArguments().getString(ARG);
        mNewsItem = ZhiHuLab.get(getActivity()).getItem(newsId);

        new ZhiHuCommentAsyncTask().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_zhi_hu_comment, container, false);
        mCommentRecyclerView = (RecyclerView)
                view.findViewById(R.id.fragment_zhi_hu_comment_recycler_view);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setUpAdapter(mZhihuLongCommentList);
        return view;
    }

    private class ZhiHuCommentAsyncTask extends AsyncTask<Void,Void,List<ZhiHuCommentItem>>{

        //后台线程获取评论，存入list数组中。两个数组，一个长评论，一个段评论
        @Override
        protected List<ZhiHuCommentItem> doInBackground(Void... params) {

            mZhihuLongCommentList =
                    new ZhiHuConnect().sendUrl(
                            getActivity(),
                            ZhiHuConnect.ZhiHuUrl.getLongCommentUrl(mNewsItem.getId()),
                            ZhiHuConnect.KIND_NEWS_COMMENT,null);

            List<ZhiHuCommentItem> shortCommentList =
                    new ZhiHuConnect().sendUrl(
                            getActivity(),
                            ZhiHuConnect.ZhiHuUrl.getShoreCommentUrl(mNewsItem.getId()),
                            ZhiHuConnect.KIND_NEWS_COMMENT,null);

            mZhihuLongCommentList.addAll(shortCommentList);

            return mZhihuLongCommentList;
        }
        //更新ui
        @Override
        protected void onPostExecute(List<ZhiHuCommentItem> list) {
            super.onPostExecute(list);
            setUpAdapter(list);
        }
    }
    /**
     * 配置adapter
     * */
    private void setUpAdapter(List<ZhiHuCommentItem> list){
        if (mCommentAdapter == null){
            mCommentAdapter = new ZhiHuCommentAdapter(list,getActivity());
            mCommentRecyclerView.setAdapter(mCommentAdapter);
        }else {
            mCommentAdapter.setList(list);
            mCommentAdapter.notifyDataSetChanged();
        }
    }
}
