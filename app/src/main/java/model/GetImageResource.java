package model;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android_chen.loadimages.MyApplication;

import org.json.JSONObject;

import java.util.ArrayList;

import bean.Photo;
import bean.PhotoArray;

/**
 * Created by android_chen on 2016/3/28.
 */
public class GetImageResource implements IGetImageResource {
    private ArrayList<String> urls = new ArrayList<>();
    private ArrayList<String> cacheQueue = new ArrayList<>(5);
    public static int START_PAGE = 1;
    private static int LOAD_SIZE = 5;

    @Override
    public ArrayList<String> getImageUrlsFromInternet(final String url, final OnLoadListener onLoadListener) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject o) {
                PhotoArray array = JSON.parseObject(o.toString(), PhotoArray.class);
                ArrayList<Photo> photos = array.getResults();
                for (int i = 0; i < photos.size(); i++) {
                    urls.add(photos.get(i).getUrl());
                }
                //监听器回掉
                if (onLoadListener != null) {
                    onLoadListener.onLoadSuccess(urls);
                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (onLoadListener != null) {
                            onLoadListener.onLoadFail();
                        }
                    }
                });
        MyApplication.getRequestQueue().add(request);
        return urls;
    }

    @Override
    public ArrayList<String> loadMoreUrlsFromInternet(final OnLoadListener onLoadListener) {
        if (cacheQueue.size() == 0) {
            final String url = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/" + (++START_PAGE);
            JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject o) {
                    PhotoArray array = JSON.parseObject(o.toString(), PhotoArray.class);
                    ArrayList<Photo> photos = array.getResults();
                    for (int i = 0; i < photos.size(); i++) {
                        if (i < LOAD_SIZE) {
                            urls.add(photos.get(i).getUrl());
                        }
                        if (i >= LOAD_SIZE) {
                            cacheQueue.add(photos.get(i).getUrl());
                        }
                    }
                    if (onLoadListener != null) {
                        onLoadListener.onLoadSuccess(urls);
                    }
                }
            },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            if (onLoadListener != null) {
                                onLoadListener.onLoadFail();
                            }
                        }
                    });
            MyApplication.getRequestQueue().add(request);
        } else {
            for (int i = 0; i < cacheQueue.size(); i++) {
                urls.add(cacheQueue.get(i));
            }
            cacheQueue.clear();
            if (onLoadListener != null) {
                onLoadListener.onLoadSuccess(urls);
            }
        }
        return null;
    }

    public interface OnLoadListener {
        void onLoadSuccess(ArrayList<String> urls);

        void onLoadFail();
    }
}
