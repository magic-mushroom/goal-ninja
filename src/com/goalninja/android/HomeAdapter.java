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
public class HomeAdapter extends BaseAdapter {
	
	private Context ctxt;
	private ArrayList<MyGoal> myList;
	String start_date, complete_date, category;
	String date_format = "MMM dd";
	SimpleDateFormat sdf = new SimpleDateFormat(date_format);
	RelativeLayout bg;
	ImageView icon;
	

	public HomeAdapter(Context ctxt, ArrayList<MyGoal> myList) {
		this.ctxt=ctxt;
		this.myList=myList;
	}
	
	public int getCount() {
		return myList.size();
	}
	
	
	public View getView(int position, View convertView, ViewGroup parent){
		if (convertView==null){
			LayoutInflater inflater = (LayoutInflater)ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_layout_home, parent, false);
		}
		
//		View box = convertView.findViewById(R.id.box_home);
		TextView title = (TextView)convertView.findViewById(R.id.title_home);
//		TextView category = (TextView)convertView.findViewById(R.id.category_home);
		TextView start = (TextView)convertView.findViewById(R.id.start_home);
		TextView schedule_text = (TextView)convertView.findViewById(R.id.schedule_text_home);
		TextView schedule = (TextView)convertView.findViewById(R.id.schedule_home);
				
		title.setText(myList.get(position).getTitle());
//		category.setText(myList.get(position).getCategory());
//		level.setText(String.valueOf(myList.get(position).getLevel()));
//		start.setText(myList.get(position).getDate().toString());
		schedule_text.setText(myList.get(position).getSchedule());
		
		if (schedule_text.getText().equals("Repeat")) {
			schedule.setText(myList.get(position).getFreqDays());
		}
		else {
			//schedule.setText(myList.get(position).getDate().toString());
			// Complete Date
			complete_date = sdf.format(myList.get(position).getDate());
			schedule.setText(complete_date);
		}

		// Start Date
		start_date = sdf.format(myList.get(position).getStart());
		start.setText(start_date);
		
		
		// Setting BG and icon as per category
		bg = (RelativeLayout) convertView.findViewById(R.id.home_rowlayout);
		icon = (ImageView) convertView.findViewById(R.id.icon_home);
		
		category = myList.get(position).getCategory();
		
		switch(category) {
		
		case "Career": 
//			bg.setBackgroundColor(Color.parseColor("#A9B0E1"));
			bg.setBackgroundColor(Color.parseColor("#33B679"));
			icon.setImageResource(R.drawable.career_icon);
			break;
			
		case "Personal":
//			bg.setBackgroundColor(Color.parseColor("#ECD078"));	
			bg.setBackgroundColor(Color.parseColor("#33B679"));
			icon.setImageResource(R.drawable.heart_icon);
			break;
			
		case "Fitness":
//			bg.setBackgroundColor(Color.parseColor("#E37F78"));
			bg.setBackgroundColor(Color.parseColor("#E5AE4F"));
			icon.setImageResource(R.drawable.fitness_icon);
			break;
			
		case "Food":
//			bg.setBackgroundColor(Color.parseColor("#AAE0E2"));
			bg.setBackgroundColor(Color.parseColor("#DF5948"));
			icon.setImageResource(R.drawable.food_icon);
			break;
							
		case "Literature":
//			bg.setBackgroundColor(Color.parseColor("#ECD078"));
			bg.setBackgroundColor(Color.parseColor("#547BCA"));
			icon.setImageResource(R.drawable.book_icon);
			break;
			
		case "Music":
//			bg.setBackgroundColor(Color.parseColor("#E37F78"));
			bg.setBackgroundColor(Color.parseColor("#DF5948"));
			icon.setImageResource(R.drawable.music_icon);
			break;
			
		case "Lifestyle":
//			bg.setBackgroundColor(Color.parseColor("#AAE0E2"));
			bg.setBackgroundColor(Color.parseColor("#E5AE4F"));
			icon.setImageResource(R.drawable.trophy_icon);
			break;
			
		case "Tech":
//			bg.setBackgroundColor(Color.parseColor("#ECD078"));
			bg.setBackgroundColor(Color.parseColor("#33B679"));
			icon.setImageResource(R.drawable.tech_icon);
			break;
			
		case "Travel/Adventure":
//			bg.setBackgroundColor(Color.parseColor("#A9B0E1"));
			bg.setBackgroundColor(Color.parseColor("#547BCA"));
			icon.setImageResource(R.drawable.map_icon);
			break;
							
		case "Other": 
//			bg.setBackgroundColor(Color.parseColor("#ECD078"));
			bg.setBackgroundColor(Color.parseColor("#33B679"));
			icon.setImageResource(R.drawable.target_icon);
			break;
	
	}
		
		
//		convertView.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//
//				if (event.getAction() == MotionEvent.ACTION_DOWN) {
//					
//					home_grey_mask.setAlpha((float) 0.4);
//					return true;
//				}
//				else {
//					home_grey_mask.setAlpha(0);
//					return true;
//				}
//			}				
//						
//		});
		

		
		
		
		
//		switch (myList.get(position).getCategory()) {
//        case "Sports":
//            box.setBackgroundColor(Color.parseColor(ctxt.getResources().getString(R.string.sports_row_color)));
//            break;
//		case "Music":
//			box.setBackgroundColor(Color.parseColor(ctxt.getResources().getString(R.string.music_row_color)));
//        	break;
//		case "Fitness":
//			box.setBackgroundColor(Color.parseColor(ctxt.getResources().getString(R.string.fitness_row_color)));
//			break;
//		case "Coding":
//			box.setBackgroundColor(Color.parseColor(ctxt.getResources().getString(R.string.coding_row_color)));
//			break;
//		case "Reading":
//			box.setBackgroundColor(Color.parseColor(ctxt.getResources().getString(R.string.reading_row_color)));
//			break;
//		case "Arts":
//			box.setBackgroundColor(Color.parseColor(ctxt.getResources().getString(R.string.arts_row_color)));
//			break;
//		default:
//			box.setBackgroundColor(Color.parseColor(ctxt.getResources().getString(R.string.default_color)));
//			break;
//        }
		
		return convertView;
	}

	@Override
	public Object getItem(int position) {
		return myList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}

