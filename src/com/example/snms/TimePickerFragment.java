package com.example.snms;

import java.util.Calendar;

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

public class TimePickerFragment extends DialogFragment
implements TimePickerDialog.OnTimeSetListener {
	
	boolean fired = false;

@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
// Use the current time as the default values for the picker

	
final Calendar c = Calendar.getInstance();
int hour = c.get(Calendar.HOUR_OF_DAY);
int minute = c.get(Calendar.MINUTE);





// Create a new instance of TimePickerDialog and return it
final TimePickerDialog myTPDialog = new TimePickerDialog(getActivity(), this, hour, minute,
DateFormat.is24HourFormat(getActivity()));



return myTPDialog;

}

public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	
	if(fired == true){
		return;
	}else{
	
		Context context = PreyOverView.getAppContext();
		AlarmUtilities Util = new AlarmUtilities();
		Intent intent = new Intent(context, AlarmReceiverActivity.class);
		//dates to be set:
		Integer [] date = new Integer [6];
		date[0] = 2013;
		date[1] = 10;
		date[2] = 18;
		date[3] = hourOfDay;
		date[4] = minute;
		date[5] = 1;
	
		
		// set calender date:
		final Calendar cal = Util.GregorianCalender(date);
		Util.SetAlarm(cal, Util.getAlarmId(cal), intent, context);
		
		//Store alarm
		DBAdapter db = new DBAdapter(context);
		db.open();
		db.insertAlarmsInDatabase(Util.RefactorToStringForSavingAlarm(date), Util.getAlarmId(cal));  
		db.close();
		
		
		fired = true;
	
	}
	
	
}


}