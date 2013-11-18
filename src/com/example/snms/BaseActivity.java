package com.example.snms;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseActivity extends SlidingFragmentActivity {

	private int mTitleRes;
	protected ListFragment mFrag;

	Fragment currentFragment1;
	Fragment currentFragment2;

	public BaseActivity(int titleRes) {
		mTitleRes = titleRes;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);        
        
		setTitle(mTitleRes);

		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager()
					.beginTransaction();
			mFrag = new SampleListFragment();
			t.replace(R.id.menu_frame, mFrag);
			t.commit();
		} else {
			mFrag = (ListFragment) this.getSupportFragmentManager()
					.findFragmentById(R.id.menu_frame);
		}
		

		

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();

		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);

		// sm.setSelectorDrawable(R.drawable.flamingo);
		sm.setSelectorEnabled(false);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.LEFT);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("");

		//getSupportActionBar().setIcon(R.drawable.ic_logo);
//		getSupportActionBar().set
		

		

		// getSupportActionBar().setIcon(R.drawable.ic_logo);
		// getSupportActionBar().set
		RequestManager.init(this);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void switchContent(Fragment fragment1, Fragment fragment2) {

		if (fragment2 == null) {
			if (currentFragment2 != null) {
				getSupportFragmentManager().beginTransaction()
						.remove(currentFragment1).remove(currentFragment2)
						.replace(R.id.content_frame, fragment1).commit();
			} else {
				getSupportFragmentManager().beginTransaction()
						.remove(currentFragment1)
						.replace(R.id.content_frame, fragment1).commit();
			}
		} else {
			getSupportFragmentManager().beginTransaction()
					.remove(currentFragment1)
					.replace(R.id.content_frame, fragment1)
					.replace(R.id.content_frame2, fragment2).commit();
		}

		currentFragment1 = fragment1;
		currentFragment2 = fragment2;

		getSlidingMenu().showContent();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

	    
	public void onToggleClicked(View view) {
	    // Is the toggle on?
	    boolean on = ((ToggleButton) view).isChecked();
	    
	    
	    
	    if (on) {
	        // Enable vibrate
	    	
	    	
	    	showTimePickerDialog();
	    } else {

//			Context context = PreyOverView.getAppContext();
//			AlarmUtilities Util = new AlarmUtilities();
//			Intent intent = new Intent(context, AlarmReceiverActivity.class);
//	    	
////			//Remove alarm
//			Util.RemoveAlarm(Util.getAlarmId(cal), intent, this);		//Denne fungerer
////			
////			//Remove alarm from database
//			DBAdapter db = new DBAdapter(this);
//			db.open();
//			db.deleteAlarmsInDatabase(Util.getAlarmId(cal)); 
//			db.close();
	    	
	    	//Må ha en id for hvert event som kan finnes i database. 
	    	//Må kunne sjekke hvilken tilstand alarmen er i under oppstart.
	    	
	    }
	}
	
	    public void showTimePickerDialog() {
	        DialogFragment newFragment = new TimePickerFragment();
	        newFragment.show(getFragmentManager(), "timePicker");
	    }
	
}
