package com.caitou.recyclerviewrefresh;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * @className:
 * @classDescription:
 * @Author: Guangzhao Cai
 * @createTime: 2016-09-23.
 */

public class NewMainActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private List<String> mList;

    private EditText editDataEtv;

    private Button addItemBtn;
    private Button removeItemBtn;
    private Button changeListViewBtn;
    private Button changeGridViewBtn;
    private Button changeWaterfallBtn;

    private boolean isFirstView = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        findViewById();
        initData();

        mAdapter = new RecyclerViewAdapter(this, mList);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setListener(new RecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(NewMainActivity.this, "点击了Item " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(NewMainActivity.this, "长按了Item " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemSubViewClick(TextView textView, int position) {

            }
        });

    }

    private void findViewById() {
        mRecyclerView = (RecyclerView) findViewById(R.id.ly_recycler_view);

        addItemBtn = (Button) findViewById(R.id.add_item_btn);
        removeItemBtn = (Button) findViewById(R.id.remove_item_btn);
        changeListViewBtn = (Button) findViewById(R.id.change_listView_btn);
        changeGridViewBtn = (Button) findViewById(R.id.change_gridView_btn);
        changeWaterfallBtn = (Button) findViewById(R.id.change_waterfall_btn);

        addItemBtn.setOnClickListener(this);
        removeItemBtn.setOnClickListener(this);
        changeListViewBtn.setOnClickListener(this);
        changeGridViewBtn.setOnClickListener(this);
        changeWaterfallBtn.setOnClickListener(this);
    }

    private void initData() {
        mList = new ArrayList<>();
        for (int i = 'A'; i <= 'Z'; ++i) {
            mList.add("" + (char)i);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_item_btn:
                mAdapter.addItem(mList.size(), "hehe");
                break;
            case R.id.remove_item_btn:
                mAdapter.removeItem(1);
                break;
            case R.id.change_listView_btn:
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                if (isFirstView) {
                    isFirstView = false;
                    onClick(findViewById(R.id.ly_recycler_view));
                }
                break;
            case R.id.change_gridView_btn:
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                break;
            case R.id.change_waterfall_btn:
                mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                        StaggeredGridLayoutManager.VERTICAL));
                break;
        }
    }
}
