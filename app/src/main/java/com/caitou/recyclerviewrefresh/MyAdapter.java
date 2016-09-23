package com.caitou.recyclerviewrefresh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * @className:
 * @classDescription:
 * @Author: Guangzhao Cai
 * @createTime: 2016-09-23.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LoadStatus mLoadStatus = LoadStatus.CLICK_LOAD_MODE;
    private static final int VIEW_TYPE_FOOTER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private List<String> mList;
    private Context mContext;
    private AdapterListener mListener;

    public MyAdapter(Context context, List<String> list, AdapterListener listener) {
        mContext = context;
        mList = list;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("xxxxxxxx onCreateViewHolder viewType = " + viewType);
        if (viewType == VIEW_TYPE_FOOTER) {
            return onCreateFooterViewHolder(parent, viewType);
        } else if (viewType == VIEW_TYPE_ITEM) {
            return onCreateItemViewHolder(parent, viewType);
        }
        return null;
    }

    private RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        System.out.println("xxxxxxx onCreateFooterViewHolder");
        View view = View.inflate(mContext, R.layout.footer_view, null);
        return new FooterViewHolder(view);

    }

    private RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        System.out.println("xxxxxxx onCreateItemViewHolder");
        View view = View.inflate(mContext, R.layout.item_layout, null);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_ITEM:
                onBindItemViewHolder(holder, position);
                break;
            case VIEW_TYPE_FOOTER:
                onBindFooterViewHolder(holder, position, mLoadStatus);
                break;
            default:
                break;
        }

    }

    private void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position, LoadStatus loadStatus) {
        FooterViewHolder viewHolder = (FooterViewHolder) holder;
        if (loadStatus == LoadStatus.CLICK_LOAD_MODE) {
            viewHolder.mLoadingLayout.setVisibility(View.GONE);
            viewHolder.mClickLoad.setVisibility(View.VISIBLE);
        } else if (loadStatus == LoadStatus.LOADING_MODE) {
            viewHolder.mLoadingLayout.setVisibility(View.VISIBLE);
            viewHolder.mClickLoad.setVisibility(View.GONE);
        }
    }

    private void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        viewHolder.mTextView.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    public void addAll(List<String> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(String data) {
        mList.add(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            // 最后一条为FooterView
            return VIEW_TYPE_FOOTER;
        }
        return VIEW_TYPE_ITEM;
    }

    public void resetData(List<String> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public void setLoadStatus(LoadStatus loadStatus) {
        this.mLoadStatus = loadStatus;
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mLoadingLayout;
        public TextView mClickLoad;

        public FooterViewHolder(View itemView) {
            super(itemView);
            mLoadingLayout = (LinearLayout) itemView.findViewById(R.id.loading_ly);
            mClickLoad = (TextView) itemView.findViewById(R.id.click_load_tv);
            mClickLoad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 加载更多
                    mListener.loadMore();
                }
            });
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.id_num);
        }
    }

    public enum LoadStatus {
        CLICK_LOAD_MODE, // 点击加载更多
        LOADING_MODE     // 正在加载更多
    }

    public interface AdapterListener {
        void loadMore();
    }
}
