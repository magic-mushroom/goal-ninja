package com.goalninja.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SetAlarmsOnBoot extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context ctxt, Intent intent) {
		WakeIntentService.acquireStaticLock(ctxt);
		Log.d("boot_completed", "I have reached SetAlarmsOnBoot");
		
		Intent i = new Intent(ctxt, SetAlarmsOnBootService.class);
		ctxt.startService(i);
	}
	
}
