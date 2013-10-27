package com.example.snms;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.Volley;
import com.example.snms.PreyListFragment.PreyListAdapter;
import com.example.snms.domain.HolydayItem;
import com.example.snms.domain.PreyItem;
import com.example.snms.domain.PreyItemList;

public class HolidayListFragment extends ListFragment {
	
private RequestQueue requestQueue;
	
	private HolidayListAdapter adapter;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.requestQueue = Volley.newRequestQueue(this.getActivity());
		putPreyItemsOnRequestQueue();
		return inflater.inflate(R.layout.list, null);
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new HolidayListAdapter(getActivity());
		this.requestQueue = Volley.newRequestQueue(this.getActivity());
		putPreyItemsOnRequestQueue();
		setListAdapter(adapter);
	}
	
	private void putPreyItemsOnRequestQueue() {
	    GsonRequest<HolydayItem[]> jsObjRequest = new GsonRequest<HolydayItem[]> (
	        Method.GET,
	        "http://192.168.0.106:3000/api/holidays",
	        HolydayItem[].class,
	        this.createSuccessListener(),
	        this.createErrorListener());
	    this.requestQueue.add(jsObjRequest);
	}
			
	private Response.Listener <HolydayItem[]> createSuccessListener() {
	    return new Response.Listener <HolydayItem[]>() {
	       
	    	@Override
			public void onResponse(HolydayItem[] response) {
				adapter.addAll(response);
				
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
	
	
	public class HolidayListAdapter extends ArrayAdapter<HolydayItem> {

		public HolidayListAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.prey_row, null);
			}
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).getName());
			return convertView;
		}
		

	}
	

}
