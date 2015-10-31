package com.goalninja.android;

import com.goalninja.android.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {
	
	// Splash screen timer
    private static int SPLASH_TIMER = 3000;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.splash_layout);
        
//        ActionBar actionbar = getActionBar();
//        actionbar.hide();
        
        new Handler().postDelayed(new Runnable() {
 
            @Override
            public void run() {

            	// starting MainActivity from here
                Intent i = new Intent(Splash.this, MainActivity.class);
                startActivity(i);
 
                // close this activity
                finish();
            }
        }, SPLASH_TIMER);
	}
	
}
