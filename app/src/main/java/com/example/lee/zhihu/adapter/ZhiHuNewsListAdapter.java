package com.example.lee.zhihu.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.lee.zhihu.Modle.ZhiHuNewsItem;
import com.example.lee.zhihu.R;

import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by lee on 2017/3/9.
 */
public class ZhiHuNewsListAdapter extends
        BaseQuickAdapter<ZhiHuNewsItem,BaseViewHolder>{

    public ZhiHuNewsListAdapter(List<ZhiHuNewsItem> list){
        super(R.layout.zhi_hu_news_list_item,list);
    }
    @Override
    protected void convert(BaseViewHolder helper, ZhiHuNewsItem item) {
        helper.setText(R.id.zhi_hu_list_item_text_view,item.getTitle());
        Picasso.with(mContext)
                .load(item.getImageUrl())
                .into((ImageView) helper.getView(R.id.zhi_hu_list_item_image_view));
    }
}
