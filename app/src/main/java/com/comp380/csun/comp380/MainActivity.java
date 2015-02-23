package com.comp380.csun.comp380;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends ActionBarActivity {
    private static boolean mFirstRun = true;
    private static final int SPLASH_DURATION = 4000; // 4 milli?

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
                    startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                    overridePendingTransition(R.anim.abc_fade_out,R.anim.abc_fade_out);
                }
            }, SPLASH_DURATION);
        }
        else{
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
