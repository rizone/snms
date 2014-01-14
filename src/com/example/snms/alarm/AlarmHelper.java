package com.example.snms.alarm;

import java.util.List;


import com.example.snms.AlarmReceiverActivity;
import com.example.snms.PreyOverView;
import com.example.snms.database.SnmsDAO;
import com.example.snms.domain.PreyItem;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent; 
public class AlarmHelper {
	
	private SnmsDAO dao; 
	
	List<Alarm> alarms; 
	 private PendingIntent pendingIntent; 
	public AlarmHelper(SnmsDAO snmsDAO){
		this.dao = snmsDAO; 
	}
	
	public boolean hasAlarm(String name) {
		if(alarms == null) 
			alarms = dao.getAlarms();
		
		for(Alarm alarm : alarms) {
			System.out.println("Dette er alarm name: " + name);
			System.out.println("Dette er alarm name: " + alarm.getPrey());
			if(alarm.getPrey().equals(name)){
				return true;} 
		}
		dao.closeDB(); 
		
		return false; 
	}
	
	public void setAlarm(String item,int offset){
		if(!hasAlarm(item)) {
			Intent myIntent = new Intent(null, AlarmReceiverActivity.class);
		    pendingIntent = PendingIntent.getBroadcast(null, 0, myIntent,0);
			Alarm alarm = new Alarm(item, offset);
			dao.insertAlarm(alarm); 
			dao.closeDB();
			alarms = dao.getAlarms(); 
			dao.closeDB();
		}
		
	}
	
	public void cancelAlarm(String item) {
		if(hasAlarm(item)) {
			dao.deleteAlarm(item); 
			alarms = dao.getAlarms(); 
			dao.closeDB();
		}
		
	}
	
	
	public void setAllAlarms(List <PreyItem> items) {
		
	}
	
	
	public void cancelAllAlarms() {
		
	}
	
	
	
	
	

}
