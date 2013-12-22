package com.example.snms.preylist;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.example.snms.PreyCountDownTimer;
import com.example.snms.R;
import com.example.snms.PreyListFragment.PreyListAdapter;
import com.example.snms.domain.PreyItem;
import com.example.snms.utils.SnmsPrayTimeAdapter;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PreyOverviewFragment extends Fragment implements  OnClickListener,  OnDateSetListener, TimePickerDialog.OnTimeSetListener{

	private DateTime currentDate;
	private DateTime timeCurrentlyUsedInPreyOverView;
	private List<PreyItem> preyTimes;
	private TextView currentDay;
	private ImageView nextDay;
	private ImageView prevDay;
	private TextView calender;
	private DateTime currentDateTime;
	DatePickerDialog datePickerDialog;
	public static final String DATEPICKER_TAG = "datepicker";

	private CountDownTimer preyCountDownTimer;

	private LinearLayout preyRowContainer;
	private Map<String, View> preyNamePreyRowMap;

	private final Integer NUMBER_OF_PRAYS = 7;

	private final String[] PREY_LABLES = { "Fajr", "Soloppgang", "Dhuhr", "Asr",
			"Maghrib", "Isha", "Jummah" };

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
		prevDay.setOnClickListener(this);
		currentDate = new DateTime();
		timeCurrentlyUsedInPreyOverView = currentDate;

        final Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
		currentDay = (TextView) root.findViewById(R.id.prey_current_day);
		return root;
	}

	private void addPreyRowsToContainerAndStoreInMap(LayoutInflater inflater) {
		preyNamePreyRowMap = new HashMap<String, View>();
		for (int i = 0; i < NUMBER_OF_PRAYS; i++) {
			View row = inflater.inflate(R.layout.prey_row, null);
			preyRowContainer.addView(row);
			preyNamePreyRowMap.put(PREY_LABLES[i], row);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		preyTimes = loadPrayTimes(new DateTime());
		setUpCurrentDay();
	//	mheaderView.setPadding(0, 0, 0, 0);
		renderPreyList();

	}

	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
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
			
			// Special handling for Jumma. Have to imporve this
			if (item.getName() == PREY_LABLES[6]) {
				renderFuture(item,preyRow,title, time,status,image);
				continue;
			}
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
			
			// Special handling for Jumma. Have to imporve this
			if (item.getName() == PREY_LABLES[6]) {
				renderFuture(item,preyRow,title, time,status,image);
				continue;
			}
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
			
			// Special handling for Jumma. Have to imporve this
			if (item.getName() == PREY_LABLES[6]) {
				renderFuture(item,preyRow,title, time,status,image);
				continue;
			}
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
			day = "L�rdag";
			break;
		case 7:
			day = "S�ndag";
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

	private List<PreyItem> loadPrayTimes(DateTime dateTime) {
		SnmsPrayTimeAdapter prayTimeAdapter = new SnmsPrayTimeAdapter(
				getActivity().getAssets());
		DateTime midnight = dateTime.minusHours(dateTime.getHourOfDay())
				.minusMinutes(dateTime.getMinuteOfHour())
				.minusSeconds(dateTime.getSecondOfMinute());
		return prayTimeAdapter.getPrayListForDate(midnight, true);

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
			
		}
		if (v.equals(prevDay)) {
			timeCurrentlyUsedInPreyOverView = timeCurrentlyUsedInPreyOverView.minusDays(1);
			setUpCurrentDay();
			preyTimes = loadPrayTimes(timeCurrentlyUsedInPreyOverView);
			renderPreyList();

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
		renderPreyList();
		
	}

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		
	}

}