package com.example.snms;

import java.util.Calendar;

import org.joda.time.DateTime;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

@SuppressLint("ValidFragment")
public class TimePickerFragment extends DialogFragment
implements TimePickerDialog.OnTimeSetListener {
	
	boolean fired = false;
	
	private String name; 
	
	private DateTime time; 
		
	
	public TimePickerFragment(DateTime time, String name){
		super();
		this.name = name; 
		this.time = time; 
		
	}
	
	
@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
// Use the current time as the default values for the picker

	int hour = time.getHourOfDay();
	int minute = time.getMinuteOfHour();
	
// Create a new instance of TimePickerDialog and return it
final TimePickerDialog myTPDialog = new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));



return myTPDialog;

}

public void onTimeSet(TimePicker view, int hour, int minute) {
	
	if(fired == true){
		return;
	}else{
		
		int year = time.getYear();
		int month = time.getMonthOfYear();
		int day = time.getDayOfMonth();

	
		Context context = PreyOverView.getAppContext();
		AlarmUtilities Util = new AlarmUtilities();
		Intent intent = new Intent(context, AlarmReceiverActivity.class);
		//dates to be set:
		Integer [] date = new Integer [6];
		date[0] = year;
		date[1] = month-1;
		date[2] = day;
		date[3] = hour;
		date[4] = minute;
		date[5] = 1;
		
		Integer [] date2 = new Integer [6];
		date2[0] = year;
		date2[1] = month-1;
		date2[2] = day;
		date2[3] = time.getHourOfDay();
		date2[4] = time.getMinuteOfHour();
		date2[5] = 1;
	
		
		// set calender date:
		final Calendar cal = Util.GregorianCalender(date);
		final Calendar cal2 = Util.GregorianCalender(date2);
		Util.SetAlarm(cal, Util.getAlarmId(cal2), intent, context);
		
		//Store alarm
		DBAdapter db = new DBAdapter(context);
		db.open();
		db.insertAlarmsInDatabase(Util.RefactorToStringForSavingAlarm(date), Util.getAlarmId(cal2));  
		db.close();
		
		
		fired = true;
	
	}
	
	
}




}