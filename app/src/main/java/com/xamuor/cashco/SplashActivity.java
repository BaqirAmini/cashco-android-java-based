package com.xamuor.cashco;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.xamuor.cashco.cashco.R;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//      Hide actinoBar
        Objects.requireNonNull(getSupportActionBar()).hide();

//        Initialize imageview spashLogo
        ImageView splashLogo = findViewById(R.id.img_logo);

//        Initiate animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.transition);
        splashLogo.startAnimation(animation);
        final Intent intent = new Intent(this, LoginActivity.class);
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();
    }
}
