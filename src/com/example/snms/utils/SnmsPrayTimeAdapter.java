package com.example.snms.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.MonthDay;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.util.Xml;
import android.widget.ImageView;

import com.example.snms.AlarmUtilities;
import com.example.snms.DBAdapter;
import com.example.snms.PreyOverView;
import com.example.snms.R;
import com.example.snms.domain.Jumma;
import com.example.snms.domain.PreyItem;
import com.example.snms.domain.PreyItemList;

public class SnmsPrayTimeAdapter {

	AssetManager assetManager;
	private static final String ns = null;


	public SnmsPrayTimeAdapter(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public List<PreyItemList> getPrayGridForMonthIndYear(int month, int year, boolean includeAlarm) {
		DateTime dateTime = new DateTime(year, month, 1, 1, 0, 0, 000);
		List<PreyItemList> dayPreyListMap = new ArrayList<PreyItemList>();
		for (int i = 1; i <= dateTime.dayOfMonth().getMaximumValue(); i++) {
			DateTime dateTime2 = new DateTime(year, month, i, 1, 0, 0, 000);
			List<PreyItem> items = this.getPrayListForDate(dateTime2,includeAlarm);
			PreyItemList list = new PreyItemList(items, i);
			dayPreyListMap.add(list);
		}
		
		return dayPreyListMap;
	}

	private List<PreyItem> readFeed(XmlPullParser parser, DateTime time)
			throws XmlPullParserException, IOException {
		List<PreyItem> entries = new ArrayList<PreyItem>();
		parser.require(XmlPullParser.START_TAG, ns, "Records");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if (name.equals("Row")
					&& parser.getAttributeValue(0).equals(String.valueOf(time.getDayOfMonth()))) {
				entries.addAll(readEntry(parser,time));
			} else {
				skip(parser);
			}
		}
		return entries;
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}
	
	
	private DateTime getPrayTimeFromString(DateTime time, String timeToParse) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("h:mm:ss aa");
		LocalTime timeFromString = LocalTime.parse(timeToParse,fmt);
		return time.plusHours(timeFromString.getHourOfDay()).plusMinutes(timeFromString.getMinuteOfHour());
	}
	private List<Jumma> getFredagsbonnListe() {
		
		/*
		 * 
		 * DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
DateTime dt = formatter.parseDateTime(string);
		 * 
		 */
		
		/*
		 * 
		 * 01. november til 10. januar: 13:00
11. januar til 31. januar: 13.30
1. februar til 10. mars: 14:00
11. mars til 31. mars: 14:30
1. april til 31. juli: 15:30
1. august til 10. oktober: 15:00 
		 * 
		 */
		
		
		List <Jumma> fredagsbonnLiset = new ArrayList<Jumma>();
	//	Jumma(int fromMonth, int toMonth, int toDay, int fromDay, long hours, long minuttes)
		
		
		fredagsbonnLiset.add(new Jumma(1,11,10,1,13,0));
		fredagsbonnLiset.add(new Jumma(11,1,31,1,13,0));
		fredagsbonnLiset.add(new Jumma(1,2,10,3,14,00));
		fredagsbonnLiset.add(new Jumma(11,3,31,3,14,30));
		fredagsbonnLiset.add(new Jumma(1,4,31,7,15,30));
		fredagsbonnLiset.add(new Jumma(1,8,10,10,15,00));

		return fredagsbonnLiset;
	}
	
	private void addFredagsbon(List <PreyItem> preyList, DateTime currentTime) {
		List <Jumma> fredagsBonns = getFredagsbonnListe();
		int day = currentTime.getDayOfWeek();
    	int daysToFriday = 5 - day;
    	//next friday is next week 
    	if(daysToFriday<0){
    		daysToFriday = 5 + day;
    	}
    	DateTime salatTime = new DateTime();
    	try {
		for(int i = 0;i<fredagsBonns.size();i++) {
			DateTime nextFriday = currentTime.plusDays(daysToFriday);
	    	if(fredagsBonns.get(i).isBetween(nextFriday)){
	    		Jumma timeOfThaOne = fredagsBonns.get(i);
	    		salatTime = currentTime.plusDays(daysToFriday).plusHours(timeOfThaOne.getHours()).plusMinutes(timeOfThaOne.getMinuttes());
	    		break;
	    	}
		}
    	}
    	catch(Exception exception) {
    		exception.printStackTrace();
    	}
		PreyItem salat = new PreyItem("Jummah", salatTime, false);	
		preyList.add(salat);
		/*
		 * 
		 * 01. november til 10. januar: 13:00
11. januar til 31. januar: 13.30
1. februar til 10. mars: 14:00
11. mars til 31. mars: 14:30
1. april til 31. juli: 15:30
1. august til 10. oktober: 15:00 
11. oktober til 31. oktober: 14:30
		 */
	}
	
	private List<PreyItem> readEntry(XmlPullParser parser,DateTime time) throws XmlPullParserException, IOException {
	    parser.require(XmlPullParser.START_TAG, ns, "Row");
	    List <PreyItem> preyList = new ArrayList<PreyItem>();
	  	DateTime fajrTime = getPrayTimeFromString(time,parser.getAttributeValue(1));
	  	PreyItem fajr = new PreyItem("Fajr", fajrTime, false);	 
	 	DateTime soloppgangTime = getPrayTimeFromString(time,parser.getAttributeValue(2));
	  	PreyItem soloppgang = new PreyItem("Soloppgang", soloppgangTime, false);		  	 
	  	DateTime dhuhrTime = getPrayTimeFromString(time,parser.getAttributeValue(3));
	  	PreyItem duhr = new PreyItem("Dhuhr", dhuhrTime, false);	
		DateTime asrTime = getPrayTimeFromString(time,parser.getAttributeValue(4));
		PreyItem asr = new PreyItem("Asr", asrTime, false);	
	    DateTime maghribTime = getPrayTimeFromString(time,parser.getAttributeValue(5));  	
		PreyItem maghrib = new PreyItem("Maghrib", maghribTime, false);	
		DateTime ishaTime = getPrayTimeFromString(time,parser.getAttributeValue(6));
		PreyItem isha = new PreyItem("Isha", ishaTime, false);	
		preyList.add(fajr);
		preyList.add(soloppgang);
		preyList.add(duhr);
		preyList.add(asr);
		preyList.add(maghrib);
		preyList.add(isha);
	
	    return preyList;
	}

	private List<PreyItem> readPrayItemFormXml(DateTime time) {
		InputStream inputStream = null;
		try {
			String test = String.valueOf(time.getMonthOfYear());
			inputStream = assetManager.open(String.valueOf(time.getMonthOfYear()) + ".xml");
			//inputStream = assetManager.open("1.xml");
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(inputStream, null);
			parser.nextTag();
			return readFeed(parser,time);}
		catch(Exception e) {
			e.printStackTrace();
		}
		 finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public List<PreyItem> getPrayListForDate(DateTime time, boolean includeAlarm) {
		
		if(true) {
			List<PreyItem> list = readPrayItemFormXml(time);
			addFredagsbon(list,time);
			if(includeAlarm){
				for(PreyItem item : list) {
					checkAlarmStateAtStartup(item);
				}
			}
			return list;
		}
			
		double latitude = 59;
		double longitude = 10;
		double timezone = 1;
		PrayTime prayers = new PrayTime();
		prayers.setTimeFormat(prayers.Time24);
		prayers.setCalcMethod(prayers.Jafari);
		prayers.setAsrJuristic(prayers.Shafii);

		Calendar cal = Calendar.getInstance();
		cal.setTime(time.toDate());
		ArrayList<String> prayerTimes = prayers.getPrayerTimes(cal, latitude,
				longitude, timezone);
		ArrayList<String> prayerNames = prayers.getTimeNames();
		List<PreyItem> listToReturn = new ArrayList<PreyItem>();
		for (int i = 0; i < prayerTimes.size(); i++) {

			DateTime timeToAdd = time.plusHours(
					Integer.valueOf(prayerTimes.get(i).split(":")[0]))
					.plusMinutes(
							Integer.valueOf(prayerTimes.get(i).split(":")[1]));
			PreyItem preyItem = new PreyItem(prayerNames.get(i), timeToAdd,
					false);
			listToReturn.add(preyItem);
			checkAlarmStateAtStartup(preyItem);
		}
		return listToReturn;

		/*
		 * public static void main(String[] args) { double latitude = 59; double
		 * longitude = 10; double timezone = 1; // Test Prayer times here
		 * PrayTime prayers = new PrayTime();
		 * 
		 * prayers.setTimeFormat(prayers.Time24);
		 * prayers.setCalcMethod(prayers.Jafari);
		 * prayers.setAsrJuristic(prayers.Shafii);
		 * prayers.setAdjustHighLats(prayers.AngleBased); int[] offsets = {0, 0,
		 * 0, 0, 0, 0, 0}; // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
		 * prayers.tune(offsets);
		 * 
		 * Date now = new Date(); Calendar cal = Calendar.getInstance();
		 * cal.setTime(now);
		 * 
		 * ArrayList<String> prayerTimes = prayers.getPrayerTimes(cal, latitude,
		 * longitude, timezone); ArrayList<String> prayerNames =
		 * prayers.getTimeNames();
		 * 
		 * for (int i = 0; i < prayerTimes.size(); i++) {
		 * System.out.println(prayerNames.get(i) + " - " + prayerTimes.get(i));
		 * }
		 * 
		 * }
		 */

	}

	public void checkAlarmStateAtStartup(PreyItem preyItem) {

		Context context = PreyOverView.getAppContext();
		// Intent intent = new Intent(context, AlarmReceiverActivity.class);
		AlarmUtilities Util = new AlarmUtilities();
		// context.startActivity(myIntent);

		// Get alarms after reboot
		DBAdapter db = new DBAdapter(context);
		db.open();

		Cursor cursor = db.getAllAlarms();

		try {
			cursor.moveToLast();
			do {
				Integer[] AlarmDate = Util.RefactorToIntegerFromDatabase(cursor
						.getString(1));
				DateTime dateTimeFromDB = new DateTime(AlarmDate[0],
						AlarmDate[1] + 1, AlarmDate[2], AlarmDate[3],
						AlarmDate[4], AlarmDate[5] - 1, 0);
				String dateTimeFromDBString = dateTimeFromDB.getYear() + ":"
						+ dateTimeFromDB.getMonthOfYear() + ":"
						+ dateTimeFromDB.getDayOfMonth() + ":"
						+ dateTimeFromDB.getHourOfDay() + ":"
						+ dateTimeFromDB.getMinuteOfHour() + ":"
						+ dateTimeFromDB.getSecondOfMinute();
				DateTime dateTimeFromEvent = preyItem.getTime();
				// Set correct alarm image at start-up
				String dateTimeFromEventString = dateTimeFromEvent.getYear()
						+ ":" + dateTimeFromEvent.getMonthOfYear() + ":"
						+ dateTimeFromEvent.getDayOfMonth() + ":"
						+ dateTimeFromEvent.getHourOfDay() + ":"
						+ dateTimeFromEvent.getMinuteOfHour() + ":"
						+ dateTimeFromEvent.getSecondOfMinute();
				if (dateTimeFromEventString.equals(dateTimeFromDBString) == true) {

					preyItem.setAlarmBoolean(true);
				}
			} while (cursor.moveToPrevious() && cursor.getInt(0) > 0);

		} catch (CursorIndexOutOfBoundsException e) {
			e.printStackTrace();
			db.close();

		}

	}

}
