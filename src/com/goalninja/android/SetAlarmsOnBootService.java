package com.goalninja.android;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

@SuppressLint("SimpleDateFormat")
public class SetAlarmsOnBootService extends WakeIntentService {
	
	ArrayList<MyGoal> myList = new ArrayList<MyGoal>();
	int id, feedId, progress;
	String title, category, schedule, start_string, date_string, freq_days, freq_time, alarmtime_string, nextcheckin_string;
	Date start, date;
	Calendar alarmtime, nextcheckin;
	
	
	public SetAlarmsOnBootService() {
		super("SetAlarmsOnBootService");
	}
	
	protected void onHandleIntent(Intent i) {
		
		Log.d("boot_completed", "I reached SetAlarmsOnBootService as well");
		myList.clear();
		new SetAlarms().execute();
		
	}
	
	private class SetAlarms extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			
			GoalDatabase dbSingleton = GoalDatabase.getInstance(SetAlarmsOnBootService.this.getApplicationContext());
			SQLiteDatabase db = dbSingleton.getReadableDatabase();
			Cursor cursor = db.rawQuery("select * from GOALS where PROGRESS<>-1" , null);
			cursor.moveToFirst(); 
			
			if(cursor.moveToFirst()) {
			do {
				MyGoal row;
				id = cursor.getInt(0);
				feedId = cursor.getInt(1);
				title = cursor.getString(2);
				category = cursor.getString(3);
				schedule = cursor.getString(4);
				start_string = cursor.getString(5);
				date_string = cursor.getString(6);
				freq_days = cursor.getString(7);
				freq_time = cursor.getString(8);
				progress = cursor.getInt(9);
				alarmtime_string = cursor.getString(10);
				nextcheckin_string = cursor.getString(11);
				
				Log.d("db_read", String.valueOf(progress));
				
				String log = String.valueOf(id) + ","
							+ String.valueOf(feedId) + "," + title + ","
							+ category + "," + schedule + "," + start_string
							+ "," + date_string + "," + freq_days + ","
							+ freq_time + "," + progress; 
				Log.d("db_read", log);
				
				SimpleDateFormat sdf_db = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				start = new Date();
				try {
					start = sdf_db.parse(start_string);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				date = new Date();
				try {
					date = sdf_db.parse(date_string);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				alarmtime = Calendar.getInstance();
				try {
					alarmtime.setTime(sdf_db.parse(alarmtime_string));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				nextcheckin = Calendar.getInstance();
				try {
					nextcheckin.setTime(sdf_db.parse(nextcheckin_string));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				row = new MyGoal(id, feedId, title, category, schedule, start, date, freq_days, freq_time, progress, alarmtime, nextcheckin);
				
				Log.d("boot_alarm", id + "," + title + "," + alarmtime + "," + nextcheckin);
				
				myList.add(row);
				
			} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void args) {
			
			// If any goals are present, set alarms for each goal
			if (!myList.isEmpty()) {
				
				int n = myList.size();
				
				for (int i=0; i<n; i++) {
					
					Intent bootAlarmIntent = new Intent(SetAlarmsOnBootService.this, AlarmReceiver.class);
					bootAlarmIntent.putExtra("ID", myList.get(i).getid());
					bootAlarmIntent.putExtra("TITLE", myList.get(i).getTitle());
					bootAlarmIntent.putExtra("CATEGORY", myList.get(i).getCategory());
					bootAlarmIntent.putExtra("SCHEDULE", myList.get(i).getSchedule());
					bootAlarmIntent.putExtra("DATE", myList.get(i).getDate());
					bootAlarmIntent.putExtra("FREQDAYS", myList.get(i).getFreqDays());
					bootAlarmIntent.putExtra("FREQTIME", myList.get(i).getFreqTime());
					bootAlarmIntent.putExtra("ALARMTIME", myList.get(i).getAlarmTime());
					bootAlarmIntent.putExtra("NEXTCHECKIN", myList.get(i).getNextCheckin());
					
					PendingIntent pi2 = PendingIntent.getBroadcast(SetAlarmsOnBootService.this, myList.get(i).getid(), bootAlarmIntent, PendingIntent.FLAG_ONE_SHOT);

					AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
					alarmMgr.set(AlarmManager.RTC_WAKEUP, myList.get(i).getAlarmTime().getTimeInMillis(), pi2);
					
					Log.d("boot_alarm", "Set alarm for" + String.valueOf(myList.get(i).getid()));
					
				}
				
				Log.d("boot_alarm", String.valueOf(n) + " alarms set");			
				
			}
			
			
		}
		
	}
	
}
