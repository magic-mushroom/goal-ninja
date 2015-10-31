package com.goalninja.android;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

public abstract class WakeIntentService extends IntentService{
	
	public static final String LOCK_NAME_STATIC = "com.example.newapp";
	private static PowerManager.WakeLock lockStatic = null;
	
	public WakeIntentService(String name) {
		super(name);
	}
	
	public static void acquireStaticLock(Context ctxt) {
		getLock(ctxt).acquire();
	}
	
	synchronized private static PowerManager.WakeLock getLock(Context ctxt) {
		if (lockStatic==null) {
			PowerManager powerMgr = (PowerManager)ctxt.getSystemService(Context.POWER_SERVICE);
			lockStatic = powerMgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOCK_NAME_STATIC);
			lockStatic.setReferenceCounted(true);
		}
		
		return lockStatic;
	}
	
	protected void onHandleIntent(Intent intent) {
		getLock(this).release();
	}
	
}
