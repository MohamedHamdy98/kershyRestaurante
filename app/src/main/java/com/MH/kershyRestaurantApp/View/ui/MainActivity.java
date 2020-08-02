package com.MH.kershyRestaurantApp.View.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.MH.kershyRestaurantApp.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.imageSplash)
    ImageView imageSplash;
    @BindView(R.id.textSplash)
    TextView textSplash;
    @BindView(R.id.motionLayout)
    LinearLayout motionLayout;
    Animation topAnim, bottomAnim;
    private static int SPLASH_SCREEN = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeNoActionBar);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        startAnimation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadLanguage();
        timeSplash();
    }

    public void loadLanguage() {
        SharedPreferences shp = getSharedPreferences(
                "CommonPrefs", Context.MODE_PRIVATE);
        String language = shp.getString("language", "");
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void startAnimation() {
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        imageSplash.setAnimation(topAnim);
        textSplash.setAnimation(bottomAnim);
    }
    private void timeSplash() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(SPLASH_SCREEN);
                    Intent i = new Intent(MainActivity.this, LogInActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}