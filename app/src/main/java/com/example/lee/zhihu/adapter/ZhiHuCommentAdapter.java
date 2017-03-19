package com.example.lee.zhihu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lee.zhihu.Modle.ZhiHuCommentItem;
import com.example.lee.zhihu.R;

import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by lee on 2017/3/12.
 */
public class ZhiHuCommentAdapter extends RecyclerView.Adapter<ZhiHuCommentAdapter.ZhiHuCommentHolder>{

    private Context mContext;
    private List<ZhiHuCommentItem> mCommentList = new ArrayList<>();

    public ZhiHuCommentAdapter(List<ZhiHuCommentItem> commentList, Context context){
        mContext = context;
        mCommentList = commentList;
    }

    public void setList(List<ZhiHuCommentItem> list){
        mCommentList = list;
    }

    @Override
    public ZhiHuCommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.zhi_hu_comment_item, parent, false);
        return new ZhiHuCommentHolder(view);
    }

    @Override
    public void onBindViewHolder(ZhiHuCommentHolder holder, int position) {
        ZhiHuCommentItem commentItem = mCommentList.get(position);
        holder.bindZhiHuComment(commentItem);
    }

    @Override
    public int getItemCount() {
        return mCommentList==null ? 0:mCommentList.size();
    }

    public class ZhiHuCommentHolder extends RecyclerView.ViewHolder {
        private ImageView mIcon;
        private TextView mName;
        private TextView mPopularity;
        private TextView mCommentContent;
        private TextView mDate;

        public ZhiHuCommentHolder(View itemView) {
            super(itemView);

            mIcon = (ImageView) itemView.findViewById(R.id.zhi_hu_comment_item_icon);
            mName = (TextView) itemView.findViewById(R.id.zhi_hu_comment_item_name);
            mPopularity = (TextView) itemView.findViewById(R.id.zhi_hu_comment_item_popularity);
            mCommentContent = (TextView) itemView.findViewById(R.id.zhi_hu_comment_item_comment_content);
            mDate = (TextView) itemView.findViewById(R.id.zhi_hu_comment_item_date);
        }

        public void bindZhiHuComment(ZhiHuCommentItem comment){
            //绑定头像
            Picasso.with(mContext)
                    .load(comment.getIconUrl())
                    .into(mIcon);
            //绑定用户名
            mName.setText(comment.getName());
            //绑定点赞数
            mPopularity.setText(comment.getPopularity());
            //绑定评论内容
            mCommentContent.setText(comment.getCommentContent());
            //绑定日期
            mDate.setText(comment.getDate());
        }
    }
}
