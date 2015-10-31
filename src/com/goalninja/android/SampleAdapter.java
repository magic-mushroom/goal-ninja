package com.goalninja.android;

import com.goalninja.android.R;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.app.FragmentStatePagerAdapter;

public class SampleAdapter extends FragmentPagerAdapter {
	Context ctxt=null;
	private CharSequence title;

	public SampleAdapter(Context ctxt, FragmentManager mgr) {
		super(mgr);
		this.ctxt=ctxt;
	}
	
	// Number of tabs
	@Override
	public int getCount(){
		return(2);
	}
	
	// Getting an instance of Tab
	@Override
	public Fragment getItem(int pos){
		Fragment tab = null;
		switch (pos) {
        case 0:
            tab = Tab1.newInstance(pos);
            break;
		case 1:
        	tab = Tab2.newInstance(pos);
        	break;
//		case 2:
//			tab = Tab3.newInstance(pos);
//			break;
        }
		return tab;
	}
	
	
	// Getting title for the tabs
	@Override
	public CharSequence getPageTitle(int pos){
		switch(pos) {
		case 0: title = ctxt.getResources().getString(R.string.tab1);
				break;
		case 1: title = ctxt.getResources().getString(R.string.tab2);
				break;
//		case 2: title = ctxt.getResources().getString(R.string.tab3);
//				break;		
		}
		return title;
	}
	
}
