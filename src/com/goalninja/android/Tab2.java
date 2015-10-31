package com.goalninja.android;

import java.util.ArrayList;

import com.goalninja.android.R;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
//import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Tab2 extends Fragment {

	static Tab2 newInstance(int pos){
		
		// Returning an instance of Tab1
		Tab2 tab2 = new Tab2();
		
		// Supplying position as an argument
		Bundle args = new Bundle();
		args.putInt("tab_no", pos);
		tab2.setArguments(args);
		
		return(tab2);		
	}
	
	private ArrayList<Feed> feedList = new ArrayList<Feed>();
	private FeedAdapter adapter;
	String title, category, schedule;
	int isAdded, id;
	ListView lv;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View layout = inflater.inflate(R.layout.tab2, container, false);
		return layout;
		
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		lv = (ListView)getActivity().findViewById(R.id.list);
		
		feedList.clear();
		new GetFeed().execute();
		
//		// Setting row elements (TBA: async task to download json & parsing it)
//		feedList = new ArrayList<Feed>();
//		Feed row1 = new Feed("Play tennis every weekend", "Sports", "Repeat", 0);
//		feedList.add(row1);
//		Feed row2 = new Feed("Learn to play Stairway to Heaven", "Music", "Complete", 1);
//		feedList.add(row2);
//		Feed row3 = new Feed("Lose 10 lbs", "Fitness", "Complete", 2);
//		feedList.add(row3);
//		Feed row4 = new Feed("Make an Android app", "Coding", "Complete", 3);
//		feedList.add(row4);
//		Feed row5 = new Feed("Read 1 book every month", "Reading", "Repeat", 4);
//		feedList.add(row5);
//		Feed row6 = new Feed("Learn basics of Photography", "Arts", "Complete", 5);
//		feedList.add(row6);
		
		
		// Setting adapter to populate 
//		adapter = new FeedAdapter(getActivity().getApplicationContext(), feedList);
//		lv.setAdapter(adapter);
		
		// Setting click listener
		lv.setOnItemClickListener(new OnItemClickListener(){
			
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long id){
				
				// Getting all items of the selected row Feed
				String title = feedList.get(position).getTitle();
				String category = feedList.get(position).getCategory();
				String schedule = feedList.get(position).getSchedule();
								
				// Passing the items through intent to RowActivity
				Intent i = new Intent(getActivity(), RowActivity.class);
				i.putExtra("title", title);
				i.putExtra("category", category);
				i.putExtra("schedule", schedule);
				i.putExtra("id", feedList.get(position).getId());
				startActivity(i);
			}

			});
	}
	
	private class GetFeed extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			GoalDatabase dbSingleton = GoalDatabase.getInstance(getActivity().getApplicationContext());
			SQLiteDatabase db = dbSingleton.getReadableDatabase();
			Cursor cursor = db.rawQuery("select * from FEED where ISADDED = 0" , null);
			cursor.moveToFirst(); 
			
			if(cursor.moveToFirst()) {
			do {
				Feed row;
				id = cursor.getInt(0);
				title = cursor.getString(1);
				category = cursor.getString(2);
				schedule = cursor.getString(3);
				isAdded = cursor.getInt(4);
								
				String log = id + "," + title + "," + category + "," + schedule + "," + isAdded; 
				Log.d("db_read_feed", log);
				
				row = new Feed(title, category, schedule, id, isAdded);
				
				feedList.add(row);
				
			} while (cursor.moveToNext());
			}
			cursor.close();
			db.close();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void args) {
			adapter = new FeedAdapter(getActivity(), feedList);
			lv.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		
		
	}
}



