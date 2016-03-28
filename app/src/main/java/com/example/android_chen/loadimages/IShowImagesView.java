package com.example.android_chen.loadimages;

import android.support.v4.widget.SwipeRefreshLayout;

import java.util.ArrayList;

/**
 * Created by android_chen on 2016/3/28.
 */
public interface IShowImagesView {
    void showNetworkDisconnect();
    void showTipsAboutWifi();
    void displayImages();
    void hideProgressbar();
    void toShowLayout(ArrayList<String> urls);
    void showLoadFail();
    StaggeredAdapter getAdapter();
    SwipeRefreshLayout getSwipeRefreshLayout();
}
