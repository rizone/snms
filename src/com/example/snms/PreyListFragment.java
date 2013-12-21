package com.example.snms;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.snms.domain.PreyItem;
import com.example.snms.domain.PreyItemList;
import com.example.snms.news.NewsItem;
import com.example.snms.utils.PrayTime;
import com.example.snms.utils.SnmsPrayTimeAdapter;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import android.widget.ListView;

import android.widget.TextView;
import android.widget.ToggleButton;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

public class PreyListFragment extends ListFragment implements OnClickListener,  OnDateSetListener, TimePickerDialog.OnTimeSetListener  {
	OnHeadlineSelectedListener mCallback;

	private DateTime currentDate;
	private DateTime timeCurrentlyUsedInPreyOverView; 
	private View mheaderView;
	private PreyListAdapter adapter;
	private List<PreyItem> preyTimes;
	private TextView currentDay;
	private ImageView nextDay;
	private ImageView prevDay;
	private TextView calender;
	private DateTime currentDateTime;
	  DatePickerDialog datePickerDialog;
	   public static final String DATEPICKER_TAG = "datepicker";
	   
	private CountDownTimer preyCountDownTimer;   
	  
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// putPreyItemsOnRequestQueue();
		mheaderView = inflater.inflate(R.layout.prey_header, null);
		currentDay = (TextView) mheaderView.findViewById(R.id.prey_current_day);
		currentDay.setOnClickListener(this);
		nextDay = (ImageView) mheaderView.findViewById(R.id.prey_next_day);
		nextDay.setOnClickListener(this);
		prevDay = (ImageView) mheaderView.findViewById(R.id.prey_prev_day);
		prevDay.setOnClickListener(this);
		currentDate = new DateTime();
		timeCurrentlyUsedInPreyOverView = currentDate;

        final Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
		return inflater.inflate(R.layout.preylist, null);

	}

//	CountDownTimer timer = new CountDownTimer(1000, 100) {
//
//		public void onTick(long millisUntilFinished) {
//			// DO NOTHING
//		}
//
//		public void onFinish() {
//			adapter.notifyDataSetChanged();
//			this.start();
//		}
//	};

	public void updateList() {

	}

	// Container Activity must implement this interface
	public interface OnHeadlineSelectedListener {
		public void onArticleSelected(DateTime time, String name);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnHeadlineSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Send the event to the host activity

		PreyItem clickedDetail = (PreyItem) l.getItemAtPosition(position);
		if (clickedDetail != null) {
			ImageView image = (ImageView) v
					.findViewById(R.id.alarmclock_inactive);

			Context context = PreyOverView.getAppContext();
			AlarmUtilities Util = new AlarmUtilities();
			Intent intent = new Intent(context, AlarmReceiverActivity.class);

			// Find correct cal to cancel
			DateTime AlarmToCancel = clickedDetail.getTime();
			Integer[] date = new Integer[6];
			date[0] = AlarmToCancel.getYear();
			date[1] = AlarmToCancel.getMonthOfYear() - 1;
			date[2] = AlarmToCancel.getDayOfMonth();
			date[3] = AlarmToCancel.getHourOfDay();
			date[4] = AlarmToCancel.getMinuteOfHour();
			date[5] = 1;

			final Calendar cal = Util.GregorianCalender(date);

			if (cal.after(Calendar.getInstance())) {
				if (clickedDetail.getAlarmBoolean() == true) {

					// //Remove alarm
					Util.RemoveAlarm(Util.getAlarmId(cal), intent, context);
					//
					// //Remove alarm from database
					DBAdapter db = new DBAdapter(context);
					db.open();
					db.deleteAlarmsInDatabase(Util.getAlarmId(cal));
					db.close();

					// Switch alarmclock image
					image.setImageResource(R.drawable.alarmclock_inactive);
					clickedDetail.setAlarmBoolean(false);

				} else {
					// IF(COMPARETO==FERDIG) DO NOTHING!
					mCallback.onArticleSelected(clickedDetail.getTime(),
							clickedDetail.getName());
					// Change picture for alarm clock
					image.setImageResource(R.drawable.alarmclock);

					// Set boolean to true to mark alarm active
					clickedDetail.setAlarmBoolean(true);

				}

			} else {
				Toast.makeText(context,
						"Oops... Bønn er aktiv, eller har allerede passert",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	public List<PreyItem> loadPrayTimes(DateTime dateTime) {

		SnmsPrayTimeAdapter prayTimeAdapter = new SnmsPrayTimeAdapter(
				getActivity().getAssets());

		DateTime midnight = dateTime.minusHours(dateTime.getHourOfDay())
				.minusMinutes(dateTime.getMinuteOfHour())
				.minusSeconds(dateTime.getSecondOfMinute());
		return prayTimeAdapter.getPrayListForDate(midnight,true);

	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		preyTimes = loadPrayTimes(new DateTime());
		getListView().setDividerHeight(0);
		setUpCurrentDay();
		adapter = new PreyListAdapter(getActivity());
		mheaderView.setPadding(0, 0, 0, 0);
		getListView().addHeaderView(mheaderView);
		setListAdapter(adapter);
		adapter.setActivePreys(preyTimes);
		adapter.clear();
		adapter.setActivePreys(preyTimes);
		for (PreyItem preyItem : preyTimes) {
			adapter.add(preyItem);
//
//			if (preyItem.getAlarmBoolean() == true) {
//				View v = getListView();
//				ImageView image = (ImageView) v
//						.findViewById(R.id.alarmclock_inactive);
//				image.setImageResource(R.drawable.alarmclock);
//			}
//
		}
		// checkAlarmStateAtStartup();
	//	timer.start();
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
				+ timeCurrentlyUsedInPreyOverView.getMonthOfYear() + "." + timeCurrentlyUsedInPreyOverView.getYear();
		currentDay.setText(day);

	}

	/*
	 * 
	 * private void putPreyItemsOnRequestQueue() { GsonRequest<PreyItemList>
	 * jsObjRequest = new GsonRequest<PreyItemList>( Method.GET,
	 * "http://46.137.184.176:3000/api/prayer/year/2013/month/11/day/4",
	 * PreyItemList.class, this.createSuccessListener(),
	 * this.createErrorListener());
	 * RequestManager.getRequestQueue().add(jsObjRequest); }
	 * 
	 * private Response.Listener<PreyItemList> createSuccessListener() { return
	 * new Response.Listener<PreyItemList>() {
	 * 
	 * @Override public void onResponse(PreyItemList response) {
	 * adapter.setActivePreys(response.getPreylist()); adapter.clear();
	 * for(PreyItem preyItem : response.getPreylist()) { adapter.add(preyItem);
	 * } timer.start(); // TODO Auto-generated method stub
	 * 
	 * } }; }
	 * 
	 * private Response.ErrorListener createErrorListener() { return new
	 * Response.ErrorListener() {
	 * 
	 * @Override public void onErrorResponse(VolleyError error) { //TODO : Log
	 * error and get prey times from local storage error.getStackTrace();
	 * Log.e("error", error.getMessage()); } }; }
	 */

	public class PreyListAdapter extends ArrayAdapter<PreyItem> {

		List<PreyItem> currentPreys;

		public PreyListAdapter(Context context) {
			super(context, 0);
		}

		public void setActivePreys(List<PreyItem> currentPreys) {
			this.currentPreys = currentPreys;
		}

		private boolean isActive(PreyItem preyItem) {
			List<PreyItem> candidates = new ArrayList<PreyItem>();
			for (PreyItem candiate : currentPreys) {
				if (candiate.getTime().isBeforeNow()) {
					candidates.add(candiate);
				}
			}
			Collections.sort(candidates);
			return candidates.get(candidates.size() - 1).equals(preyItem);
			// Hvis det er større enn
		}

		private boolean isNext(PreyItem preyItem) {
			if(preyItem.getTime().getDayOfYear() == currentDate.getDayOfYear()) {
			List<PreyItem> candidates = new ArrayList<PreyItem>();
			for (PreyItem candiate : currentPreys) {
				if (candiate.getTime().isAfterNow()) {
					candidates.add(candiate);
				}
			}
			Collections.sort(candidates);
			return candidates.get(0).equals(preyItem);
			}
			else {
				return false;
			}
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.prey_row, null);
			TextView title = (TextView) convertView
					.findViewById(R.id.row_title);
			title.setText(getItem(position).getName());
			TextView time = (TextView) convertView.findViewById(R.id.row_time);
			PreyItem item = getItem(position);

			ImageView image = (ImageView) convertView
					.findViewById(R.id.alarmclock_inactive);
			
			//indicator.setBackgroundDrawable(background); 

			String ZeroPlusHour = Integer.toString(getItem(position).getTime()
					.getHourOfDay());
			if (getItem(position).getTime().getHourOfDay() < 10) {
				ZeroPlusHour = "0" + ZeroPlusHour;
			}
			String ZeroPlusMin = Integer.toString(getItem(position).getTime()
					.getMinuteOfHour());
			if (getItem(position).getTime().getMinuteOfHour() < 10) {
				ZeroPlusMin = "0" + ZeroPlusMin;
			}

			time.setText(ZeroPlusHour + ":" + ZeroPlusMin);

			TextView status = (TextView) convertView
					.findViewById(R.id.row_status);
			DateTime preyDate = item.getTime();
			String preyText = "";
			final float scale = getContext().getResources().getDisplayMetrics().density;
			int pixels = (int) (20 * scale + 0.5f);
			convertView.setPadding(pixels,4, 0, 4);
			if (preyDate.isBeforeNow()) {
				preyText = "";
				title.setTextColor(Color.LTGRAY);
				time.setTextColor(Color.LTGRAY);
				status.setTextColor(Color.LTGRAY);
				if (isActive(item) && (preyDate.getDayOfMonth()==DateTime.now().getDayOfMonth() && preyDate.getMonthOfYear()==DateTime.now().getMonthOfYear())) {
					preyText = "";
					title.setTextColor(Color.BLACK);
					convertView.setBackgroundResource(R.drawable.border_active_pray);
					//convertView.setLayoutParams(parent.getLayoutParams());
					convertView.setPadding(pixels, 30, 0, 30);
					status.setTextColor(Color.BLACK);
				
				}
			}
			if (preyDate.isAfterNow()) {
				
				if (isNext(item)) {
					//TODO: Don't hard code time difference
					DateTime delta = item.getTime().minus(
							DateTime.now().getMillis());
//					int hoursDelta = item.getTime().getHourOfDay() -now.getHourOfDay();
//							delta.toTimeOfDay().toString();
//					int secondsDelta = delta.getSecondOfMinute();
					preyCountDownTimer = new PreyCountDownTimer(delta.getMillis(), 1000, status, adapter);
					preyCountDownTimer.start();
				}

			}
			if (item.getAlarmBoolean() == true) {
			
				image.setImageResource(R.drawable.alarmclock);

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
		if (v.equals(currentDay)) {
		       datePickerDialog.setYearRange(1985, 2028);
               datePickerDialog.show(getFragmentManager(), DATEPICKER_TAG);
			/*
			timeCurrentlyUsedInPreyOverView = timeCurrentlyUsedInPreyOverView.plusDays(1);
			setUpCurrentDay();
			preyTimes = loadPrayTimes(timeCurrentlyUsedInPreyOverView);
			adapter.setActivePreys(preyTimes);
			adapter.clear();
			adapter.setActivePreys(preyTimes);
			for (PreyItem preyItem : preyTimes) {
				adapter.add(preyItem);
				// checkAlarmStateAtStartup(preyItem);
			}
			adapter.notifyDataSetChanged();
			*/
		}
		if (v.equals(nextDay)) {
			timeCurrentlyUsedInPreyOverView = timeCurrentlyUsedInPreyOverView.plusDays(1);
			setUpCurrentDay();
			preyTimes = loadPrayTimes(timeCurrentlyUsedInPreyOverView);
			adapter.setActivePreys(preyTimes);
			adapter.clear();
			adapter.setActivePreys(preyTimes);
			for (PreyItem preyItem : preyTimes) {
				adapter.add(preyItem);
				// checkAlarmStateAtStartup(preyItem);
			}
			adapter.notifyDataSetChanged();
			
		}
		if (v.equals(prevDay)) {
			timeCurrentlyUsedInPreyOverView = timeCurrentlyUsedInPreyOverView.minusDays(1);
			setUpCurrentDay();
			preyTimes = loadPrayTimes(timeCurrentlyUsedInPreyOverView);
			adapter.setActivePreys(preyTimes);
			adapter.clear();
			adapter.setActivePreys(preyTimes);
			for (PreyItem preyItem : preyTimes) {
				adapter.add(preyItem);
				// checkAlarmStateAtStartup(preyItem);
			}
			adapter.notifyDataSetChanged();

		}

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
		adapter.setActivePreys(preyTimes);
		adapter.clear();
		adapter.setActivePreys(preyTimes);
		for (PreyItem preyItem : preyTimes) {
			adapter.add(preyItem);
			// checkAlarmStateAtStartup(preyItem);
		}
		adapter.notifyDataSetChanged();
		
	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		
	}

}
