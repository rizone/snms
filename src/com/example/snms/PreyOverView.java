package com.example.snms;


import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.view.Menu;

public class PreyOverView extends  BaseActivity {
	
	 

	public PreyOverView() {
		super(R.string.left_and_right);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSlidingMenu().setMode(SlidingMenu.LEFT_RIGHT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		currentFragment1 =  new PreyListFragment();
		currentFragment2 = new HolidayListFragment();
		
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
