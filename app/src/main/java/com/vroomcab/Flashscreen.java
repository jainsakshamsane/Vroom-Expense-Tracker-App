package com.vroomcab;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class Flashscreen extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_TIME = 2500;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashscreen);

        logo = findViewById(R.id.image);

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(logo, "alpha", 0f, 1f);
        alphaAnimator.setDuration(1500);

        ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat(logo, "scaleX", 0.5f, 1f);
        ObjectAnimator scaleAnimatorY = ObjectAnimator.ofFloat(logo, "scaleY", 0.5f, 1f);
        scaleAnimatorX.setDuration(1500);
        scaleAnimatorY.setDuration(1500);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(alphaAnimator, scaleAnimatorX, scaleAnimatorY);

        animatorSet.start();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(Flashscreen.this, welcomepage.class);
                startActivity(intent); // Start the welcomepage activity
                finish(); // Finish the current activity if needed
            }
        }, SPLASH_DISPLAY_TIME);
    }
}
