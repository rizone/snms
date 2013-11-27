package com.example.snms.utils;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.LocalTime;
import org.joda.time.MonthDay;
import org.joda.time.chrono.GregorianChronology;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.widget.ImageView;

import com.example.snms.AlarmUtilities;
import com.example.snms.DBAdapter;
import com.example.snms.PreyOverView;
import com.example.snms.R;
import com.example.snms.domain.PreyItem;
import com.example.snms.domain.PreyItemList;

public class SnmsPrayTimeAdapter {
	
	
	public List<PreyItemList> getPrayGridForMonthIndYear(int month, int year) {
		DateTime dateTime = new DateTime(year, month, 1, 1, 0, 0, 000);
		List<PreyItemList> dayPreyListMap = new ArrayList<PreyItemList>();
		for(int i = 1; i<=dateTime.dayOfMonth().getMaximumValue();i++) {
			DateTime dateTime2 = new DateTime(year, month, i, 1, 0, 0, 000);
			List<PreyItem> items = this.getPrayListForDate(dateTime2);
			PreyItemList list = new PreyItemList(items,i);
			dayPreyListMap.add(list);
			/*
			String [] preyTimeArray = new String[items.size()];
			for(int x=0;x<items.size();x++) {
				LocalTime time = new LocalTime(items.get(x).getTime().getHourOfDay(),items.get(x).getTime().getMinuteOfHour());
				preyTimeArray[x] = time.toString();
			}
			dayPreyListMap.put(i, preyTimeArray);
			*/
		}
		return dayPreyListMap;
	}
	
	
	public List<PreyItem> getPrayListForDate(DateTime time) {
		
//        double latitude = 59;
        double latitude = 170;
        double longitude = 10;
        double timezone = 1;
        PrayTime prayers = new PrayTime();
        prayers.setTimeFormat(prayers.Time24);
        prayers.setCalcMethod(prayers.Jafari);
        prayers.setAsrJuristic(prayers.Shafii);
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(time.toDate());
        
        ArrayList<String> prayerTimes = prayers.getPrayerTimes(cal,
                latitude, longitude, timezone);
        ArrayList<String> prayerNames = prayers.getTimeNames();
        
        List<PreyItem> listToReturn = new ArrayList<PreyItem>();
        
        for (int i = 0; i < prayerTimes.size(); i++) {
        	
        	DateTime timeToAdd = time.plusHours(Integer.valueOf(prayerTimes.get(i).split(":")[0])).plusMinutes(Integer.valueOf(prayerTimes.get(i).split(":")[1]));
        	PreyItem preyItem = new PreyItem(prayerNames.get(i),timeToAdd, false);
        	listToReturn.add(preyItem);
        	checkAlarmStateAtStartup(preyItem);
        }
        return listToReturn;
		
		/*
	    public static void main(String[] args) {
	        double latitude = 59;
	        double longitude = 10;
	        double timezone = 1;
	        // Test Prayer times here
	        PrayTime prayers = new PrayTime();

	        prayers.setTimeFormat(prayers.Time24);
	        prayers.setCalcMethod(prayers.Jafari);
	        prayers.setAsrJuristic(prayers.Shafii);
	        prayers.setAdjustHighLats(prayers.AngleBased);
	        int[] offsets = {0, 0, 0, 0, 0, 0, 0}; // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
	        prayers.tune(offsets);

	        Date now = new Date();
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(now);

	        ArrayList<String> prayerTimes = prayers.getPrayerTimes(cal,
	                latitude, longitude, timezone);
	        ArrayList<String> prayerNames = prayers.getTimeNames();

	        for (int i = 0; i < prayerTimes.size(); i++) {
	            System.out.println(prayerNames.get(i) + " - " + prayerTimes.get(i));
	        }

	    }
		*/
		
	}
	
public void checkAlarmStateAtStartup(PreyItem preyItem){
		
		Context context = PreyOverView.getAppContext();
//		Intent intent = new Intent(context, AlarmReceiverActivity.class);
		  AlarmUtilities Util = new AlarmUtilities();
//		  context.startActivity(myIntent);
		  
		//Get alarms after reboot
			DBAdapter db = new DBAdapter(context);
			db.open();
			
			Cursor cursor = db.getAllAlarms();
			
			try{
				cursor.moveToLast();
				do{
					Integer [] AlarmDate = Util.RefactorToIntegerFromDatabase(cursor.getString(1));
					
					DateTime dateTimeFromDB = new DateTime(AlarmDate[0], AlarmDate[1]+1, AlarmDate[2],
							AlarmDate[3], AlarmDate[4], AlarmDate[5]-1, 0); 
					String dateTimeFromDBString = dateTimeFromDB.getYear() + ":" + dateTimeFromDB.getMonthOfYear() + ":" +
							dateTimeFromDB.getDayOfMonth() + ":" + dateTimeFromDB.getHourOfDay() + ":" + 
							dateTimeFromDB.getMinuteOfHour() + ":" + dateTimeFromDB.getSecondOfMinute();
					
					System.out.println("dateTimeFromDB: " + dateTimeFromDBString);
					

						DateTime dateTimeFromEvent = preyItem.getTime();
						//Set correct alarm image at start-up
						String dateTimeFromEventString = dateTimeFromEvent.getYear() + ":" + 
						dateTimeFromEvent.getMonthOfYear() + ":" + dateTimeFromEvent.getDayOfMonth() + 
						":" + dateTimeFromEvent.getHourOfDay() + ":" + 
						dateTimeFromEvent.getMinuteOfHour() + ":" + dateTimeFromEvent.getSecondOfMinute();
						System.out.println("dateTimeFromEvent: " + dateTimeFromEventString);
						
						if(dateTimeFromEventString.equals(dateTimeFromDBString)==true){
							
							preyItem.setAlarmBoolean(true);					
						}

				}while(cursor.moveToPrevious() && cursor.getInt(0)>0);
			
				System.out.println("AlarmPictures have been set on start-up");
				
			}catch(CursorIndexOutOfBoundsException e){
		    	System.out.println("There are no alarmPictures to be set...");
			
			db.close();
			
			

			}	
	  
	}
	

}
