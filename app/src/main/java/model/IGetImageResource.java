package model;

import java.util.ArrayList;

import bean.Photo;

/**
 * Created by android_chen on 2016/3/28.
 */
public interface IGetImageResource {
    ArrayList<String> getImageUrlsFromInternet(String url, GetImageResource.OnLoadListener onLoadListener);
    ArrayList<String> loadMoreUrlsFromInternet(GetImageResource.OnLoadListener onLoadListener);
}
