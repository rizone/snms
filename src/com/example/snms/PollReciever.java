package com.example.snms;


import java.util.Calendar;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;


public class PollReciever extends BroadcastReceiver {


	  @Override
	  public void onReceive(Context context, Intent myIntent) {
		  
		  
		  Intent intent = new Intent(context, AlarmReceiverActivity.class);
		  AlarmUtilities Util = new AlarmUtilities();
//		  context.startActivity(myIntent);
		  
		//Get alarms after reboot
			DBAdapter db = new DBAdapter(context);
			db.open();
			
			Cursor cursor = db.getAllAlarms();
			
			try{
				cursor.moveToLast();
				do{
					
					String Alarmdate = cursor.getString(1);
					int id = cursor.getInt(2);
					int id_nr = cursor.getInt(0);
					System.out.println("data from boot. Alarmdate: " + 
					Alarmdate + " id: " + id + "id-nr: " + id_nr);
					
					final Calendar cal = Util.GregorianCalender(Util.RefactorToIntegerForSettingRebootAlarm(Alarmdate));
					Util.SetAlarm(cal, Util.getAlarmId(cal), intent, context);					
					
					if (cal.before(Calendar.getInstance())) {
						db.deleteAlarmsInDatabase(id); //TEST DETTE
						System.out.println("After boot, alarm has been removed from database: " + 
								Alarmdate + " id: " + id + "Id nr: " + id_nr);
					}
					
				}while(cursor.moveToPrevious() && cursor.getInt(0)>0);
			
				System.out.println("Alarms have been set after reboot");
				
			}catch(CursorIndexOutOfBoundsException e){
		    	System.out.println("There are no alarms to be set...");
			
			db.close();
			
			

			
	  }
	}
}


	
