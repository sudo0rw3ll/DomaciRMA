package com.dvvee.dnevnjakapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.bumptech.glide.Glide;
import com.dvvee.dnevnjakapp.MainActivity;
import com.dvvee.dnevnjakapp.R;
import com.dvvee.dnevnjakapp.fragments.LoginFragment;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        setContentView(R.layout.splash_activity);

        splashScreen.setKeepOnScreenCondition(() -> {
            try{
                Thread.sleep(4000);
                if(checkPreferences() == false) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.splash_container, new LoginFragment())
                            .commit();
                }else{
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }catch(InterruptedException e){
                throw new RuntimeException(e);
            }
            return false;
        });

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                checkPreferences();
//            }
//        }, SPLASH_TIME_OUT);
    }

    private boolean checkPreferences(){
        SharedPreferences preferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1);

        if(userId == -1){
            return false;
        }else {
            return true;
        }
    }
}
