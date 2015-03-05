package com.comp380.csun.comp380;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

/**
 * Class that handles the application's start up.
 * This Activity is called upon the application's onResume
 * to handle the pin lock feature and interfaces with the
 * database. Upon first run the application checks the
 * database for a password hash, if there is one the
 * appropriate activity is launched, otherwise a different
 * activity is called which would prompt the user to create
 * a new password.
 */

public class SplashActivity extends ActionBarActivity {
    private static final int SPLASH_DURATION = 2000; // 2 milli?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHandler db = new DatabaseHandler(this,null,null,1);
        // no password, prompt user for one
        if(db.hasPassword() == false) {
            setContentView(R.layout.activity_splash);
            new Handler().postDelayed(new Runnable() {
                public void run(){
                    finish();
                    // this activity call the WelcomeActivity
                    startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                    overridePendingTransition(R.anim.activityfadein,R.anim.splashfadeout);
                }
            }, SPLASH_DURATION);
        }
        // password exists, force user to login.
        else{
            finish();
            // start the login activity.
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}