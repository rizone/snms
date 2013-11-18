package com.example.snms;

import java.util.Calendar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_ALARMDATE = "Alarmdate";
    public static final String KEY_ID = "Id";

    
    
    private static final String TAG = "DBAdapter";
    
    private static final String DATABASE_NAME = "datas";
    private static final String DATABASE_TABLE_DATES = "alarmdate";

    
    
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_DATES =
        "create table "+ DATABASE_TABLE_DATES + " (_id integer primary key autoincrement, "
        + "Alarmdate text not null, Id text not null);"; 
    

    private static final String DATABSE_TABLE_LOCAL_SUBSCRIPTION="t_local_subscription";
//	private static final String DATABASE_CREATE_LOCAL_SUBSCRIPTION = "create table t_local_subscription (id integer primary key,subscription text);";
    private final Context context;  
	
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
        
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
            db.execSQL(DATABASE_CREATE_DATES);

            
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                  + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS calls");

            onCreate(db);
        }

    }    
    

   //---opens the database---
    public DBAdapter open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    
    //---insert an alarm into the database---
    public long insertAlarmsInDatabase(String cal, Integer id) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ALARMDATE, cal);
        initialValues.put(KEY_ID, id);
        
        
        return db.insert(DATABASE_TABLE_DATES, null, initialValues);
    }


    //---deletes a particular title---
    public boolean deleteAlarmsInDatabase(Integer id) 
    {
        return db.delete(DATABASE_TABLE_DATES, KEY_ID + "=" + id, null) > 0;
        
    }


    //---retrieves all the titles---
    public Cursor getAllAlarms() 
    {
        return db.query(DATABASE_TABLE_DATES, new String[] {
        		KEY_ROWID, KEY_ALARMDATE, KEY_ID}, null, null, null, null, KEY_ROWID);
    }
 

    //---retrieves a particular title---
    public Cursor getAlarm() throws SQLException 
    {
        Cursor mCursor = db.query(true, DATABASE_TABLE_DATES, new String[] {KEY_ROWID, 
        		KEY_ALARMDATE, KEY_ID}, null, null, null, null, KEY_ID, null);
        if(mCursor.moveToFirst()==false){
        	throw new IndexOutOfBoundsException();
        }else{
        mCursor.moveToFirst();
        }
        return mCursor;
    }


    
  
	public SQLiteDatabase getDatabase() {
		// TODO Auto-generated method stub
		return DBHelper.getWritableDatabase();
	}
	
	   public BaseActivity getSubscription() 
		{
	    	Gson gson = new GsonBuilder().create();
			Cursor cursor = db.query(DATABSE_TABLE_LOCAL_SUBSCRIPTION, null, null, null, null, null,null);
			if (cursor.moveToFirst()) 
			{
				String subscriptonJson = (cursor.getString(1));
				return gson.fromJson(subscriptonJson, BaseActivity.class);
				
			}
			else 
			{
				return null;
			}
		}
	    
	    public void saveSubscription(String subscription)
		{
			
			ContentValues initialValues = new ContentValues();
			initialValues.put("id", 1);
			initialValues.put("subscription", subscription);
			
			
			db.insert(DATABSE_TABLE_LOCAL_SUBSCRIPTION, null, initialValues);
		}
	    
	    
	    public boolean updateSubscription(String json)
	    {
	    	 ContentValues args = new ContentValues();
	         args.put("subscription", json);
	         return db.update(DATABSE_TABLE_LOCAL_SUBSCRIPTION, args, 
	                 "id=1", null) > 0;
	    }
    



}
