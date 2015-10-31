package com.goalninja.android;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.goalninja.android.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

@SuppressLint("SimpleDateFormat")
public class EditGoal extends Activity {
		
	TextView title_text, category_text, complete_text, freq_days_text, freq_time_text;
	RelativeLayout complete, bg;
	LinearLayout repeat_block;
	Spinner days, time;
	Button save;
	
	String title, category, schedule, freq_days, freq_time, date_text;
	int id, progress, days_pos, time_pos;
	Date start, date = new Date();
	
	String freq_days_prev, freq_time_prev;
	Date date_prev;
	
	Calendar cal, alarm, newAlarm, start_cal, nextCheckin;
	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // set action bar as Overlay
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        
        setContentView(R.layout.edit_goal_layout);
        
        // Making actionbar translucent - bg-A8A8A8, alpha-0.6
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#96A8A8A8")));
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        title_text = (TextView) findViewById(R.id.edit_title);
        category_text = (TextView) findViewById(R.id.edit_category);
        complete_text = (TextView) findViewById(R.id.edit_complete);
        freq_days_text = (TextView) findViewById(R.id.edit_date_text);
        freq_time_text = (TextView) findViewById(R.id.edit_time_text);
        
        complete = (RelativeLayout) findViewById(R.id.edit_complete_block);
        repeat_block = (LinearLayout) findViewById(R.id.edit_repeat_block);
        days = (Spinner) findViewById(R.id.edit_freq_day);
        time = (Spinner) findViewById(R.id.edit_freq_time);
        
        save = (Button) findViewById(R.id.edit_save);
        bg = (RelativeLayout) findViewById(R.id.edit_goal_layout);
        
        title = getIntent().getStringExtra("title");
        category = getIntent().getStringExtra("category");
        schedule = getIntent().getStringExtra("schedule");
        start = (Date) getIntent().getSerializableExtra("start");
        date = (Date) getIntent().getSerializableExtra("date");
        freq_days = getIntent().getStringExtra("freq_days");
        freq_time = getIntent().getStringExtra("freq_time");
        alarm = (Calendar) getIntent().getSerializableExtra("alarm_time");
        nextCheckin = (Calendar) getIntent().getSerializableExtra("next_checkin");
        id = getIntent().getIntExtra("id", 0);
        
        title_text.setText(title);
        category_text.setText(category);
        
        // Remembering the before-edit info 
        freq_days_prev = freq_days;
        freq_time_prev = freq_time;
        date_prev = date;
        
        // Converting start date to calendar object
        start_cal = Calendar.getInstance();
        start_cal.setTime(start);
        
        
//        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        
        // Setting background according to Category
        
        switch (category) {

		case "Career":
			bg.setBackgroundColor(Color.parseColor("#33B679"));
			break;

		case "Personal":
			bg.setBackgroundColor(Color.parseColor("#33B679"));
			break;

		case "Fitness":
			bg.setBackgroundColor(Color.parseColor("#E5AE4F"));
			break;

		case "Food":
			bg.setBackgroundColor(Color.parseColor("#DF5948"));
			break;

		case "Literature":
			bg.setBackgroundColor(Color.parseColor("#547BCA"));
			break;

		case "Music":
			bg.setBackgroundColor(Color.parseColor("#DF5948"));
			break;

		case "Lifestyle":
			bg.setBackgroundColor(Color.parseColor("#E5AE4F"));
			break;

		case "Tech":
			bg.setBackgroundColor(Color.parseColor("#33B679"));
			break;

		case "Travel/Adventure":
			bg.setBackgroundColor(Color.parseColor("#547BCA"));
			break;

		case "Other":
			bg.setBackgroundColor(Color.parseColor("#33B679"));
			break;

		}

        // Setting ID of freq_days and freq_time in spinner dropdown
        switch (freq_days) {
        case "Daily":
        	days_pos = 0;
        	break;
        
        case "Weekdays":
        	days_pos = 1;
        	break;
        	
        case "Weekends":
        	days_pos = 2;
        	break;
        	
        case "Once a week":
        	days_pos = 3;
        	break;
        	
        case "Fortnightly":
        	days_pos = 4;
        	break;
        	
        case "Monthly":
        	days_pos = 5;
        	break;
        }
        
        switch (freq_time) {
        case "Morning":
        	time_pos = 0;
        	break;
        	
        case "Afternoon":
        	time_pos = 1;
        	break;
        	
        case "Evening":
        	time_pos = 2;
        	break;
        	
        case "Night":
        	time_pos = 3;
        	break;
        }
        
        
        
        
        // Setting spinner adapters
        days = (Spinner)findViewById(R.id.edit_freq_day);
        ArrayAdapter<CharSequence> days_dropdown =  ArrayAdapter.createFromResource(this, R.array.freq_day_dropdown, android.R.layout.simple_spinner_item);
        days_dropdown.setDropDownViewResource(R.layout.nav_drawer_list_item);
        days.setAdapter(days_dropdown);
        days.setSelection(days_pos);
        
        days.setOnItemSelectedListener(new OnItemSelectedListener(){
        
	        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	        	freq_days = (String) parent.getItemAtPosition(pos);
	        	freq_days_text.setText(freq_days);
	        }
        
	        public void onNothingSelected(AdapterView<?> parent) {
	        	// Nothing
	        }
        
        });
        
        time = (Spinner)findViewById(R.id.edit_freq_time);
        ArrayAdapter<CharSequence> time_dropdown =  ArrayAdapter.createFromResource(this, R.array.freq_time_dropdown, android.R.layout.simple_spinner_item);
        time_dropdown.setDropDownViewResource(R.layout.nav_drawer_list_item);
        time.setAdapter(time_dropdown);
        time.setSelection(time_pos);
        
        time.setOnItemSelectedListener(new OnItemSelectedListener(){
        
	        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	        	freq_time = (String) parent.getItemAtPosition(pos);
	        	freq_time_text.setText(freq_time);
	        }
        
	        public void onNothingSelected(AdapterView<?> parent) {
	        	// Nothing
	        }
        
        });
        
        
        // Setting text for complete_date
        
        cal = Calendar.getInstance();
        cal.setTime(date);
        date_text = sdf.format(date);
        complete_text.setText(date_text);
//        complete.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        
		complete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundl = new Bundle();
				bundl.putSerializable("calendarEdit", cal);
				showDatePickerDialog(bundl);
			}
		});
        
		// Setting listener for Save button
		
		save.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				if (schedule.equalsIgnoreCase("Repeat")) {
					if ((!freq_days_prev.equals(freq_days)) || (!freq_time_prev.equals(freq_time))) {
						
						removePrevAlarm();
					}
					else {
						goToUpdateProgress();
					}
				}
				else {
					if (date_prev.compareTo(date)!=0) {
						removePrevAlarm();
					}
					else {
						goToUpdateProgress();
					}
				}
				
			}
			
		});
       
		// Setting visibility of views according to schedule
        
        if (schedule.equalsIgnoreCase("Repeat")) {
        	
        	complete.setVisibility(View.GONE);
        	
        }
        
        else {
        	
        	repeat_block.setVisibility(View.GONE);
        	
        }
        
        
        
	}
	
	
//	@Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);    
//    	getMenuInflater().inflate(R.menu.edit, menu);
//        return true;
//    }
	
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	
    	case android.R.id.home:
    		onBackPressed();
    		return true;
    		
//    	case R.id.save_edit:
//    		
//    		if (schedule.equalsIgnoreCase("Repeat")) {
//				if ((!freq_days_prev.equals(freq_days)) || (!freq_time_prev.equals(freq_time))) {
//					
//					removePrevAlarm();
//				}
//				else {
//					goToUpdateProgress();
//				}
//			}
//			else {
//				if (date_prev.compareTo(date)!=0) {
//					removePrevAlarm();
//				}
//				else {
//					goToUpdateProgress();
//				}
//			}
//    		return true;
    	}
        return super.onOptionsItemSelected(item);
       }
	
	
	public class DatePickerFragment extends DialogFragment
	implements DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			Calendar c = Calendar.getInstance();

			Bundle bundle = this.getArguments();
			if (bundle != null) {
				c = (Calendar) bundle.getSerializable("calendarEdit");
			}

			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			DatePickerDialog dp = new DatePickerDialog(getActivity(), this,
					year, month, day);
			dp.setCancelable(true);
			dp.setCanceledOnTouchOutside(true);

			// Setting min date as tomorrow's
			Calendar tom = Calendar.getInstance();
			tom.set(Calendar.DAY_OF_YEAR, tom.get(Calendar.DAY_OF_YEAR) + 1);
			dp.getDatePicker().setMinDate(tom.getTime().getTime());
			return dp;
			
		}

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			Calendar c1 = Calendar.getInstance();
			c1.set(Calendar.YEAR, year);
			c1.set(Calendar.MONTH, monthOfYear);
			c1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			String new_cal = sdf.format(c1.getTime());
			complete_text.setText(new_cal);
			cal = c1;
			date = c1.getTime();

		}

	}
	
	
	public void showDatePickerDialog(Bundle bundl) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.setArguments(bundl);
		newFragment.show(getFragmentManager(), "datePickerEdit");
		
	}
	
	
	private class SaveEdit extends AsyncTask<MyGoal, Void, Void> {

		@Override
		protected Void doInBackground(MyGoal... arg0) {
			GoalDatabase dbSingleton = GoalDatabase.getInstance(getApplicationContext());
			SQLiteDatabase db = dbSingleton.getWritableDatabase();
			
			ContentValues cv = new ContentValues();
			cv.put("FREQDAYS", freq_days);
			cv.put("FREQTIME", freq_time);
			
			SimpleDateFormat sdf_db = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
			String date_db = sdf_db.format(date);
			String alarm_db = sdf_db.format(newAlarm.getTime());
			
			cv.put("DATE", date_db);
			cv.put("ALARMTIME", alarm_db);
			
			db.update("GOALS", cv, "ID = " + id, null);
			db.close();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void args0) {
			
			goToUpdateProgress();
			
		}
		
	}
	
	
	public void goToUpdateProgress() {
		
		Intent iPostEdit = new Intent(EditGoal.this, UpdateProgress.class);
//		iPostEdit.putExtra("title", editTitle.getText().toString());
//		iPostEdit.putExtra("category", category);
//		iPostEdit.putExtra("schedule", schedule);
//		iPostEdit.putExtra("start", start);
//		iPostEdit.putExtra("date", date);
//		iPostEdit.putExtra("freq_days", freq_days);
//		iPostEdit.putExtra("freq_time", freq_time);
//		iPostEdit.putExtra("progress", progress);
		iPostEdit.putExtra("id", id);
		iPostEdit.putExtra("schedule", schedule);
		iPostEdit.putExtra("fromAlarm", "no");
		iPostEdit.putExtra("category", category);
		
		iPostEdit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		Log.d("db_save_edit", "I reached end of SaveEdit");
		
		startActivity(iPostEdit);
		
	}
	
	
	
	public void removePrevAlarm() {
		
		Intent i = new Intent(this, AlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, id, i, PendingIntent.FLAG_ONE_SHOT);
		
//		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//		alarmMgr.set(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), pi);
		
		if (pi!= null) {
//			alarmMgr.cancel(pi);
			pi.cancel();
		}
		
		// Setting new alarm times
		newAlarm = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		SharedPreferences sharedPref = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
		
		if (schedule.equalsIgnoreCase("Repeat")) {
			
			switch (freq_time) {

			case ("Morning"):

				Long morn_time = sharedPref.getLong("morn_time", 0);
				Date morn_date = new Date(morn_time);
				newAlarm.setTime(morn_date);
				break;

			case ("Afternoon"):
				Long aft_time = sharedPref.getLong("aft_time", 0);
				Date aft_date = new Date(aft_time);
				newAlarm.setTime(aft_date);
				break;

			case ("Evening"):
				Long eve_time = sharedPref.getLong("eve_time", 0);
				Date eve_date = new Date(eve_time);
				newAlarm.setTime(eve_date);
				break;

			case ("Night"):
				Long night_time = sharedPref.getLong("night_time", 0);
				Date night_date = new Date(night_time);
				newAlarm.setTime(night_date);
				break;
			}

			// Setting date as today
			newAlarm.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
			newAlarm.set(Calendar.MONTH, now.get(Calendar.MONTH));
			newAlarm.set(Calendar.YEAR, now.get(Calendar.YEAR));
			
			// If time of alarm has already passed, set to next day

			if (newAlarm.compareTo(now) <= 0) {
				newAlarm.set(Calendar.DAY_OF_YEAR,
						newAlarm.get(Calendar.DAY_OF_YEAR) + 1);
			}

			switch (freq_days) {
			case ("Daily"):
				// Do nothing
				break;

			case ("Weekdays"):

				// If Saturday, make it Monday
				if (newAlarm.get(Calendar.DAY_OF_WEEK) == 7) {
					newAlarm.set(Calendar.DAY_OF_YEAR,
							newAlarm.get(Calendar.DAY_OF_YEAR) + 2);
				}
				// If Sunday, make it Monday
				if (newAlarm.get(Calendar.DAY_OF_WEEK) == 1) {
					newAlarm.set(Calendar.DAY_OF_YEAR,
							newAlarm.get(Calendar.DAY_OF_YEAR) + 1);
				}
				// If Monday has already passed, make it next Monday
				if (newAlarm.compareTo(now) <= 0) {
					newAlarm.set(Calendar.DAY_OF_YEAR,
							newAlarm.get(Calendar.DAY_OF_YEAR) + 7);
				}
				break;

			case ("Weekends"):

				// If weekdays, make it Saturday
				if (newAlarm.get(Calendar.DAY_OF_WEEK) != 1
						|| newAlarm.get(Calendar.DAY_OF_WEEK) != 7) {
					newAlarm.set(Calendar.DAY_OF_WEEK, 7);
				}

				if (newAlarm.compareTo(now) <= 0) {
					newAlarm.set(Calendar.DAY_OF_YEAR,
							newAlarm.get(Calendar.DAY_OF_YEAR) + 7);
				}
				break;

			case ("Once a week"):

				// Set for Wednesday or Saturday, whichever is nearer
				if (newAlarm.get(Calendar.DAY_OF_WEEK) <= 4) {
					newAlarm.set(Calendar.DAY_OF_WEEK, 4);
				} else {
					newAlarm.set(Calendar.DAY_OF_WEEK, 7);
				}

				if (newAlarm.compareTo(now) <= 0) {
					newAlarm.set(Calendar.DAY_OF_YEAR,
							newAlarm.get(Calendar.DAY_OF_YEAR) + 7);
				}
				break;

			case ("Fortnightly"):

				int day_of_month = newAlarm.get(Calendar.DAY_OF_MONTH);

				// Set for 7th and 12th of the month
				if (day_of_month <= 7) {
					newAlarm.set(Calendar.DAY_OF_MONTH, 7);
				} else if (day_of_month <= 12) {
					newAlarm.set(Calendar.DAY_OF_MONTH, 12);
				} else if (day_of_month <= 21) {
					newAlarm.set(Calendar.DAY_OF_MONTH, 21);
				} else if (day_of_month <= 28) {
					newAlarm.set(Calendar.DAY_OF_MONTH, 28);
				} else {
					newAlarm.set(Calendar.MONTH, newAlarm.get(Calendar.MONTH) + 1);
					newAlarm.set(Calendar.DAY_OF_MONTH, 7);
				}
				break;

			case ("Monthly"):
				int day_of_month2 = newAlarm.get(Calendar.DAY_OF_MONTH);

				// Set to 15th or 25th of the month
				if (day_of_month2 <= 15) {
					newAlarm.set(Calendar.DAY_OF_MONTH, 15);
				} else if (day_of_month2 <= 25) {
					newAlarm.set(Calendar.DAY_OF_MONTH, 25);
				} else {
					newAlarm.set(Calendar.MONTH, newAlarm.get(Calendar.MONTH) + 1);
					newAlarm.set(Calendar.DAY_OF_MONTH, 15);
				}
				break;
			}
			
						
		}
		
		else {
			
			// For Goals
			
			Boolean morning = sharedPref.getBoolean("morning", true);
			Boolean afternoon = sharedPref.getBoolean("afternoon", true);
			Boolean evening = sharedPref.getBoolean("evening", true);
			Boolean night = sharedPref.getBoolean("night", true);
			Long which_time;
			
			if (morning) {
				
				which_time = sharedPref.getLong("morn_time", 0);
				freq_time = "Morning";
			}
			
			else if (afternoon) {
				
				which_time = sharedPref.getLong("aft_time", 0);
				freq_time = "Afternoon";
			}
			
			else if (evening) {
				
				which_time = sharedPref.getLong("eve_time", 0);
				freq_time = "Evening";
			}
			
			else {
				
				which_time = sharedPref.getLong("night_time", 0);
				freq_time = "Night";
			}
			
			Date when = new Date (which_time);
			newAlarm.setTime(when);
			newAlarm.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
			newAlarm.set(Calendar.MONTH, now.get(Calendar.MONTH));
			newAlarm.set(Calendar.YEAR, now.get(Calendar.YEAR));
			
			// setting alarm dates
			long date_sec = cal.getTimeInMillis();
			long start_sec = start_cal.getTimeInMillis();
			long diff = (date_sec - start_sec) / (24*60*60*1000);
			
			if (diff <=14) {
				// If less than 2 weeks, set alarm every 2 days
				if (diff > 2) {
					newAlarm.set(Calendar.DAY_OF_YEAR, newAlarm.get(Calendar.DAY_OF_YEAR) + 2);
					freq_days = "2";
				}
				else {
					newAlarm.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR));
					freq_days = "N/A";
				}
				
			}
			else if (diff <=30) {
				// If less than 1 month, set alarm every 4 days
				
				newAlarm.set(Calendar.DAY_OF_YEAR, newAlarm.get(Calendar.DAY_OF_YEAR) + 4);
				freq_days = "4";
			}
			else if (diff <=90) {
				// If less than 3 months, set alarm once a week
				
				newAlarm.set(Calendar.DAY_OF_YEAR, newAlarm.get(Calendar.DAY_OF_YEAR) + 7);
				freq_days = "7";
			}
			else {
				// set alarm once every 2 weeks
				
				newAlarm.set(Calendar.DAY_OF_YEAR, newAlarm.get(Calendar.DAY_OF_YEAR) + 14);
				freq_days = "14";
			}
			
			
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String prev_alarm_string = sdf.format(alarm.getTime());
		String new_alarm_string = sdf.format(newAlarm.getTime());
		Log.d("PREV_ALARM", prev_alarm_string);
		Log.d("NEW_ALARM", new_alarm_string);
		
		Log.d("remove_alarm", "I reached end of removeAlarm");
		
		setNewAlarm();
		
	}
	
	
	public void setNewAlarm() {
		
		Intent i = new Intent(this, AlarmReceiver.class);
		i.putExtra("ID", id);
		i.putExtra("TITLE", title);
		i.putExtra("CATEGORY", category);
		i.putExtra("SCHEDULE", schedule);
		i.putExtra("DATE", date);
		i.putExtra("FREQDAYS", freq_days);
		i.putExtra("FREQTIME", freq_time);
		i.putExtra("ALARMTIME", newAlarm);
		i.putExtra("NEXTCHECKIN", nextCheckin);
		
		
		PendingIntent pi = PendingIntent.getBroadcast(this, id, i, PendingIntent.FLAG_ONE_SHOT);
		
		//id, title, category, schedule, date, frequency_days, frequency_time, alarmtime
		
		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmMgr.set(AlarmManager.RTC_WAKEUP, newAlarm.getTimeInMillis(), pi);
		
		Log.d("new_alarm", "I reached end of setNewAlarm");
		
		new SaveEdit().execute();
		
	}
	

	
}
