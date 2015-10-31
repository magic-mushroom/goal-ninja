package com.goalninja.android;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.goalninja.android.R;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

@SuppressLint("SimpleDateFormat")
public class AlarmService extends WakeIntentService {
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
	int id;
	String title, category, schedule, freq_days, freq_time, date_string;
	Date date;
	Calendar alarmtime, nextCheckin;
		
	public AlarmService() {
		super("AlarmService");
	}
	
	protected void onHandleIntent(Intent i) {
		
		id = i.getIntExtra("ID", 0);
		title = i.getStringExtra("TITLE");
		category = i.getStringExtra("CATEGORY");
		schedule = i.getStringExtra("SCHEDULE");
		freq_days = i.getStringExtra("FREQDAYS");
		freq_time = i.getStringExtra("FREQTIME");
		date = (Date) i.getSerializableExtra("DATE");
		alarmtime = (Calendar) i.getSerializableExtra("ALARMTIME");
		String prevalarm_time = sdf.format(alarmtime.getTime());
		
		nextCheckin = (Calendar) i.getSerializableExtra("NEXTCHECKIN");
		
		
//		GoalDatabase dbsingleton = GoalDatabase.getInstance(getApplicationContext());
//		SQLiteDatabase db = dbsingleton.getReadableDatabase();
//		
//		Cursor cursor = db.rawQuery("select * from GOALS where ID = " + id , null);
//		cursor.moveToFirst();
//
//		if (cursor.moveToFirst()) {
//			do {
//				
//				date_string = cursor.getString(6);
//				freq_days = cursor.getString(7);
//				freq_time = cursor.getString(8);			
//
//			} while (cursor.moveToNext());
//		}
//		cursor.close();
//		
//		db.close();
//		
//		date = new Date();
//		try {
//			date = sdf.parse(date_string);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
		
		
		
		// Show notification only if "notif_yes" is true 
		SharedPreferences sharedPref = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
		
		Boolean notif_yes = sharedPref.getBoolean("notif_yes", true);
		Boolean morning = sharedPref.getBoolean("morning", true);
		Boolean afternoon = sharedPref.getBoolean("afternoon", true);
		Boolean evening = sharedPref.getBoolean("evening", true);
		Boolean night = sharedPref.getBoolean("night", true);
		
		Calendar now = Calendar.getInstance();
//		// No notification if "nextcheckin" date is in future (user already checked in)
//		
//		int checkinAllowed;
//		if ((now.get(Calendar.YEAR) - nextCheckin.get(Calendar.YEAR)) > 0) {
//			checkinAllowed = 1;
//		}
//		else {
//			if ((now.get(Calendar.YEAR)- nextCheckin.get(Calendar.YEAR)) < 0) {
//				checkinAllowed = -1;
//			}
//			else {
//				checkinAllowed = now.get(Calendar.DAY_OF_YEAR) - nextCheckin.get(Calendar.DAY_OF_YEAR);
//			}
//			
//		}
		
		// No notification if alarmtime is already passed
		int alarmtimePast;
		if ((now.get(Calendar.YEAR) - alarmtime.get(Calendar.YEAR)) > 0) {
			alarmtimePast = -1;
		}
		else {
			if ((now.get(Calendar.YEAR) - alarmtime.get(Calendar.YEAR)) < 0) {
				alarmtimePast = 1;
			}
			else {
				alarmtimePast = alarmtime.get(Calendar.DAY_OF_YEAR) - now.get(Calendar.DAY_OF_YEAR);
			}
		}
		
		
		if (alarmtimePast >=0) {

			if (notif_yes && (morning || afternoon || evening || night)) {
				NotificationManager mgr = (NotificationManager) this
						.getSystemService(NOTIFICATION_SERVICE);
				Intent notifIntent = new Intent(this, UpdateProgress.class);

				notifIntent.putExtra("id", id);
				notifIntent.putExtra("schedule", schedule);
				notifIntent.putExtra("category", category);

				// Put Extra to notify UpdateProgress to show Dialog
				if (schedule.equalsIgnoreCase("Repeat")) {
					switch (freq_days) {
					case ("Daily"):
						notifIntent.putExtra("fromAlarm", "daily");
						break;

					case ("Weekdays"):
						// Dialog on Fridays
						if (alarmtime.get(Calendar.DAY_OF_WEEK) == 6) {
							notifIntent.putExtra("fromAlarm", "friday");
						} else {
							notifIntent.putExtra("fromAlarm", "yes");
						}
						break;

					case ("Weekends"):
						// Dialog on Sundays
						if (alarmtime.get(Calendar.DAY_OF_WEEK) == 1) {
							notifIntent.putExtra("fromAlarm", "sunday");
						} else {
							notifIntent.putExtra("fromAlarm", "yes");
						}
						break;

					case ("Once a week"):
						// Dialog on Saturdays
						if (alarmtime.get(Calendar.DAY_OF_WEEK) == 7) {
							notifIntent.putExtra("fromAlarm", "saturday");
						} else {
							notifIntent.putExtra("fromAlarm", "yes");
						}
						break;

					case ("Fortnightly"):
						// Dialog on 12th and 28th of month
						if (alarmtime.get(Calendar.DAY_OF_MONTH) == 12
								|| alarmtime.get(Calendar.DAY_OF_MONTH) == 28) {
							notifIntent.putExtra("fromAlarm", String
									.valueOf(alarmtime
											.get(Calendar.DAY_OF_MONTH)));
						} else {
							notifIntent.putExtra("fromAlarm", "yes");
						}
						break;

					case ("Monthly"):
						// Dialog on 25th of month
						if (alarmtime.get(Calendar.DAY_OF_MONTH) == 25) {
							notifIntent.putExtra("fromAlarm", "25");
						} else {
							notifIntent.putExtra("fromAlarm", "yes");
						}
						break;

					}

				} else {
					if (freq_days.equalsIgnoreCase("N/A")) {
						notifIntent.putExtra("fromAlarm", "end");
					} else {
						notifIntent.putExtra("fromAlarm", "yes");
					}
				}

				// notifIntent.putExtra("schedule", schedule);
				// if (freq_days.equalsIgnoreCase("N/A")) {
				// notifIntent.putExtra("end", "yes");
				// }
				// else {
				// notifIntent.putExtra("end", "no");
				// }

				Log.d("from_alarm", String.valueOf(id) + "," + schedule);

				PendingIntent pi = PendingIntent.getActivity(this, id,
						notifIntent, PendingIntent.FLAG_ONE_SHOT);
				
//				Uri notifSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
				
				Notification notif = new NotificationCompat.Builder(this)
						.setContentTitle(this.getString(R.string.app_name))
						.setContentText(title)
						.setContentIntent(pi)
						.setSmallIcon(R.drawable.logo_notif).setAutoCancel(true)
						.build();
				
//				notif.defaults |= Notification.DEFAULT_SOUND;
//				notif.defaults |= Notification.DEFAULT_VIBRATE;
				notif.defaults |= Notification.DEFAULT_ALL;

				int ID = id;
				mgr.notify(ID, notif);
			}

		}
		

		Calendar end_date = Calendar.getInstance();
		end_date.setTime(date);
		
		Long next_alarm_time = (long) 0;
		
		if (schedule.equalsIgnoreCase("Repeat")) {
			
			// For Habits, just change the Alarm date as per "freq_days"
			switch(freq_days) {
			case ("Daily"):
				alarmtime.set(Calendar.DAY_OF_YEAR, alarmtime.get(Calendar.DAY_OF_YEAR) + 1);
				break;
			
			case ("Weekdays"):
				
				alarmtime.set(Calendar.DAY_OF_YEAR, alarmtime.get(Calendar.DAY_OF_YEAR) + 1);
			
				// If Saturday, make it Monday
				if (alarmtime.get(Calendar.DAY_OF_WEEK)==7) {
					alarmtime.set(Calendar.DAY_OF_YEAR, alarmtime.get(Calendar.DAY_OF_YEAR) + 2);
				}
				break;
			
			case ("Weekends"):
				
				alarmtime.set(Calendar.DAY_OF_YEAR, alarmtime.get(Calendar.DAY_OF_YEAR) + 1);
				
				// If Monday, make it Saturday
				if (alarmtime.get(Calendar.DAY_OF_WEEK)==2) {
					alarmtime.set(Calendar.DAY_OF_WEEK, 7);
				}
				
				if (alarmtime.compareTo(now)<=0){
					alarmtime.set(Calendar.DAY_OF_YEAR, alarmtime.get(Calendar.DAY_OF_YEAR) + 7);
				}
				break;
				
			case ("Once a week"):

				// Set for Wednesday or Saturday, whichever is next
				if (alarmtime.get(Calendar.DAY_OF_WEEK) == 4) {
					alarmtime.set(Calendar.DAY_OF_WEEK, 7);
				}
				else {
					alarmtime.set(Calendar.DAY_OF_WEEK, 4);
				}
				
				if (alarmtime.compareTo(now) <= 0) {
					alarmtime.set(Calendar.DAY_OF_YEAR, alarmtime.get(Calendar.DAY_OF_YEAR) + 7);
				}
				break;
				
			case ("Fortnightly"):
				
				int day_of_month = alarmtime.get(Calendar.DAY_OF_MONTH);
			
				// Set for 7th and 12th of the month
				if (day_of_month ==7) {
					alarmtime.set(Calendar.DAY_OF_MONTH, 12);
				}
				else if (day_of_month ==12) {
					alarmtime.set(Calendar.DAY_OF_MONTH, 21);
				}
				else if (day_of_month ==21) {
					alarmtime.set(Calendar.DAY_OF_MONTH, 28);
				}
				else {
					alarmtime.set(Calendar.MONTH, alarmtime.get(Calendar.MONTH) + 1);
					alarmtime.set(Calendar.DAY_OF_MONTH, 7);
				}
				break;
				
			case ("Monthly"):
				int day_of_month2 = alarmtime.get(Calendar.DAY_OF_MONTH);
				
				// Set to 15th or 25th of the month
				if (day_of_month2 ==15) {
					alarmtime.set(Calendar.DAY_OF_MONTH, 25);				
				}
				else {
					alarmtime.set(Calendar.MONTH, alarmtime.get(Calendar.MONTH) + 1);
					alarmtime.set(Calendar.DAY_OF_MONTH, 15);
				}
				break;
			}
		}
		
		else {
			
			// For Goals, change the date as per "freq_days" and time as per "freq_time" - don't forget to check the end_date
			
			
						
			// Setting time for alarm
			if (freq_time.equalsIgnoreCase("Morning")) {
				next_alarm_time = sharedPref.getLong("morn_time", 0);
				if (afternoon) {
					next_alarm_time = sharedPref.getLong("aft_time", 0);
					freq_time = "Afternoon";
				}
				else if (evening) {
					next_alarm_time = sharedPref.getLong("eve_time", 0);
					freq_time = "Evening";
				}
				else if (night) {
					next_alarm_time = sharedPref.getLong("night_time", 0);
					freq_time = "Night";
				}
				else {
					// Do nothing
				}
			}
			
			else if (freq_time.equalsIgnoreCase("Afternoon")) {
				next_alarm_time = sharedPref.getLong("aft_time", 0);
				if (evening) {
					next_alarm_time = sharedPref.getLong("eve_time", 0);
					freq_time = "Evening";
				}
				else if (night) {
					next_alarm_time = sharedPref.getLong("night_time", 0);
					freq_time = "Night";
				}
				else if (morning) {
					next_alarm_time = sharedPref.getLong("morn_time", 0);
					freq_time = "Morning";
				}
				else {
					// Do Nothing
				}
			}
			
			else if (freq_time.equalsIgnoreCase("Evening")) {
				next_alarm_time = sharedPref.getLong("eve_time", 0);
				if (night) {
					next_alarm_time = sharedPref.getLong("night_time", 0);
					freq_time = "Night";
				}
				else if (morning) {
					next_alarm_time = sharedPref.getLong("morn_time", 0);
					freq_time = "Morning";
				}
				else if (afternoon) {
					next_alarm_time = sharedPref.getLong("aft_time", 0);
					freq_time = "Afternoon";
				}
				else {
					// Do Nothing
				}
			}
			
			else {
				next_alarm_time = sharedPref.getLong("night_time", 0);
				if (morning) {
					next_alarm_time = sharedPref.getLong("morn_time", 0);
					freq_time = "Morning";
				}
				else if (afternoon) {
					next_alarm_time = sharedPref.getLong("aft_time", 0);
					freq_time = "Afternoon";
				}
				else if (evening) {
					next_alarm_time = sharedPref.getLong("eve_time", 0);
					freq_time = "Evening";
				}
				else {
					// Do Nothing
				}
			}
			
			Date alarm = new Date(next_alarm_time);
			alarmtime.setTime(alarm);
			
			// Setting alarm date 
			if (!freq_days.equalsIgnoreCase("N/A")) {
				alarmtime.set(Calendar.DAY_OF_YEAR,	now.get(Calendar.DAY_OF_YEAR)	+ Integer.parseInt(freq_days));

			}
			
		}
		
		
		// No next alarm if this is the goal end date
		if (!freq_days.equalsIgnoreCase("N/A")) {
			String alarm_string = sdf.format(alarmtime.getTime());

			Log.d("NEXT_ALARM", alarm_string);
			Log.d("PREV_ALARM", prevalarm_time);
			
			long date_sec = alarmtime.getTimeInMillis();
			long end_sec = end_date.getTimeInMillis();
			long diff = (end_sec - date_sec) / (24 * 60 * 60 * 1000);

			if (diff <= 0) {
				alarmtime.set(Calendar.DAY_OF_YEAR, end_date.get(Calendar.DAY_OF_YEAR));
				freq_days = "N/A";
			}
			
			
			// remove prev alarm
			Intent iDeletePrevAlarm = new Intent(this, AlarmReceiver.class);
			PendingIntent piDeletePrevAlarm = PendingIntent.getBroadcast(this,
					id, iDeletePrevAlarm, PendingIntent.FLAG_ONE_SHOT);

			if (piDeletePrevAlarm != null) {
				piDeletePrevAlarm.cancel();
			}
			
			
			// Setting new alarm
			Intent newAlarmIntent = new Intent(this, AlarmReceiver.class);
			newAlarmIntent.putExtra("ID", id);
			newAlarmIntent.putExtra("TITLE", title);
			newAlarmIntent.putExtra("CATEGORY", category);
			newAlarmIntent.putExtra("SCHEDULE", schedule);
			newAlarmIntent.putExtra("DATE", date);
			newAlarmIntent.putExtra("FREQDAYS", freq_days);
			newAlarmIntent.putExtra("FREQTIME", freq_time);
			newAlarmIntent.putExtra("ALARMTIME", alarmtime);
			newAlarmIntent.putExtra("NEXTCHECKIN", nextCheckin);
			
			PendingIntent pi2 = PendingIntent.getBroadcast(this, id, newAlarmIntent, PendingIntent.FLAG_ONE_SHOT);

			AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			alarmMgr.set(AlarmManager.RTC_WAKEUP, alarmtime.getTimeInMillis(), pi2);
			
			new SaveAlarm().execute();
		}
		
		
				
		super.onHandleIntent(i);
	}
	
	private class SaveAlarm extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			GoalDatabase dbSingleton = GoalDatabase.getInstance(getApplicationContext());
			SQLiteDatabase db = dbSingleton.getWritableDatabase();
			
			String newAlarmTime = sdf.format(alarmtime.getTime());
			
			ContentValues cv = new ContentValues();
			cv.put("FREQDAYS", freq_days);
			cv.put("FREQTIME", freq_time);
			cv.put("ALARMTIME", newAlarmTime);
			
			db.update("GOALS", cv, "ID =" + id, null);
		
			db.close();
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void args) {
			
			
			Log.d("db_save_alarm", "I reached End of SaveAlarm");
			
		}
			
	}
	
}
