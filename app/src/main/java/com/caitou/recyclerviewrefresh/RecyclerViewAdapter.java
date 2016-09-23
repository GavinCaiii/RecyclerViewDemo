package com.caitou.recyclerviewrefresh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @className:
 * @classDescription:
 * @Author: Guangzhao Cai
 * @createTime: 2016-09-23.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{
    private List<String> mList;
    private Context mContext;
    private ItemClickListener mListener;
    private List<Integer> mHeightList;

    public RecyclerViewAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
        getRandomHeight(mList);
    }

    public void setListener(ItemClickListener listener) {
        mListener = listener;
    }

    // 得到随机的Item的高度
    private void getRandomHeight(List<String> list) {
        mHeightList = new ArrayList<>();
        for (int i = 0; i < list.size(); i ++) {
            mHeightList.add((int) (200 + Math.random()) * 400);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.item_layout, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // 得到item的layoutParams的布局参数
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        // 把随机的高度赋予item布局
        params.height = mHeightList.get(position);
        // 把params设置item布局
//        holder.itemView.setLayoutParams(params);

        holder.textView.setText(mList.get(position));

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onItemSubViewClick(holder.textView, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public MyViewHolder(final View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.id_num);
            // 为item添加普通点击回调
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null)
                        mListener.onItemClick(itemView, getPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mListener != null)
                        mListener.onItemLongClick(itemView, getPosition());
                    return true;
                }
            });
        }
    }

    public void addItem(int position, String data) {
        mList.add(position, data);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
        void onItemSubViewClick(TextView textView, int position);
    }
}
