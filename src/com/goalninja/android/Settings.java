package com.goalninja.android;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.goalninja.android.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

@SuppressLint("SimpleDateFormat")
public class Settings extends Activity {
	
	TextView morning_time, afternoon_time, evening_time, night_time;
	Switch notif;
//	RelativeLayout notif_yes_layout;
	View mask;
	CheckBox morn, aft, eve, night;
	SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
	Calendar cal_morn, cal_aft, cal_eve, cal_night;
	SharedPreferences sharedPref;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        morning_time = (TextView) findViewById(R.id.morning_time);
        afternoon_time = (TextView) findViewById(R.id.afternoon_time);
        evening_time = (TextView) findViewById(R.id.evening_time);
        night_time = (TextView) findViewById(R.id.night_time);
        
//      notif_yes_layout = (RelativeLayout) findViewById(R.id.notif_yes);
        //mask = (View) findViewById(R.id.mask);
        notif = (Switch) findViewById(R.id.show_notification);
        morn = (CheckBox) findViewById(R.id.morning);
        aft = (CheckBox) findViewById(R.id.afternoon);
        eve = (CheckBox) findViewById(R.id.evening);
        night = (CheckBox) findViewById(R.id.night);
        
        
        // Getting values from shared preferences
        sharedPref = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean notif_yes = sharedPref.getBoolean("notif_yes", true);
        //boolean notif_yes = false;
        if (notif_yes) {
        	notif.setChecked(true);
        	//mask.setVisibility(View.GONE);
        }
        else {
        	notif.setChecked(false);
        	//mask.setAlpha((float) 0.4);
        	morning_time.setEnabled(false);
        	afternoon_time.setEnabled(false);
        	evening_time.setEnabled(false);
        	night_time.setEnabled(false);
        	morn.setEnabled(false);
        	aft.setEnabled(false);
        	eve.setEnabled(false);
        	night.setEnabled(false);
        	
        }
        
        
        // Setting morning checkbox and time from SharedPref
        boolean morning = sharedPref.getBoolean("morning", true);
        if (morning) {
        	morn.setChecked(true);
        }
        else {
        	morn.setChecked(false);
        	morning_time.setEnabled(false);
        }
        
        Long morn_time = sharedPref.getLong("morn_time", 0);
        Log.d("settings_time_sett", morn_time.toString());
        Date morn_date = new Date(morn_time);
        Log.d("settings_date", morn_date.toString());
        cal_morn = Calendar.getInstance();
        cal_morn.setTime(morn_date);
        
        String time = sdf.format(cal_morn.getTime());
        morning_time.setText(time);
        
        
        // Setting afternoon checkbox and time from SharedPref
        boolean afternoon = sharedPref.getBoolean("afternoon", true);
        if (afternoon) {
        	aft.setChecked(true);
        }
        else {
        	aft.setChecked(false);
        	afternoon_time.setEnabled(false);
        }
        
        Long aft_time = sharedPref.getLong("aft_time", 0);
        Log.d("settings_time_sett", aft_time.toString());
        Date aft_date = new Date(aft_time);
        Log.d("settings_date", aft_date.toString());
        cal_aft = Calendar.getInstance();
        cal_aft.setTime(aft_date);
        
        String time2 = sdf.format(cal_aft.getTime());
        afternoon_time.setText(time2);
        
        // Setting evening checkbox and time from SharedPref
        boolean evening = sharedPref.getBoolean("evening", true);
        if (evening) {
        	eve.setChecked(true);
        }
        else {
        	eve.setChecked(false);
        	evening_time.setEnabled(false);
        }
        
        Long eve_time = sharedPref.getLong("eve_time", 0);
        Log.d("settings_time_sett", eve_time.toString());
        Date eve_date = new Date(eve_time);
        Log.d("settings_date", eve_date.toString());
        cal_eve = Calendar.getInstance();
        cal_eve.setTime(eve_date);
        
        String time3 = sdf.format(cal_eve.getTime());
        evening_time.setText(time3);
        
        
        
        // Setting night checkbox and time from SharedPref
        boolean night_check = sharedPref.getBoolean("night", true);
        if (night_check) {
        	night.setChecked(true);
        }
        else {
        	night.setChecked(false);
        	night_time.setEnabled(false);
        }
        
        Long nigh_time = sharedPref.getLong("night_time", 0);
        Log.d("settings_time_sett", nigh_time.toString());
        Date night_date = new Date(nigh_time);
        Log.d("settings_date", night_date.toString());
        cal_night = Calendar.getInstance();
        cal_night.setTime(night_date);
        
        String time4 = sdf.format(cal_night.getTime());
        night_time.setText(time4);
        
        
        // Setting onclick to create TimePickerdialog and pass current values in a bundle for morning
        morning_time.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle bundl = new Bundle();
				bundl.putSerializable("calendar", cal_morn);
				bundl.putInt("which", 1);
				showTimePickerDialog(bundl);
			}
		});
        
        
        // Setting onclick to create TimePickerdialog and pass current values in a bundle for afternoon
        afternoon_time.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle bundl = new Bundle();
				bundl.putSerializable("calendar", cal_aft);
				bundl.putInt("which", 2);
				showTimePickerDialog(bundl);
			}
		});
        
        
        // Setting onclick to create TimePickerdialog and pass current values in a bundle for evening
        evening_time.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle bundl = new Bundle();
				bundl.putSerializable("calendar", cal_eve);
				bundl.putInt("which", 3);
				showTimePickerDialog(bundl);
			}
		});
        
        
        // Setting onclick to create TimePickerdialog and pass current values in a bundle for night
        night_time.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle bundl = new Bundle();
				bundl.putSerializable("calendar", cal_night);
				bundl.putInt("which", 4);
				showTimePickerDialog(bundl);
			}
		});	
        
        
        
        // Setting onCheckedChangeListener for notif switch
        notif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					//mask.setVisibility(View.GONE);
					if (morn.isChecked()) {
		        		morning_time.setEnabled(true);	
		        	}
		        	if (aft.isChecked()) {
		        		afternoon_time.setEnabled(true);	
		        	}
		        	if (eve.isChecked()) {
		        		evening_time.setEnabled(true);	
		        	}
		        	if (night.isChecked()) {
		        		night_time.setEnabled(true);	
		        	}
		        	morn.setEnabled(true);
		        	aft.setEnabled(true);
		        	eve.setEnabled(true);
		        	night.setEnabled(true);
		        	
		        	// saving to SharedPreferences
		        	SharedPreferences.Editor editor = sharedPref.edit();
		        	editor.putBoolean("notif_yes", true);   		        	
		        	editor.commit();
		        }
		        else {
		        	//mask.setAlpha((float) 0.4);
		        	if (morn.isChecked()) {
		        		morning_time.setEnabled(false);	
		        	}
		        	if (aft.isChecked()) {
		        		afternoon_time.setEnabled(false);	
		        	}
		        	if (eve.isChecked()) {
		        		evening_time.setEnabled(false);	
		        	}
		        	if (night.isChecked()) {
		        		night_time.setEnabled(false);	
		        	}
		        	
		        	morn.setEnabled(false);
		        	aft.setEnabled(false);
		        	eve.setEnabled(false);
		        	night.setEnabled(false);
		        	
		        	// saving to SharedPreferences
		        	SharedPreferences.Editor editor = sharedPref.edit();
		        	editor.putBoolean("notif_yes", false);   		        	
		        	editor.commit();
		        }
				
			}
		});

        
        
        // Setting onCheckedChangeListener for morning checkbox
        morn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					morning_time.setEnabled(true);
					
					// saving to SharedPreferences
		        	SharedPreferences.Editor editor = sharedPref.edit();
		        	editor.putBoolean("morning", true);   		        	
		        	editor.commit();
				}
				else {
					morning_time.setEnabled(false);
					
					// saving to SharedPreferences
		        	SharedPreferences.Editor editor = sharedPref.edit();
		        	editor.putBoolean("morning", false);   		        	
		        	editor.commit();
				}
				
			}
		});
        
        
        // Setting onCheckedChangeListener for afternoon checkbox
        aft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					afternoon_time.setEnabled(true);
					
					// saving to SharedPreferences
		        	SharedPreferences.Editor editor = sharedPref.edit();
		        	editor.putBoolean("afternoon", true);   		        	
		        	editor.commit();
				}
				else {
					afternoon_time.setEnabled(false);
					
					// saving to SharedPreferences
		        	SharedPreferences.Editor editor = sharedPref.edit();
		        	editor.putBoolean("afternoon", false);   		        	
		        	editor.commit();
				}
				
			}
		});
        
        
        // Setting onCheckedChangeListener for evening checkbox
        eve.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					evening_time.setEnabled(true);
					
					// saving to SharedPreferences
		        	SharedPreferences.Editor editor = sharedPref.edit();
		        	editor.putBoolean("evening", true);   		        	
		        	editor.commit();
				}
				else {
					evening_time.setEnabled(false);
					
					// saving to SharedPreferences
		        	SharedPreferences.Editor editor = sharedPref.edit();
		        	editor.putBoolean("evening", false);   		        	
		        	editor.commit();
				}
				
			}
		});
        
        
        // Setting onCheckedChangeListener for night checkbox
        night.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					night_time.setEnabled(true);
					
					// saving to SharedPreferences
		        	SharedPreferences.Editor editor = sharedPref.edit();
		        	editor.putBoolean("night", true);   		        	
		        	editor.commit();
				}
				else {
					night_time.setEnabled(false);
					
					// saving to SharedPreferences
		        	SharedPreferences.Editor editor = sharedPref.edit();
		        	editor.putBoolean("night", false);   		        	
		        	editor.commit();
				}
				
			}
		});
        
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	
    	case android.R.id.home:
    		onBackPressed();
    		return true;
    	}
        return super.onOptionsItemSelected(item);
       }
	
	
	public class TimePickerFragment extends DialogFragment 
									implements TimePickerDialog.OnTimeSetListener{
		
		static final int MORN_TIME = 1;
		static final int AFT_TIME = 2;
		static final int EVE_TIME = 3;
		static final int NIGHT_TIME = 4;
		
		int which = 0;
		Calendar c;
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			
			Bundle bundle = this.getArguments();
			if (bundle !=null) {
				which = bundle.getInt("which");
				c = (Calendar) bundle.getSerializable("calendar");
			}
			
			TimePickerDialog tp = new TimePickerDialog(getActivity(), this, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
			tp.setCancelable(true);
			tp.setCanceledOnTouchOutside(true);
			return tp;
			
			
		}

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			
			Calendar c1 = Calendar.getInstance();
			c1.set(Calendar.HOUR_OF_DAY, hourOfDay);
			c1.set(Calendar.MINUTE, minute);
			String time1 = sdf.format(c1.getTime());
			
			Long new_time = c1.getTime().getTime();
			
			SharedPreferences.Editor editor = sharedPref.edit();
			
			// Setting the appropriate textview to the new value
			switch (which) {
			case MORN_TIME: 
				morning_time.setText(time1);
				cal_morn = c1;
				
				// saving to SharedPreferences	        	
	        	editor.putLong("morn_time", new_time);   		        	
	        	editor.commit();
				break;
				
			case AFT_TIME:
				afternoon_time.setText(time1);
				cal_aft = c1;
				
				// saving to SharedPreferences
	        	editor.putLong("aft_time", new_time);   		        	
	        	editor.commit();
				break;
				
			case EVE_TIME:
				evening_time.setText(time1);
				cal_eve = c1;
				
				// saving to SharedPreferences
	        	editor.putLong("eve_time", new_time);   		        	
	        	editor.commit();
				break;
				
			case NIGHT_TIME:
				night_time.setText(time1);
				cal_night = c1;
				
				// saving to SharedPreferences
	        	editor.putLong("night_time", new_time);   		        	
	        	editor.commit();
				break;
				
			}
			
		}
			
	}

	public void showTimePickerDialog(Bundle bundl) {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.setArguments(bundl);
		newFragment.show(getFragmentManager(), "timePicker");
		
	}
		
		
}

	
	

