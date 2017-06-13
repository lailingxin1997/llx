package com.example.lenovo.hutu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by molly on 2017/5/12.
 */

public class MyAdapter extends BaseAdapter {

    private List<ItemBean> mlist;
    private LayoutInflater mInflater;
    private Context mContext;
    public MyAdapter(Context context, List<ItemBean> list){
        mlist=list;
        mContext=context;
        mInflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=mInflater.inflate(R.layout.list_item,null);
            viewHolder.Title=(TextView)convertView.findViewById(R.id.tv_title);
            viewHolder.Cost=(TextView)convertView.findViewById(R.id.tv_cost);
            viewHolder.Date=(TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.Cate=(TextView) convertView.findViewById(R.id.tv_category);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        ItemBean bean=mlist.get(position);
        viewHolder.Title.setText(bean.ItemTitle);
        viewHolder.Cost.setText(bean.ItemCost);
        viewHolder.Date.setText(bean.ItemDate);
        viewHolder.Cate.setText(bean.ItemCate);

        return convertView;
    }

    public static class ViewHolder{
        public TextView Title;
        public TextView Cost;
        public TextView Date;
        public TextView Cate;

    }
}
