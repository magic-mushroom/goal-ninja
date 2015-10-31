package com.goalninja.android;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.goalninja.android.R;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.viewpagerindicator.TabPageIndicator;

//import android.app.FragmentTransaction;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
//import android.app.ActionBar;
//import android.app.ActionBar.Tab;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;


@SuppressLint("SimpleDateFormat")
public class MainActivity extends FragmentActivity{

    ViewPager pager;
    private String[] navdrawer_items;
    private ListView navigation_drawer;
    private DrawerLayout drawer_layout;
    private ActionBarDrawerToggle drawer_toggle;
    RelativeLayout nav_drawer_layout;
    Button nav_drawer_button;

    SharedPreferences sharedPref;
    
    String user_email;
    
    // DB object to store goals
    //static GoalDatabase db = null;

    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        // Setting adapter for ViewPager
        pager=(ViewPager)findViewById(R.id.pager);
        
        pager.setAdapter(new SampleAdapter(this, getSupportFragmentManager()));  
        
        
        // Binding the title indicator to the adapter
        TabPageIndicator tabindicator = (TabPageIndicator)findViewById(R.id.tabs);
        tabindicator.setViewPager(pager);
        
        // Setting page change listener
        tabindicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener());
        
      	
       	
        // Handling intent extras to show Toast
        String toastie = getIntent().getStringExtra("toast");
        if (toastie!=null){
        	Toast toast = Toast.makeText(this, toastie, Toast.LENGTH_SHORT);
            toast.show();
        }
        
//        // Handling intent extras to show DialogFragment if from AlarmService
//        String fromAlarm = getIntent().getStringExtra("fromAlarm");
//        if (fromAlarm!=null && !fromAlarm.isEmpty()) {
//        	showFragmentDialog();
//        }
              
        
        // Setting Default SharedPreferences for reminder time
        
        Calendar morn = Calendar.getInstance();
        morn.set(Calendar.HOUR_OF_DAY, 8);
        morn.set(Calendar.MINUTE, 0);
        Date morn_date = morn.getTime();
        Long morn_time = morn_date.getTime();
        Log.d("settings_time_main", morn_time.toString());
        
        Calendar aft = Calendar.getInstance();
        aft.set(Calendar.HOUR_OF_DAY, 14);
        aft.set(Calendar.MINUTE, 0);
        Date aft_date = aft.getTime();
        Long aft_time = aft_date.getTime();
        
        Calendar eve = Calendar.getInstance();
        eve.set(Calendar.HOUR_OF_DAY, 17);
        eve.set(Calendar.MINUTE, 0);
        Date eve_date = eve.getTime();
        Long eve_time = eve_date.getTime();
        
        Calendar night = Calendar.getInstance();
        night.set(Calendar.HOUR_OF_DAY, 21);
        night.set(Calendar.MINUTE, 0);
        Date night_date = night.getTime();
        Long night_time = night_date.getTime();
        
        user_email = getEmail(this);
        Log.d("user_email", user_email);
        
        sharedPref = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        if (!sharedPref.contains("notif_yes")) {
        	SharedPreferences.Editor editor = sharedPref.edit();
        	editor.putBoolean("notif_yes", true);
        	editor.putBoolean("morning", true);
        	editor.putLong("morn_time", morn_time);
        	editor.putBoolean("afternoon", true);
        	editor.putLong("aft_time", aft_time);
        	editor.putBoolean("evening", true);
        	editor.putLong("eve_time", eve_time);
        	editor.putBoolean("night", true);
        	editor.putLong("night_time", night_time);
        	editor.putString("user_email", user_email);
//        	editor.putBoolean("from_boot", false);
        	
        	editor.commit();
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c3 = Calendar.getInstance();
        Log.d("calendar_test", sdf.format(c3.getTime()));
        
        c3.set(Calendar.DAY_OF_YEAR, c3.get(Calendar.DAY_OF_YEAR) + 120);
        Log.d("calendar_test_+120", sdf.format(c3.getTime()));
        
        Calendar now = Calendar.getInstance();
		Calendar next_yr = Calendar.getInstance();
		next_yr.set(Calendar.YEAR, 2015);
		next_yr.set(Calendar.DAY_OF_MONTH, 1);
		next_yr.set(Calendar.MONTH, 0);
		int diff = now.get(Calendar.DAY_OF_YEAR) - next_yr.get(Calendar.DAY_OF_YEAR);
		Log.d("Calendar_Test", String.valueOf(diff));
		Log.d("Calendar_Test_now", String.valueOf(now.get(Calendar.DAY_OF_YEAR)));
		Log.d("Calendar_Test_future", String.valueOf(now.get(Calendar.DAY_OF_MONTH)));
        
		
		// Analytics for AppOpened
		ParseAnalytics.trackAppOpenedInBackground(getIntent());
		
		
        
        
//        // Adding Action Bar tabs
//        final ActionBar actionBar = getActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//        
//        // Listener for tab change
//        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
//			
//			@Override
//			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
//				pager.setCurrentItem(tab.getPosition());
//				
//			}
//
//			@Override
//			public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//		};
//		
//		// Adding 3 tabs and setting tab's Text & TabListener
//		for(int i=0; i<3; i++){
//			switch(i){
//				case 0: actionBar.addTab(actionBar.newTab().setText(getString(R.string.tab1)).setTabListener(tabListener));
//						break;
//				case 1: actionBar.addTab(actionBar.newTab().setText(getString(R.string.tab2)).setTabListener(tabListener));
//						break;
//				case 2: actionBar.addTab(actionBar.newTab().setText(getString(R.string.tab3)).setTabListener(tabListener));
//						break;
//			}
//		};
//	
//		
//		// Adding Page change Listener
//		pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
//			@Override
//			public void onPageSelected(int position) {
//				getActionBar().setSelectedNavigationItem(position);				
//			}
//		});
//		
		
		// Initializing the Drawer List
		drawer_layout = (DrawerLayout)findViewById(R.id.drawer_layout);
		navdrawer_items = getResources().getStringArray(R.array.navigation_items);
		navigation_drawer = (ListView)findViewById(R.id.navigation_drawer);
		nav_drawer_layout = (RelativeLayout) findViewById(R.id.nav_drawer_layout);
		nav_drawer_button = (Button) findViewById(R.id.nav_drawer_button);
		
		// Setting adapter for populating the Drawer List and click listener
		navigation_drawer.setAdapter(new ArrayAdapter<String>(this, R.layout.nav_drawer_list_item, navdrawer_items));
		navigation_drawer.setOnItemClickListener(new DrawerItemClickListener());
		
		
		// Setting SendFeedback click listener 
		nav_drawer_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Setting EditText view to get user feedback
				final EditText feedback = new EditText(MainActivity.this);

				new AlertDialog.Builder(MainActivity.this)
						.setTitle(R.string.send_feedback)
						.setMessage(getText(R.string.feedback_text))
						.setView(feedback)
						.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int arg1) {
										
										String message = feedback.getText().toString();
										
										if (message != null && !message.isEmpty()) {
											
											// Saving note to Parse
											ParseObject feedbackObj= new ParseObject("Feedback");
											feedbackObj.put("userEmail", sharedPref.getString("user_email", "DNE"));
											feedbackObj.put("message", message);										

											feedbackObj.saveEventually();
										
											dialog.cancel();
											
											drawer_layout.closeDrawer(nav_drawer_layout); 
											
											// raise a toast
											Toast toast = Toast.makeText(MainActivity.this, getString(R.string.feedback_sent_msg), Toast.LENGTH_SHORT);
											toast.show();
										}
										
									}
									
								})
						.setNegativeButton(android.R.string.no,	new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,	int which) {
										dialog.cancel();

									}
								}).show();

			}

		});
				
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		// Handling open and close of Drawer
		drawer_toggle = new ActionBarDrawerToggle(this,drawer_layout,R.drawable.ic_drawer,R.string.open_drawer,R.string.close_drawer){
			
			public void onDrawerClosed(View view){
//				super.onDrawerClosed(view);
				getActionBar().setTitle(getString(R.string.app_name));
				drawer_layout.closeDrawer(nav_drawer_layout);
				invalidateOptionsMenu();
			}
			
			public void onDrawerOpened(View view){
//				super.onDrawerOpened(view);
				getActionBar().setTitle(getString(R.string.app_name));
				drawer_layout.openDrawer(nav_drawer_layout);
				invalidateOptionsMenu();
			}
		};
		
		
		drawer_layout.setDrawerListener(drawer_toggle);
		drawer_toggle.setDrawerIndicatorEnabled(true);
		
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);    
    	getMenuInflater().inflate(R.menu.main, menu);
        return true;
        }
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	//Nav drawer action on action bar app icon click
    	if (drawer_toggle.onOptionsItemSelected(item)){
    		return true;
    	}
    	
    	// Activity for adding new goal
    	switch(item.getItemId()){
    		case R.id.add_new_goal:
    			Intent iForNewGoal = new Intent(this, NewGoal.class);
    			startActivity(iForNewGoal);
    			return true;
    	}
        return super.onOptionsItemSelected(item);
       }
    
    // Called when invalidateOptionsMenu() is called
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
    	
    	//Hide action buttons on drawer open
    	boolean drawerOpen=drawer_layout.isDrawerOpen(nav_drawer_layout);
    	menu.findItem(R.id.add_new_goal).setVisible(!drawerOpen);
    	return super.onPrepareOptionsMenu(menu);
    	
    }
    
    
    
    // functions to be called when using actionbardrawertoggle
    @Override
    protected void onPostCreate(Bundle savedInstanceState){
    	super.onPostCreate(savedInstanceState);
    	drawer_toggle.syncState();
    }
    
    @Override
	public void onConfigurationChanged(Configuration newConfig){
    	super.onConfigurationChanged(newConfig);
    	drawer_toggle.onConfigurationChanged(newConfig);
    }
    
    
    
    // Drawer item click listener
    public class DrawerItemClickListener implements OnItemClickListener {

    	@Override
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    		showItem(position);

    	}
    }
    	
    // Open new activity on drawer option click
    private void showItem(int position) {
    	
    	Intent intent;
    	switch (position) {
    	case 0:
    		intent = new Intent(this, Archives.class);
        	startActivity(intent);
        	break;
        	
    	case 1: 
    		intent = new Intent(this, Settings.class);
        	startActivity(intent);
        	break;
    		
//    	case 2:
//    		intent = new Intent(this, Archives.class);
//    		startActivity(intent);
//    		break;
    	}
    	
    	
    	navigation_drawer.setItemChecked(position, true);
    	drawer_layout.closeDrawer(nav_drawer_layout);    	
    		
    }

    
	// Fetch primary email of user

	String getEmail(Context ctxt) {

		AccountManager accountMgr = AccountManager.get(ctxt);
		Account acct = getAccount(accountMgr);

		if (acct == null) {
			return null;
		} else {
			return acct.name;
		}
	}

	private Account getAccount(AccountManager accountMgr) {

		Account[] accts = accountMgr.getAccountsByType("com.google");
		Account acct;
		if (accts.length > 0) {
			acct = accts[0];
		} else {
			acct = null;
		}

		return acct;
	}
    
}

