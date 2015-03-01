package com.comp380.csun.comp380;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;


public class SplashActivity extends ActionBarActivity {
    private static final int SPLASH_DURATION = 2000; // 2 milli?

//    DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHandler db = new DatabaseHandler(this,null,null,1);

        //delete database when needed
        //this.deleteDatabase("expenseTracker.db");

        // no password, prompt user for one
        if(db.hasPassword() == false) {
            setContentView(R.layout.activity_splash);
            new Handler().postDelayed(new Runnable() {
                public void run(){
                    finish();
                    startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                    overridePendingTransition(R.anim.activityfadein,R.anim.splashfadeout);
                }
            }, SPLASH_DURATION);
        }
        else{
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
