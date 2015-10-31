package com.goalninja.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.goalninja.android.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class ArchiveAdapter extends BaseAdapter {
	
	Context ctxt;
	ArrayList<MyGoal> myList;
	String schedule, category;
	TextView title, start_string, date_string, date_text;
	SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
	RelativeLayout bg;
	ImageView icon;
	
	public ArchiveAdapter(Context ctxt, ArrayList<MyGoal> myList) {
		this.ctxt=ctxt;
		this.myList=myList;
	}
	
	public int getCount() {
		return myList.size();
	}
	
	
	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		
		if (convertView==null){
			LayoutInflater inflater = (LayoutInflater)ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_layout_archive, parent, false);
		}
		
		title = (TextView) convertView.findViewById(R.id.row_archive_title);
//		category = (TextView) convertView.findViewById(R.id.row_archive_category);
		start_string = (TextView) convertView.findViewById(R.id.row_archive_start);
		date_string = (TextView) convertView.findViewById(R.id.row_archive_date);
		date_text = (TextView) convertView.findViewById(R.id.row_archive_date_text);
		
		title.setText(myList.get(pos).getTitle());
//		category.setText(myList.get(pos).getCategory());
		start_string.setText(sdf.format(myList.get(pos).getStart()));
		
		schedule = myList.get(pos).getSchedule();
		
		if (schedule.equalsIgnoreCase("Repeat")) {
			date_text.setText(ctxt.getResources().getString(R.string.repeated));
			date_string.setText(myList.get(pos).getFreqDays());			
		}
		
		else {
			date_text.setText(ctxt.getResources().getString(R.string.completed));
			date_string.setText(sdf.format(myList.get(pos).getDate()));
		}
		
		
		// Setting BG and icon according to category
		bg = (RelativeLayout) convertView.findViewById(R.id.archive_rowlayout);
		icon = (ImageView) convertView.findViewById(R.id.row_archive_icon);
		
		category = myList.get(pos).getCategory();
		
		switch(category) {
		
		case "Career": 
			bg.setBackgroundColor(Color.parseColor("#33B679"));
			icon.setImageResource(R.drawable.career_icon);
			break;
			
		case "Personal":
			bg.setBackgroundColor(Color.parseColor("#33B679"));
			icon.setImageResource(R.drawable.heart_icon);
			break;
			
		case "Fitness":
			bg.setBackgroundColor(Color.parseColor("#E5AE4F"));
			icon.setImageResource(R.drawable.fitness_icon);
			break;
			
		case "Food":
			bg.setBackgroundColor(Color.parseColor("#DF5948"));
			icon.setImageResource(R.drawable.food_icon);
			break;
							
		case "Literature":
			bg.setBackgroundColor(Color.parseColor("#547BCA"));
			icon.setImageResource(R.drawable.book_icon);
			break;
			
		case "Music":
			bg.setBackgroundColor(Color.parseColor("#DF5948"));
			icon.setImageResource(R.drawable.music_icon);
			break;
			
		case "Lifestyle":
			bg.setBackgroundColor(Color.parseColor("#E5AE4F"));
			icon.setImageResource(R.drawable.trophy_icon);
			break;
			
		case "Tech":
			bg.setBackgroundColor(Color.parseColor("#33B679"));
			icon.setImageResource(R.drawable.tech_icon);
			break;
			
		case "Travel/Adventure":
			bg.setBackgroundColor(Color.parseColor("#547BCA"));
			icon.setImageResource(R.drawable.map_icon);
			break;
							
		case "Other": 
			bg.setBackgroundColor(Color.parseColor("#33B679"));
			icon.setImageResource(R.drawable.target_icon);
			break;
	
		}
		
		return convertView;
	}
	
	

	@Override
	public Object getItem(int pos) {
		return myList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	
	
}
