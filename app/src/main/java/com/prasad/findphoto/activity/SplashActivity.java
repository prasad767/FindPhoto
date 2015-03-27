package com.prasad.findphoto.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import com.prasad.findphoto.R;
import com.prasad.findphoto.utils.Constans;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loadNextScreen();
    }

    private void loadNextScreen(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, SearchActivity.class));
                finish();
            }
        }, Constans.SPLASH_SCREEN_TIME);


    }

}
