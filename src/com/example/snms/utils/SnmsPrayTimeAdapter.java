package com.example.snms.utils;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import com.example.snms.domain.PreyItem;

public class SnmsPrayTimeAdapter {

	
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
        	PreyItem preyItem = new PreyItem(prayerNames.get(i),timeToAdd);
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
