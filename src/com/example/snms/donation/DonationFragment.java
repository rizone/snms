package com.example.snms.donation;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.NumberPicker;
import android.widget.TextView;

public class DonationFragment extends Fragment implements OnClickListener, NumberPicker.OnValueChangeListener  {
	
	
	private NumberPicker picker; 
	private Button donationButton; 
	private String[] nums = new String[100];
	private TextView dontationText; 
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.donation_wigdet, null);
		picker = (NumberPicker)root.findViewById(R.id.donationPicker);
		donationButton =(Button)root.findViewById(R.id.donerButton);
		dontationText = (TextView)root.findViewById(R.id.donationSum);
		picker.setOnValueChangedListener(this);
		 
		for(int i=0; i<nums.length; i++)
		   nums[i] = Integer.toString(i*50);

		
		return root;
	}

	/*
	 * image.setImageUrl(newsItem.getImgUrl(),
	 * ImageCacheManager.getInstance().getImageLoader());
	 */

	@Override
	public void onResume() {
		super.onResume();
		donationButton.setOnClickListener(this);
		picker.setMaxValue(nums.length-1);
		picker.setMinValue(0);
		picker.setWrapSelectorWheel(false);
		picker.setDisplayedValues(nums);

	}

	@Override
	public void onPause() {
		super.onPause();

	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public DonationFragment() {
		super();

	}

	@Override
	public void onClick(View v) {
		if(v.equals(donationButton)){
			 Intent smsIntent = new Intent(Intent.ACTION_VIEW);
			    smsIntent.putExtra("sms_body", "Jeg donerer " + picker.getValue()*50 +" kr."); 
			    smsIntent.putExtra("address", "93892904");
			    smsIntent.setType("vnd.android-dir/mms-sms");
			    startActivity(smsIntent);
		}
		
	}

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		dontationText.setText(newVal*50 + "kr");
		
	}


}
