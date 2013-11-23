package com.example.snms;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmUtilities {
	
	public void RemoveAlarm(int id, Intent intent, Context context){
//		Intent intent = new Intent(this, AlarmReceiverActivity.class);
		
        PendingIntent sender = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        alarmManager.cancel(sender);
        
        Toast.makeText(context, "Alarm er blitt deaktivert.", Toast.LENGTH_SHORT).show();
        System.out.println("Alarm with ID:" + id + " canceled");
		
	}
	
	void SetAlarm(Calendar cal, int id, Intent intent, Context context) {
		// TODO Auto-generated method stub
		
		
		if (cal.after(Calendar.getInstance())) {
			
//			Intent intent = new Intent(this, AlarmReceiverActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, id,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);
			AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
			am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
			
			Toast.makeText(context, "Alarm er blitt satt", Toast.LENGTH_SHORT).show();
			
		}else{
			Toast.makeText(context, "Oops... Alarmdato har allerede passert", Toast.LENGTH_SHORT).show();
			
		}
	}
	
	public int getAlarmId(Calendar cal){
		
		int id = Integer.parseInt(Integer.toString(cal.get(Calendar.YEAR)-2012) +
				Integer.toString(cal.get(Calendar.MONTH)) + 
				Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) +
				Integer.toString(cal.get(Calendar.HOUR_OF_DAY)) + 
				Integer.toString(cal.get(Calendar.MINUTE)));
		 
		System.out.println("ID:" + id);
		return id;
		
	}
	

	
	//Refactor from String (2013:9:23:...) to Integer[]	
	public Integer[] RefactorToIntegerForSettingRebootAlarm(String AlarmDateString){

		Integer [] AlarmDateInt = new Integer[6]; 
		
		String [] separated = AlarmDateString.split(":");
		
		AlarmDateInt[0] = Integer.parseInt(separated[0]);
		AlarmDateInt[1] = Integer.parseInt(separated[1]);
		AlarmDateInt[2] = Integer.parseInt(separated[2]);
		AlarmDateInt[3] = Integer.parseInt(separated[3]);
		AlarmDateInt[4] = Integer.parseInt(separated[4]);
		AlarmDateInt[5] = Integer.parseInt(separated[5]);
	
		return AlarmDateInt;
	}
	
	public String RefactorToStringForSavingAlarm(Integer[] date){
		String AlarmDateString = Integer.toString(date[0])+ ":" + Integer.toString(date[1]) + ":" + 
				Integer.toString(date[2]) + ":" + Integer.toString(date[3]) + ":" +Integer.toString(date[4]) + ":" +
				Integer.toString(date[5]); 
		
		return AlarmDateString; 
	}
	
	public Integer [] RefactorToIntegerFromDatabase(String AlarmDate){
		
		Integer [] AlarmDateInteger = new Integer [6];
		String [] AlarmDateString = new String [6];
		AlarmDateString = AlarmDate.split(":");
		AlarmDateInteger[0] = Integer.parseInt(AlarmDateString[0]);
		AlarmDateInteger[1] = Integer.parseInt(AlarmDateString[1]);
		AlarmDateInteger[2] = Integer.parseInt(AlarmDateString[2]);
		AlarmDateInteger[3] = Integer.parseInt(AlarmDateString[3]);
		AlarmDateInteger[4] = Integer.parseInt(AlarmDateString[4]);
		AlarmDateInteger[5] = Integer.parseInt(AlarmDateString[5]);
		
		
		return AlarmDateInteger;
	}
	

	public Calendar GregorianCalender(Integer [] date) {

		// Create an offset from the current time in which the alarm will go
		// off.
		Calendar cal = Calendar.getInstance();
		// Calendar now = Calendar.getInstance();
		int YearNow = cal.get(Calendar.YEAR);
		int MonthNow = cal.get(Calendar.MONTH);
		int DayNow = cal.get(Calendar.DAY_OF_MONTH);
		int HourNow = cal.get(Calendar.HOUR_OF_DAY);
		int MinuteNow = cal.get(Calendar.MINUTE);
		int SecondNow = cal.get(Calendar.SECOND);

		cal.add(Calendar.YEAR, date[0] - YearNow);
		cal.add(Calendar.MONTH, date[1] - MonthNow);
		cal.add(Calendar.DAY_OF_MONTH, date[2] - DayNow);
		cal.add(Calendar.HOUR_OF_DAY, date[3] - HourNow);
		cal.add(Calendar.MINUTE, date[4] - MinuteNow);
		cal.add(Calendar.SECOND, -SecondNow);

		System.out.println("year");
		System.out.println(date[0] - YearNow);
		System.out.println("month");
		System.out.println(date[1] - MonthNow);
		System.out.println("day");
		System.out.println(date[2] - DayNow);
		System.out.println("hour");
		System.out.println(date[3] - HourNow);
		System.out.println("minute");
		System.out.println(date[4] - MinuteNow);
		System.out.println("second");
		System.out.println(-SecondNow);

		return cal;

	}
}

