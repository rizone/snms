package com.example.snms.alarm;

import java.util.List;

import com.example.snms.database.SnmsDAO;
import com.example.snms.domain.PreyItem;

public class AlarmHelper {
	
	private SnmsDAO dao; 
	
	List<Alarm> alarms; 
	
	public AlarmHelper(SnmsDAO snmsDAO){
		this.dao = snmsDAO; 
	}
	
	public boolean hasAlarm(String name) {
		if(alarms == null) 
			alarms = dao.getAlarms();
		
		for(Alarm alarm : alarms) {
			if(alarm.getPrey().equals(name)){
				return true;} 
		}
		dao.closeDB(); 
		
		return false; 
	}
	
	public void setAlarm(String item,int offset){
		if(!hasAlarm(item)) {
			Alarm alarm = new Alarm(item, offset);
			dao.insertAlarm(alarm); 
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
