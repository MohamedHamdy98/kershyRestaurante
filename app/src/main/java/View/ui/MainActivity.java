package View.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testeverythingtwo.R;

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
        timeSplash();
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
                    Intent i = new Intent(MainActivity.this, CategoriesActivity.class);
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