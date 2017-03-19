package com.example.lee.zhihu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lee.zhihu.Modle.ZhiHuThemeItem;
import com.example.lee.zhihu.R;

import java.util.List;

/**
 * Created by lee on 2017/3/14.
 */
public class ZhiHuThemeListViewAdapter extends ArrayAdapter<ZhiHuThemeItem>{
    private int mResId;

    public ZhiHuThemeListViewAdapter(Context context, int resource, List<ZhiHuThemeItem> objects) {
        super(context, resource, objects);
        mResId = resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ZhiHuThemeItem item = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView==null){
            view = LayoutInflater.from(getContext()).inflate(mResId,null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) view.findViewById(R.id.zhi_hu_theme_list_text_view);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mTextView.setText(item.getThemeName());
        return view;
    }
    class ViewHolder{
        TextView mTextView;
    }
}
