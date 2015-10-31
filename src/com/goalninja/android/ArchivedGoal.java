package com.goalninja.android;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.goalninja.android.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ArchivedGoal extends Activity{
	
	// For Goals
	String title, category, schedule, start_string, date_string, freq_days, freq_time, alarm_string;
	int id, progress;
	TextView archive_title, archive_category, archive_start, archive_end;
	SimpleDateFormat sdf_db = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss", Locale.US);
	SimpleDateFormat sdf = new SimpleDateFormat("MMM dd", Locale.US);
	
	// For Notes
	String note, added_date_string; 
	int goal_id;
	ArrayList<Note> NoteList = new ArrayList<Note>();
	ListView notes;
	Date added_date;
	GoalProgAdapter progAdapter;
	
	Date start, date;
	
	SQLiteDatabase db;
	GoalDatabase dbSingleton;
	
	// For BG
	RelativeLayout bg;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // set action bar as Overlay
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        
        setContentView(R.layout.archived_goal_layout);
        
        // Making actionbar translucent - bg-A8A8A8, alpha-0.6
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#96A8A8A8")));
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        archive_title = (TextView) findViewById(R.id.archive_title);
//      archive_category = (TextView) findViewById(R.id.archive_category);
        archive_start = (TextView) findViewById(R.id.archive_start);
        archive_end = (TextView) findViewById(R.id.archive_end);
        
        bg = (RelativeLayout) findViewById(R.id.archive_goal_info);
        
//      archive_date_text = (TextView) findViewById(R.id.archive_start);
        
        
        notes = (ListView) findViewById(R.id.archive_progress_list);
        
        id = getIntent().getIntExtra("id", 0);
        category = getIntent().getStringExtra("category");
                
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
        
        NoteList.clear();
        new GetArchiveGoal().execute();
        
	}
	
	private class GetArchiveGoal extends AsyncTask<Void, Void, Void> {

		// TABLE GOALS (ID, TITLE, CATEGORY, SCHEDULE, START, DATE, FREQDAYS,
		// FREQTIME, PROGRESS)

		@SuppressLint("SimpleDateFormat")
		@Override
		protected Void doInBackground(Void... args) {

			dbSingleton = GoalDatabase.getInstance(getApplicationContext());
			db = dbSingleton.getReadableDatabase();
			Cursor cursor = db.rawQuery("select * from GOALS where ID = " + id,	null);
			cursor.moveToFirst();

			if (cursor.moveToFirst()) {
				do {
					
					
					title = cursor.getString(2);
					category = cursor.getString(3);
					schedule = cursor.getString(4);
					start_string = cursor.getString(5);
					date_string = cursor.getString(6);
					freq_days = cursor.getString(7);
					freq_time = cursor.getString(8);
					progress = cursor.getInt(9);
					alarm_string = cursor.getString(10);

					String log = String.valueOf(id) + "," + title + "," + category
							+ "," + schedule + "," + start_string + ","
							+ date_string + "," + freq_days + "," + freq_time;
					Log.d("db_read_archives", log);

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

				} while (cursor.moveToNext());
			}
			cursor.close();
			
			Cursor cursor1 = db.rawQuery("select * from PROG where GOALID = " + id , null);
			cursor1.moveToLast(); 
			
			Log.d("db_get_notes", String.valueOf(cursor1.moveToLast()));
			
			if(cursor1.moveToLast()) {
			do {
				Note row;
				//row_id = cursor.getInt(0);
				goal_id = cursor1.getInt(1);
				note = cursor1.getString(2);
				added_date_string = cursor1.getString(3);
				
				
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
				
				NoteList.add(row);
				
			} while (cursor1.moveToPrevious());
			}
			cursor1.close();
			
			db.close();
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {

			// Update Goal info
			archive_title.setText(title);
//			archive_category.setText(category);
			archive_start.setText("Started on " + sdf.format(start));
			
			if (schedule.equalsIgnoreCase("Repeat")) {
				
				archive_end.setText("Repeat " + freq_days);				
			}
			
			else {
				
				archive_end.setText("Completed on " + sdf.format(date));		
			}
			
			
			// Update Notes
			progAdapter = new GoalProgAdapter(ArchivedGoal.this, NoteList, dbSingleton);
			notes.setAdapter(progAdapter);
			progAdapter.notifyDataSetChanged();
			
			
			
		}

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
	
	
	
}
