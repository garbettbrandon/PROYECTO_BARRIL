package com.example.barril.ui.splashscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.barril.R;
import com.example.barril.ui.auth.LogIn;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.w3c.dom.Text;

public class SplashScreen extends AppCompatActivity {

    TextView idTituloPaincipal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        idTituloPaincipal = findViewById(R.id.idTituloPrincipal);

        //rotar imagen
        ImageView imageView = (ImageView) findViewById(R.id.id_icon_splash);
        imageView.setImageResource(R.drawable.icon_splash);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotar360);
        imageView.startAnimation(animation);

        //desaparecer titulo
        new CountDownTimer(3500, 3500) {
            public void onTick(long millisUntilFinished) {
                // No necesitas hacer nada en cada tick
            }

            public void onFinish() {
                idTituloPaincipal.setVisibility(View.INVISIBLE);
            }
        }.start();

        //Analytics event
        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString("message", "Integración completa");
        analytics.logEvent("InitScreen", bundle);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashScreen.this, LogIn.class);
                startActivity(intent);
                finish();
            }
        }, 4200);
    }
}