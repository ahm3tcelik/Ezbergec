package com.ahmetc.ezbergec.Activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ahmetc.ezbergec.Adapters.SliderAdapter;
import com.ahmetc.ezbergec.R;

public class Slider extends AppCompatActivity {
    private TextView[] mDots;
    private LinearLayout mDotLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        viewPager = findViewById(R.id.viewPager);
        mDotLayout = findViewById(R.id.mDotLayout);
        SliderAdapter sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);
        viewPager.addOnPageChangeListener(viewListener);
        AddDots(0);
    }
    public void AddDots(int position) {

        mDots = new TextView[6];
        mDotLayout.removeAllViews();
        for(int i=0;i<mDots.length;i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.divider));
            mDotLayout.addView(mDots[i]);
        }
        if(mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.icons));
        }
    }
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) { }

        @Override
        public void onPageSelected(int i) { AddDots(i); }

        @Override
        public void onPageScrollStateChanged(int i) {}
    };

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}