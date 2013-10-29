package com.example.snms;


import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.snms.domain.PreyItem;
import com.example.snms.domain.PreyItemList;


import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PreyListFragment extends ListFragment {
	
	private RequestQueue requestQueue;
	private View mheaderView;
	private PreyListAdapter adapter;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.requestQueue = Volley.newRequestQueue(this.getActivity());
		putPreyItemsOnRequestQueue();
		mheaderView = inflater.inflate(R.layout.prey_header, null);
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
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new PreyListAdapter(getActivity());
		this.requestQueue = Volley.newRequestQueue(this.getActivity());
		mheaderView.setPadding(0, 0, 0, 0);
		getListView().addHeaderView(mheaderView);
		putPreyItemsOnRequestQueue();
		setListAdapter(adapter);
		timer.start();
	}
	
	private void putPreyItemsOnRequestQueue() {
	    GsonRequest<PreyItemList> jsObjRequest = new GsonRequest<PreyItemList>(
	        Method.GET,
	        "http://46.137.184.176:3000/api/prayer/year/2013/month/10/day/30",
	        PreyItemList.class,
	        this.createSuccessListener(),
	        this.createErrorListener());
	    this.requestQueue.add(jsObjRequest);
	}
			
	private Response.Listener<PreyItemList> createSuccessListener() {
	    return new Response.Listener<PreyItemList>() {
	       
	    	@Override
			public void onResponse(PreyItemList response) {
	    		adapter.clear();
	    		for(PreyItem preyItem : response.getPreylist()) {
	    			adapter.add(preyItem);
	    		}
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
	
		
	public class PreyListAdapter extends ArrayAdapter<PreyItem> {
		
		List<PreyItem> currentPreys; 
		
		public PreyListAdapter(Context context) {
			super(context, 0);
		}
		
		private boolean isActive(PreyItem preyItem) {
			Date currentTime = new Date(System.currentTimeMillis());
			return true;
			//Hvis det er større enn
			
		}
		

		public View getView(int position, View convertView, ViewGroup parent) {
			
			Date currentTime = new Date(System.currentTimeMillis());
			
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.prey_row, null);
			}
				
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).getName());
			TextView time = (TextView) convertView.findViewById(R.id.row_time);
			PreyItem item = getItem(position);
			time.setText(getItem(position).getTime().getHours()+":" + getItem(position).getTime().getMinutes());
			
			TextView status = (TextView) convertView.findViewById(R.id.row_status);
			String newHour = String.valueOf(item.getTime().getHours() - currentTime.getHours());
			String newMinute = String.valueOf(item.getTime().getMinutes() - currentTime.getMinutes());
			status.setText(newHour + ":" + newMinute);
			
			return convertView;
		}
		
	
		

	}

}
