package com.goalninja.android;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.goalninja.android.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Tab1 extends Fragment {
	
	static Tab1 newInstance(int pos) {

		// Returning an instance of Tab1
		Tab1 tab1 = new Tab1();

		// Supplying position as an argument
		Bundle args = new Bundle();
		args.putInt("tab_no", pos);
		tab1.setArguments(args);

		return (tab1);
	}
	
	HomeAdapter myAdapter;
	ArrayList<MyGoal> myList = new ArrayList<MyGoal>();
	ListView myGoalsList;
	TextView noGoals;
	SQLiteDatabase db;
	String title, category, start_string, schedule, date_string, freq_days, freq_time, alarmtime_string, nextcheckin_string;
	int level, progress, id, feedId;
	Date start, date;
	Calendar alarmtime, nextcheckin;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View layout = inflater.inflate(R.layout.tab1, container, false);
		Log.d("lifecycle", "onCreateView()");
		return layout;
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		Log.d("lifecycle", "onActivityCreated()");

		
		// Initializing the Listview
		myGoalsList = (ListView)getActivity().findViewById(R.id.my_added_goals);
		noGoals = (TextView)getActivity().findViewById(R.id.no_goals);
		
//		myGoalsList.setItemsCanFocus(false);
		
		myList.clear();
		new GetGoal().execute();
		
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
//		if (myList.isEmpty()) {
//			// do nothing
//			Log.d("on resume", "why am i here");
//		}
//		else {
//
//			myGoalsList.setVisibility(View.VISIBLE);
//			noGoals.setVisibility(View.INVISIBLE);
//			myList.clear();
//			Log.d("on resume", "Do nothing");
//		}
		Log.d("lifecycle", "onResume()");
//		myList.clear();
//		new GetGoal().execute();
		
	}		
		
	
	private class GetGoal extends AsyncTask<Void, Void, ArrayList<MyGoal>>{
		
//		TABLE GOALS (_id INTEGER PRIMARY KEY AUTOINCREMENT, FEEDID, TITLE, CATEGORY, SCHEDULE, START, DATE, FREQDAYS, FREQTIME, PROGRESS, ALARMTIME)
		
		@SuppressLint("SimpleDateFormat")
		@Override
		protected ArrayList<MyGoal> doInBackground(Void... args) {
			
			GoalDatabase dbSingleton = GoalDatabase.getInstance(getActivity().getApplicationContext());
			db = dbSingleton.getReadableDatabase();
			Cursor cursor = db.rawQuery("select * from GOALS where PROGRESS<>-1" , null);
			cursor.moveToLast(); 
			
			if(cursor.moveToLast()) {
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
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				start = new Date();
				try {
					start = dateFormat.parse(start_string);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				date = new Date();
				try {
					date = dateFormat.parse(date_string);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				alarmtime = Calendar.getInstance();
				try {
					alarmtime.setTime(dateFormat.parse(alarmtime_string));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				nextcheckin = Calendar.getInstance();
				try {
					nextcheckin.setTime(dateFormat.parse(nextcheckin_string));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				row = new MyGoal(id, feedId, title, category, schedule, start, date, freq_days, freq_time, progress, alarmtime, nextcheckin);
				
				myList.add(row);
				
			} while (cursor.moveToPrevious());
			}
			cursor.close();
			db.close();
			return myList;
		}
		
		@Override
		protected void onPostExecute(ArrayList<MyGoal> myGoals) {
			
			if (myGoals.isEmpty()) {
				myGoalsList.setVisibility(View.GONE);
				noGoals.setVisibility(View.VISIBLE);
				noGoals.setText(getText(R.string.no_goals_added));
				Log.d("possible_exception1", "No goals added");
			}
			else {
				myAdapter = new HomeAdapter(getActivity(), myGoals);
				myGoalsList.setAdapter(myAdapter);
				myAdapter.notifyDataSetChanged();
				myGoalsList.setVisibility(View.VISIBLE);
				noGoals.setVisibility(View.GONE);
				Log.d("db_read_yes", String.valueOf(myList.isEmpty()));
				
				myGoalsList.setOnItemClickListener(new OnItemClickListener(){
					 
					@Override
					public void onItemClick(AdapterView<?> adapter, View v, int position, long id){
						
						// Passing the goal ID through intent to UpdateProgress activity
						Intent i = new Intent(getActivity(), UpdateProgress.class);
						MyGoal goal = myList.get(position);
//						i.putExtra("title", goal.getTitle());
//						i.putExtra("category", goal.getCategory());
//						i.putExtra("schedule", goal.getSchedule());
//						i.putExtra("start", goal.getStart());
//						i.putExtra("date", goal.getDate());
//						i.putExtra("freq_days", goal.getFreqDays());
//						i.putExtra("freq_time", goal.getFreqTime());
//						i.putExtra("progress", goal.getProgress());
						
						i.putExtra("id", goal.getid());
						i.putExtra("schedule", goal.getSchedule());
						i.putExtra("category", goal.getCategory());
						i.putExtra("fromAlarm", "no");

						Log.d("send to UpdateProgress", String.valueOf(goal.getid()));
						
						startActivity(i);
					}

					});
				
				
			}
		}	
			//Log.d("db_read", status.toString());
		}
	
}








