package com.example.snms;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.Volley;
import com.example.snms.HolidayListFragment.HolidayListAdapter;
import com.example.snms.domain.HolydayItem;
import com.example.snms.domain.NewsItem;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewsListFragment extends ListFragment {

	private NewsListAdapter adapter;
	private View mheaderView;
	private RequestQueue requestQueue;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.requestQueue = Volley.newRequestQueue(this.getActivity());
		mheaderView = inflater.inflate(R.layout.news_header, null);
		return inflater.inflate(R.layout.list, null);
	}
	
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new NewsListAdapter(getActivity());
		putPreyItemsOnRequestQueue();
		getListView().addHeaderView(mheaderView);
		setListAdapter(adapter);
	}
	
	private void putPreyItemsOnRequestQueue() {
	    GsonRequest<NewsItem[]> jsObjRequest = new GsonRequest<NewsItem[]> (
	        Method.GET,
	        "http://46.137.184.176:3000/api/news",
	        NewsItem[].class,
	        this.createSuccessListener(),
	        this.createErrorListener());
	    this.requestQueue.add(jsObjRequest);
	}
			
	private Response.Listener <NewsItem[]> createSuccessListener() {
	    return new Response.Listener <NewsItem[]>() {
	       
	    	@Override
			public void onResponse(NewsItem[] response) {
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
	
	
	public class NewsListAdapter extends ArrayAdapter<NewsItem> {

		public NewsListAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_row, null);
			}
			TextView title = (TextView) convertView.findViewById(R.id.row_news_title);
			TextView text = (TextView) convertView.findViewById(R.id.row_news_text);
			
			NewsItem h =  getItem(position);
			
			title.setText(getItem(position).getTitle());
			text.setText(h.getText());
			return convertView;
		}
}
	
}
