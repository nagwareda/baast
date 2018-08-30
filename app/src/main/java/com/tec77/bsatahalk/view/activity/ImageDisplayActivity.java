package com.tec77.bsatahalk.view.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.loader.glide.GlideImageLoader;
import com.github.piasy.biv.view.BigImageView;
import com.squareup.picasso.Picasso;
import com.tec77.bsatahalk.R;

public class ImageActivity extends BaseActivity {

    private PhotoView img;
    private String urlImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        BigImageViewer.initialize(GlideImageLoader.with(getApplicationContext()));
        urlImg = getIntent().getStringExtra("imgUrl");
        img = findViewById(R.id.ImageActivity_ImageView);
        Glide.with(this)
                .load(urlImg)
                .into(img);
//        img.showImage(Uri.parse(urlImg));
//        img.setInitScaleType(BigImageView.INIT_SCALE_TYPE_CENTER_CROP);

    }
}
