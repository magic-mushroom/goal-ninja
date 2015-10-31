package com.goalninja.android;

import java.util.Calendar;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver{
	
	@Override
	public void onReceive(Context ctxt, Intent intent) {
		WakeIntentService.acquireStaticLock(ctxt);
		Intent i = new Intent(ctxt, AlarmService.class);
		i.putExtra("ID", intent.getIntExtra("ID", 0));
		i.putExtra("TITLE", intent.getStringExtra("TITLE"));
		i.putExtra("CATEGORY", intent.getStringExtra("CATEGORY"));
		i.putExtra("SCHEDULE", intent.getStringExtra("SCHEDULE"));
		i.putExtra("DATE", (Date) intent.getSerializableExtra("DATE"));
		i.putExtra("FREQDAYS", intent.getStringExtra("FREQDAYS"));
		i.putExtra("FREQTIME", intent.getStringExtra("FREQTIME"));
		i.putExtra("ALARMTIME", (Calendar) intent.getSerializableExtra("ALARMTIME"));
		i.putExtra("NEXTCHECKIN", (Calendar)intent.getSerializableExtra("NEXTCHECKIN"));
		ctxt.startService(i);
	}

}
