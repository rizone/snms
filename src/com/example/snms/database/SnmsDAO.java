package com.example.snms.database;

import java.util.ArrayList;
import java.util.List;

import com.example.snms.alarm.Alarm;
import com.example.snms.domain.Jumma;
import android.util.Log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SnmsDAO extends  SQLiteOpenHelper {
	
	static SnmsDAO instance; 
	// Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "snms";
	
    private static final String TABLE_JUMMA = "jummatimes";
    
    private static final String TABLE_JUMMA_ID = "jummaid";
    
    private static final String TABLE_ALARM_ID = "alarmid";
    
    private static final String TABLE_ALARM_PREY = "prey";
    
    private static final String TABLE_ALARM_OFFSET = "offset";
    
    private static final String TABLE_JUMMA_FROM_MONTH = "jummafrommonth";
    
    private static final String TABLE_JUMMA_TO_MONTH = "jummatomonth";
    
    private static final String TABLE_JUMMA_FROM_DAY = "jummafromday";
    
    private static final String TABLE_JUMMA_TO_DAY = "jummatoday";
    
    private static final String TABLE_JUMMA_HOUR = "jummahour";
    
    private static final String TABLE_JUMMA_MINUTES = "jummaminutes";
    
    
    private static final String TABLE_JUMMA_UPDATED = "jummaupdated";
    
    private static final String TABLE_ALARM = "alarm";
    
	public SnmsDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
	
	 private static final String CREATE_TABLE_JUMMA = "CREATE TABLE "
	            + TABLE_JUMMA + "(" + TABLE_JUMMA_ID + " INTEGER PRIMARY KEY," + TABLE_JUMMA_FROM_MONTH
	            + " INTEGER," + TABLE_JUMMA_TO_MONTH + " INTEGER,"  + TABLE_JUMMA_FROM_DAY + " INTEGER," + TABLE_JUMMA_TO_DAY + " INTEGER," + TABLE_JUMMA_HOUR + " INTEGER,"+  TABLE_JUMMA_MINUTES+ " INTEGER," + TABLE_JUMMA_UPDATED
	            + " DATETIME" + ")";
	 
	 private static final String CREATE_TABLE_ALARM = "CREATE TABLE "
	            + TABLE_ALARM + "( ID INTEGER PRIMARY KEY   AUTOINCREMENT," +  TABLE_ALARM_PREY
	            + " TEXT," + TABLE_ALARM_OFFSET + " INTEGER" + ")";
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		 db.execSQL(CREATE_TABLE_JUMMA);
		 db.execSQL(CREATE_TABLE_ALARM);
		
	}
	
	public void deleteJummaList() {
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_JUMMA, null, null);
	}
	
	public void updateJummaList(List<Jumma> newJummaListFromServer)  {
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(TABLE_JUMMA, null, null);
	    for(Jumma item : newJummaListFromServer){
	    	ContentValues values = new ContentValues();
	        values.put(TABLE_JUMMA_FROM_MONTH, item.getFromMonth());
	        values.put(TABLE_JUMMA_TO_MONTH, item.getToMonth());
	        values.put(TABLE_JUMMA_TO_DAY,item.getToDay());
	        values.put(TABLE_JUMMA_FROM_DAY, item.getFromDay());
	        values.put(TABLE_JUMMA_HOUR, item.getHours());
	        values.put(TABLE_JUMMA_MINUTES, item.getMinuttes());
	        db.insert(TABLE_JUMMA, null, values);
	    }
	}
	/*
	 * 
	 * 	public Jumma( int fromDay,int fromMonth,int toDay,int toMonth,int hours, int minuttes) {
		this.fromMonth = fromMonth; 
		this.toMonth = toMonth; 
		this.fromDay = fromDay; 
		this.toDay = toDay; 
		this.hours = hours;
		this.minuttes = minuttes;
	}
	 * 
	 */
	
	 
	 public List<Alarm> getAlarms() {
	       List<Alarm> alarms = new ArrayList<Alarm>();
	        String selectQuery = "SELECT  * FROM " + TABLE_ALARM;
	        Log.e("alarm", selectQuery);
	        SQLiteDatabase db = this.getReadableDatabase();
	        Cursor c = db.rawQuery(selectQuery, null);
	 
	        // looping through all rows and adding to list
	        if (c.moveToFirst()) {
	            do {
	                Alarm td = new Alarm(c.getString(c.getColumnIndex(TABLE_ALARM_PREY)),c.getInt(c.getColumnIndex(TABLE_ALARM_ID)));
	                alarms.add(td);
	            } while (c.moveToNext());
	        }
	        return alarms;
	    }
	 
	 
	 	
	 public void deleteAlarm(String alarm) {
	        String selectQuery = "DELETE FROM " + TABLE_ALARM + " WHERE " + TABLE_ALARM_PREY + " like " + alarm;
	        Log.e("alarm", selectQuery);
	        SQLiteDatabase db = this.getReadableDatabase();
	        db.rawQuery(selectQuery, null);
	 }
	 
	 
	 public void insertAlarm(Alarm alarm) {
	        String selectQuery = "INSERT INTO " + TABLE_ALARM + "(" + TABLE_ALARM_PREY + "," + TABLE_ALARM_OFFSET +") VALUES (" + alarm.getPrey() + "," + alarm.getOffset() + ")";
	        Log.e("alarm", selectQuery);
	        SQLiteDatabase db = this.getReadableDatabase();
	        db.rawQuery(selectQuery, null);
	 }
	 
		 
		 
	
	
	  public List<Jumma> getJummaList() {
	        List<Jumma> jumma = new ArrayList<Jumma>();
	        String selectQuery = "SELECT  * FROM " + TABLE_JUMMA;
	        Log.e("jumma", selectQuery);
	        SQLiteDatabase db = this.getReadableDatabase();
	        Cursor c = db.rawQuery(selectQuery, null);
	 
	        // looping through all rows and adding to list
	        if (c.moveToFirst()) {
	            do {
	                Jumma td = new Jumma(c.getInt(c.getColumnIndex(TABLE_JUMMA_FROM_DAY)),c.getInt(c.getColumnIndex(TABLE_JUMMA_FROM_MONTH))
	                		,c.getInt(c.getColumnIndex(TABLE_JUMMA_TO_DAY)),c.getInt(c.getColumnIndex(TABLE_JUMMA_TO_MONTH)),
	                		c.getInt(c.getColumnIndex(TABLE_JUMMA_HOUR)),c.getInt(c.getColumnIndex(TABLE_JUMMA_MINUTES))
	                		);
	                jumma.add(td);
	            } while (c.moveToNext());
	        }
	        return jumma;
	    }
	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		   db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_JUMMA);
		   onCreate(db);
	}
	
	 public void closeDB() {
	        SQLiteDatabase db = this.getReadableDatabase();
	        if (db != null && db.isOpen())
	            db.close();
	 }
	
	
}
