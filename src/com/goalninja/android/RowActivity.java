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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class RowActivity extends Activity {
	
	String title, category;
	
	String frequency_days, frequency_time, schedule;;
	int feed_id, progress;
	int id = 0;
	int seq;
	Button add_goal, cancel;
	Spinner freq_day, freq_time;
	TextView complete_date, date_text, time_text;
	LinearLayout repeat_block;
	RelativeLayout complete_block, bg;
	ImageView icon;
	MyGoal newGoal;
	Calendar newCal, nextCheckin;
	SharedPreferences sharedPref;
	
	// For Goal complete date
	Calendar cal;
	
	// For Goal alarm
	Boolean morning, afternoon, evening, night;
	Long which_time;
	
	
	private static final String TABLE_GOALS = "GOALS";
	private static final String TABLE_PROG = "PROG";
	private static final String TABLE_FEED = "FEED";

	static final String ID = "ID";
	static final String FEEDID = "FEEDID";
	static final String TITLE = "TITLE";
	static final String CATEGORY = "CATEGORY";
	static final String SCHEDULE = "SCHEDULE";
	static final String START = "START";
	static final String DATE = "DATE";
	static final String FREQ_DAYS = "FREQDAYS";
	static final String FREQ_TIME = "FREQTIME";
	static final String PROGRESS = "PROGRESS";
	static final String ALARM_TIME = "ALARMTIME";
	static final String NEXT_CHECKIN = "NEXTCHECKIN";
	
	//static final String PROG_ID = "PROGID";
	static final String GOAL_ID = "GOALID";
	static final String NOTE = "NOTE";
	static final String PROG_DATE = "DATE";
	
	Context ctxt;
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");
	
	//String schedule = "Repeat";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        // set action bar as Overlay
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        
        setContentView(R.layout.row_activity_layout);
                
        // Making actionbar translucent - bg-A8A8A8, alpha-0.6
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#96A8A8A8")));
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
//      getActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>New Goal</font>"));
        
        
        
        TextView category_text = (TextView)findViewById(R.id.category);
        TextView title_text = (TextView)findViewById(R.id.title);
//      TextView complete_text = (TextView)findViewById(R.id.complete_text);
//      TextView freq_text = (TextView)findViewById(R.id.freq_text);
        repeat_block = (LinearLayout) findViewById(R.id.repeat_block);
        bg = (RelativeLayout) findViewById(R.id.row_activity_parent);
        icon = (ImageView) findViewById(R.id.icon);
        
        title = getIntent().getStringExtra("title");
        title_text.setText(title);
        category = getIntent().getStringExtra("category");
        category_text.setText(category);
        
        date_text = (TextView) findViewById(R.id.date_text);
        time_text = (TextView) findViewById(R.id.time_text);
        
//      level = getIntent().getIntExtra("level", 0);
//      level_text.setText(String.valueOf(level));
//      count_text.setText(String.valueOf(getIntent().getIntExtra("count", 0)));
        
        // Setting schedule (repeat/complete)
        schedule = getIntent().getStringExtra("schedule");

        
        // Getting the goal ID
        feed_id = getIntent().getIntExtra("id", 0);
        
        // Getting shared Preferences
        sharedPref = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        
        // Setting BG and icon as per category
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
        
        
        
        
        
        
        
//        // Setting start dropdown
//        Spinner start_goal = (Spinner)findViewById(R.id.start);
//        ArrayAdapter<CharSequence> start_goal_dropdown =  ArrayAdapter.createFromResource(this, R.array.start_goal_dropdown, android.R.layout.simple_spinner_item);
//        start_goal_dropdown.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        start_goal.setAdapter(start_goal_dropdown);
//        start_goal.setSelection(0);
//        
//        start_goal.setOnItemSelectedListener(new OnItemSelectedListener(){
//        
//	        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//	        	start_when = (String) parent.getItemAtPosition(pos);
//	        }
//        
//	        public void onNothingSelected(AdapterView<?> parent) {
//	        	// Nothing
//	        }
//        
//        });
        
        // Setting complete_date textview
        complete_date = (TextView)findViewById(R.id.complete);
        complete_block = (RelativeLayout) findViewById(R.id.complete_date_block);
        
        cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)+7);
        Date date = cal.getTime();
        String date_string = sdf.format(date);
        
        complete_date.setText(date_string);
//      complete_date.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        
        complete_block.setOnClickListener(new View.OnClickListener() {
        	
        	@Override
        	public void onClick (View v) {
        		Bundle bundl = new Bundle();
				bundl.putSerializable("calendar", cal);
				showDatePickerDialog(bundl);
        	}
        });
        
        
//        ArrayAdapter<CharSequence> complete_date_dropdown =  ArrayAdapter.createFromResource(this, R.array.complete_date_dropdown, android.R.layout.simple_spinner_item);
//        complete_date_dropdown.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        complete_date.setAdapter(complete_date_dropdown);
//        complete_date.setSelection(0);
//        
//        complete_date.setOnItemSelectedListener(new OnItemSelectedListener(){
//        
//	        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//	        	
//	        	if (pos==3) {
//	        		// show date-picker
//	        		showDatePickerDialog();
//	        	}
//	        	else {
//	        		complete_when = (String) parent.getItemAtPosition(pos);
//	        	}
//	        	
//	        }
//        
//	        public void onNothingSelected(AdapterView<?> parent) {
//	        	// Nothing
//	        }
//        
//        });
        
        // Setting freq_day spinner
        freq_day = (Spinner)findViewById(R.id.freq_day);
        ArrayAdapter<CharSequence> freq_day_dropdown =  ArrayAdapter.createFromResource(this, R.array.freq_day_dropdown, R.layout.spinner_text);
        freq_day_dropdown.setDropDownViewResource(R.layout.nav_drawer_list_item);
        freq_day.setAdapter(freq_day_dropdown);
        freq_day.setSelection(0);
        
        freq_day.setOnItemSelectedListener(new OnItemSelectedListener(){
        
	        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	        	frequency_days = (String) parent.getItemAtPosition(pos);
	        	date_text.setText(frequency_days);
	        }
        
	        public void onNothingSelected(AdapterView<?> parent) {
	        	// Nothing
	        }
        
        });
        
        // Setting freq_time spinner
        freq_time = (Spinner)findViewById(R.id.freq_time);
        ArrayAdapter<CharSequence> freq_time_dropdown =  ArrayAdapter.createFromResource(this, R.array.freq_time_dropdown, R.layout.spinner_text);
        freq_time_dropdown.setDropDownViewResource(R.layout.nav_drawer_list_item);
        freq_time.setAdapter(freq_time_dropdown);
        freq_time.setSelection(0);
        
        freq_time.setOnItemSelectedListener(new OnItemSelectedListener(){
        
	        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	        	frequency_time = (String) parent.getItemAtPosition(pos);
	        	time_text.setText(frequency_time);
	        }
        
	        public void onNothingSelected(AdapterView<?> parent) {
	        	// Nothing
	        }
        
        });
        
        
        
        
        //final String toastie; 
        
//        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//				RadioButton checkedButton = (RadioButton)findViewById(checkedId);
//				String option = checkedButton.getText().toString();
//				toastie = option;
//				
//				if (option.equals("Repeat")) {
//					complete_date.setVisibility(View.INVISIBLE);
//					freq.setVisibility(View.VISIBLE);
//				}
//				else {
//					freq.setVisibility(View.INVISIBLE);
//					complete_date.setVisibility(View.VISIBLE);
//				}
//				
//			}
//		});        
        
        
        // Setting appropriate view for the schedule (repeat/complete)
        if (schedule.equals("Repeat")) {
        	complete_block.setVisibility(View.GONE);
//	        freq_text.setText(schedule);
		}
		else {
//			complete_text.setText(schedule);
			repeat_block.setVisibility(View.GONE);
		}
        
        // Setting color of Category box
		
//		switch (category) {
//        case "Sports":
//            category_text.setBackgroundColor(Color.parseColor(getString(R.string.sports_row_color)));
//            break;
//		case "Music":
//			category_text.setBackgroundColor(Color.parseColor(getString(R.string.music_row_color)));
//        	break;
//		case "Fitness":
//			category_text.setBackgroundColor(Color.parseColor(getString(R.string.fitness_row_color)));
//			break;
//		case "Coding":
//			category_text.setBackgroundColor(Color.parseColor(getString(R.string.coding_row_color)));
//			break;
//		case "Reading":
//			category_text.setBackgroundColor(Color.parseColor(getString(R.string.reading_row_color)));
//			break;
//		case "Arts":
//			category_text.setBackgroundColor(Color.parseColor(getString(R.string.arts_row_color)));
//			break;
//		case "Cooking":
//			category_text.setBackgroundColor(Color.parseColor(getString(R.string.cooking_row_color)));
//			break;
//        }
        
		
		// Setting action for buttons
		add_goal=(Button)findViewById(R.id.add_goal);
/*		cancel=(Button)findViewById(R.id.cancel);
		
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v){
				onBackPressed();
			}
		});*/
		
		add_goal.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
//				add_goal.setText("Adding...");
				add_goal.setBackgroundColor(Color.parseColor("#828282"));
				add_goal.setEnabled(false);
				
				Date date = cal.getTime();
				
				progress = 0;
				
				if (!schedule.equalsIgnoreCase("Repeat")) {
					frequency_days = "N/A";
					frequency_time = "N/A";					
				}
				
				
				Log.d("dates", date.toString());
				
				Date start = new Date();

				
				// Removing Start date
				
/*				
				
				if (schedule.equals("Repeat")) {
					Calendar cal1 = Calendar.getInstance();
					switch (start_when) {
					case "Today":
						start = cal1.getTime();
						break;
					case "Tomorrow":
						cal1.add(Calendar.DAY_OF_YEAR, 1);
						start = cal1.getTime();
						break;
					case "Next week":
						cal1.add(Calendar.DAY_OF_YEAR, 7);
						start = cal1.getTime();
						break;
					}
				}*/
				
				Calendar alarmtime = Calendar.getInstance();
				Calendar nextcheckin = Calendar.getInstance();
				
//				Log.d("dates", start.toString());
				
				newGoal = new MyGoal(id, feed_id, title, category, schedule, start, date, frequency_days, frequency_time, progress, alarmtime, nextcheckin);
				//GoalDatabase db = GoalDatabase.getInstance(getApplicationContext());
				//db.addGoal(newGoal, RowActivity.this);
				
				String log = feed_id + "," + title + "," + category +  "," + schedule + ","  + start.toString()+ "," +
						date.toString() + "," + frequency_days + "," + frequency_time + "," + progress + "," + sdf.format(alarmtime.getTime())
								+ "," + sdf.format(nextcheckin.getTime()); 
				Log.d("db_insert", log);
				
				// Setting the first alarm and writing goal + alarm info to DB
				setAlarm(newGoal);
				
				// Going to Home Screen
//				goHome();
				
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

//	public void goHome() {
//		
//		Intent i = new Intent(this, MainActivity.class);
//		i.putExtra("toast", "Goal added");
//		i.putExtra("position", 0);
//		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		this.startActivity(i);
//	}
	
	public void setAlarm(MyGoal newGoal) {
		
		
		newCal = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		

		//cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) + 30);
		
		if (newGoal.getSchedule().equalsIgnoreCase("Repeat")) {
			
			// For Habits
			
			switch (newGoal.getFreqTime()) {

			case ("Morning"):

				Long morn_time = sharedPref.getLong("morn_time", 0);
				Date morn_date = new Date(morn_time);
				newCal.setTime(morn_date);
				newCal.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
				newCal.set(Calendar.MONTH, now.get(Calendar.MONTH));
				newCal.set(Calendar.YEAR, now.get(Calendar.YEAR));
				break;

			case ("Afternoon"):
				Long aft_time = sharedPref.getLong("aft_time", 0);
				Date aft_date = new Date(aft_time);
				newCal.setTime(aft_date);
				newCal.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
				newCal.set(Calendar.MONTH, now.get(Calendar.MONTH));
				newCal.set(Calendar.YEAR, now.get(Calendar.YEAR));
				break;

			case ("Evening"):
				Long eve_time = sharedPref.getLong("eve_time", 0);
				Date eve_date = new Date(eve_time);
				newCal.setTime(eve_date);
				newCal.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
				newCal.set(Calendar.MONTH, now.get(Calendar.MONTH));
				newCal.set(Calendar.YEAR, now.get(Calendar.YEAR));
				break;

			case ("Night"):
				Long night_time = sharedPref.getLong("night_time", 0);
				Date night_date = new Date(night_time);
				newCal.setTime(night_date);
				newCal.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
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
			
			// For Goals
			// Setting alarm times
			morning = sharedPref.getBoolean("morning", true);
			afternoon = sharedPref.getBoolean("afternoon", true);
			evening = sharedPref.getBoolean("evening", true);
			night = sharedPref.getBoolean("night", true);
			
			if (morning) {
				
				which_time = sharedPref.getLong("morn_time", 0);
				newGoal.setFreqTime("Morning");
			}
			
			else if (afternoon) {
				
				which_time = sharedPref.getLong("aft_time", 0);
				newGoal.setFreqTime("Afternoon");
			}
			
			else if (evening) {
				
				which_time = sharedPref.getLong("eve_time", 0);
				newGoal.setFreqTime("Evening");
			}
			
			else {
				
				which_time = sharedPref.getLong("night_time", 0);
				newGoal.setFreqTime("Night");
			}
			
			Date when = new Date (which_time);
			newCal.setTime(when);
			newCal.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
			newCal.set(Calendar.MONTH, now.get(Calendar.MONTH));
			newCal.set(Calendar.YEAR, now.get(Calendar.YEAR));
			
			// setting alarm dates
			long date_sec = cal.getTimeInMillis();
			long now_sec = now.getTimeInMillis();
			long diff = (date_sec - now_sec) / (24*60*60*1000);
			
			if (diff <=14) {
				// If less than 2 weeks, set alarm every 2 days
				if (diff > 2) {
					newCal.set(Calendar.DAY_OF_YEAR, newCal.get(Calendar.DAY_OF_YEAR) + 2);
					newGoal.setFreqDays("2");
				}
				else {
					newCal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR));
					newGoal.setFreqDays("N/A");
				}
				
			}
			else if (diff <=30) {
				// If less than 1 month, set alarm every 4 days
				
				newCal.set(Calendar.DAY_OF_YEAR, newCal.get(Calendar.DAY_OF_YEAR) + 4);
				newGoal.setFreqDays("4");
			}
			else if (diff <=90) {
				// If less than 3 months, set alarm once a week
				
				newCal.set(Calendar.DAY_OF_YEAR, newCal.get(Calendar.DAY_OF_YEAR) + 7);
				newGoal.setFreqDays("7");
			}
			else {
				// set alarm once every 2 weeks
				
				newCal.set(Calendar.DAY_OF_YEAR, newCal.get(Calendar.DAY_OF_YEAR) + 14);
				newGoal.setFreqDays("14");
			}
			
			
						
		}
		
		newGoal.setAlarmTime(newCal);
		
		
//		Intent i = new Intent(this, AlarmReceiver.class);
//		i.putExtra(ID, newGoal.getid());
//		i.putExtra(TITLE, newGoal.getTitle());
//		i.putExtra(CATEGORY, newGoal.getCategory());
//		i.putExtra(SCHEDULE, newGoal.getSchedule());
//		i.putExtra(DATE, newGoal.getDate());
//		i.putExtra(FREQ_DAYS, newGoal.getFreqDays());
//		i.putExtra(FREQ_TIME, newGoal.getFreqTime());
//		i.putExtra(ALARM_TIME, newCal);
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String alarm_string = sdf.format(newCal.getTime());
//		Log.d("ALARM", alarm_string);
//		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_ONE_SHOT);
//		
//		//id, title, category, schedule, start, date, frequency_days, frequency_time, progress, alarmtime
//		
//		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//		alarmMgr.set(AlarmManager.RTC_WAKEUP, newCal.getTimeInMillis(), pi);
//		
//
//		Log.d("Alarm", "Alarm is set");
//		String log = String.valueOf(newGoal.getid()) + "," + newGoal.getTitle() + "," + newGoal.getCategory() +  "," + newGoal.getSchedule() + ","  
//				+ newGoal.getFreqDays() + "," + newGoal.getFreqTime() + "," + sdf.format(newGoal.getAlarmTime().getTime()); 
//		Log.d("db_insert", log);
		
		new GetLatestGoalId().execute();
		
//		new AddGoal().execute(new MyGoal[] {newGoal});
		
	}
	
	
	
	private class AddGoal extends AsyncTask<MyGoal, Void, Boolean>{

		@SuppressLint("SimpleDateFormat")
		@Override
		protected Boolean doInBackground(MyGoal... goal) {
			GoalDatabase dbSingleton = GoalDatabase.getInstance(getApplicationContext());
			SQLiteDatabase db = dbSingleton.getWritableDatabase();
			
			// Inserting into goals table
			ContentValues values = new ContentValues();
			values.put(FEEDID, goal[0].getFeedId());
			values.put(TITLE, goal[0].getTitle());
			values.put(CATEGORY, goal[0].getCategory());
			values.put(SCHEDULE, goal[0].getSchedule());
			values.put(FREQ_DAYS, goal[0].getFreqDays());
			values.put(FREQ_TIME, goal[0].getFreqTime());
			values.put(PROGRESS, goal[0].getProgress());

			
			// for dates
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
			String start = dateFormat.format(goal[0].getStart());
			String date = dateFormat.format(goal[0].getDate());
			String alarmtime = dateFormat.format(goal[0].getAlarmTime().getTime());
			String nextcheckin_time = dateFormat.format(goal[0].getNextCheckin().getTime());
			

			Log.d("db_insert", start);
			Log.d("db_insert", date);
			Log.d("db_insert", alarmtime);
			Log.d("db_insert", nextcheckin_time);
			
			values.put(START, start);
			values.put(DATE, date);
			values.put(ALARM_TIME, alarmtime);
			values.put(NEXT_CHECKIN, nextcheckin_time);
			
			Log.d("db_insert", "I reached here");
			
//			Log.d("db_insert", dateFormat.format(goal[0].getStart()));
//			Log.d("db_insert", dateFormat.format(goal[0].getDate()));
			
			
			// inserting row
			db.insert(TABLE_GOALS, null, values);
			
			// Inserting into PROG table for goals
			ContentValues notes = new ContentValues();
			notes.put(GOAL_ID, id);
			notes.put(NOTE, "Added Goal");
			notes.put(PROG_DATE, dateFormat.format(goal[0].getStart()));
			
			db.insert(TABLE_PROG, null, notes);
			
			Log.d("db_insert", "Inserted into notes as well");
			
			// Updating Feed table "isAdded" column
			ContentValues isAdded = new ContentValues();
			isAdded.put("ISADDED", 1);
			
			db.update(TABLE_FEED, isAdded, "ID =" + goal[0].getFeedId(), null);
			
			db.close();			
			
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean status) {
			Log.d("DB_INSERT", status.toString());
			
			// Saving to Parse
			ParseObject feedGoal= new ParseObject("GoalInfo");
			feedGoal.put("userEmail", sharedPref.getString("user_email", "DNE"));
			feedGoal.put("goalId", newGoal.getid());
			feedGoal.put("goalTitle", title);
			feedGoal.put("category", category);
			feedGoal.put("type", schedule);
			feedGoal.put("fromFeed", "Y");
			feedGoal.put("action", "Goal Added");
			
//			feedGoal.saveInBackground();
			feedGoal.saveEventually();
			
			
			
			// Setting up alarm
			Intent i = new Intent(RowActivity.this, AlarmReceiver.class);
			i.putExtra(ID, newGoal.getid());
			i.putExtra(TITLE, newGoal.getTitle());
			i.putExtra(CATEGORY, newGoal.getCategory());
			i.putExtra(SCHEDULE, newGoal.getSchedule());
			i.putExtra(DATE, newGoal.getDate());
			i.putExtra(FREQ_DAYS, newGoal.getFreqDays());
			i.putExtra(FREQ_TIME, newGoal.getFreqTime());
			i.putExtra(ALARM_TIME, newCal);
			i.putExtra(NEXT_CHECKIN, newGoal.getNextCheckin());
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String alarm_string = sdf.format(newCal.getTime());
			Log.d("ALARM", alarm_string);
			PendingIntent pi = PendingIntent.getBroadcast(RowActivity.this, newGoal.getid(), i, PendingIntent.FLAG_ONE_SHOT);
			
			//id, title, category, schedule, start, date, frequency_days, frequency_time, progress, alarmtime
			
			AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			alarmMgr.set(AlarmManager.RTC_WAKEUP, newCal.getTimeInMillis(), pi);
			

			Log.d("Alarm", "Alarm is set");
			String log = String.valueOf(newGoal.getid()) + "," + newGoal.getTitle() + "," + newGoal.getCategory() +  "," + newGoal.getSchedule() + ","  
					+ newGoal.getFreqDays() + "," + newGoal.getFreqTime() + "," + sdf.format(newGoal.getAlarmTime().getTime()); 
			Log.d("db_insert", log);
			
			
			// Go back to Home screen
			Intent iForMainActivity = new Intent(RowActivity.this, MainActivity.class);
			iForMainActivity.putExtra("toast", "Goal added");
			iForMainActivity.putExtra("position", 0);
			iForMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			iForMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(iForMainActivity);
			//RowActivity.this.startActivity(i);
			
			
			// Do Nothing
			
		}
		//CATEGORY, START, LEVEL, SCHEDULE, DATE, FREQ);
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
			
			newGoal.setid(id);
			
			new AddGoal().execute(new MyGoal[] {newGoal});			
			
			// Do Nothing
			
		}
	}
	
	
	public class DatePickerFragment extends DialogFragment
									implements DatePickerDialog.OnDateSetListener {
		
		@Override
		public Dialog onCreateDialog (Bundle savedInstanceState) {
			
			Calendar c = Calendar.getInstance();
			
			Bundle bundle = this.getArguments();
			if (bundle !=null) {
				c = (Calendar) bundle.getSerializable("calendar");
			}
			
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			
			DatePickerDialog dp =  new DatePickerDialog(getActivity(), this, year, month, day);
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
		newFragment.show(getFragmentManager(), "datePicker");
		
	}
}

