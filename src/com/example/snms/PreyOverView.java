package com.example.snms;


import java.util.List;

import org.joda.time.DateTime;

import com.example.snms.domain.PreyItem;
import com.example.snms.utils.SnmsPrayTimeAdapter;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.view.Menu;

public class PreyOverView extends  BaseActivity {
	
	 
	
	List <PreyItem> preyTimes; 
	
	public PreyOverView() {
		super(R.string.left_and_right);
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSlidingMenu().setMode(SlidingMenu.LEFT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		if(currentFragment1 == null) {
			currentFragment1 =  new PreyListFragment();
			((PreyListFragment) currentFragment1).setPreyList(preyTimes);
			currentFragment2 = new NewsListFragment();
		
		setContentView(R.layout.content_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.add(R.id.content_frame, currentFragment1)
		.add(R.id.content_frame2, currentFragment2)
		.commit();
		
		getSlidingMenu().setSecondaryMenu(R.layout.menu_frame_two);
		getSlidingMenu().setSecondaryShadowDrawable(R.drawable.shadowright);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame_two, new SampleListFragment())
		.commit();
		}
	}
    
	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year,
			int month, int day) {
		// TODO Auto-generated method stub
		
	}

	
}
