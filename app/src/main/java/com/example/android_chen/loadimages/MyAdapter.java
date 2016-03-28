package com.example.android_chen.loadimages;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by android_chen on 2016/3/26.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view ,int position);
    }
    private List<String> urls;
    private Context context;
    private OnItemClickListener mOnItemClickListener;
    public MyAdapter(List<String> urls,Context context){
        this.urls = urls;
        this.context = context;
    }
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item,parent,false));
        return viewHolder;
    }

    public void setmOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }
    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {
         holder.iv.setImageResource(R.mipmap.ic_launcher);
         if(mOnItemClickListener != null){
             holder.iv.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     mOnItemClickListener.onItemClick(v,position);
                 }
             });
             holder.iv.setOnLongClickListener(new View.OnLongClickListener() {
                 @Override
                 public boolean onLongClick(View v) {
                     mOnItemClickListener.onItemLongClick(v,position);
                     return true;
                 }
             });
         }
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView iv;
        public ViewHolder(View view){
            super(view);
            this.iv =  (ImageView) view.findViewById(R.id.photo);
        }
    }
}
