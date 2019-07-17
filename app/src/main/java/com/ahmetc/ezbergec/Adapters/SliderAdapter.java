package com.ahmetc.ezbergec.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmetc.ezbergec.Activities.Root;
import com.ahmetc.ezbergec.R;

public class SliderAdapter extends PagerAdapter {

    private String[] Aciklama;
    private int Images[] = {R.drawable.tutorial1,R.drawable.tutorial2,R.drawable.tutorial3,R.drawable.tutorial4,R.drawable.tutorial5,R.drawable.tutorial5};
    private Drawable colorList[];
    private  Context context;
    public SliderAdapter(Context context) {
        this.context = context;
        colorList = new Drawable[]{context.getResources().getDrawable(R.color.renk1), context.getResources().getDrawable(R.color.renk2), context.getResources().getDrawable(R.color.renk3), context.getResources().getDrawable(R.color.renk4), context.getResources().getDrawable(R.color.renk5)};
        Aciklama = context.getResources().getStringArray(R.array.Aciklamalar);
    }
    @Override
    public int getCount() {
        return Aciklama.length;
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view==o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slider,container,false);
        ConstraintLayout sliderLayout = view.findViewById(R.id.sliderLayout);
        TextView slider_aciklama = view.findViewById(R.id.slider_aciklama);
        ImageView slider_resim = view.findViewById(R.id.slider_resim);
        FrameLayout sliderFrame = view.findViewById(R.id.sliderFrame);
        Button sliderButton = view.findViewById(R.id.sliderButton);
        ImageView sliderLogo = view.findViewById(R.id.sliderLogo);
        slider_aciklama.setText(Aciklama[position]);
        slider_resim.setImageResource(Images[position]);

        if(position == Aciklama.length -1) {
            slider_aciklama.setVisibility(View.INVISIBLE);
            slider_resim.setVisibility(View.INVISIBLE);
            sliderButton.setVisibility(View.VISIBLE);
            sliderLogo.setVisibility(View.VISIBLE);
            sliderFrame.setVisibility(View.VISIBLE);

        }
        else {
            slider_aciklama.setVisibility(View.VISIBLE);
            slider_resim.setVisibility(View.VISIBLE);
            sliderButton.setVisibility(View.INVISIBLE);
            sliderLogo.setVisibility(View.INVISIBLE);
            sliderFrame.setVisibility(View.INVISIBLE);
        }
        sliderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Root.class));
            }
        });
        sliderLayout.setBackground(colorList[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}