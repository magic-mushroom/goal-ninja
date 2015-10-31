package com.goalninja.android;

import com.goalninja.android.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Tab3 extends Fragment {
	
	static Tab3 newInstance(int pos) {

		// Returning an instance of Tab1
		Tab3 tab3 = new Tab3();

		// Supplying position as an argument
		Bundle args = new Bundle();
		args.putInt("tab_no", pos);
		tab3.setArguments(args);

		return (tab3);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View layout = inflater.inflate(R.layout.tab3, container, false);
		return layout;
		
	}
}