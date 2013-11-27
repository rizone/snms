package com.example.snms;

import com.android.volley.toolbox.NetworkImageView;
import com.example.snms.NewsListFragment.NewsListAdapter;
import com.example.snms.NewsListFragment.NewsScrollListner;
import com.example.snms.domain.NewsItem;
import com.example.snms.domain.PreyItem;
import com.example.snms.domain.PreyItemList;
import com.example.snms.domain.image.ImageCacheManager;
import com.example.snms.utils.SnmsPrayTimeAdapter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class PrayCalenderListFragment extends Fragment {

	ListView prayGridForMonth;
	SnmsPrayTimeAdapter prayTimeAdapter; 
	PreyCalenderAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.calenderwidget, container,
				false);

		prayGridForMonth = (ListView) rootView.findViewById(R.id.preyCalender);
		adapter = new PreyCalenderAdapter(getActivity());
		return rootView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		prayTimeAdapter = new SnmsPrayTimeAdapter(getActivity().getAssets());
		prayGridForMonth.setAdapter(adapter);
		adapter.addAll(prayTimeAdapter.getPrayGridForMonthIndYear(11, 2013,false));
		adapter.notifyDataSetChanged();
	}

	public class PreyCalenderAdapter extends ArrayAdapter<PreyItemList> {

		public PreyCalenderAdapter(Context context) {
			super(context, 0);

		}
		public View getView(int position, View convertView, ViewGroup parent) {
		

				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.calender_row, parent,false);

				ViewGroup layout = (ViewGroup) convertView;
				PreyItemList h = getItem(position);
				TextView day = new TextView(getContext());
				day.setHeight(60);
				day.setMaxWidth(30);
				day.setWidth(30);
				day.setTextSize(13);
				day.setPadding(1, 1, 1, 1);
				day.setText(h.getDay().toString());
				layout.addView(day,70,30);
				for (PreyItem item : h.getPreylist()) {
					TextView prey = new TextView(getContext());
					prey.setHeight(60);
					prey.setTextSize(13);
					prey.setPadding(1, 1, 1, 1);
					prey.setText(item.getTimeOfDayAsString());
					layout.addView(prey,70,30);
				}


			return convertView;
		}
	}

}
