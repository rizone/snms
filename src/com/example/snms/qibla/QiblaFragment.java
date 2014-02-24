package com.example.snms.qibla;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.android.volley.toolbox.NetworkImageView;
import com.example.snms.R;
import com.example.snms.R.id;
import com.example.snms.R.layout;
import com.example.snms.images.ImageCacheManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class QiblaFragment extends Fragment implements  SensorEventListener, LocationListener  {
	
	
	
	/*
	 * 
	 * var y = Math.sin(dLon) * Math.cos(lat2);
var x = Math.cos(lat1)*Math.sin(lat2) -
        Math.sin(lat1)*Math.cos(lat2)*Math.cos(dLon);
var brng = Math.atan2(y, x).toDeg();
	 * 
	 */
	private LocationManager locationManager;
	  // define the display assembly compass picture
    private QuiblaCompassImage image;

    // record the compass picture angle turned
    private float currentDegree = 0f;

    // device sensor manager
    private SensorManager mSensorManager;
    
    
    private Location location; 

    TextView tvHeading;
    
    double qiblaLat = 21.4225;
    double qiblaLong = 39.8261 ;


	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

	    View root = inflater.inflate(R.layout.qibla,null);

        // 
        image = (QuiblaCompassImage) root.findViewById(R.id.imageViewCompass);
        // TextView that will tell the user what degree is he heading
        tvHeading = (TextView) root.findViewById(R.id.tvHeading);

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) this.getActivity().getSystemService(Activity.SENSOR_SERVICE);
        locationManager = (LocationManager) this.getActivity().getSystemService(Activity.LOCATION_SERVICE);
		return root;
		}

		/*
		 * image.setImageUrl(newsItem.getImgUrl(),
		 * ImageCacheManager.getInstance().getImageLoader());
		 */


	@Override
	public void onResume() {
		super.onResume();
	    // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
      locationManager.requestLocationUpdates(
             LocationManager.NETWORK_PROVIDER,0,0, this);
        
        
       // getQiblaAngle();
        float angle = (float)getQiblaAngle();
        this.image.setAngle(angle);
     
	}
	
	   @Override
	    public void onPause() {
	        super.onPause();

	        // to stop the listener and save battery
	        mSensorManager.unregisterListener(this);
	    }

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public QiblaFragment() {
		super();

	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		   // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);

        tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree, 
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f, 
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        currentDegree = -degree;
		
	}

	private double getQiblaAngle() {
		/*
		var y = Math.sin(dLon) * Math.cos(lat2);
		var x = Math.cos(lat1)*Math.sin(lat2) -
		        Math.sin(lat1)*Math.cos(lat2)*Math.cos(dLon);
		var brng = Math.atan2(y, x).toDeg();
		
		*/
		
	//	double lat1 = this.location.getLatitude();
	//	double lon1 = this.location.getLongitude();
		
		double lat1 = 59.0;
		double lon1 = 18.0;
		double lat2 = qiblaLat;
		double lon2 = qiblaLong;
		
		double y = Math.sin(lon2-lon1)*Math.cos(lat2);
		double x = Math.cos(lat1)*Math.sin(lat2) -   Math.sin(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1);
//		double tc1 = -1; 
//		  if (y > 0){
//		    if(x > 0)
//		    	tc1 = Math.atan(y/x)*180/Math.PI;
//		    if (x <0) 
//		    	tc1 = 180 - Math.atan(-y/x)*180/Math.PI;
//		    if(x == 0) 
//		    	tc1 = 90.0;
//		  }
//		  if(y < 0) {
//		    if (x > 0)
//		    	tc1 = -Math.atan(-y/x)*180/Math.PI;
//		    if (x < 0) 
//		    	tc1 = (Math.atan(y/x)*180/Math.PI)-180;
//		    if (x == 0)
//		    	tc1 = 270.0;
//		  }
//		  if( y == 0 ){
//		    if (x > 0) 
//		    	tc1 = 0.0;
//		    if (x < 0 )
//		    	tc1 = 180.0;
//		    if (x == 0)
//		    	tc1 = 0.0;
//		  }
		
		
		double angle = (Math.atan2(y, x) % 2*Math.PI)*180/Math.PI;
//		angle = angle + 360 % 360;
		
		
		
		return angle;
	}
	
	
	
	
	@Override
	public void onLocationChanged(Location location) {
		if(location!=null){
			this.location = location; 
			locationManager.removeUpdates(this);
		}
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	//1) Get location
	//2) Calculate vetvor
	//3) Draw vetvor 
	
	
	
	
	
	
	
	
	
	
	
	
	


}
