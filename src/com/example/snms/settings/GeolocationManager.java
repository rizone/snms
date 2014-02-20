package com.example.snms.settings;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.example.snms.domain.GeolocationSearchResult;
import com.example.snms.network.GsonRequest;
import com.example.snms.network.RequestManager;

public class GeolocationManager {

	private final String TAG = getClass().getSimpleName();
	private static GeolocationManager mInstance;

	private static String NEWS_BASE =  "https://maps.googleapis.com/maps/api/geocode/json";


	public static GeolocationManager getInstance(){
		if(mInstance == null) {
			mInstance = new GeolocationManager();
		}

		return mInstance;
	}

	public void getGeolocation(Listener<GeolocationSearchResult> listener, ErrorListener errorListener, String search){
		Uri.Builder uriBuilder = Uri.parse(NEWS_BASE).buildUpon().appendQueryParameter("address",search).appendQueryParameter("sensor","false") ;
		String uri = uriBuilder.build().toString();
		GsonRequest<GeolocationSearchResult> request = new GsonRequest<GeolocationSearchResult>(Method.GET
				, uri
				, GeolocationSearchResult.class
				, listener
				, errorListener);
		Log.v(TAG, request.toString());
		RequestManager.getRequestQueue().add(request);
	}

}
