package com.example.android_chen.loadimages;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewStub;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import itemDecoration.DividerGridItemDecoration;
import model.GetImageResource;
import presenter.LoadImagesPresenter;


public class MainActivity extends AppCompatActivity implements IShowImagesView {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ViewStub mViewStub;
    private StaggeredAdapter mMyAdapter;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LoadImagesPresenter mPresenter = new LoadImagesPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showTipsAboutWifi();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mPresenter.loadMoreFromNetwork();
                }
            }
        });
        //如果高度固定,设置这个可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mViewStub = (ViewStub) findViewById(R.id.loading_layout);
        mViewStub.inflate();
        mProgressBar = (ProgressBar) findViewById(R.id.loading);
    }

    private void initData() {
        String url = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/" + GetImageResource.START_PAGE;
        mPresenter.firstGetImageResource(url);
    }

    @Override
    public void showNetworkDisconnect() {

    }

    @Override
    public void showTipsAboutWifi() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("初次使用网络会消耗一下流量,确认要下载吗?");
        builder.setTitle("注意!");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initView();
                initData();
            }
        });
        builder.setNegativeButton("否",null);
        builder.show();
    }

    @Override
    public void displayImages() {

    }

    @Override
    public void hideProgressbar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void toShowLayout(final ArrayList<String> urls) {
        mMyAdapter = new StaggeredAdapter(this, urls);
        mMyAdapter.setmOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //进入图片放大模式
                Intent intent = new Intent(MainActivity.this, ShowPicture.class);
                Bundle bundle = new Bundle();
                bundle.putString("memeryCacheUrl", urls.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                urls.remove(position);
                mMyAdapter.notifyItemRangeRemoved(position, urls.size());
            }
        });
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mMyAdapter);
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE://滑动结束

                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                }
            }
        });
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showLoadFail() {
        Toast.makeText(this, "load fail", Toast.LENGTH_LONG).show();
    }

    @Override
    public StaggeredAdapter getAdapter() {
        return mMyAdapter;
    }

    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }
}
