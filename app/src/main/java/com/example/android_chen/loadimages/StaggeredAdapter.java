package com.example.android_chen.loadimages;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import defineview.EquleImage;

/**
 * Created by android_chen on 2016/3/27.
 */
public class StaggeredAdapter extends RecyclerView.Adapter<StaggeredAdapter.StaggeredViewHolder>{
    private LayoutInflater mInflater;
    private ArrayList<String> urls;
    private ArrayList<Integer> heights;
    private MyAdapter.OnItemClickListener mOnItemClickListener;
    private ImageLoader imageLoader = MyApplication.getImageLoader();
    private DisplayImageOptions mOptions;
    private Context context;
    private Activity mActivity;
    public StaggeredAdapter(Context context,ArrayList urls){
        mInflater = LayoutInflater.from(context);
        heights = new ArrayList<>();
        this.urls = urls;
        this.context = context;
        initData();
    }
    //初始化参数
    private void initData() {
        for(int i = 0;i < urls.size();i++){
            heights.add((int)(100+ Math.random()*300));
        }
        mOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory()
                .cacheOnDisc()
                .build();
    }

    @Override
    public StaggeredViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        StaggeredViewHolder staggeredViewHolder = new StaggeredViewHolder(mInflater.inflate(R.layout.item,viewGroup,false));
        return staggeredViewHolder;
    }

    @Override
    public void onBindViewHolder(StaggeredViewHolder staggeredViewHolder, final int i) {

        imageLoader.displayImage(urls.get(i),staggeredViewHolder.iv,mOptions);
        if(mOnItemClickListener != null){
            staggeredViewHolder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v,i);
                }
            });
            staggeredViewHolder.iv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(v,i);
                    return false;
                }
            });
        }
    }
    public void setmOnItemClickListener(MyAdapter.OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }
    @Override
    public int getItemCount() {
        return urls.size();
    }

    public static class StaggeredViewHolder extends RecyclerView.ViewHolder{
        private EquleImage iv;
        public StaggeredViewHolder(View itemView) {
            super(itemView);
            iv = (EquleImage) itemView.findViewById(R.id.photo);
        }
        public StaggeredViewHolder(ViewGroup parent, @LayoutRes int layout){
            super(LayoutInflater.from(parent.getContext()).inflate(layout,parent,false));
        }
    }
}
