package com.goalninja.android;

import java.util.ArrayList;

import com.goalninja.android.R;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FeedAdapter extends BaseAdapter{
	
	private Context ctxt;
	private ArrayList<Feed> feedList;
	int pos;

	
	public FeedAdapter(Context ctxt, ArrayList<Feed> feedList) {
		this.ctxt=ctxt;
		this.feedList=feedList;
	}
	
	public int getCount() {
		return feedList.size();
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		if (convertView==null){
			LayoutInflater inflater = (LayoutInflater)ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_layout_feed, parent, false);
		}
		
//		View box = convertView.findViewById(R.id.box);
		TextView title = (TextView)convertView.findViewById(R.id.feed_title);
		TextView category = (TextView)convertView.findViewById(R.id.feed_category);
		//TextView isAdded = (TextView)convertView.findViewById(R.id.is_added);
//		TextView level = (TextView)convertView.findViewById(R.id.level);
//		TextView count = (TextView)convertView.findViewById(R.id.count);
		
		title.setText(feedList.get(position).getTitle());
		category.setText(Html.fromHtml(String.format(ctxt.getResources().getString(R.string.in), feedList.get(position).getCategory())));
		
		
		// Setting background color and category icon
		RelativeLayout bg = (RelativeLayout) convertView.findViewById(R.id.rowlayout);
		ImageView icon = (ImageView) convertView.findViewById(R.id.feed_icon);
		
		// Setting tag for icon and bg
//		pos = position;
//		icon.setTag(pos);
//		bg.setTag(pos);
		
		
		String category_text = feedList.get(position).getCategory();
		
		switch(category_text) {
		
			case "Career": 
//				bg.setBackgroundColor(Color.parseColor("#A9B0E1"));
				bg.setBackgroundColor(Color.parseColor("#33B679"));
				icon.setImageResource(R.drawable.career_icon);
				break;
				
			case "Personal":
//				bg.setBackgroundColor(Color.parseColor("#ECD078"));
				bg.setBackgroundColor(Color.parseColor("#33B679"));
				icon.setImageResource(R.drawable.heart_icon);
				break;
				
			case "Fitness":
//				bg.setBackgroundColor(Color.parseColor("#E37F78"));
				bg.setBackgroundColor(Color.parseColor("#E5AE4F"));
				icon.setImageResource(R.drawable.fitness_icon);
				break;
				
			case "Food":
//				bg.setBackgroundColor(Color.parseColor("#AAE0E2"));
				bg.setBackgroundColor(Color.parseColor("#DF5948"));
				icon.setImageResource(R.drawable.food_icon);
				break;
								
			case "Literature":
//				bg.setBackgroundColor(Color.parseColor("#ECD078"));
				bg.setBackgroundColor(Color.parseColor("#547BCA"));
				icon.setImageResource(R.drawable.book_icon);
				break;
				
			case "Music":
//				bg.setBackgroundColor(Color.parseColor("#E37F78"));
				bg.setBackgroundColor(Color.parseColor("#DF5948"));
				icon.setImageResource(R.drawable.music_icon);
				break;
				
			case "Lifestyle":
//				bg.setBackgroundColor(Color.parseColor("#AAE0E2"));
				bg.setBackgroundColor(Color.parseColor("#E5AE4F"));
				icon.setImageResource(R.drawable.trophy_icon);
				break;
				
			case "Tech":
//				bg.setBackgroundColor(Color.parseColor("#ECD078"));
				bg.setBackgroundColor(Color.parseColor("#33B679"));
				icon.setImageResource(R.drawable.tech_icon);
				break;
				
			case "Travel/Adventure":
//				bg.setBackgroundColor(Color.parseColor("#A9B0E1"));
				bg.setBackgroundColor(Color.parseColor("#547BCA"));
				icon.setImageResource(R.drawable.map_icon);
				break;
								
			case "Other": 
//				bg.setBackgroundColor(Color.parseColor("#ECD078"));
				bg.setBackgroundColor(Color.parseColor("#33B679"));
				icon.setImageResource(R.drawable.target_icon);
				break;
		
		}
		
//		if (feedList.get(position).getCategory().equalsIgnoreCase("Music")) {
//			bg.setBackgroundColor(Color.parseColor("#AADFE3"));
//		}
//		else {
//			bg.setBackgroundColor(Color.parseColor("#E47E79"));
//		}
		
	
//		String categ = feedList.get(position).getCategory();
//		
//		switch (categ) {
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
//		case "Cooking":
//			box.setBackgroundColor(Color.parseColor(ctxt.getResources().getString(R.string.cooking_row_color)));
//        }
		
		return convertView;
	}

	@Override
	public Object getItem(int position) {
		return feedList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
