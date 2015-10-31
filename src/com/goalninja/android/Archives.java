package com.goalninja.android;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.goalninja.android.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("SimpleDateFormat")
public class Archives extends Activity {
	
	ListView archivedGoals;
	TextView noArchivedGoals;
	ArrayList<MyGoal> myArchivedGoals = new ArrayList<MyGoal>();
	ArchiveAdapter archiveAdapter;
	SQLiteDatabase db;
	GoalDatabase dbSingleton;
	String title, category, schedule, start_string, date_string, freq_days, freq_time, alarm_string, nextcheckin_string;
	Date start, date;
	Calendar alarmtime, nextcheckin;
	int id, feed_id, progress;
	SimpleDateFormat sdf_db = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
	SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
	
	Context context;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archives_layout);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        archivedGoals = (ListView) findViewById(R.id.archived_goals);
        noArchivedGoals = (TextView) findViewById(R.id.no_archived_goals);
        
        archivedGoals.setVisibility(View.GONE);
        noArchivedGoals.setText(R.string.loading);
        
        context = this;
        
        myArchivedGoals.clear();        
        new GetArchives().execute();
        
        
	}
	
	
	private class GetArchives extends AsyncTask<Void, Void, ArrayList<MyGoal>> {

		// TABLE GOALS (ID, TITLE, CATEGORY, SCHEDULE, START, DATE, FREQDAYS,
		// FREQTIME, PROGRESS)

		@SuppressLint("SimpleDateFormat")
		@Override
		protected ArrayList<MyGoal> doInBackground(Void... args) {

			dbSingleton = GoalDatabase.getInstance(getApplicationContext());
			db = dbSingleton.getReadableDatabase();
			Cursor cursor = db.rawQuery("select * from GOALS where PROGRESS = -1", null);
			cursor.moveToFirst();

			if (cursor.moveToFirst()) {
				do {
					MyGoal row;
					id = cursor.getInt(0);
					feed_id = cursor.getInt(1);
					title = cursor.getString(2);
					category = cursor.getString(3);
					schedule = cursor.getString(4);
					start_string = cursor.getString(5);
					date_string = cursor.getString(6);
					freq_days = cursor.getString(7);
					freq_time = cursor.getString(8);
					progress = cursor.getInt(9);
					alarm_string = cursor.getString(10);
					nextcheckin_string = cursor.getString(11);
					

					String log = String.valueOf(id) + "," + title + "," + category + ","
							+ schedule + "," + start_string + "," + date_string
							+ "," + freq_days + "," + freq_time;
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

					alarmtime = Calendar.getInstance();
					try {
						alarmtime.setTime(sdf_db.parse(alarm_string));
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
					
					
					row = new MyGoal(id, feed_id, title, category, schedule, start, date, freq_days, freq_time, progress, alarmtime, nextcheckin);

					myArchivedGoals.add(row);

				} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
			return myArchivedGoals;
		}

		@Override
		protected void onPostExecute(ArrayList<MyGoal> myGoals) {

			if (myArchivedGoals.isEmpty()) {
				archivedGoals.setVisibility(View.GONE);
				noArchivedGoals.setVisibility(View.VISIBLE);
				noArchivedGoals.setText(R.string.no_archived_goals);
				Log.d("possible_exception", "No archived goals");
			} 
			
			else {
				archiveAdapter = new ArchiveAdapter(context, myArchivedGoals);
				archivedGoals.setAdapter(archiveAdapter);
				archiveAdapter.notifyDataSetChanged();
				archivedGoals.setVisibility(View.VISIBLE);
				noArchivedGoals.setVisibility(View.GONE);
				Log.d("db_read_yes", String.valueOf(myArchivedGoals.isEmpty()));

				archivedGoals.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> adapter, View v,
							int position, long id) {

						// Passing the goal ID through intent to UpdateProgress
						// activity
						Intent i = new Intent(context, ArchivedGoal.class);
						MyGoal goal = myArchivedGoals.get(position);
						i.putExtra("category", goal.getCategory());
						// i.putExtra("schedule", goal.getSchedule());
						// i.putExtra("start", goal.getStart());
						// i.putExtra("date", goal.getDate());
						// i.putExtra("freq_days", goal.getFreqDays());
						// i.putExtra("freq_time", goal.getFreqTime());
						// i.putExtra("progress", goal.getProgress());

						i.putExtra("id", goal.getid());

						Log.d("send to ArchivedGoal", String.valueOf(goal.getid()));

						startActivity(i);
					}

				});
			}
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
