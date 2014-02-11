package com.example.snms.preylist;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.example.snms.AlarmUtilities;
import com.example.snms.BaseActivity;
import com.example.snms.MainApplication;
import com.example.snms.NewsDetailsFragment;
import com.example.snms.PreyCountDownTimer;
import com.example.snms.PreyOverView;
import com.example.snms.R;
import com.example.snms.alarm.Alarm;
import com.example.snms.alarm.AlarmChangeListner;
import com.example.snms.alarm.AlarmDialogFragment;
import com.example.snms.database.SnmsDAO;
import com.example.snms.domain.PreyItem;
import com.example.snms.images.ImageCacheManager;
import com.example.snms.jumma.JummaAdaptor;
import com.example.snms.jumma.JummaListner;
import com.example.snms.news.NewsItem;
import com.example.snms.news.NewsManager;
import com.example.snms.news.NewsListFragment.NewsListAdapter;
import com.example.snms.news.NewsListFragment.NewsScrollListner;
import com.example.snms.utils.SnmsPrayTimeAdapter;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.sleepbot.datetimepicker.time.TimePickerDialog.OnTimeSetListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PreyOverviewFragment extends Fragment implements  OnClickListener,  OnDateSetListener, OnTimeSetListener, JummaListner, AlarmChangeListner{

	private DateTime currentDate;
	private DateTime timeCurrentlyUsedInPreyOverView;
	private int newsPage; 
	private int newsOffset;
	private int filter; 
	private List<PreyItem> preyTimes;
	private TextView currentDay;
	private ImageView nextDay;
	private ImageView  prevDay;
	private ImageView nextNews;
	private ImageView prevNews;
	private TextView calender;
	private DateTime currentDateTime;
	DatePickerDialog datePickerDialog;
	public static final String DATEPICKER_TAG = "datepicker";
	private JummaAdaptor jummaAdaptor; 
	private CountDownTimer preyCountDownTimer;
	private LinearLayout preyRowContainer;
	private LinearLayout jummaContainer;
	private RelativeLayout latestNewsContainer;
	private Map<String, View> preyNamePreyRowMap;
	private Map<String, ImageView> alarmButtonNameMap = new HashMap<String,ImageView>();
	
	private NetworkImageView newsImage1; 
	private TextView newsText1;
	
	private NetworkImageView newsImage2; 
	private TextView newsText2;

	private final Integer NUMBER_OF_PRAYS = 6;

	private final String[] PREY_LABLES = { "Fajr", "Soloppgang", "Dhuhr", "Asr",
			"Maghrib", "Isha"};
	protected NewsItem currentNewsItem2;
	protected NewsItem currentNewsItem1;
	Intent intent = new Intent(getAppContext(), PreyOverviewFragment.class);
	 
	AlarmUtilities Util;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.preyoverview, null);
		preyRowContainer = (LinearLayout) root
				.findViewById(R.id.preyRowContainer);
		addPreyRowsToContainerAndStoreInMap(inflater);
		currentDay = (TextView) root.findViewById(R.id.prey_current_day);
		currentDay.setOnClickListener(this);
		nextDay = (ImageView) root.findViewById(R.id.prey_next_day);
		nextDay.setOnClickListener(this);
		prevDay = (ImageView) root.findViewById(R.id.prey_prev_day);
		
		nextNews = (ImageView) root.findViewById(R.id.next_news);
		nextNews.setOnClickListener(this);
		prevNews = (ImageView) root.findViewById(R.id.prev_news);
		prevNews.setOnClickListener(this);
		
		
		latestNewsContainer = (RelativeLayout)root.findViewById(R.id.latestNewsContainer);
		prevDay.setOnClickListener(this);
		currentDate = new DateTime();
		timeCurrentlyUsedInPreyOverView = currentDate;
		newsImage1 = (NetworkImageView)root.findViewById(R.id.newsImage1);
		newsImage1.setVisibility(View.GONE);
		newsImage2 = (NetworkImageView)root.findViewById(R.id.newsImage2);
		newsImage2.setVisibility(View.GONE);
		newsText1 = (TextView)root.findViewById(R.id.newsImage1Text);
		newsText1.setVisibility(View.GONE);
		newsText2= (TextView)root.findViewById(R.id.newsImage2Text);
		newsText2.setVisibility(View.GONE);
		jummaContainer = (LinearLayout)root.findViewById(R.id.jummacontainer);
        final Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
		currentDay = (TextView) root.findViewById(R.id.prey_current_day);
		jummaAdaptor = new JummaAdaptor(((PreyOverView) getActivity()).getDAO());

		Util = new AlarmUtilities(((PreyOverView) getActivity()).getDAO());
		jummaAdaptor.addJummaListner(this);
		newsImage1.setOnClickListener(this);
		newsImage2.setOnClickListener(this);
		return root;
	}

	private void addPreyRowsToContainerAndStoreInMap(LayoutInflater inflater) {
		preyNamePreyRowMap = new HashMap<String, View>();
		for (int i = 0; i < NUMBER_OF_PRAYS; i++) {
			View row = inflater.inflate(R.layout.prey_row, null);
			preyRowContainer.addView(row);
			preyNamePreyRowMap.put(PREY_LABLES[i], row);
			ImageView alarmIcon = (ImageView)row.findViewById(R.id.alarmclock_inactive);
			alarmButtonNameMap.put(PREY_LABLES[i],alarmIcon);
			alarmIcon.setOnClickListener(this);
			
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		preyTimes = loadPrayTimes(new DateTime());
		setUpCurrentDay();
		jummaAdaptor.tryFethingJummaRemote(this.timeCurrentlyUsedInPreyOverView);
	//	mheaderView.setPadding(0, 0, 0, 0);
		renderPreyList();
		renderAlarmState();
		NewsManager.getInstance().getNews(createSuccessListener(), createErrorListener(),2,0,1);

	}

	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	public void renderAlarmState() {
		for (String key : alarmButtonNameMap.keySet()) {
			ImageView alarmIcon = alarmButtonNameMap.get(key);
			if(Util.hasAlarm(key) == true){
				alarmIcon.setImageResource(R.drawable.alarmclock);
			}else {
				alarmIcon.setImageResource(R.drawable.alarmclock_inactive);
			}
		}
		
	}
	
	
	
	public void renderPreyList() {
		
		if(this.preyCountDownTimer!=null) {
			this.preyCountDownTimer.cancel();
			this.preyCountDownTimer = null; 
		}
		
		if(currentDateUsedInAppIsMoreThanOneDayAgo()){
			renderAllPreysAsPassed(); 
		}
		if(currentDateUsedInAppIsMoreThanOneInthaFuture()){
			renderAllPreysAsFuturu(); 
		}
		
		for (PreyItem item : preyTimes) {
			View preyRow = this.preyNamePreyRowMap.get(item.getName());
			TextView title = (TextView) preyRow
					.findViewById(R.id.row_title);
			TextView status = (TextView) preyRow
					.findViewById(R.id.row_status);
			TextView time = (TextView) preyRow.findViewById(R.id.row_time);
			ImageView image = (ImageView) preyRow
					.findViewById(R.id.alarmclock_inactive);
			
			if (isPassed(item)) {
				renderPassed(item,preyRow,title, time,status,image);
			}
			if (isFuture(item)) {
				renderFuture(item,preyRow,title, time,status,image);
			}
			if (isActive(item)) {
				renderActive(item,preyRow,title, time,status,image);
			}
			if (isNext(item)) {
				renderNext(item,preyRow,title, time,status,image);
				// TODO: Trigger a new count down
			}
		}
		

	}

	private void renderAllPreysAsFuturu() {
		for (PreyItem item : preyTimes) {
			View preyRow = this.preyNamePreyRowMap.get(item.getName());
			TextView title = (TextView) preyRow
					.findViewById(R.id.row_title);
			TextView status = (TextView) preyRow
					.findViewById(R.id.row_status);
			TextView time = (TextView) preyRow.findViewById(R.id.row_time);
			ImageView image = (ImageView) preyRow
					.findViewById(R.id.alarmclock_inactive);
			renderFuture(item,preyRow,title, time,status,image);
		// TODO: Trigger a new count down
			}
		
	}

	private void renderAllPreysAsPassed() {
		for (PreyItem item : preyTimes) {
			View preyRow = this.preyNamePreyRowMap.get(item.getName());
			TextView title = (TextView) preyRow
					.findViewById(R.id.row_title);
			TextView status = (TextView) preyRow
					.findViewById(R.id.row_status);
			TextView time = (TextView) preyRow.findViewById(R.id.row_time);
			ImageView image = (ImageView) preyRow
					.findViewById(R.id.alarmclock_inactive);
			renderPassed(item,preyRow,title, time,status,image);
			}
		
	}

	private boolean currentDateUsedInAppIsMoreThanOneInthaFuture() {
		return timeCurrentlyUsedInPreyOverView.getDayOfYear()<DateTime.now().getDayOfYear();
	}

	private boolean currentDateUsedInAppIsMoreThanOneDayAgo() {
		return timeCurrentlyUsedInPreyOverView.getDayOfYear()<DateTime.now().getDayOfYear();
	}

	private boolean isActive(PreyItem preyItem) {
		List<PreyItem> candidates = new ArrayList<PreyItem>();
		if(!preyItem.getTime().isBeforeNow())
			return false;
		for (PreyItem candiate : preyTimes) {
			if (candiate.getTime().isBeforeNow()) {
				candidates.add(candiate);
			}
		}
		Collections.sort(candidates);
		return candidates.get(candidates.size() - 1).equals(preyItem) && (preyItem.getTime().getDayOfMonth()==DateTime.now().getDayOfMonth() && preyItem.getTime().getMonthOfYear()==DateTime.now().getMonthOfYear());
	}

	private boolean isNext(PreyItem preyItem) {
		if(!preyItem.getTime().isAfterNow())
			return false;
		if (preyItem.getTime().getDayOfYear() == currentDate.getDayOfYear()) {
			List<PreyItem> candidates = new ArrayList<PreyItem>();
			for (PreyItem candiate : preyTimes) {
				if (candiate.getTime().isAfterNow()) {
					candidates.add(candiate);
				}
			}
			Collections.sort(candidates);
			return candidates.get(0).equals(preyItem);
		} else {
			return false;
		}
	}

	private boolean isPassed(PreyItem key) {
		return key.getTime().isBeforeNow();
	}

	private boolean isFuture(PreyItem key) {
		return key.getTime().isAfterNow();
	}

	private void renderActive(PreyItem key,View container, TextView title, TextView time, TextView status, ImageView image) {
		final float scale = this.getResources().getDisplayMetrics().density;
		int pixels = (int) (20 * scale + 0.5f);
		title.setTextColor(Color.BLACK);
		container.setBackgroundResource(R.drawable.border_active_pray);
		container.setPadding(pixels, 30, 0, 30);
		status.setTextColor(Color.BLACK);
	}

	private void renderPassed(PreyItem key,View container, TextView title, TextView time, TextView status, ImageView image) {
		View preyRow = this.preyNamePreyRowMap.get(key.getName());
		renderGeneralProperties(key,preyRow,title,time,status,image);
		title.setTextColor(Color.LTGRAY);
		time.setTextColor(Color.LTGRAY);
		
		status.setTextColor(Color.LTGRAY);
	}

	private void renderNext(PreyItem key,View container, TextView title, TextView time, TextView status, ImageView image) {
		DateTime delta = key.getTime().minus(
				DateTime.now().getMillis());
		preyCountDownTimer = new PreyCountDownTimer(delta.getMillis(), 1000, status, this);
		preyCountDownTimer.start();
	}

	private void renderFuture(PreyItem key,View container, TextView title, TextView time, TextView status, ImageView image) {
		title.setTextColor(Color.BLACK);
		status.setTextColor(Color.BLACK);
		time.setTextColor(Color.BLACK);
		renderGeneralProperties(key, container, title, time, status, image);
	}
	
	
	private void renderGeneralProperties(PreyItem item,View convertView,TextView title, TextView time, TextView status, ImageView image) {
		//indicator.setBackgroundDrawable(background); 
		String ZeroPlusHour = Integer.toString(item.getTime()
				.getHourOfDay());
		if (item.getTime().getHourOfDay() < 10) {
			ZeroPlusHour = "0" + ZeroPlusHour;
		}
		String ZeroPlusMin = Integer.toString(item.getTime()
				.getMinuteOfHour());
		if (item.getTime().getMinuteOfHour() < 10) {
			ZeroPlusMin = "0" + ZeroPlusMin;
		}
		time.setText(ZeroPlusHour + ":" + ZeroPlusMin);
		title.setText(item.getName());
		status.setText("");
		convertView.setBackgroundResource(R.drawable.border_none_active_pray);
		final float scale = this.getResources().getDisplayMetrics().density;
		int pixels = (int) (20 * scale + 0.5f);
		convertView.setPadding(pixels,10, 0, 10);
	}

	private void setUpCurrentDay() {
		int dayOfWeek = timeCurrentlyUsedInPreyOverView.getDayOfWeek();
		String day;
		switch (dayOfWeek) {
		case 1:
			day = "Mandag";
			break;
		case 2:
			day = "Tirsdag";
			break;
		case 3:
			day = "Onsdag";
			break;
		case 4:
			day = "Torsdag";
			break;
		case 5:
			day = "Fredag";
			break;
		case 6:
			day = "Lørdag";
			break;
		case 7:
			day = "Søndag";
			break;
		default:
			day = "Ukjent";
			break;

		}
		day += " " + timeCurrentlyUsedInPreyOverView.getDayOfMonth() + "."
				+ timeCurrentlyUsedInPreyOverView.getMonthOfYear() + "."
				+ timeCurrentlyUsedInPreyOverView.getYear();
		currentDay.setText(day);
	}

	public List<PreyItem> loadPrayTimes(DateTime dateTime) {
		SnmsPrayTimeAdapter prayTimeAdapter = new SnmsPrayTimeAdapter(
				getActivity().getAssets());
		DateTime midnight = dateTime.minusHours(dateTime.getHourOfDay())
				.minusMinutes(dateTime.getMinuteOfHour())
				.minusSeconds(dateTime.getSecondOfMinute());
		return prayTimeAdapter.getPrayListForDate(midnight);

	}

	@Override
	public void onClick(View v) {
		if (v.equals(currentDay)) {
		       datePickerDialog.setYearRange(1985, 2028);
               datePickerDialog.show(getFragmentManager(), DATEPICKER_TAG);
		}
		if (v.equals(nextDay)) {
			timeCurrentlyUsedInPreyOverView = timeCurrentlyUsedInPreyOverView.plusDays(1);
			setUpCurrentDay();
			preyTimes = loadPrayTimes(timeCurrentlyUsedInPreyOverView);
			renderPreyList();
			jummaAdaptor.tryFetchningJummaLocally(this.timeCurrentlyUsedInPreyOverView);
			
		}
		if (v.equals(prevDay)) {
			timeCurrentlyUsedInPreyOverView = timeCurrentlyUsedInPreyOverView.minusDays(1);
			setUpCurrentDay();
			preyTimes = loadPrayTimes(timeCurrentlyUsedInPreyOverView);
			renderPreyList();
			jummaAdaptor.tryFetchningJummaLocally(this.timeCurrentlyUsedInPreyOverView);

		}
		if (v.equals(nextNews)) {
			newsPage++; 
			newsImage1.setVisibility(View.GONE);
			newsImage2.setVisibility(View.GONE);
			newsText1.setVisibility(View.GONE);
			newsText2.setVisibility(View.GONE);
			NewsManager.getInstance().getNews(createSuccessListener(), createErrorListener(),2,newsPage,1);
		}
		if (v.equals(prevNews)) {
			newsPage--; 
			newsImage1.setVisibility(View.GONE);
			newsImage2.setVisibility(View.GONE);
			newsText1.setVisibility(View.GONE);
			newsText2.setVisibility(View.GONE);
			NewsManager.getInstance().getNews(createSuccessListener(), createErrorListener(),2,newsPage,1);
		}
		
		if (v.equals(newsImage1)) {
			   NewsDetailsFragment myDetailFragment = new NewsDetailsFragment(currentNewsItem1);
			   switchFragment(myDetailFragment,null);
		}
		if (v.equals(newsImage2)) {
			NewsDetailsFragment myDetailFragment = new NewsDetailsFragment(currentNewsItem2);
			switchFragment(myDetailFragment,null);
		}
		
		for(String key  :alarmButtonNameMap.keySet()){
			if(v.equals(alarmButtonNameMap.get(key))) {
				if(!Util.hasAlarm(key)) { 
					for(PreyItem preyItem:preyTimes){
						if(preyItem.getName().equals(key)){
					
							FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction(); //endre dette til å bruke setReapeting
							AlarmDialogFragment newFragment = AlarmDialogFragment.newInstance(preyItem); //var key
							newFragment.show(ft, "dialog");
							Bundle args = new Bundle(); 
							args.putString("prey", key);
							newFragment.setArguments(args);
							
						}
					}
				
			}else {
				
				AlarmUtilities Util = new AlarmUtilities(((PreyOverView) getActivity()).getDAO());
				Alarm alarm = Util.getAlarm(key);
				Util.RemoveAlarm(alarm.getId(), getAppContext(), alarm.getPrey());
				renderAlarmState();
				}
				
			}
		}
	}
		
	public static Context getAppContext() {
	    return MainApplication.getAppContext();
	}

	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year,
			int month, int day) {
		if(preyCountDownTimer!=null) {
			preyCountDownTimer.cancel();
			preyCountDownTimer = null;
		}
		timeCurrentlyUsedInPreyOverView = new DateTime(year,month+1,day,0,0);
		setUpCurrentDay();
		preyTimes = loadPrayTimes(timeCurrentlyUsedInPreyOverView);
		renderPreyList();
		jummaAdaptor.tryFetchningJummaLocally(this.timeCurrentlyUsedInPreyOverView);
		
	}
	
	private void switchFragment(Fragment fragment1, Fragment fragment2) {
		if (getActivity() == null)
			return;
		
		if (getActivity() instanceof BaseActivity) {
			BaseActivity fca = (BaseActivity) getActivity();
			fca.switchContent(fragment1, fragment2);
		} 
	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		
	}
	
	private Response.ErrorListener createErrorListener() {
	    return new Response.ErrorListener() {
	        @Override
	        public void onErrorResponse(VolleyError error) {
	        	//TODO : Log error and get prey times from local storage
	            //error.getStackTrace();
	        	newsText2.setText("ingen nyheter er tilgjenngelig");
	        	Log.e("error",error.toString());
	        }
	    };
	}
	

			
	@SuppressLint("NewApi")
	private Response.Listener <NewsItem[]> createSuccessListener() {
	    return new Response.Listener <NewsItem[]>() {
	       
	    	@Override
			public void onResponse(NewsItem[] response) {
	    		
	    		Display display = getActivity().getWindowManager().getDefaultDisplay();
	    		Point size = new Point();
	    		display.getSize(size);
	    		int width = (size.x/2);
	    	
	    		if(response.length>0) {
	    			NewsItem item = response[0];
	    			newsImage1.setVisibility(View.VISIBLE);
	    			newsText1.setVisibility(View.VISIBLE);
	    			newsText1.setText(item.getTitle());
					currentNewsItem1 = item;
					newsText1.setWidth(width);
					newsImage1.getLayoutParams().width = width;
					Uri uri = Uri.parse(item.getImgUrl());
				//	text.setText(h.getText());
					newsImage1.setImageUrl(item.getImgUrl(), ImageCacheManager.getInstance().getImageLoader());
	    		}
	    		
	    		if(response.length>1) {
	    			NewsItem item = response[1];
	    			currentNewsItem2 = item;
					newsText2.setText(item.getTitle());
					newsImage2.setVisibility(View.VISIBLE);
	    			newsText2.setVisibility(View.VISIBLE);
					newsText2.setWidth(width);
					Uri uri = Uri.parse(item.getImgUrl());
				//	text.setText(h.getText());
					newsImage2.getLayoutParams().width = width;
					newsImage2.setImageUrl(item.getImgUrl(), ImageCacheManager.getInstance().getImageLoader());
	    		}		
			}
	    };	
	}
	
	

	
	@Override
	public void updateJumma(PreyItem item) {
		
	 if(isAdded()) {
		TextView jummaTime = (TextView)jummaContainer.findViewById(R.id.row_time_jumma);
		TextView jummaStatus = (TextView)jummaContainer.findViewById(R.id.row_status_jumma);
		TextView jummaTitle = (TextView)jummaContainer.findViewById(R.id.row_title_jumma);
		jummaContainer.setBackgroundResource(R.drawable.border_none_active_pray);
		final float scale = this.getResources().getDisplayMetrics().density;
		int pixels = (int) (20 * scale + 0.5f);
		jummaContainer.setPadding(pixels,10, 0, 10);
		
		jummaTime.setText(item.getTimeOfDayAsString());
		jummaStatus.setText("");
		
		String ZeroPlusHour = Integer.toString(item.getTime()
				.getHourOfDay());
		if (item.getTime().getHourOfDay() < 10) {
			ZeroPlusHour = "0" + ZeroPlusHour;
		}
		String ZeroPlusMin = Integer.toString(item.getTime()
				.getMinuteOfHour());
		if (item.getTime().getMinuteOfHour() < 10) {
			ZeroPlusMin = "0" + ZeroPlusMin;
		}
		jummaTime.setText(ZeroPlusHour + ":" + ZeroPlusMin);
		jummaTitle.setText(item.getName());
		
	}}
	public void setAlarm(View v) {
		v.setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void alarmChanged() {
//		Util.SetRepeatingAlarm(prey, id, context, alarm, offset);
		renderAlarmState();
	}

}
