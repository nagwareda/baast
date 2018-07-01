package com.tec77.bsatahalk.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.database.SharedPref;
import io.fabric.sdk.android.Fabric;

public class splashSliderActivity extends BaseActivity {
    SharedPref pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_slider);
        pref = SharedPref.getInstance(this);
        if (pref.loggeedIn()) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        }

        final ViewPager viewPager = (ViewPager) findViewById(R.id.SplashActivity_viewPager);
        ImagePagerAdapter adapter = new ImagePagerAdapter(this);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPager, true);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()

        {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                if (position == viewPager.getAdapter().getCount()-1) {
                    Intent intent = new Intent(splashSliderActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    private class ImagePagerAdapter extends PagerAdapter {
        private Context context;

        public ImagePagerAdapter(Context mContext) {
            this.context = mContext;

        }

        private int[] mImages = new int[]{
                R.drawable.introduction,
                R.drawable.vedio,
                R.drawable.quiz,
                R.drawable.conclusion,
                R.drawable.top_ten_splash,
                R.drawable.feedback
        };

        @Override
        public int getCount() {
            return mImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            // imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            if (position < getCount() - 1)
                imageView.setImageResource(mImages[position]);
            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }
}

