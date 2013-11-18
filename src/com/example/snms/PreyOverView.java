package com.example.snms;


import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.os.Bundle;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Menu;

public class PreyOverView extends  BaseActivity{
	
	private static Context context;		//Dag-Martin
	public PreyOverView() {
		super(R.string.left_and_right);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PreyOverView.context = getApplicationContext();		//Dag-Martin
		getSlidingMenu().setMode(SlidingMenu.LEFT_RIGHT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		setContentView(R.layout.content_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.add(R.id.content_frame, new PreyListFragment())
		.add(R.id.content_frame2, new HolidayListFragment())
		.commit();
		
		getSlidingMenu().setSecondaryMenu(R.layout.menu_frame_two);
		getSlidingMenu().setSecondaryShadowDrawable(R.drawable.shadowright);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame_two, new SampleListFragment())
		.commit();
	}
	


	public static Context getAppContext() {
	    return PreyOverView.context;
	}
	
    
}
