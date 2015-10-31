package com.goalninja.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.goalninja.android.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class GoalProgAdapter extends BaseAdapter {
	
	private Context ctxt;
	private ArrayList<Note> notes;
	String start_date, complete_date;
	String date_format = "MMM dd";
	SimpleDateFormat sdf = new SimpleDateFormat(date_format);
	SimpleDateFormat sdf_db = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
	GoalDatabase dbsingleton;
	int id, pos, rowpos;
	Note rowItem;

	public GoalProgAdapter(Context ctxt, ArrayList<Note> notes, GoalDatabase dbsingleton) {
		this.ctxt=ctxt;
		this.notes=notes;
		this.dbsingleton=dbsingleton;
	}
	
	public int getCount() {
		return notes.size();
	}
	
//	public void remove(int position) {
//		this.notes.remove(position);
//	}
	
	
	public View getView(int position, View convertView, ViewGroup parent){
		if (convertView==null){
			LayoutInflater inflater = (LayoutInflater)ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.note_row_layout, parent, false);
		}
		
//		RelativeLayout row_height = (RelativeLayout)convertView.findViewById(R.id.row_layout_update);
		
		id = notes.get(position).getGoalID();
		pos = position;
		
		TextView note_text = (TextView)convertView.findViewById(R.id.note_text);
		TextView note_date = (TextView)convertView.findViewById(R.id.note_date);
//		View line = convertView.findViewById(R.id.line);
//		View half_line = convertView.findViewById(R.id.half_line);
//		ImageView delete_note_icon = (ImageView) convertView.findViewById(R.id.delete_note_icon);
		final Button delete_note = (Button) convertView.findViewById(R.id.delete_note_button);
		
		note_text.setText(notes.get(position).getNote());
		note_date.setText(sdf.format(notes.get(position).getDate()));
		
		
		
		Log.d("note_row", notes.get(position).getNote());
		Log.d("note_row", String.valueOf(position));
		
		
//		Button delete_note_button = (Button) convertView.findViewById(R.id.delete_note_button);
		
//		rowItem.setGoalID(id);
//		rowItem.setDate(notes.get(position).getDate());
//		rowItem.setNote(notes.get(position).getNote());
		
		
		delete_note.setTag(pos);
		
		// Setting onclick listener for Delete_Note button
		delete_note.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				rowpos = (int) delete_note.getTag();

				new AlertDialog.Builder(ctxt)
    			.setTitle(R.string.delete_note)
    			.setMessage(R.string.delete_note_text)
    			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						new DeleteNote().execute();
						dialog.cancel();
						
					}
				})
				.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						
					}
				})
				.show();
				
			}
						
		});
		
		
//		// Hiding the delete icon for "Added goal" entry
//		if (notes.get(position).getNote().equalsIgnoreCase("Added goal")) {
//
//			delete_note.setVisibility(View.GONE);
//			delete_note_icon.setVisibility(View.GONE);
//
//		}

		
//		float dipUnit = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, ctxt.getResources().getDisplayMetrics());
//		int dipUnitInt = Math.round(dipUnit);
//		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(dipUnitInt, convertView.getHeight());
//		line.setLayoutParams(layoutParams);
		
		
		
/*		if (notes.get(position).getNote().equalsIgnoreCase("Added goal")) {
			
			
			delete_note.setVisibility(View.INVISIBLE);
			
			dipUnit = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, ctxt.getResources().getDisplayMetrics());
			dipUnitInt = Math.round(dipUnit);
			layoutParams = new RelativeLayout.LayoutParams(dipUnitInt, 3*dipUnitInt);
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
			line.setLayoutParams(layoutParams);
			
			
		}*/
		
//		if (notes.get(position).getNote().equalsIgnoreCase("Added goal")) {
//			
//			dipUnit = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, ctxt.getResources().getDisplayMetrics());
//			dipUnitInt = Math.round(dipUnit);
//			layoutParams = new RelativeLayout.LayoutParams(dipUnitInt, 3*dipUnitInt);
//			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
//			line.setLayoutParams(layoutParams);
//			
//			Log.d("note_row", notes.get(position).getNote());
//			
//		}
		
		
//		RelativeLayout row_layout = (RelativeLayout)convertView.findViewById(R.id.row_layout_update);
//		View line = convertView.findViewById(R.id.line);
//		
//		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(line.getMeasuredWidth(), row_layout.getMeasuredHeight());
//		line.setLayoutParams(layoutParams);
//		
//		Log.d("row_note", notes.get(position).getNote());
//		Log.d("row_dimen", String.valueOf(line.getMeasuredWidth()));
//		Log.d("row_dimen", String.valueOf(row_layout.getMeasuredHeight()));
		
		refreshList(); 
		
		return convertView;
	}
	
	@Override
	public Object getItem(int position) {
		return notes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	
	private class DeleteNote extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			
//			dbsingleton = GoalDatabase.getInstance(ctxt.getApplicationContext());
			SQLiteDatabase db = dbsingleton.getWritableDatabase();
			
			String note = notes.get(rowpos).getNote();
			String date = sdf_db.format(notes.get(rowpos).getDate());
			
			
			try {
				
				String [] whereArgs = new String[] {note, date};
								
				db.delete("PROG", "GOALID = " + id + " AND NOTE = ?" + " AND DATE = ?", whereArgs);
				
				Log.d("db_delete_note", "Trying to delete note");
			}
			
			finally {
				db.close();
			}
			
			notes.remove(rowpos);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void args) {
			
			
			refreshList();
			
			// Raise a toast
						
			String toast_string = "Note deleted";
			Toast toast = Toast.makeText(ctxt, toast_string, Toast.LENGTH_SHORT);
            toast.show();
			
			Log.d("db_delete_note", "I reached End of DeleteNote");
		}
		
	}
	
	public void refreshList() {
		
		notifyDataSetChanged();
	}
	
	
}
