package com.checkedin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.checkedin.R;
import com.checkedin.utility.UserPreferences;

public class SplashActivity extends Activity {
    private Thread showScreen;
//    public static final boolean isDeveloperPreview = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        showScreen = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    Intent intent;
                    if (UserPreferences.fetchUserId(SplashActivity.this) != null) {
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                    } else {
                        intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                    }
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        showScreen.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        showScreen = null;
        finish();
    }

}
