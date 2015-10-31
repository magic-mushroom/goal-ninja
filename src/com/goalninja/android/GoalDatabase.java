package com.goalninja.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GoalDatabase extends SQLiteOpenHelper {
	
	private static GoalDatabase singleton = null;
	
	private static final String DB_NAME = "mygoals.db";
	private static final int SCHEMA = 1;
	//private static final String TABLE_GOALS = "GOALS";
	//private static final String TABLE_PROG = "PROG";
	//private static final String TABLE_ALARM = "ALARM";

	static final String ID = "ID";
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
	
	synchronized static GoalDatabase getInstance(Context ctxt) {
		if (singleton == null) {
			singleton = new GoalDatabase(ctxt.getApplicationContext());
		}
		
		return(singleton);
	}
	
	public GoalDatabase(Context ctxt) {
		super(ctxt, DB_NAME, null, SCHEMA);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try{
			db.beginTransaction();
			db.execSQL("CREATE TABLE GOALS (ID INTEGER PRIMARY KEY AUTOINCREMENT, FEEDID, TITLE, CATEGORY, SCHEDULE, START, DATE, FREQDAYS, FREQTIME, PROGRESS, ALARMTIME, NEXTCHECKIN);");
			db.execSQL("CREATE TABLE PROG (_id INTEGER PRIMARY KEY AUTOINCREMENT, GOALID, NOTE, DATE);");
			db.execSQL("CREATE TABLE FEED (ID, TITLE, CATEGORY, SCHEDULE, ISADDED);");
			
			// Inserting the initial feed values
			ContentValues cv=new ContentValues();
			
			cv.put("ID", 1);
			cv.put("TITLE", "Write the best thing about the day");
			cv.put("CATEGORY", "Lifestyle");
			cv.put("SCHEDULE", "Repeat");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 2);
			cv.put("TITLE", "Prepare for the half-marathon");
			cv.put("CATEGORY", "Fitness");
			cv.put("SCHEDULE", "Complete");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 3);
			cv.put("TITLE", "Plan a road trip!");
			cv.put("CATEGORY", "Travel/Adventure");
			cv.put("SCHEDULE", "Complete");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 4);
			cv.put("TITLE", "Learn to code");
			cv.put("CATEGORY", "Tech");
			cv.put("SCHEDULE", "Complete");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 5);
			cv.put("TITLE", "Volunteer for my favourite cause");
			cv.put("CATEGORY", "Lifestyle");
			cv.put("SCHEDULE", "Repeat");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 6);
			cv.put("TITLE", "Go for a morning run");
			cv.put("CATEGORY", "Fitness");
			cv.put("SCHEDULE", "Repeat");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 7);
			cv.put("TITLE", "Go skydiving! Be Superman!");
			cv.put("CATEGORY", "Travel/Adventure");
			cv.put("SCHEDULE", "Complete");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 8);
			cv.put("TITLE", "Learn to speak Spanish, amigo");
			cv.put("CATEGORY", "Lifestyle");
			cv.put("SCHEDULE", "Complete");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 9);
			cv.put("TITLE", "Practice Yoga");
			cv.put("CATEGORY", "Fitness");
			cv.put("SCHEDULE", "Repeat");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 10);
			cv.put("TITLE", "Get that promotion I so deserve");
			cv.put("CATEGORY", "Career");
			cv.put("SCHEDULE", "Complete");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 11);
			cv.put("TITLE", "Plan an outing with family");
			cv.put("CATEGORY", "Personal");
			cv.put("SCHEDULE", "Complete");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 12);
			cv.put("TITLE", "Try different new cuisines");
			cv.put("CATEGORY", "Food");
			cv.put("SCHEDULE", "Repeat");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 13);
			cv.put("TITLE", "Call my mom");
			cv.put("CATEGORY", "Lifestyle");
			cv.put("SCHEDULE", "Repeat");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 14);
			cv.put("TITLE", "Write a novel");
			cv.put("CATEGORY", "Literature");
			cv.put("SCHEDULE", "Complete");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 15);
			cv.put("TITLE", "Create my own website/blog");
			cv.put("CATEGORY", "Tech");
			cv.put("SCHEDULE", "Complete");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 16);
			cv.put("TITLE", "Lose 10 lbs, get that fab body!");
			cv.put("CATEGORY", "Fitness");
			cv.put("SCHEDULE", "Complete");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 17);
			cv.put("TITLE", "Plan for a Europe backpacking trip");
			cv.put("CATEGORY", "Travel/Adventure");
			cv.put("SCHEDULE", "Complete");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 18);
			cv.put("TITLE", "Catch up on the news everyday");
			cv.put("CATEGORY", "Lifestyle");
			cv.put("SCHEDULE", "Repeat");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 19);
			cv.put("TITLE", "Find the awesome job I would love");
			cv.put("CATEGORY", "Career");
			cv.put("SCHEDULE", "Complete");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 20);
			cv.put("TITLE", "Call your friends for a dinner party");
			cv.put("CATEGORY", "Lifestyle");
			cv.put("SCHEDULE", "Repeat");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 21);
			cv.put("TITLE", "No fast food for a month");
			cv.put("CATEGORY", "Fitness");
			cv.put("SCHEDULE", "Complete");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 22);
			cv.put("TITLE", "Cook a new dish every weekend");
			cv.put("CATEGORY", "Food");
			cv.put("SCHEDULE", "Repeat");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 23);
			cv.put("TITLE", "Read a book regularly");
			cv.put("CATEGORY", "Literature");
			cv.put("SCHEDULE", "Repeat");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 24);
			cv.put("TITLE", "Learn to play guitar, be a rockstar");
			cv.put("CATEGORY", "Music");
			cv.put("SCHEDULE", "Complete");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 25);
			cv.put("TITLE", "Save up to buy a car");
			cv.put("CATEGORY", "Lifestyle");
			cv.put("SCHEDULE", "Complete");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 26);
			cv.put("TITLE", "Read tech blogs and news sites");
			cv.put("CATEGORY", "Tech");
			cv.put("SCHEDULE", "Repeat");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 27);
			cv.put("TITLE", "Plan a trip this month");
			cv.put("CATEGORY", "Travel/Adventure");
			cv.put("SCHEDULE", "Complete");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 28);
			cv.put("TITLE", "Let's startup");
			cv.put("CATEGORY", "Career");
			cv.put("SCHEDULE", "Complete");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 29);
			cv.put("TITLE", "Plan a special romantic dinner");
			cv.put("CATEGORY", "Personal");
			cv.put("SCHEDULE", "Complete");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 30);
			cv.put("TITLE", "Go play tennis on weekends");
			cv.put("CATEGORY", "Fitness");
			cv.put("SCHEDULE", "Repeat");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 31);
			cv.put("TITLE", "Be vegetarian for a month");
			cv.put("CATEGORY", "Food");
			cv.put("SCHEDULE", "Complete");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 32);
			cv.put("TITLE", "Read atleast 3 articles daily");
			cv.put("CATEGORY", "Literature");
			cv.put("SCHEDULE", "Repeat");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 33);
			cv.put("TITLE", "Explore a new band every week");
			cv.put("CATEGORY", "Music");
			cv.put("SCHEDULE", "Repeat");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			cv.put("ID", 34);
			cv.put("TITLE", "Make an Android app");
			cv.put("CATEGORY", "Tech");
			cv.put("SCHEDULE", "Complete");
			cv.put("ISADDED", 0);
			db.insert("FEED", null, cv);
			
			db.setTransactionSuccessful();
		}
		finally {
			db.endTransaction();
		}
		
	}
	
//	feedList = new ArrayList<Feed>();
//	Feed row1 = new Feed("Play tennis every weekend", "Sports", "Repeat", 0);
//	feedList.add(row1);
//	Feed row2 = new Feed("Learn to play Stairway to Heaven", "Music", "Complete", 1);
//	feedList.add(row2);
//	Feed row3 = new Feed("Lose 10 lbs", "Fitness", "Complete", 2);
//	feedList.add(row3);
//	Feed row4 = new Feed("Make an Android app", "Coding", "Complete", 3);
//	feedList.add(row4);
//	Feed row5 = new Feed("Read 1 book every month", "Reading", "Repeat", 4);
//	feedList.add(row5);
//	Feed row6 = new Feed("Learn basics of Photography", "Arts", "Complete", 5);
//	feedList.add(row6);

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	

	
	// Adding a goal
/*	
	public void addGoal (MyGoal goal, Context context){
		this.ctxt=context;
		new AddGoal().execute(new MyGoal[] {goal});
	}
	
	private class AddGoal extends AsyncTask<MyGoal, Void, Boolean>{

		@SuppressLint("SimpleDateFormat")
		@Override
		protected Boolean doInBackground(MyGoal... goal) {
			SQLiteDatabase db = singleton.getWritableDatabase();
			
			// Inserting into goals table
			ContentValues values = new ContentValues();
			values.put(ID, goal[0].getid());
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
			

			Log.d("db_insert", start);
			Log.d("db_insert", date);
			
			values.put(START, start);
			values.put(DATE, date);
			
			Log.d("db_insert", "I reached here");
			
//			Log.d("db_insert", dateFormat.format(goal[0].getStart()));
//			Log.d("db_insert", dateFormat.format(goal[0].getDate()));
			
			
			// inserting row
			db.insert(TABLE_GOALS, null, values);
			
			// Inserting into PROG table for goals
			ContentValues notes = new ContentValues();
			notes.put(GOAL_ID, goal[0].getid());
			notes.put(NOTE, "Added Goal");
			notes.put(PROG_DATE, dateFormat.format(goal[0].getStart()));
			
			db.insert(TABLE_PROG, null, notes);
			
			Log.d("db_insert", "Inserted into notes as well");
			
			db.close();			
			
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean status) {
			Log.d("DB_INSERT", status.toString());
			
			
			Intent i = new Intent(ctxt, MainActivity.class);
			i.putExtra("toast", "Goal added");
			i.putExtra("position", 0);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			ctxt.startActivity(i);
			
			
			// Do Nothing
			
		}
		//CATEGORY, START, LEVEL, SCHEDULE, DATE, FREQ);
	}*/
	
}

