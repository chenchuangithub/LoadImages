package com.example.android_chen.loadimages;

import android.app.Application;
import android.os.Environment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

/**
 * Created by android_chen on 2016/3/27.
 */
public class MyApplication extends Application{
    private static ImageLoader mImageLoader;
    private File cacheDir;
    private static RequestQueue mRequestQueue;
    @Override
    public void onCreate() {
        super.onCreate();
        cacheDir = new File(Environment.getExternalStorageDirectory(),"imageloader/memoryCache");
        if(!cacheDir.exists()){
            cacheDir.mkdirs();
        }
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480,800)
                .threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCache(new UsingFreqLimitedMemoryCache(2*1024*1024))
                .memoryCacheSize(2*1024*1024)
                .discCacheSize(50*1024*1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .discCacheFileCount(100)
                .discCache(new UnlimitedDiscCache(cacheDir))
                .build();
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(configuration);
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }
    public static ImageLoader getImageLoader(){
        return mImageLoader;
    }
    public static RequestQueue getRequestQueue(){
        return mRequestQueue;
    }
}
