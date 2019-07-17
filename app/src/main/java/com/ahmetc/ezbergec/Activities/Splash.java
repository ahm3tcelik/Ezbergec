package com.ahmetc.ezbergec.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.ahmetc.ezbergec.R;

public class Splash extends AppCompatActivity {
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_splash);
         Animation alphaAnimation = AnimationUtils.loadAnimation(Splash.this, R.anim.alpha);
         Animation alpha_reverse = AnimationUtils.loadAnimation(Splash.this, R.anim.alpha_reverse);
         preferences = getSharedPreferences("EzBerGec",MODE_PRIVATE);
         ImageView splashLogo = findViewById(R.id.splashLogo);
         TextView splashBaslik = findViewById(R.id.splashBaslik);
         TextView splashYazi = findViewById(R.id.splashYazi);
         TextView splashCopy = findViewById(R.id.splashCopy);
         splashLogo.setAnimation(alphaAnimation);
         splashBaslik.setAnimation(alphaAnimation);
         splashYazi.setAnimation(alphaAnimation);
         splashCopy.setAnimation(alpha_reverse);



        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    if(preferences.getBoolean("First",true)) {
                        startActivity(new Intent(Splash.this, Slider.class));
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("First",false);
                        editor.apply();
                    }
                    else {
                        startActivity(new Intent(Splash.this, Root.class));
                    }
                    finish();
                }
            }
        };
        timerThread.start();

    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
