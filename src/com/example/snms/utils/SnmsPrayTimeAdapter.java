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
		
        double latitude = 59;
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
	

}
