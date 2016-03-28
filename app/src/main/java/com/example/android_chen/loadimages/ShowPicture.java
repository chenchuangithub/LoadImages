package com.example.android_chen.loadimages;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by android_chen on 2016/3/27.
 */
public class ShowPicture extends Activity{
    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showpicture);
        mImageView = (ImageView) findViewById(R.id.picture);
        Intent intent = getIntent();
        String url = intent.getExtras().getString("memeryCacheUrl");
        mImageView.setImageBitmap(BitmapFactory.decodeFile(MyApplication.getImageLoader().getDiscCache().get(url).toString()));
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }
}
