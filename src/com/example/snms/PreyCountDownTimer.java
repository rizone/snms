package com.example.snms;

import org.joda.time.DateTime;

//import com.example.snms.PreyListFragment.PreyListAdapter;
import com.example.snms.preylist.PreyOverviewFragment;

import android.os.CountDownTimer;
import android.widget.TextView;

public class PreyCountDownTimer extends CountDownTimer {
	
	TextView textViewToUpdate; 
	PreyOverviewFragment adapter;
	
	public PreyCountDownTimer(long time, long interval, TextView textView,PreyOverviewFragment adapter) {
		super(time,interval);
		this.textViewToUpdate = textView;
		this.adapter = adapter;
	}
	
	@Override
	public void onFinish() {
		this.adapter.renderPreyList();
		
	}

	@Override
	public void onTick(long millisUntilFinished) {
		DateTime delta = new DateTime(millisUntilFinished);
		String preyText = delta.minusHours(1).toTimeOfDay().toString("HH:mm:ss");
		this.textViewToUpdate.setText(preyText);
		
	}

}
