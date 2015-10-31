package com.goalninja.android;

import com.parse.Parse;
import com.parse.ParseCrashReporting;

import android.app.Application;

public class MyApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		// Enabling Local Datastore
		
		Parse.enableLocalDatastore(this);
		
		// Enabling Crash Reporting
		
		ParseCrashReporting.enable(this);
		
		Parse.initialize(this, "ad8FRGfmDrdr6Su9UTAVxRpxVGIJmDL2R7JKhW9e", "6kPHs843Arctq18HWrYvJadCE41wpUuP1nnGbZJE");
	}
	
}
