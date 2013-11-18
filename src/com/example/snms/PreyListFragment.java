package com.example.snms;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.snms.domain.PreyItem;
import com.example.snms.domain.PreyItemList;
import com.example.snms.utils.PrayTime;
import com.example.snms.utils.SnmsPrayTimeAdapter;


import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PreyListFragment extends ListFragment implements OnClickListener {
	

	private DateTime currentDate; 
	private View mheaderView;
	private PreyListAdapter adapter;
	private List<PreyItem> preyTimes;
	private TextView currentDay; 
	private TextView nextDay; 
	private TextView calender;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	//	putPreyItemsOnRequestQueue();
		mheaderView = inflater.inflate(R.layout.prey_header, null);
		currentDay = (TextView)mheaderView.findViewById(R.id.prey_current_day); 
		nextDay = (TextView)mheaderView.findViewById(R.id.prey_next_day); 
		nextDay.setOnClickListener(this);
		currentDate = new DateTime();
		return inflater.inflate(R.layout.list, null);
		
	}
	
	CountDownTimer timer =  new CountDownTimer(60000, 6000) {

	     public void onTick(long millisUntilFinished) {
	       //DO NOTHING
	     }

	     public void onFinish() {
	    	 adapter.notifyDataSetChanged();
	         this.start();
	     }
	  };
	  
	public void updateList() {
		
	}
	
	
	public List<PreyItem> loadPrayTimes(DateTime dateTime) {
		
		SnmsPrayTimeAdapter prayTimeAdapter = new SnmsPrayTimeAdapter();
		//sdsds
		DateTime midnight = dateTime.minusHours(dateTime.getHourOfDay()).minusMinutes(dateTime.getMinuteOfHour()).minusSeconds(dateTime.getSecondOfMinute());
		return prayTimeAdapter.getPrayListForDate(midnight);

	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		preyTimes = loadPrayTimes(new DateTime());
		setUpCurrentDay();
		adapter = new PreyListAdapter(getActivity());
		mheaderView.setPadding(0, 0, 0, 0);
		getListView().addHeaderView(mheaderView);
		//putPreyItemsOnRequestQueue();
		setListAdapter(adapter);
		adapter.setActivePreys(preyTimes);
		adapter.clear();
		adapter.setActivePreys(preyTimes);
		for(PreyItem preyItem :  preyTimes) {
			adapter.add(preyItem);
		}
		timer.start();
	}
	
	
	private void setUpCurrentDay() {
		
		int dayOfWeek = currentDate.getDayOfWeek();
		String day; 
		switch (dayOfWeek) {
			
			case 1 : 
				day = "Mandag"; 
				break; 
			case 2 : 
				day = "Tirsdag"; 
				break; 
			case 3 : 
				day = "Onsdag"; 
				break;
			case 4 : 
				day = "Torsdag"; 
				break;
			case 5 : 
				day = "Fredag"; 
				break;
			case 6 : 
				day = "Lørdag"; 
				break;
			case 7 : 
				day = "Søndag"; 
				break;
			default : 
				day = "Ukjent";
				break;				
			
		}
		day+= " " + currentDate.getDayOfMonth() + "." + currentDate.getMonthOfYear() + "." + currentDate.getYear();
		currentDay.setText(day);
		
		
	}
	
	
	/*
	
	private void putPreyItemsOnRequestQueue() {
	    GsonRequest<PreyItemList> jsObjRequest = new GsonRequest<PreyItemList>(
	        Method.GET,
	        "http://46.137.184.176:3000/api/prayer/year/2013/month/11/day/4",
	        PreyItemList.class,
	        this.createSuccessListener(),
	        this.createErrorListener());
	    RequestManager.getRequestQueue().add(jsObjRequest);
	}
			
	private Response.Listener<PreyItemList> createSuccessListener() {
	    return new Response.Listener<PreyItemList>() {
	       
	    	@Override
			public void onResponse(PreyItemList response) {
	    		adapter.setActivePreys(response.getPreylist());
	    		adapter.clear();
	    		for(PreyItem preyItem : response.getPreylist()) {
	    			adapter.add(preyItem);
	    		}
	    		timer.start();
				// TODO Auto-generated method stub
				
			}
	    };
	}
	
	private Response.ErrorListener createErrorListener() {
	    return new Response.ErrorListener() {
	        @Override
	        public void onErrorResponse(VolleyError error) {
	        	//TODO : Log error and get prey times from local storage
	            error.getStackTrace();
	        	Log.e("error", error.getMessage());
	        }
	    };
	}
	
	*/
		
	public class PreyListAdapter extends ArrayAdapter<PreyItem> {
		
		List<PreyItem> currentPreys; 
		
		public PreyListAdapter(Context context) {
			super(context, 0);
		}
		
		public void setActivePreys(List<PreyItem> currentPreys){
			this.currentPreys = currentPreys;
		}
		
		private boolean isActive(PreyItem preyItem) {
			List <PreyItem> candidates = new ArrayList<PreyItem>();
			for(PreyItem candiate : currentPreys){
				if(candiate.getTime().isBeforeNow()){
					candidates.add(candiate);
				}
			}
			Collections.sort(candidates);
			return candidates.get(candidates.size()-1).equals(preyItem);
			//Hvis det er større enn
		}
		
		private boolean isNext(PreyItem preyItem) {
			List <PreyItem> candidates = new ArrayList<PreyItem>();
			for(PreyItem candiate : currentPreys){
				if(candiate.getTime().isAfterNow()){
					candidates.add(candiate);
				}
			}
			Collections.sort(candidates);
			return candidates.get(0).equals(preyItem);
		}
		

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.prey_row, null);
			}
				
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).getName());
			TextView time = (TextView) convertView.findViewById(R.id.row_time);
			PreyItem item = getItem(position);
			
			time.setText(getItem(position).getTime().getHourOfDay()+":" + getItem(position).getTime().getMinuteOfHour());
			
			TextView status = (TextView) convertView.findViewById(R.id.row_status);
			DateTime preyDate = item.getTime();			
			String preyText = ""; 
			
			if(preyDate.isBeforeNow()){
				preyText = "Ferdig";
				if(isActive(item)){
					preyText = "Aktiv";
				}
			}
			if(preyDate.isAfterNow()) {
				preyText = "-";
				if(isNext(item)){
					DateTime delta = item.getTime().minus(DateTime.now().getMillis());
					delta = delta.minusHours(1);
					preyText = String.valueOf(delta.getMinuteOfDay());
				}
		
			}
			status.setText(preyText);
			
			return convertView;
		}
		
	
		

	}



	public void setPreyList(List<PreyItem> preyTimes2) {
		preyTimes = preyTimes2;
		
	}


	@Override
	public void onClick(View v) {
		if(v.equals(nextDay)){
			currentDate = currentDate.plusDays(1);
			setUpCurrentDay();
			preyTimes = loadPrayTimes(currentDate);
			adapter.setActivePreys(preyTimes);
			adapter.clear();
			adapter.setActivePreys(preyTimes);
			for(PreyItem preyItem :  preyTimes) {
				adapter.add(preyItem);
			}
			adapter.notifyDataSetChanged();
		}
		
	}




}
