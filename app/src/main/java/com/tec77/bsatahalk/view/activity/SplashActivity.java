package com.tec77.bsatahalk.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.database.SharedPref;

import java.util.ArrayList;
import java.util.HashMap;


public class SplashActivity extends BaseActivity implements ViewPagerEx.OnPageChangeListener {

    private SliderLayout mDemoSlider;
    private TextView startBtn;
    SharedPref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mDemoSlider =  findViewById(R.id.slider);
        startBtn = findViewById(R.id.start);
        pref = SharedPref.getInstance(this);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        if (pref.loggeedIn()) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        }

        //HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        ArrayList<Integer>img = new ArrayList<>();
        img.add(R.drawable.introduction);
        img.add(R.drawable.vedio);
        img.add(R.drawable.quiz);
        img.add(R.drawable.conclusion);
        img.add(R.drawable.top_ten_splash);
        img.add(R.drawable.feedback);

//        file_maps.put("1", R.drawable.introduction);
//        file_maps.put("2", R.drawable.vedio);
//        file_maps.put("3", R.drawable.quiz);
//        file_maps.put("4", R.drawable.conclusion);
//        file_maps.put("5", R.drawable.top_ten_splash);
//        file_maps.put("6", R.drawable.feedback);

      //  for (String name : file_maps.keySet()) {
            for(int i=0;i<img.size();i++){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(""+(i+1))
                    .image(img.get(i));

            mDemoSlider.addSlider(textSliderView);

        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);

    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }


    @Override
    public void onPageScrolled(int i, float v, int i1) {
      // Toast.makeText(this, i+"", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageSelected(int i) {
        Toast.makeText(this, i+"", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}

