package com.caitou.recyclerviewrefresh;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private LinearLayoutManager mLayoutManager;

    private MyAdapter mAdapter;

    private Button mAddBtn;
    private EditText mDataEt;

    private List<String> mData;

    private int itemPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.ly_refresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.rl_list);
        mAddBtn = (Button) findViewById(R.id.add_data_btn);
        mDataEt = (EditText) findViewById(R.id.data_et);

        mLayoutManager = new LinearLayoutManager(this);

        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 设置Item增加，删除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // 添加分割线

//        initData();
        mData = new ArrayList<>();


        mAdapter = new MyAdapter(this, mData, new MyAdapter.AdapterListener() {
            @Override
            public void loadMore() {
                // 加载更多
                loadMoreData();
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                System.out.println("xxxxxxxxxx onRefresh");
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(500);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mDataEt.getText().toString().equals("")) {
                                        mSwipeRefreshLayout.setRefreshing(false);
                                        return;
                                    }
                                    addData(mDataEt.getText().toString());
                                    mAdapter.resetData(mData);
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * 滚动状态变化时回调
             * param1:recyclerView, 当前在滚动的RecyclerView
             * param2:newState, 当前滚动状态
             * */
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                System.out.println("xxxxxxxxx onScrollStateChanged");
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && itemPosition + 1 == mAdapter.getItemCount()) {
                    // 加载更多
                    loadMoreData();
                }
            }

            // 滚动时回调
            /** 滚动时回调
             *  param1: recyclerView, 当前在滚动的RecyclerView
             *  param2: dx, 水平滚动距离
             *  param3: dy，垂直滚动距离
             * */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                System.out.println("xxxxxxxxx onScrolled");
                itemPosition = mLayoutManager.findFirstVisibleItemPosition();
                System.out.println("xxxxxxxxx onScrolled itemPosition = " + itemPosition);
            }
        });

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDataEt.getText().toString().equals(""))
                    return;
                mAdapter.addItem(mDataEt.getText().toString());
                mDataEt.setText("");
            }
        });
    }

    public void loadMoreData() {
        mAdapter.setLoadStatus(MyAdapter.LoadStatus.LOADING_MODE);
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(500);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.addAll(mData);
                            mAdapter.setLoadStatus(MyAdapter.LoadStatus.LOADING_MODE);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    private void addData(String data) {
            mData.add(data);
    }

    private void initData() {
        for (int i = 'A'; i <= 'Z'; ++i) {
            mData.add("" + (char) i);
        }
    }
}
