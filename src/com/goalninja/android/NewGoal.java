package com.goalninja.android;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.goalninja.android.R;
import com.parse.ParseObject;


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
import android.database.Cursor;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class NewGoal extends Activity {
	
	Spinner category, freq_days, freq_time;
	LinearLayout repeat_block;
	RelativeLayout complete_block, bg;
	ImageView icon;
	TextView complete_date, category_text, days_text, time_text;
	RadioGroup schedule;
	RadioButton repeat, complete;
	Button add_goal, cancel;
	EditText title;
	MyGoal addNewGoal;
	SharedPreferences sharedPref;
	
	int id = 0, feed_id = 0, seq;
	String freq_days_text, freq_time_text, categ_text, schedule_text, title_text;
	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");
	Calendar cal, nextcheckin;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // set action bar as Overlay
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        
        setContentView(R.layout.new_goal);
        
        // Making actionbar translucent - bg-A8A8A8, alpha-0.6
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#96A8A8A8")));
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
//      getActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>New Goal</font>"));
        
        title = (EditText) findViewById(R.id.new_title);
        
        category = (Spinner) findViewById(R.id.new_category_spinner);
        freq_days = (Spinner) findViewById(R.id.new_freq_days);
        freq_time = (Spinner) findViewById(R.id.new_freq_time);
        
        category_text = (TextView) findViewById(R.id.new_category);
        days_text = (TextView) findViewById(R.id.new_days_text);
        time_text = (TextView) findViewById(R.id.new_time_text);
        
        repeat_block = (LinearLayout) findViewById(R.id.new_repeat_block);
//        freq_time_block = (RelativeLayout) findViewById(R.id.new_repeat_time_block);
        complete_block = (RelativeLayout) findViewById(R.id.new_complete_block);
        
        complete_date = (TextView) findViewById(R.id.new_date);
        
        schedule = (RadioGroup) findViewById(R.id.radiogroup);
        repeat = (RadioButton) findViewById(R.id.new_repeat);
        complete = (RadioButton) findViewById(R.id.new_complete);
        
        add_goal = (Button) findViewById(R.id.new_add_goal);
        
        bg = (RelativeLayout) findViewById(R.id.new_goal_parent);
        icon = (ImageView) findViewById(R.id.new_icon);
        
                
        // Setting listener for complete Date
        cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) + 7);
        complete_date.setText(sdf.format(cal.getTime()));
//      complete_date.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        
        complete_block.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundl = new Bundle();
				bundl.putSerializable("calendarNew", cal);
				showDatePickerDialog(bundl);
			}
		});
        
		
		// Listener for RadioGroup for schedule
		
		repeat.isChecked();
		complete_block.setVisibility(View.GONE);
        schedule_text = "Repeat";
        freq_days_text = "Daily";
        freq_time_text = "Morning";
        categ_text = "Arts";
		
		schedule.setOnCheckedChangeListener(new OnCheckedChangeListener() {
        	
        	@Override
        	public void onCheckedChanged(RadioGroup group, int checkedId) {
        		if (checkedId == R.id.new_repeat) {
        			// Repeat selected
        			complete_block.setVisibility(View.GONE);
        			repeat_block.setVisibility(View.VISIBLE);
        			schedule_text = "Repeat";
        		}
        		else {
        			// Complete selected
        			repeat_block.setVisibility(View.GONE);
        			complete_block.setVisibility(View.VISIBLE);
        			schedule_text = "Complete";
        		}
        	}
        	
        });
        

        // Setting listeners for all 3 spinners
        
        ArrayAdapter<CharSequence> freq_day_dropdown =  ArrayAdapter.createFromResource(this, R.array.freq_day_dropdown, R.layout.spinner_text);
        freq_day_dropdown.setDropDownViewResource(R.layout.nav_drawer_list_item);
        freq_days.setAdapter(freq_day_dropdown);
        freq_days.setSelection(0);
        
        freq_days.setOnItemSelectedListener(new OnItemSelectedListener(){
        
	        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	        	freq_days_text = (String) parent.getItemAtPosition(pos);
	        	days_text.setText(freq_days_text);
	        }
        
	        public void onNothingSelected(AdapterView<?> parent) {
	        	// Nothing
	        }
        
        });
        
        ArrayAdapter<CharSequence> freq_time_dropdown =  ArrayAdapter.createFromResource(this, R.array.freq_time_dropdown, R.layout.spinner_text);
        freq_time_dropdown.setDropDownViewResource(R.layout.nav_drawer_list_item);
        freq_time.setAdapter(freq_time_dropdown);
        freq_time.setSelection(0);
        
        freq_time.setOnItemSelectedListener(new OnItemSelectedListener(){
        
	        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	        	freq_time_text = (String) parent.getItemAtPosition(pos);
	        	time_text.setText(freq_time_text);
	        }
        
	        public void onNothingSelected(AdapterView<?> parent) {
	        	// Nothing
	        }
        
        });
        
        ArrayAdapter<CharSequence> categ_dropdown =  ArrayAdapter.createFromResource(this, R.array.categories, R.layout.spinner_text_light);
        categ_dropdown.setDropDownViewResource(R.layout.nav_drawer_list_item);
        category.setAdapter(categ_dropdown);
        category.setSelection(0);
        
        category.setOnItemSelectedListener(new OnItemSelectedListener(){
        
	        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	        	categ_text = (String) parent.getItemAtPosition(pos);
	        	category_text.setText(categ_text);
	        	setBackgroundIcon(categ_text);
	        }
        
	        public void onNothingSelected(AdapterView<?> parent) {
	        	// Nothing
	        }
        
        });
        
        
        
        
       
        // Setting listeners for AddGoal and Cancel buttons
        
//        cancel.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				onBackPressed();				
//			}
//        	
//        });
        
        sharedPref = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        
        add_goal.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				title_text = title.getText().toString();
				
				if (title_text != null && !title_text.isEmpty()) {
					
					add_goal.setBackgroundColor(Color.parseColor("#828282"));
					add_goal.setEnabled(false);
					
					// Setting Id, start, progress
					Calendar now = Calendar.getInstance();
					nextcheckin = Calendar.getInstance();
					
					addNewGoal = new MyGoal(id, feed_id, title.getText().toString(),
							categ_text, schedule_text, now.getTime(),
							cal.getTime(), freq_days_text, freq_time_text, 0, now, nextcheckin);
					
					//id, title, category, schedule, start, date, frequency_days, frequency_time, progress, alarmtime);
					
					String log = id + "," + title.getText().toString() + "," + categ_text +  "," + schedule_text + ","  + now.getTime().toString()+ "," +
							cal.getTime().toString() + "," + freq_days_text + "," + freq_time_text + "," + sdf.format(now.getTime()); 
					Log.d("db_insert_new", log);
										
					//RowActivity nGoal = new RowActivity();
					setAlarm(addNewGoal);
					
				}
				
				
				
			}
        	
        });
        
        
	
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			Calendar c = Calendar.getInstance();

			Bundle bundle = this.getArguments();
			if (bundle != null) {
				c = (Calendar) bundle.getSerializable("calendarNew");
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
			complete_date.setText(new_cal);
			cal = c1;

		}

	}

	public void showDatePickerDialog(Bundle bundl) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.setArguments(bundl);
		newFragment.show(getFragmentManager(), "datePickerNew");

	}
	
	
	public void setAlarm(MyGoal newGoal) {

		Calendar newCal = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		

		// cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) + 30);

		if (newGoal.getSchedule().equalsIgnoreCase("Repeat")) {

			// For Habits

			switch (newGoal.getFreqTime()) {

			case ("Morning"):

				Long morn_time = sharedPref.getLong("morn_time", 0);
				Date morn_date = new Date(morn_time);
				newCal.setTime(morn_date);
				newCal.set(Calendar.DAY_OF_MONTH,
						now.get(Calendar.DAY_OF_MONTH));
				newCal.set(Calendar.MONTH, now.get(Calendar.MONTH));
				newCal.set(Calendar.YEAR, now.get(Calendar.YEAR));
				break;

			case ("Afternoon"):
				Long aft_time = sharedPref.getLong("aft_time", 0);
				Date aft_date = new Date(aft_time);
				newCal.setTime(aft_date);
				newCal.set(Calendar.DAY_OF_MONTH,
						now.get(Calendar.DAY_OF_MONTH));
				newCal.set(Calendar.MONTH, now.get(Calendar.MONTH));
				newCal.set(Calendar.YEAR, now.get(Calendar.YEAR));
				break;

			case ("Evening"):
				Long eve_time = sharedPref.getLong("eve_time", 0);
				Date eve_date = new Date(eve_time);
				newCal.setTime(eve_date);
				newCal.set(Calendar.DAY_OF_MONTH,
						now.get(Calendar.DAY_OF_MONTH));
				newCal.set(Calendar.MONTH, now.get(Calendar.MONTH));
				newCal.set(Calendar.YEAR, now.get(Calendar.YEAR));
				break;

			case ("Night"):
				Long night_time = sharedPref.getLong("night_time", 0);
				Date night_date = new Date(night_time);
				newCal.setTime(night_date);
				newCal.set(Calendar.DAY_OF_MONTH,
						now.get(Calendar.DAY_OF_MONTH));
				newCal.set(Calendar.MONTH, now.get(Calendar.MONTH));
				newCal.set(Calendar.YEAR, now.get(Calendar.YEAR));
				break;
			}

			// If time of alarm has already passed, set to next day

			if (newCal.compareTo(now) <= 0) {
				newCal.set(Calendar.DAY_OF_YEAR,
						newCal.get(Calendar.DAY_OF_YEAR) + 1);
			}

			switch (newGoal.getFreqDays()) {
			case ("Daily"):
				// Do nothing
				break;

			case ("Weekdays"):

				// If Saturday, make it Monday
				if (newCal.get(Calendar.DAY_OF_WEEK) == 7) {
					newCal.set(Calendar.DAY_OF_YEAR,
							newCal.get(Calendar.DAY_OF_YEAR) + 2);
				}
				// If Sunday, make it Monday
				if (newCal.get(Calendar.DAY_OF_WEEK) == 1) {
					newCal.set(Calendar.DAY_OF_YEAR,
							newCal.get(Calendar.DAY_OF_YEAR) + 1);
				}
				if (newCal.compareTo(now) <= 0) {
					newCal.set(Calendar.DAY_OF_YEAR,
							newCal.get(Calendar.DAY_OF_YEAR) + 7);
				}
				break;

			case ("Weekends"):

				// If weekdays, make it Saturday
				if (newCal.get(Calendar.DAY_OF_WEEK) != 1
						|| newCal.get(Calendar.DAY_OF_WEEK) != 7) {
					newCal.set(Calendar.DAY_OF_WEEK, 7);
				}

				if (newCal.compareTo(now) <= 0) {
					newCal.set(Calendar.DAY_OF_YEAR,
							newCal.get(Calendar.DAY_OF_YEAR) + 7);
				}
				break;

			case ("Once a week"):

				// Set for Wednesday or Saturday, whichever is nearer
				if (newCal.get(Calendar.DAY_OF_WEEK) <= 4) {
					newCal.set(Calendar.DAY_OF_WEEK, 4);
				} else {
					newCal.set(Calendar.DAY_OF_WEEK, 7);
				}

				if (newCal.compareTo(now) <= 0) {
					newCal.set(Calendar.DAY_OF_YEAR,
							newCal.get(Calendar.DAY_OF_YEAR) + 7);
				}
				break;

			case ("Fortnightly"):

				int day_of_month = newCal.get(Calendar.DAY_OF_MONTH);

				// Set for 7th and 12th of the month
				if (day_of_month <= 7) {
					newCal.set(Calendar.DAY_OF_MONTH, 7);
				} else if (day_of_month <= 12) {
					newCal.set(Calendar.DAY_OF_MONTH, 12);
				} else if (day_of_month <= 21) {
					newCal.set(Calendar.DAY_OF_MONTH, 21);
				} else if (day_of_month <= 28) {
					newCal.set(Calendar.DAY_OF_MONTH, 28);
				} else {
					newCal.set(Calendar.MONTH, newCal.get(Calendar.MONTH) + 1);
					newCal.set(Calendar.DAY_OF_MONTH, 7);
				}
				break;

			case ("Monthly"):
				int day_of_month2 = newCal.get(Calendar.DAY_OF_MONTH);

				// Set to 15th or 25th of the month
				if (day_of_month2 <= 15) {
					newCal.set(Calendar.DAY_OF_MONTH, 15);
				} else if (day_of_month2 <= 25) {
					newCal.set(Calendar.DAY_OF_MONTH, 25);
				} else {
					newCal.set(Calendar.MONTH, newCal.get(Calendar.MONTH) + 1);
					newCal.set(Calendar.DAY_OF_MONTH, 15);
				}
				break;
			}

		}

		else {

			long which_time;
			// For Goals
			// Setting alarm times
			boolean morning = sharedPref.getBoolean("morning", true);
			boolean afternoon = sharedPref.getBoolean("afternoon", true);
			boolean evening = sharedPref.getBoolean("evening", true);
			boolean night = sharedPref.getBoolean("night", true);

			if (morning) {

				which_time = sharedPref.getLong("morn_time", 0);
				addNewGoal.setFreqTime("Morning");
			}

			else if (afternoon) {

				which_time = sharedPref.getLong("aft_time", 0);
				addNewGoal.setFreqTime("Afternoon");
			}

			else if (evening) {

				which_time = sharedPref.getLong("eve_time", 0);
				addNewGoal.setFreqTime("Evening");
			}

			else {

				which_time = sharedPref.getLong("night_time", 0);
				addNewGoal.setFreqTime("Night");
			}

			Date when = new Date(which_time);
			newCal.setTime(when);
			newCal.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
			newCal.set(Calendar.MONTH, now.get(Calendar.MONTH));
			newCal.set(Calendar.YEAR, now.get(Calendar.YEAR));

			// setting alarm dates
			long date_sec = cal.getTimeInMillis();
			long now_sec = now.getTimeInMillis();
			long diff = (date_sec - now_sec) / (24 * 60 * 60 * 1000);

			if (diff <= 14) {
				// If less than 2 weeks, set alarm every 2 days
				if (diff > 2) {
					newCal.set(Calendar.DAY_OF_YEAR,
							newCal.get(Calendar.DAY_OF_YEAR) + 2);
					addNewGoal.setFreqDays("2");
				} else {
					newCal.set(Calendar.DAY_OF_YEAR,
							cal.get(Calendar.DAY_OF_YEAR));
					addNewGoal.setFreqDays("N/A");
				}

			} else if (diff <= 30) {
				// If less than 1 month, set alarm every 4 days

				newCal.set(Calendar.DAY_OF_YEAR,
						newCal.get(Calendar.DAY_OF_YEAR) + 2);
				addNewGoal.setFreqDays("4");
			} else if (diff <= 90) {
				// If less than 3 months, set alarm once a week

				newCal.set(Calendar.DAY_OF_YEAR,
						newCal.get(Calendar.DAY_OF_YEAR) + 7);
				addNewGoal.setFreqDays("7");
			} else {
				// set alarm once every 2 weeks

				newCal.set(Calendar.DAY_OF_YEAR,
						newCal.get(Calendar.DAY_OF_YEAR) + 14);
				addNewGoal.setFreqDays("14");
			}

		}

		addNewGoal.setAlarmTime(newCal);
		
		
		new GetLatestGoalId().execute();

//		Intent i = new Intent(this, AlarmReceiver.class);
//		i.putExtra("ID", newGoal.getid());
//		i.putExtra("TITLE", newGoal.getTitle());
//		i.putExtra("CATEGORY", newGoal.getCategory());
//		i.putExtra("SCHEDULE", newGoal.getSchedule());
//		i.putExtra("DATE", newGoal.getDate());
//		i.putExtra("FREQDAYS", newGoal.getFreqDays());
//		i.putExtra("FREQTIME", newGoal.getFreqTime());
//		i.putExtra("ALARMTIME", newCal);
//
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String alarm_string = sdf.format(newCal.getTime());
//		Log.d("ALARM", alarm_string);
//		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i,
//				PendingIntent.FLAG_ONE_SHOT);
//
//		// id, title, category, schedule, start, date, frequency_days,
//		// frequency_time, progress, alarmtime
//
//		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//		alarmMgr.set(AlarmManager.RTC_WAKEUP, newCal.getTimeInMillis(), pi);
//		
//		String log = String.valueOf(newGoal.getid()) + "," + newGoal.getTitle() + "," + newGoal.getCategory() +  "," + newGoal.getSchedule() + ","  + newGoal.getDate().toString()+ "," +
//				 newGoal.getFreqDays() + "," + newGoal.getFreqTime() + "," + newGoal.getAlarmTime().getTime().toString(); 
//		Log.d("db_insert_new", log);
//		
//		Log.d("Alarm", "Alarm is set");
		


//		new AddGoal().execute(new MyGoal[] { newGoal });

	}

	private class AddGoal extends AsyncTask<MyGoal, Void, Boolean> {

		@SuppressLint("SimpleDateFormat")
		@Override
		protected Boolean doInBackground(MyGoal... goal) {
			GoalDatabase dbSingleton = GoalDatabase
					.getInstance(getApplicationContext());
			SQLiteDatabase db = dbSingleton.getWritableDatabase();

			// Inserting into goals table
			ContentValues values = new ContentValues();
			values.put("ID", goal[0].getid());
			values.put("TITLE", goal[0].getTitle());
			values.put("CATEGORY", goal[0].getCategory());
			values.put("SCHEDULE", goal[0].getSchedule());
			values.put("FREQDAYS", goal[0].getFreqDays());
			values.put("FREQTIME", goal[0].getFreqTime());
			values.put("PROGRESS", goal[0].getProgress());

			// for dates
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd  HH:mm:ss");
			String start = dateFormat.format(goal[0].getStart());
			String date = dateFormat.format(goal[0].getDate());
			String alarmtime = dateFormat.format(goal[0].getAlarmTime()
					.getTime());
			String nextcheckintime = dateFormat.format(goal[0].getNextCheckin().getTime());

			Log.d("db_insert", start);
			Log.d("db_insert", date);
			Log.d("db_insert", alarmtime);
			Log.d("db_insert", nextcheckintime);

			values.put("START", start);
			values.put("DATE", date);
			values.put("ALARMTIME", alarmtime);
			values.put("NEXTCHECKIN", nextcheckintime);

			Log.d("db_insert", "I reached here");

			// Log.d("db_insert", dateFormat.format(goal[0].getStart()));
			// Log.d("db_insert", dateFormat.format(goal[0].getDate()));

			// inserting row
			db.insert("GOALS", null, values);

			// Inserting into PROG table for goals
			ContentValues notes = new ContentValues();
			notes.put("GOALID", goal[0].getid());
			notes.put("NOTE", "Added Goal");
			notes.put("DATE", dateFormat.format(goal[0].getStart()));

			db.insert("PROG", null, notes);

			Log.d("db_insert", "Inserted into notes as well");

			// Updating Feed table "isAdded" column
			ContentValues isAdded = new ContentValues();
			isAdded.put("ISADDED", 1);

			db.update("FEED", isAdded, "ID =" + goal[0].getid(), null);

			db.close();

			return true;
		}

		@Override
		protected void onPostExecute(Boolean status) {
			Log.d("DB_INSERT", status.toString());
			
			

			// Saving to Parse
			ParseObject newGoal= new ParseObject("GoalInfo");
			newGoal.put("userEmail", sharedPref.getString("user_email", "DNE"));
			newGoal.put("goalId", addNewGoal.getid());
			newGoal.put("goalTitle", title_text);
			newGoal.put("category", categ_text);
			newGoal.put("type", schedule_text);
			newGoal.put("fromFeed", "N");
			newGoal.put("action", "Goal Added");
			
//			newGoal.saveInBackground();
			newGoal.saveEventually();
			
			
			// Setting up alarm
			Intent i = new Intent(NewGoal.this, AlarmReceiver.class);
			i.putExtra("ID", addNewGoal.getid());
			i.putExtra("TITLE", addNewGoal.getTitle());
			i.putExtra("CATEGORY", addNewGoal.getCategory());
			i.putExtra("SCHEDULE", addNewGoal.getSchedule());
			i.putExtra("DATE", addNewGoal.getDate());
			i.putExtra("FREQDAYS", addNewGoal.getFreqDays());
			i.putExtra("FREQTIME", addNewGoal.getFreqTime());
			i.putExtra("ALARMTIME", addNewGoal.getAlarmTime());
			i.putExtra("NEXTCHECKIN", addNewGoal.getNextCheckin());

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String alarm_string = sdf.format(addNewGoal.getAlarmTime().getTime());
			Log.d("ALARM", alarm_string);
			PendingIntent pi = PendingIntent.getBroadcast(NewGoal.this, addNewGoal.getid(), i,
					PendingIntent.FLAG_ONE_SHOT);

			// id, title, category, schedule, start, date, frequency_days,
			// frequency_time, progress, alarmtime

			AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			alarmMgr.set(AlarmManager.RTC_WAKEUP, addNewGoal.getAlarmTime().getTimeInMillis(), pi);
			
			String log = String.valueOf(addNewGoal.getid()) + "," + addNewGoal.getTitle() + "," + addNewGoal.getCategory() +  "," + addNewGoal.getSchedule() + ","  + addNewGoal.getDate().toString()+ "," +
					addNewGoal.getFreqDays() + "," + addNewGoal.getFreqTime() + "," + addNewGoal.getAlarmTime().getTime().toString(); 
			Log.d("db_insert_new", log);
			
			Log.d("Alarm", "Alarm is set");

			// Go back to Home screen
			Intent iForMainActivity = new Intent(NewGoal.this, MainActivity.class);
			iForMainActivity.putExtra("toast", "Goal added");
			iForMainActivity.putExtra("position", 0);
			iForMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			iForMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(iForMainActivity);
			// RowActivity.this.startActivity(i);

			// Do Nothing

		}
		// CATEGORY, START, LEVEL, SCHEDULE, DATE, FREQ);
	}
	
	
	private class GetLatestGoalId extends AsyncTask<Void, Void, Void>{

		@SuppressLint("SimpleDateFormat")
		@Override
		protected Void doInBackground(Void... params) {
			GoalDatabase dbInstance = GoalDatabase.getInstance(getApplicationContext());
			SQLiteDatabase db1 = dbInstance.getReadableDatabase();
			Cursor cursor = db1.rawQuery("select seq from sqlite_sequence where name=\"GOALS\"", null);
			cursor.moveToFirst();

			if (cursor.moveToFirst()) {
				do {
					seq = cursor.getInt(0);
					
					
					Log.d("db_read_seq", String.valueOf(seq));
					} while (cursor.moveToNext());
			}
			else {
				seq = 0;
			}
			cursor.close();
			db1.close();
			return null;
		}
		
		
		@Override
		protected void onPostExecute(Void args) {
			
			id = seq+1;
			
			Log.d("db_read_seq", String.valueOf(id));
			
			addNewGoal.setid(id);
			
			new AddGoal().execute(new MyGoal[] {addNewGoal});			
			
			// Do Nothing
			
		}
	}
	
	public void setBackgroundIcon(String category) {
		
		switch(category) {
		
		case "Career": 
//			bg.setBackgroundColor(Color.parseColor("#283593"));
			bg.setBackgroundColor(Color.parseColor("#33B679"));
			icon.setImageResource(R.drawable.career_icon);
			break;
			
		case "Personal":
//			bg.setBackgroundColor(Color.parseColor("#C8871E"));
			bg.setBackgroundColor(Color.parseColor("#33B679"));
			icon.setImageResource(R.drawable.heart_icon);
			break;
			
		case "Fitness":
//			bg.setBackgroundColor(Color.parseColor("#A03024"));
			bg.setBackgroundColor(Color.parseColor("#E5AE4F"));
			icon.setImageResource(R.drawable.fitness_icon);
			break;
			
		case "Food":
//			bg.setBackgroundColor(Color.parseColor("#006064"));
			bg.setBackgroundColor(Color.parseColor("#DF5948"));
			icon.setImageResource(R.drawable.food_icon);
			break;
							
		case "Literature":
//			bg.setBackgroundColor(Color.parseColor("#C8871E"));
			bg.setBackgroundColor(Color.parseColor("#547BCA"));
			icon.setImageResource(R.drawable.book_icon);
			break;
			
		case "Music":
//			bg.setBackgroundColor(Color.parseColor("#A03024"));
			bg.setBackgroundColor(Color.parseColor("#DF5948"));
			icon.setImageResource(R.drawable.music_icon);
			break;
			
		case "Lifestyle":
//			bg.setBackgroundColor(Color.parseColor("#006064"));
			bg.setBackgroundColor(Color.parseColor("#E5AE4F"));
			icon.setImageResource(R.drawable.trophy_icon);
			break;
			
		case "Tech":
//			bg.setBackgroundColor(Color.parseColor("#C8871E"));
			bg.setBackgroundColor(Color.parseColor("#33B679"));
			icon.setImageResource(R.drawable.tech_icon);
			break;
			
		case "Travel/Adventure":
//			bg.setBackgroundColor(Color.parseColor("#283593"));
			bg.setBackgroundColor(Color.parseColor("#547BCA"));
			icon.setImageResource(R.drawable.map_icon);
			break;
							
		case "Other": 
//			bg.setBackgroundColor(Color.parseColor("#A03024"));
			bg.setBackgroundColor(Color.parseColor("#33B679"));
			icon.setImageResource(R.drawable.target_icon);
			break;
	
	}
		
	}
	
	
	
}
