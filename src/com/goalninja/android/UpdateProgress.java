package com.goalninja.android;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.goalninja.android.R;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class UpdateProgress extends Activity {
	
	int id, feed_id;
	SQLiteDatabase db;
	String title, category, schedule, start_text, date_text, freq_days, freq_time, start_text_simple, date_text_simple, alarm_time, nextcheckin_time, streak;
	int progress;
	Date start, date, alarm_date;
	Calendar alarm, startCal, nextCheckin;
	String date_format = "MMM dd";
	SimpleDateFormat sdf = new SimpleDateFormat(date_format);
	Button cancel;
	Button  save_progress;
	GoalDatabase dbsingleton;
	Menu MyMenu;
	MenuItem action_complete, action_archive;
	RelativeLayout bg;
	ImageView checkin_icon;
	
	TextView title_text, category_text, schedule_text;
	
	// For goals
	GoalProgAdapter progAdapter;
	ArrayList<Note> list_prog = new ArrayList<Note>();
	int row_id, goal_id;
	String note, added_date_string;
	String new_note;
	String fromAlarm;
	Date added_date, date_today;
	ListView list;
	EditText user_note;
	
	// For new alarm
	Boolean morning, afternoon, evening, night;
	SharedPreferences sharedPref;
	long which_time;
	Calendar newCal, now, newAlarm;
	CustomDialog cd;
	SuccessDialog cd1;
	
	// For goal/habit end
	Boolean thisIsTheEnd = false, endOfDay = false, endOfWeekday = false,
			endOfWeekend = false, endOfWeek = false, endOfFortnight = false,
			endOfMonth = false, archiveGoal = false, dateChanged = false,
			checkin = false;
	String msgGoal, msgHabit;
	
	
	// For notes
	SimpleDateFormat sdf_db = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");

	
	static final String PROGRESS = "PROGRESS";
	private static final String TABLE_GOALS = "GOALS";
	private static final String TABLE_PROG = "PROG";

	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // set action bar as Overlay
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        
        setContentView(R.layout.update_progress_layout);
        
        // Making actionbar translucent - bg-A8A8A8, alpha-0.6
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#96A8A8A8")));
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
//      getActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Journal</font>"));
        
//      getActionBar().setHomeAsUpIndicator(R.drawable.go);
//      getActionBar().setIcon(R.drawable.go);
        
        dbsingleton = GoalDatabase.getInstance(getApplicationContext());
        
        id = getIntent().getIntExtra("id", 0);
        schedule = getIntent().getStringExtra("schedule");
        fromAlarm = getIntent().getStringExtra("fromAlarm");
        category = getIntent().getStringExtra("category");
        
        // Finding out if habit record time
        if (fromAlarm.equalsIgnoreCase("daily")) {
        	endOfDay = true;
        }
        else if (fromAlarm.equalsIgnoreCase("friday")) {
        	endOfWeekday = true;
        }
        else if (fromAlarm.equalsIgnoreCase("sunday")) {
        	endOfWeekend = true;
        }
        else if (fromAlarm.equalsIgnoreCase("saturday")) {
        	endOfWeek = true;
        }
        else if (fromAlarm.equalsIgnoreCase("12") || fromAlarm.equalsIgnoreCase("28")) {
        	endOfFortnight = true;
        }
        else if (fromAlarm.equalsIgnoreCase("25")) {
        	endOfMonth = true;
        }
        
        
        //endOfDay, endOfWeekday, endOfWeekend, endOfWeek, endOfFortnight, endOfMonth;
        
        
        sharedPref = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        
        newCal = Calendar.getInstance();
        now = Calendar.getInstance();
        newAlarm = Calendar.getInstance();
        
        cd = new CustomDialog(UpdateProgress.this);
        cd1 = new SuccessDialog(UpdateProgress.this);
        

        list = (ListView) findViewById(R.id.progress_list);        
        
        title_text = (TextView) findViewById(R.id.update_title);
        schedule_text = (TextView) findViewById(R.id.update_schedule);
        save_progress = (Button) findViewById(R.id.save_note_button);
        
        user_note = (EditText) findViewById(R.id.add_note);

        
        // Setting background colors
        bg = (RelativeLayout) findViewById(R.id.update_goal_info);
        
        switch (category) {

		case "Career":
//			bg.setBackgroundColor(Color.parseColor("#283593"));
			bg.setBackgroundColor(Color.parseColor("#33B679"));
			break;

		case "Personal":
//			bg.setBackgroundColor(Color.parseColor("#C8871E"));
			bg.setBackgroundColor(Color.parseColor("#33B679"));
			break;

		case "Fitness":
//			bg.setBackgroundColor(Color.parseColor("#A03024"));
			bg.setBackgroundColor(Color.parseColor("#E5AE4F"));
			break;

		case "Food":
//			bg.setBackgroundColor(Color.parseColor("#006064"));
			bg.setBackgroundColor(Color.parseColor("#DF5948"));
			break;

		case "Literature":
//			bg.setBackgroundColor(Color.parseColor("#C8871E"));
			bg.setBackgroundColor(Color.parseColor("#547BCA"));
			break;

		case "Music":
//			bg.setBackgroundColor(Color.parseColor("#A03024"));
			bg.setBackgroundColor(Color.parseColor("#DF5948"));
			break;

		case "Lifestyle":
//			bg.setBackgroundColor(Color.parseColor("#006064"));
			bg.setBackgroundColor(Color.parseColor("#E5AE4F"));
			break;

		case "Tech":
//			bg.setBackgroundColor(Color.parseColor("#C8871E"));
			bg.setBackgroundColor(Color.parseColor("#33B679"));
			break;

		case "Travel/Adventure":
//			bg.setBackgroundColor(Color.parseColor("#283593"));
			bg.setBackgroundColor(Color.parseColor("#547BCA"));
			break;

		case "Other":
//			bg.setBackgroundColor(Color.parseColor("#A03024"));
			bg.setBackgroundColor(Color.parseColor("#33B679"));
			break;

		}
        
        
        new GetProgress().execute();
        
        date_today = new Date();
        
        
        // Getting the checkin icon and setting up a listener
        checkin_icon = (ImageView) findViewById(R.id.update_checkin);
        
        checkin_icon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!schedule.equalsIgnoreCase("Repeat")) {
					new AlertDialog.Builder(UpdateProgress.this)
					.setTitle(R.string.complete_goal_heading)
					.setMessage(R.string.complete_goal_msg)
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							new_note = "Goal completed";
							thisIsTheEnd = true;
							new SaveNote().execute();
							dialog.cancel();
							cd1.show();
							
						}
					})
					.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
							
						}
					})
					.show();
				}
				
				else {
					
					// For Habits
					// If already checked in, just raise a toast
					//You have already completed the task for %1$s!
					String msg_freq = "today";
					switch(freq_days){
					
					case ("Daily"):
						msg_freq = "today";
						break;
					
					case ("Weekdays"):
						msg_freq = "the week";
						break;
						
					case ("Weekends"):
						msg_freq = "the weekend";
						break;
						
					case ("Once a week"):
						msg_freq = "the week";
						break;
						
					case ("Fortnightly"):
						msg_freq = "the fortnight";
						break;
						
					case ("Monthly"):
						msg_freq = "the month";
						break;
					
					}
					
					
					int checkinAllowed1;
					if ((now.get(Calendar.YEAR) - nextCheckin.get(Calendar.YEAR)) > 0) {
						checkinAllowed1 = 1;
					}
					else {
						if ((now.get(Calendar.YEAR)- nextCheckin.get(Calendar.YEAR)) < 0) {
							checkinAllowed1 = -1;
						}
						else {
							checkinAllowed1 = now.get(Calendar.DAY_OF_YEAR) - nextCheckin.get(Calendar.DAY_OF_YEAR);
						}
						
					}
					
					if (checkinAllowed1 < 0) {

						// raise a toast
						Toast toast = Toast.makeText(UpdateProgress.this, String.format(getString(R.string.checkin_toast_msg), msg_freq), Toast.LENGTH_SHORT);
						toast.show();
					}

					else {
						
						// If not yet checked in
						// Are you sure you have completed the task for %1$s?

						new AlertDialog.Builder(UpdateProgress.this)
								.setTitle(R.string.checkin_heading)
								.setMessage(Html.fromHtml(String.format(getResources().getString(R.string.checkin_msg), msg_freq)))
								.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												checkin = true;
												progress = progress + 1;
												new_note = "";
												
												// Setting next checkin date
												switch (freq_days) {
												
												case "Daily": // setting to next day
													nextCheckin.set(Calendar.DAY_OF_YEAR, now.get(Calendar.DAY_OF_YEAR) + 1);
													break;
													
												case "Fortnightly": // setting to 15th of this month or 1st of next month
													if (now.get(Calendar.DAY_OF_MONTH) < 15) {
														nextCheckin.set(Calendar.DAY_OF_MONTH, 15);
													}
													else {
														nextCheckin.set(Calendar.MONTH, now.get(Calendar.MONTH) + 1);
														nextCheckin.set(Calendar.DAY_OF_MONTH, 1);
													}
													break;
													
												case "Monthly": // setting to next month 1st
													nextCheckin.set(Calendar.MONTH, now.get(Calendar.MONTH) + 1);
													nextCheckin.set(Calendar.DAY_OF_MONTH, 1);
													break;
													
												default: // setting to next Monday (week start)
													int days = Calendar.SATURDAY - now.get(Calendar.DAY_OF_WEEK) + 2;
													nextCheckin.add(Calendar.DAY_OF_YEAR, days);
													break;
												
												}
												
												Log.d("Checkin_Calendar", nextCheckin.toString());
												
												// Setting the checkin image and streak text
												checkin_icon.setImageResource(R.drawable.checkin_on);
												
												switch(freq_days){
												
												case "Daily":
													if (progress == 1){
														streak = progress + " day";
													}
													else {
														streak = progress + " days";
													}
													break;
													
												case "Weekdays":
													if (progress == 1){
														streak = progress + " week";
													}
													else {
														streak = progress + " weeks";
													}
													break;
													
												case "Weekends":
													if (progress == 1){
														streak = progress + " week";
													}
													else {
														streak = progress + " weeks";
													}
													break;
													
												case "Once a week":
													if (progress == 1){
														streak = progress + " week";
													}
													else {
														streak = progress + " weeks";
													}
													break;
													
												case "Fortnightly":
													if (progress == 1){
														streak = progress + " fortnight";
													}
													else {
														streak = progress + " fortnights";
													}
													break;
													
												case "Monthly":
													if (progress == 1){
														streak = progress + " month";
													}
													else {
														streak = progress + " months";
													}
													break;
												}
												
												
												schedule_text.setText(Html.fromHtml(String.format(getResources().getString(R.string.repeat_update), streak)));
												
												new SaveHabit().execute();
												
												dialog.cancel();

												// Deciding whether to show the SuccessDialog
												switch (freq_days) {

												case ("Daily"):
													if (progress % 7 == 0) {
														cd1.show();
													}
													break;

												case ("Weekdays"):
													if (progress % 2 == 0) {
														cd1.show();
													}
													break;

												case ("Weekends"):
													if (progress % 2 == 0) {
														cd1.show();
													}
													break;

												case ("Once a week"):
													if (progress % 2 == 0) {
														cd1.show();
													}
													break;

												case ("Fortnightly"):
													cd1.show();
													break;

												case ("Monthly"):
													cd1.show();
													break;

												}

											}
										})
								.setNegativeButton(android.R.string.no,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.cancel();

											}
										}).show();

					}
				}
				
				
			}
        	
        });        
        
        
        
        
        
        //new GetGoal().execute();
//        category_text.setText(category);
//        title_text.setText(title);
//        
//        // Parsing date into correct format
//        start_text = sdf.format(start);
//        date_text = sdf.format(date);
        
        

        
//        // Setting values depending on whether Repeat or Complete
//        if (schedule.equalsIgnoreCase("Repeat")) {        	
//        	schedule_text.setText("Added on " + start_text);
//        	save_progress.setText(R.string.done);
//        	if (progress==0){ 
//        		streak_text.setText("No current streak");
//        	}
//        	else {
//        		streak_text.setText("Current Streak : " + String.valueOf(progress));
//        	}
//        }
//        else {
//        	schedule_text.setText("Due on " + date_text);
//        	save_progress.setText(R.string.save);
//        	
//        	//new GetProgress().execute();
//        	
//        	streak_text.setVisibility(View.GONE);
//        }
        
        // Going back to Tab1
//        cancel.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				onBackPressed();
//			}
//		});
        
        
        // Trying to delete a note
//        Button delete_note = (Button) list.findViewById(R.id.delete_note_button);
//        
//        delete_note.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//
//				int note_id = (int) v.getTag();
//				progAdapter.remove(note_id);
//				progAdapter.notifyDataSetChanged();
//				
//				
//			}
//		});
        
        
        	
        // Saving note and calling appropriate async task for habit/goal
		save_progress.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				new_note = user_note.getText().toString();

				if (new_note != null && !new_note.isEmpty()) {
					
//					save_progress.setText("Saving...");
//					save_progress.setBackgroundColor(Color
//							.parseColor("#6E6E6E"));
					save_progress.setEnabled(false);
					
					// Hiding the keypad
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

					Note latest_note = new Note(id, date_today, new_note);

					list_prog.add(0, latest_note);

					progAdapter.notifyDataSetChanged();

					Log.d("db_new_note", new_note);
					
					user_note.getText().clear();
					
					save_progress.setBackgroundColor(Color.parseColor("#04B404"));
					save_progress.setEnabled(true);

					if (schedule.equalsIgnoreCase("Repeat")) {
						//streak_text.setText("Current Streak : "+ String.valueOf(progress + 1));
//						save_progress.setText(R.string.done);
						new SaveHabit().execute();
						
					} else {
						
//						save_progress.setText(R.string.save);
						new SaveNote().execute();
					}
				}
				
				
								
				
			} 
		});
        
       
    }
		
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate menu items in action bar

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.update_progress, menu);
		
//		MyMenu = menu;
//		action_complete = (MenuItem) menu.findItem(R.id.action_complete);
//		action_archive = (MenuItem) menu.findItem(R.id.action_archive);
		
//		if (schedule.equalsIgnoreCase("Repeat")) {
//			menu.findItem(R.id.action_complete).setVisible(false);
//		}
//		else {
//			menu.findItem(R.id.action_archive).setVisible(false);
//		}
		
		Log.d("optionsmenu_end", "End of OnCreateOptionsMenu");
		
		return super.onCreateOptionsMenu(menu);
		
	}
	
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	
    	case android.R.id.home:
    		Intent iFromUp = new Intent(this, MainActivity.class);
    		iFromUp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//    		iFromUp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		startActivity(iFromUp);
//    		NavUtils.navigateUpFromSameTask(this);
    		this.finish();
    		return true;
    	
    	case R.id.action_edit:
    		// New Activity for EditActivity 
    		Intent iForEditActivity = new Intent(this, EditGoal.class);
    		iForEditActivity.putExtra("title", title);
    		iForEditActivity.putExtra("category", category);
    		iForEditActivity.putExtra("schedule", schedule);
    		iForEditActivity.putExtra("start", start);
    		iForEditActivity.putExtra("date", date);
    		iForEditActivity.putExtra("freq_days", freq_days);
    		iForEditActivity.putExtra("freq_time", freq_time);
    		iForEditActivity.putExtra("progress", progress);
    		iForEditActivity.putExtra("alarm_time", alarm);
    		iForEditActivity.putExtra("next_checkin", nextCheckin);
    		iForEditActivity.putExtra("id", id);
    		
    		startActivity(iForEditActivity);
    		
    		return true;
    		
    	case R.id.action_delete:
    		new AlertDialog.Builder(this)
    			.setTitle(R.string.delete_goal_heading)
    			.setMessage(R.string.delete_goal_msg)
    			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						new DeleteGoal().execute();
						dialog.cancel();
						
					}
				})
				.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						
					}
				})
				.show();
    		return true;
    		
/*    	case R.id.action_complete:
    		new AlertDialog.Builder(this)
			.setTitle(R.string.complete_goal_heading)
			.setMessage(R.string.complete_goal_msg)
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					new_note = "Goal completed";
					thisIsTheEnd = true;
					new SaveNote().execute();
					dialog.cancel();
					
				}
			})
			.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					
				}
			})
			.show();
    		return true;*/
    		
    	case R.id.action_archive:
    		new AlertDialog.Builder(this)
			.setTitle(R.string.archive_goal_heading)
			.setMessage(R.string.archive_goal_msg)
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					new_note = "Did " + progress + " times";
					archiveGoal = true;
					new SaveHabit().execute();
					dialog.cancel();
					
				}
			})
			.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					
				}
			})
			.show();
    		return true;
    		
    	}
        return super.onOptionsItemSelected(item);
    }
	
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	
		if (!schedule.equalsIgnoreCase("Repeat")) {
			menu.getItem(2).setVisible(false);
		}
		
		return super.onPrepareOptionsMenu(menu);
		
	}
	
	
	private class GetProgress extends AsyncTask<Void, Void, ArrayList<Note>>{

		@Override
		protected ArrayList<Note> doInBackground(Void... arg0) {

			db = dbsingleton.getReadableDatabase();
			Cursor cursor = db.rawQuery("select * from PROG where GOALID = " + id , null);
			cursor.moveToLast(); 
			
			Log.d("db_get_notes", String.valueOf(cursor.moveToLast()));
			
			if(cursor.moveToLast()) {
			do {
				Note row;
				//row_id = cursor.getInt(0);
				goal_id = cursor.getInt(1);
				note = cursor.getString(2);
				added_date_string = cursor.getString(3);
				
				
				added_date = new Date(); 
				try {
					added_date = sdf_db.parse(added_date_string);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
								
				String log = goal_id + "," + note + "," + added_date_string; 
				Log.d("db_read_note", log);
				
				row = new Note(goal_id, added_date, note);
				
				list_prog.add(row);
				
			} while (cursor.moveToPrevious());
			}
			cursor.close();
			
			Cursor cursor1 = db.rawQuery("select * from GOALS where ID = " + id , null);
			cursor1.moveToFirst();

			if (cursor1.moveToFirst()) {
				do {
					feed_id = cursor1.getInt(1);
					title = cursor1.getString(2);
					category = cursor1.getString(3);
					schedule = cursor1.getString(4);
					start_text = cursor1.getString(5);
					date_text = cursor1.getString(6);
					freq_days = cursor1.getString(7);
					freq_time = cursor1.getString(8);
					progress = cursor1.getInt(9);
					alarm_time = cursor1.getString(10);
					nextcheckin_time = cursor1.getString(11);

				} while (cursor1.moveToNext());
			}
			cursor1.close();
			
			db.close();
			
			String log = feed_id + "," + title + "," + start_text + "," + date_text + "," + alarm_time + "," + nextcheckin_time + "," + freq_days;
			Log.d("db_read", log);
			return list_prog;

		}
		
		@Override
		protected void onPostExecute(ArrayList<Note> list_prog) {
			
			// Setting notes UI
			progAdapter = new GoalProgAdapter(UpdateProgress.this, list_prog, dbsingleton);
			list.setAdapter(progAdapter);
			progAdapter.notifyDataSetChanged();
			
			// Setting goal items UI
			//category_text.setText(category);
	        title_text.setText(title);
	        
	        start = new Date();
			try {
				start = sdf_db.parse(start_text);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			date = new Date();
			try {
				date = sdf_db.parse(date_text);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			alarm_date = new Date();
			try {
				alarm_date = sdf_db.parse(alarm_time);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			alarm = Calendar.getInstance();
			alarm.setTime(alarm_date);
			
			nextCheckin = Calendar.getInstance();
			try {
				nextCheckin.setTime(sdf_db.parse(nextcheckin_time));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			start_text_simple = sdf.format(start);
			date_text_simple = sdf.format(date);
			
			startCal = Calendar.getInstance();
			startCal.setTime(start);
			
			//streak_text.setVisibility(View.GONE);
			
			Log.d("db_get_progress", "Going to set menu item");
	        
			// Setting values depending on whether Repeat or Complete
			if (schedule.equalsIgnoreCase("Repeat")) {
				
				switch(freq_days){
				
				case "Daily":
					if (progress == 1){
						streak = progress + " day";
					}
					else {
						streak = progress + " days";
					}
					break;
					
				case "Weekdays":
					if (progress == 1) {
						streak = progress + " week";
					}
					else {
						streak = progress + " weeks";
					}
					break;
					
				case "Weekends":
					if (progress == 1){
						streak = progress + " week";
					}
					else {
						streak = progress + " weeks";
					}
					break;
					
				case "Once a week":
					if (progress == 1){
						streak = progress + " week";
					}
					else {
						streak = progress + " weeks";
					}
					
				case "Fortnightly":
					if (progress == 1){
						streak = progress + " fortnight";
					}	
					else {
						streak = progress + " fortnights";
					}
					break;
					
				case "Monthly":
					if (progress == 1){
						streak = progress + " month";
					}
					else {
						streak = progress + " months";
					}
					break;
				}
				
				
				if (progress == 0) {
					schedule_text.setText("Not yet started");
				}
				else {
					schedule_text.setText(Html.fromHtml(String.format(getResources().getString(R.string.repeat_update), streak)));
				}
				
				
				// Setting icon of checkin if "nextcheckin" date is in future
				int checkinAllowed;
				if ((now.get(Calendar.YEAR) - nextCheckin.get(Calendar.YEAR)) > 0) {
					checkinAllowed = 1;
				}
				else {
					if ((now.get(Calendar.YEAR)- nextCheckin.get(Calendar.YEAR)) < 0) {
						checkinAllowed = -1;
					}
					else {
						checkinAllowed = now.get(Calendar.DAY_OF_YEAR) - nextCheckin.get(Calendar.DAY_OF_YEAR);
					}
					
				}
				
				if (checkinAllowed < 0) {
					checkin_icon.setImageResource(R.drawable.checkin_on);
				}
				
//				if ((now.get(Calendar.YEAR) - nextCheckin.get(Calendar.YEAR)) > 0) {
//					checkinAllowed1 = 1;
//				}
//				else {
//					if ((now.get(Calendar.YEAR)- nextCheckin.get(Calendar.YEAR)) < 0) {
//						checkinAllowed1 = -1;
//					}
//					else {
//						checkinAllowed1 = now.get(Calendar.DAY_OF_YEAR) - nextCheckin.get(Calendar.DAY_OF_YEAR);
//					}
//					
//				}
//				
//				if (checkinAllowed1 < 0)	
//				

				
			} else {
				
				
				schedule_text.setText(Html.fromHtml(String.format(getResources().getString(R.string.complete_update), date_text_simple)));

			}
			
			
			
	        
			// For Habits, show Dialog as per value of fromAlarm
			if (schedule.equalsIgnoreCase("Repeat")) {
				
				if (!(fromAlarm.equalsIgnoreCase("yes") || fromAlarm.equalsIgnoreCase("no"))) {
					cd.show(); 
				}
				
			}
			else {
				
				// For GOALS, show dialog if endDate has passed already or if fromAlarm= "end";
				Calendar end_date = Calendar.getInstance();
				end_date.setTime(date);
				int diff;
				if ((now.get(Calendar.YEAR) - end_date.get(Calendar.YEAR)) > 0) {
					diff = 1;
				}
				else {
					if ((now.get(Calendar.YEAR) - end_date.get(Calendar.YEAR) < 0)) {
						diff = 0; 
					}
					else {
						diff = now.get(Calendar.DAY_OF_YEAR) - end_date.get(Calendar.DAY_OF_YEAR);
					}
					
				}
				
				Log.d("error_dialog", String.valueOf(diff));
				
				thisIsTheEnd = (fromAlarm.equalsIgnoreCase("end") || (diff>0)) ;
				
				if (thisIsTheEnd) {
//					showFragmentDialog();
					
					// Show Dialog here
//					CustomDialog cd = new CustomDialog(UpdateProgress.this);
					cd.show();
					
					
					
					
				}
			}
			
			
			
			
			
	        Log.d("db_get_progress", "I reached End of GetProgress");
		}
	}
	
	private class SaveNote extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			
			
			date_today = new Date();
			
			//GoalDatabase dbsingleton = GoalDatabase.getInstance(getApplicationContext());
			db = dbsingleton.getWritableDatabase();
			ContentValues note_values = new ContentValues();
			note_values.put("GOALID", id);
			note_values.put("NOTE", new_note);
			note_values.put("DATE", sdf_db.format(date_today));
			
			db.insert("PROG", null, note_values);
			
			Log.d("db_save_note", String.valueOf(id)+","+new_note+","+sdf.format(date_today));
			
			if (thisIsTheEnd) {
				ContentValues goal_end = new ContentValues();
				goal_end.put("DATE", sdf_db.format(date_today));
				goal_end.put("PROGRESS", -1);
				
				db.update(TABLE_GOALS, goal_end, "ID = " + id, null);
				
				ContentValues goal_end_feed = new ContentValues();
				goal_end_feed.put("ISADDED", 0);
				
				ParseObject endGoal= new ParseObject("GoalInfo");
				endGoal.put("userEmail", sharedPref.getString("user_email", "DNE"));
				endGoal.put("goalId", id);
				endGoal.put("goalTitle", title);
				endGoal.put("category", category);
				endGoal.put("type", schedule);
				
				if (feed_id!=0) {
					db.update("FEED", goal_end_feed, "ID = " + feed_id, null);
					
					
					endGoal.put("fromFeed", "Y");
					
				}
				
				else {
					
					endGoal.put("fromFeed", "N");
				}
				
				endGoal.put("action", "Goal complete");
				
//				endGoal.saveInBackground();
				endGoal.saveEventually();
				
				
				
			}
			
			else {
				
				Map<String, String> noteGoalInfo = new HashMap<String, String>();
				noteGoalInfo.put("category", category);
				noteGoalInfo.put("type", schedule);
				
				ParseAnalytics.trackEventInBackground("addNote", noteGoalInfo);
				
			}
			
			db.close();
			return null;

		}
		
		@Override
		protected void onPostExecute(Void args) {
			
			// Go to MainActivity if goal is complete
			if (thisIsTheEnd) {
				
				// remove alarm
				Intent i = new Intent(UpdateProgress.this, AlarmReceiver.class);
				PendingIntent pi = PendingIntent.getBroadcast(UpdateProgress.this, id, i, PendingIntent.FLAG_ONE_SHOT);
				
				if (pi!= null) {
					pi.cancel();
				}
				
//				String toast = "Goal marked as Complete";
//				goToMainActivity(toast);
				
			}
			Log.d("db_save_notes", "I reached End of SaveNote");
		}
	}
	
	
	private class SaveHabit extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			
			date_today = new Date();

			//GoalDatabase dbsingleton = GoalDatabase.getInstance(getApplicationContext());
			db = dbsingleton.getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put(PROGRESS, progress);
			
			db.update(TABLE_GOALS, cv, "ID =" + id, null);
			
			
		
			if (new_note != null && !new_note.isEmpty()) {
				ContentValues note_values = new ContentValues();
				note_values.put("GOALID", id);
				note_values.put("NOTE", new_note);
				note_values.put("DATE", sdf_db.format(date_today));

				db.insert(TABLE_PROG, null, note_values);

				Log.d("db_save_note", String.valueOf(id) + "," + new_note + "," + sdf.format(date_today));
			}
			
		
			
			if (archiveGoal) {
				ContentValues goal_archive = new ContentValues();
				goal_archive.put("PROGRESS", -1);
				
				db.update(TABLE_GOALS, goal_archive, "ID = " + id, null);
				
				ContentValues goal_archive_feed = new ContentValues();
				goal_archive_feed.put("ISADDED", 0);
											
				// Saving to Parse
				ParseObject endGoal= new ParseObject("GoalInfo");
				endGoal.put("userEmail", sharedPref.getString("user_email", "DNE"));
				endGoal.put("goalId", id);
				endGoal.put("goalTitle", title);
				endGoal.put("category", category);
				endGoal.put("type", schedule);
				
				if (feed_id!=0) {
					db.update("FEED", goal_archive_feed, "ID = " + feed_id, null);
					
					endGoal.put("fromFeed", "Y");
				}
				
				else {
					endGoal.put("fromFeed", "N");
				}
				
				endGoal.put("action", "Goal archived");
				
//				endGoal.saveInBackground();
				endGoal.saveEventually();
				
			}
			
			else if (!checkin){
			
				
				Map<String, String> noteGoalInfo = new HashMap<String, String>();
				noteGoalInfo.put("category", category);
				noteGoalInfo.put("type", schedule);
				
				ParseAnalytics.trackEventInBackground("addNote", noteGoalInfo);
				
//				ParseObject addNote= new ParseObject("GoalInfo");
//				addNote.put("goalTitle", title);
//				addNote.put("category", category);
//				addNote.put("type", schedule);
//
//				if (feed_id!=0) {
//					
//					addNote.put("fromFeed", "Y");
//					
//				}
//				
//				else {
//					
//					addNote.put("fromFeed", "N");
//				}
//				addNote.put("action", "Note added");
//				
//				addNote.saveInBackground();
			}
			else {
				ContentValues goal_checkin = new ContentValues();
				goal_checkin.put("NEXTCHECKIN", sdf_db.format(nextCheckin.getTime()));
				
				db.update(TABLE_GOALS, goal_checkin, "ID =" + id, null);
				
			}
			
			checkin = false;
			
			db.close();
			return null;

		}
		
		@Override
		protected void onPostExecute(Void args) {
			
			if (archiveGoal) {
				
				// remove alarm
				Intent i = new Intent(UpdateProgress.this, AlarmReceiver.class);
				PendingIntent pi = PendingIntent.getBroadcast(UpdateProgress.this, id, i, PendingIntent.FLAG_ONE_SHOT);
				
				if (pi!= null) {
					pi.cancel();
				}
				
				String toast = "Goal archived";
				goToMainActivity(toast);
			}
			Log.d("db_get_notes", "I reached End of SaveHabit");
			
//			Intent i = new Intent(UpdateProgress.this, MainActivity.class);
//			i.putExtra("toast", "Progress saved");
//			i.putExtra("position", 0);
//			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			UpdateProgress.this.startActivity(i);
		}
	}
	
	
	
	private class DeleteGoal extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			db = dbsingleton.getWritableDatabase();
			try {
				db.delete(TABLE_GOALS, "ID = " + id, null);
				
				ContentValues delete_goal = new ContentValues();
				delete_goal.put("ISADDED", 0);
				
				ParseObject endGoal= new ParseObject("GoalInfo");
				endGoal.put("userEmail", sharedPref.getString("user_email", "DNE"));
				endGoal.put("goalId", id);
				endGoal.put("goalTitle", title);
				endGoal.put("category", category);
				endGoal.put("type", schedule);									
				
				
				if (feed_id!=0) {
					db.update("FEED", delete_goal, "ID = " + feed_id, null);
					
					endGoal.put("fromFeed", "Y");
				}
				else {
					endGoal.put("fromFeed", "N");
				}
				
				endGoal.put("action", "Goal deleted");
//				endGoal.saveInBackground();
				endGoal.saveEventually();
				
				db.delete("PROG", "GOALID = " + id, null);
			}
			finally {
				db.close();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void args) {
			
			// remove alarm
			Intent i = new Intent(UpdateProgress.this, AlarmReceiver.class);
			PendingIntent pi = PendingIntent.getBroadcast(UpdateProgress.this, id, i, PendingIntent.FLAG_ONE_SHOT);
			
			if (pi!= null) {
				pi.cancel();
			}
			
			
			String toast = "Goal deleted";
			goToMainActivity(toast);
			
			Log.d("db_save_notes", "I reached End of DeleteGoal");
		}
	}
	
	public void goToMainActivity(String toast) {
		Intent i = new Intent(this, MainActivity.class);
		i.putExtra("toast", toast);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(i);
	}
	
	
	// To save new Goal end-date and setting new alarms
	private class SaveDate extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			db = dbsingleton.getWritableDatabase();
			try {
							
				ContentValues save_date = new ContentValues();
				
				save_date.put("DATE", sdf_db.format(date));
				save_date.put("FREQDAYS", freq_days);
				save_date.put("FREQTIME", freq_time);
				save_date.put("ALARMTIME", sdf_db.format(newAlarm.getTime()));
				
				db.update("GOALS", save_date, "ID = " + id, null);
				
			}
			finally {
				db.close();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void args) {
			
			cd.dismiss();
			
//			String toast = "Goal deleted";
//			goToMainActivity(toast);
			
			Log.d("db_save_date", "I reached End of SaveDate");
		}
	}
	
	
/*	// Defining Alarm Dialog Fragment
    public static class AlarmDialogFragment extends DialogFragment {

    	static AlarmDialogFragment newInstance(String fromAlarm, String schedule) {
    		AlarmDialogFragment alarmFrag =  new AlarmDialogFragment();
    		alarmFrag.setCancelable(true);
    		
    		Bundle args = new Bundle();
    		args.putString("fromAlarm", fromAlarm);
    		args.putString("schedule", schedule);
    		alarmFrag.setArguments(args);
    		return alarmFrag;
    	}
    	@Override
    	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			
    		String msgFromAlarm = getArguments().getString("fromAlarm");
    		String scheduleType = getArguments().getString("schedule");
    		
    		getDialog().setTitle(scheduleType);
    		
    		// To customize the dialogfragment
    		
    		// getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    		
    		View v = inflater.inflate(R.layout.alarm_fragment, container, false);
    		final EditText note_frag = (EditText) v.findViewById(R.id.dialog_add_note);
    		TextView tv = (TextView) v.findViewById(R.id.dialog_message);
    		if (scheduleType.equalsIgnoreCase("Repeat")) {
    			tv.setText(msgFromAlarm);
    		}
    		else {
    			tv.setText(getString(R.string.msg_goal_end));
    		}
    		
    		Button done = (Button) v.findViewById(R.id.dialog_done);
    		Button cancel = (Button) v.findViewById(R.id.dialog_cancel);
    		Button delay = (Button) v.findViewById(R.id.dialog_delay);
    		
    		done.setOnClickListener(new View.OnClickListener() {


				@Override
				public void onClick(View arg0) {
					new_note = note_frag.getText().toString();
					//SaveCompleteGoal();
				}
    			
    		});
    		    		
    		return v;
    		
    	}
    }
    

    
    // Calling Dialog Fragment
    public void showFragmentDialog() {
		DialogFragment alarmFragment = AlarmDialogFragment.newInstance(fromAlarm, schedule);
		alarmFragment.show(getFragmentManager(), "alarmFragment");
		
	}*/
    
    public class CustomDialog extends Dialog implements android.view.View.OnClickListener {

    	Activity context;
    	
		public CustomDialog(Activity context) {
			super(context);
			this.context=context;
		}
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			
			// Set the layout, views
			getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			
			setContentView(R.layout.alarm_dialog);
			
									
			// Set Title here
			//setTitle("Goal Deadline");
//			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.dialog_title);
			TextView dialog_title = (TextView) findViewById(R.id.dialog_title);
			ImageView dialog_icon = (ImageView) findViewById(R.id.dialog_icon);
			
			
			
			
			
			TextView tv = (TextView) findViewById(R.id.dialog_message);
    		
    		
//    		EditText note_frag = (EditText) findViewById(R.id.dialog_add_note);
    		
    		LinearLayout goal_buttons = (LinearLayout) findViewById(R.id.dialog_buttons);
    		Button completed = (Button) findViewById(R.id.dialog_done);
    		Button cancel = (Button) findViewById(R.id.dialog_cancel);
    		Button delay = (Button) findViewById(R.id.dialog_delay);
    		
    		LinearLayout habit_buttons = (LinearLayout) findViewById(R.id.dialog_habit_buttons);
    		Button done = (Button) findViewById(R.id.dialog_habit_done);
    		Button skip = (Button) findViewById(R.id.dialog_habit_skip);
			
    		// Habit
			if (schedule.equalsIgnoreCase("repeat")) {
				
				goal_buttons.setVisibility(View.GONE);
				
				if (endOfDay) {
					dialog_title.setText("Daily update");
					tv.setText(getString(R.string.daily_update));
					
				}
				else if (endOfWeekday) {
					dialog_title.setText("Weekday update");
					tv.setText(getString(R.string.weekday_update));
					new_note = "Done this week";
				}
				else if (endOfWeekend) {
					dialog_title.setText("Weekend update");
					tv.setText(getString(R.string.weekend_update));
					new_note = "Done this weekend";
				}
				else if (endOfWeek) {
					dialog_title.setText("Weekly update");
					tv.setText(getString(R.string.weekly_update));
					new_note = "Done this week";
				}
				else if (endOfFortnight) {
					dialog_title.setText("Fortnightly update");
					tv.setText(getString(R.string.fortnight_update));
					new_note = "Done this fortnight";
				}
				else if (endOfMonth) {
					dialog_title.setText("Monthly update");
					tv.setText(getString(R.string.month_update));
					new_note = "Done this month";
				}
							
				
				done.setOnClickListener(this);
				skip.setOnClickListener(this);	
				//endOfDay, endOfWeekday, endOfWeekend, endOfWeek, endOfFortnight, endOfMonth;
				
			}
			
			// Goal
			else {
				
				habit_buttons.setVisibility(View.GONE);
				
				dialog_title.setText("Goal Deadline");
				tv.setText(getString(R.string.msg_goal_end));
				
				completed.setOnClickListener(this);
				cancel.setOnClickListener(this);
				delay.setOnClickListener(this);
				
			}
    		

			
			// Setting category icon
			switch(category) {
			
			case "Career":
				dialog_icon.setImageResource(R.drawable.career_icon);
				break;
				
			case "Personal":
				dialog_icon.setImageResource(R.drawable.heart_icon);
				break;
				
			case "Fitness":
				dialog_icon.setImageResource(R.drawable.fitness_icon);
				break;
				
			case "Food":
				dialog_icon.setImageResource(R.drawable.food_icon);
				break;
								
			case "Literature":
				dialog_icon.setImageResource(R.drawable.book_icon);
				break;
				
			case "Music":
				dialog_icon.setImageResource(R.drawable.music_icon);
				break;
				
			case "Lifestyle":
				dialog_icon.setImageResource(R.drawable.trophy_icon);
				break;
				
			case "Tech":
				dialog_icon.setImageResource(R.drawable.tech_icon);
				break;
				
			case "Travel/Adventure":
				dialog_icon.setImageResource(R.drawable.map_icon);
				break;
								
			case "Other": 
				dialog_icon.setImageResource(R.drawable.target_icon);
				break;
		
			}
			
    		
    		
		}

		@Override
		public void onClick(View v) {
			
			EditText note_frag = (EditText) findViewById(R.id.dialog_add_note);
			
			switch (v.getId()) {
			
			case R.id.dialog_done:
				
				new_note = note_frag.getText().toString();
				
				if (new_note==null || new_note.isEmpty()) {
					new_note = "Goal Completed";
				}
				
				new SaveNote().execute();
				dismiss();
				cd1.show();
				break;
				
			case R.id.dialog_cancel:
				dismiss();
				break;
				
			case R.id.dialog_delay:
				// Get new date, change "date" and "alarmtime", save to DB, set new alarms
				Calendar date_cal = Calendar.getInstance();
				date_cal.setTime(date);
				
				Bundle bundl = new Bundle();
				bundl.putSerializable("calendar", date_cal);
				showDatePickerDialog(bundl);
				
				break;
				
			case R.id.dialog_habit_done:
				// something
				
				new_note = note_frag.getText().toString();
				
				if (new_note != null && !new_note.isEmpty()) {
					
					Note latest_note = new Note(id, date_today, new_note);

					list_prog.add(0, latest_note);

					progAdapter.notifyDataSetChanged();
					
				}	
				progress = progress + 1;
				checkin = true;
				
				switch (freq_days) {
				
				case "Daily": // setting to next day
					nextCheckin.set(Calendar.DAY_OF_YEAR, now.get(Calendar.DAY_OF_YEAR) + 1);
					break;
					
				case "Fortnightly": // setting to 15th of this month or 1st of next month
					if (now.get(Calendar.DAY_OF_MONTH) < 15) {
						nextCheckin.set(Calendar.DAY_OF_MONTH, 15);
					}
					else {
						nextCheckin.set(Calendar.MONTH, now.get(Calendar.MONTH) + 1);
						nextCheckin.set(Calendar.DAY_OF_MONTH, 1);
					}
					break;
					
				case "Monthly": // setting to next month 1st
					nextCheckin.set(Calendar.MONTH, now.get(Calendar.MONTH) + 1);
					nextCheckin.set(Calendar.DAY_OF_MONTH, 1);
					break;
					
				default: // setting to next Monday (week start)
					int days = Calendar.SATURDAY - now.get(Calendar.DAY_OF_WEEK) + 2;
					nextCheckin.add(Calendar.DAY_OF_YEAR, days);
					break;
				
				}
				
				Log.d("Checkin_Calendar", nextCheckin.toString());
				
				// Setting the checkin image and streak text
				checkin_icon.setImageResource(R.drawable.checkin_on);
				
				switch(freq_days){
				
				case "Daily":
					if (progress == 1){
						streak = progress + " day";
					}
					else {
						streak = progress + " days";
					}
					break;
					
				case "Weekdays":
					if (progress == 1){
						streak = progress + " week";
					}
					else {
						streak = progress + " weeks";
					}
					break;
					
				case "Weekends":
					if (progress == 1){
						streak = progress + " week";
					}
					else {
						streak = progress + " weeks";
					}
					break;
					
				case "Once a week":
					if (progress == 1){
						streak = progress + " week";
					}
					else {
						streak = progress + " weeks";
					}
					break;
					
				case "Fortnightly":
					if (progress == 1){
						streak = progress + " fortnight";
					}
					else {
						streak = progress + " fortnights";
					}
					break;
					
				case "Monthly":
					if (progress == 1){
						streak = progress + " month";
					}
					else {
						streak = progress + " months";
					}
					break;
				}
				
				
				schedule_text.setText(Html.fromHtml(String.format(getResources().getString(R.string.repeat_update), streak)));
				
				new SaveHabit().execute();
				
				dismiss();
				
				// Deciding whether to show SuccessDialog
				switch(freq_days){
				
				case ("Daily"):
					if (progress%7 == 0){
						cd1.show();
					}
					break;
					
				case ("Weekdays"):
					if (progress%2 == 0) {
						cd1.show();
					}
					break;
					
				case ("Weekends"):
					if (progress%2 == 0) {
						cd1.show();
					}
					break;
				
				case ("Once a week"):
					if (progress%2 == 0) {
						cd1.show();
					}
					break;
					
				case ("Fortnightly"):
					cd1.show();
					break;
					
				case ("Monthly"):
					cd1.show();
					break;
					
				}
				
				break;
				
			case R.id.dialog_habit_skip:
				dismiss();
				break;
			
			}
			
		}
    	
    }
    
    
    public class SuccessDialog extends Dialog implements android.view.View.OnClickListener {

    	Activity context;
    	
		public SuccessDialog(Activity context) {
			super(context);
			this.context=context;
		}
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			
			// Set the layout, views
			getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			
			setContentView(R.layout.success_dialog);
			
									
//			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.dialog_title);
//			TextView success_title = (TextView) findViewById(R.id.success_dialog_title);
			ImageView success_icon = (ImageView) findViewById(R.id.success_dialog_icon);
			
			TextView success_msg = (TextView) findViewById(R.id.success_dialog_msg);
//			success_msg.setText("Just some placeholder text to see how it looks.\nLet's also try 2 lines!");
			
			if (schedule.equalsIgnoreCase("Repeat")) {
				
				switch (freq_days) {

				case ("Daily"):
					success_msg.setText(getString(R.string.daily_success));
					break;
					
				case ("Fortnightly"):
					success_msg.setText(getString(R.string.fortnightly_success));
					break;
					
				case ("Monthly"):
					success_msg.setText(getString(R.string.monthly_success));
					break;
					
				default:
					success_msg.setText(getString(R.string.weekly_success));
					break;
					
				}
				
			}
			
			else {
				
				success_msg.setText(getString(R.string.goal_success));				
				
			}
    		
    		Button success_done = (Button) findViewById(R.id.success_dialog_done);
    		success_done.setOnClickListener(this);
    		
			// Setting category icon
			switch(category) {
			
			case "Career":
				success_icon.setImageResource(R.drawable.career_icon);
				break;
				
			case "Personal":
				success_icon.setImageResource(R.drawable.heart_icon);
				break;
				
			case "Fitness":
				success_icon.setImageResource(R.drawable.fitness_icon);
				break;
				
			case "Food":
				success_icon.setImageResource(R.drawable.food_icon);
				break;
								
			case "Literature":
				success_icon.setImageResource(R.drawable.book_icon);
				break;
				
			case "Music":
				success_icon.setImageResource(R.drawable.music_icon);
				break;
				
			case "Lifestyle":
				success_icon.setImageResource(R.drawable.trophy_icon);
				break;
				
			case "Tech":
				success_icon.setImageResource(R.drawable.tech_icon);
				break;
				
			case "Travel/Adventure":
				success_icon.setImageResource(R.drawable.map_icon);
				break;
								
			case "Other": 
				success_icon.setImageResource(R.drawable.target_icon);
				break;
		
			}
    		
		}

		@Override
		public void onClick(View v) {
			
			switch (v.getId()) {
			
			case R.id.success_dialog_done:
				
				if (schedule.equalsIgnoreCase("Repeat")) {
					
					dismiss();
					break;
					
				}
				else {
					
					dismiss();
					goToMainActivity("Goal completed");
					break;
					
				}
				
								
			}
			
		}
    	
    }
    
    
    
	public class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			Calendar c = Calendar.getInstance();

			Bundle bundle = this.getArguments();
			if (bundle != null) {
				c = (Calendar) bundle.getSerializable("calendar");
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

			
						
			newCal.set(Calendar.YEAR, year);
			newCal.set(Calendar.MONTH, monthOfYear);
			newCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			String new_date = sdf.format(newCal.getTime());
			date = newCal.getTime();
			schedule_text.setText("Due on " + new_date);
			
			setNewAlarm();
			
			

		}

	}

	public void showDatePickerDialog(Bundle bundl) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.setArguments(bundl);
		newFragment.show(getFragmentManager(), "datePickerDialog");

	}
	
	public void setNewAlarm() {
		
		// Removing previous alarm
		
		Intent i = new Intent(UpdateProgress.this, AlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(UpdateProgress.this, id,
				i, PendingIntent.FLAG_ONE_SHOT);

		if (pi != null) {
			pi.cancel();
		}
		
				
		// New alarm times
		// Setting alarm times
		morning = sharedPref.getBoolean("morning", true);
		afternoon = sharedPref.getBoolean("afternoon", true);
		evening = sharedPref.getBoolean("evening", true);
		night = sharedPref.getBoolean("night", true);

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

		Date when = new Date(which_time);
		newAlarm.setTime(when);
		newAlarm.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
		newAlarm.set(Calendar.MONTH, now.get(Calendar.MONTH));
		newAlarm.set(Calendar.YEAR, now.get(Calendar.YEAR));

		// setting alarm dates
		long date_sec = newCal.getTimeInMillis();
		long start_sec = startCal.getTimeInMillis();
		long diff = (date_sec - start_sec) / (24 * 60 * 60 * 1000);

		if (diff <= 14) {
			// If less than 2 weeks, set alarm every 2 days
			if (diff > 2) {
				newAlarm.set(Calendar.DAY_OF_YEAR, newAlarm.get(Calendar.DAY_OF_YEAR) + 2);
				freq_days = "2";
				
			} else {
				
				newAlarm.set(Calendar.DAY_OF_YEAR, newCal.get(Calendar.DAY_OF_YEAR));
				freq_days = "N/A";
			}

		} else if (diff <= 30) {
			
			// If less than 1 month, set alarm every 4 days

			newAlarm.set(Calendar.DAY_OF_YEAR, newAlarm.get(Calendar.DAY_OF_YEAR) + 4);
			freq_days = "4";
			
		} else if (diff <= 90) {
			
			// If less than 3 months, set alarm once a week

			newAlarm.set(Calendar.DAY_OF_YEAR, newAlarm.get(Calendar.DAY_OF_YEAR) + 7);
			freq_days = "7";
			
		} else {
			
			// set alarm once every 2 weeks

			newAlarm.set(Calendar.DAY_OF_YEAR, newAlarm.get(Calendar.DAY_OF_YEAR) + 14);
			freq_days = "14";
		}
		
		
		
		// Setting new alarm
		Intent iForNewAlarm = new Intent(this, AlarmReceiver.class);
		iForNewAlarm.putExtra("ID", id);
		iForNewAlarm.putExtra("TITLE", title);
		iForNewAlarm.putExtra("CATEGORY", category);
		iForNewAlarm.putExtra("SCHEDULE", schedule);
		iForNewAlarm.putExtra("DATE", date);
		iForNewAlarm.putExtra("FREQDAYS", freq_days);
		iForNewAlarm.putExtra("FREQTIME", freq_time);
		iForNewAlarm.putExtra("ALARMTIME", newAlarm);		
		
		
		PendingIntent piForNewAlarm = PendingIntent.getBroadcast(this, id, iForNewAlarm, PendingIntent.FLAG_ONE_SHOT);
		
		//id, title, category, schedule, date, frequency_days, frequency_time, alarmtime
		
		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmMgr.set(AlarmManager.RTC_WAKEUP, newAlarm.getTimeInMillis(), piForNewAlarm);
		
		Log.d("new_alarm", "I reached end of setNewAlarm");

		new SaveDate().execute();
	}
	
}
