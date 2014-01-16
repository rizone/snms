package com.example.snms;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.android.volley.toolbox.NetworkImageView;
import com.example.snms.images.ImageCacheManager;
import com.example.snms.news.NewsItem;
import com.example.snms.news.NewsManager;
import com.example.snms.news.EventListFragment.NewsScrollListner;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class NewsDetailsFragment extends Fragment implements OnClickListener {

	NewsItem newsItem;

	// News stuff
	TextView createdDate;
	TextView text;
	TextView title;

	TextView ingress;
	NetworkImageView imageHeader;
	NetworkImageView image;
	TextView imageText;

	// Event stuff
	TextView timeFrom;
	TextView addressLine1;
	TextView addressLine2;

	public NewsDetailsFragment() {
		super();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (this.newsItem.getCat()==1
				|| this.newsItem.getCat()==2) {
			View root = inflater.inflate(R.layout.news_widget, null);
			imageHeader = (NetworkImageView) root
					.findViewById(R.id.headerImage1);
			image = (NetworkImageView) root.findViewById(R.id.newsImage);
			imageText = (TextView) root.findViewById(R.id.headerText1);
			createdDate = (TextView) root.findViewById(R.id.created);
			ingress = (TextView) root.findViewById(R.id.newsIngress);
			text = (TextView) root.findViewById(R.id.Newstext);
			// ingress = (TextView) root.findViewById(R.id.newsIngress);
			// text = (TextView) root.findViewById(R.id.newsIngress);
			return root;
		} else {
			View root = inflater.inflate(R.layout.event_widget, null);
			image = (NetworkImageView) root.findViewById(R.id.newsImage);
			imageText = (TextView) root.findViewById(R.id.headerText1);
			imageHeader = (NetworkImageView) root
					.findViewById(R.id.headerImage1);
			addressLine1 = (TextView) root.findViewById(R.id.addressLine1);
			addressLine2 = (TextView) root.findViewById(R.id.addressLine2);
			timeFrom = (TextView) root.findViewById(R.id.timeText);
			text = (TextView) root.findViewById(R.id.Newstext);
			return root;
		}

		/*
		 * image.setImageUrl(newsItem.getImgUrl(),
		 * ImageCacheManager.getInstance().getImageLoader());
		 */

	}

	@Override
	public void onResume() {
		super.onResume();
		if (this.newsItem.getCat()==1
				|| this.newsItem.getCat()==2) {
			Uri uri = Uri.parse(newsItem.getImgUrl());
			imageHeader.setImageUrl(newsItem.getImgUrl(), ImageCacheManager
					.getInstance().getImageLoader());
			image.setImageUrl(newsItem.getImgUrl(), ImageCacheManager
					.getInstance().getImageLoader());
			imageText.setText(newsItem.getTitle());
			String created = "Publisert "
					+ newsItem.getCreatedDate().toString() + " av "
					+ newsItem.getAuthor();
			createdDate.setText(created);
			ingress.setText(newsItem.getIngress());
			text.setText(newsItem.getText());
		} else {
		String gmapsUrl = "http://maps.googleapis.com/maps/api/staticmap?center="+newsItem.getLat()+","+newsItem.getLng()+"&zoom=15&size=600x500&sensor=false&markers=color:blue%7Clabel:S%7C"+newsItem.getLat()+","+newsItem.getLng();	
			imageHeader.setImageUrl(gmapsUrl, ImageCacheManager
					.getInstance().getImageLoader());
			imageHeader.setOnClickListener(this);
			DateTime from = newsItem.getFrom();
			DateTime to = newsItem.getTo();
			imageText.setText(newsItem.getTitle());
			from = from.plusHours(1);
			DateTimeFormatter formatter = DateTimeFormat.forPattern("EEEE, MMM d");
			DateTimeFormatter formatter2 = DateTimeFormat.forPattern("kk:mm");
			String formattedDate = formatter.print(from);
			String formatedTime = formatter2.print(from);
			timeFrom.setText(formattedDate + " klokken " + formatedTime);
			text.setText(newsItem.getText());
			String [] address =  newsItem.getAddress().split(",");
			if(address.length>1){
				addressLine1.setText(address[0]);
				String addressLine2 = "";
				for(int i = 1;i<address.length;i++){
					addressLine2+=address[i].trim()+"\n";
				}
				this.addressLine2.setText(addressLine2);
			}else {
				addressLine1.setText(newsItem.getAddress());
			}
			
		}
		// ingress.setText(newsItem.getIngress());
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public NewsDetailsFragment(NewsItem newsItem) {
		super();
		this.newsItem = newsItem;

	}

	@Override
	public void onClick(View v) {
		if(v.equals(imageHeader)){
			String uri = String.format(Locale.ENGLISH, "geo:%f,%f?z=%d&q=%f,%f (%s)", newsItem.getLat(), newsItem.getLng(),10,  newsItem.getLat(), newsItem.getLng(), newsItem.getTitle());
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
			getActivity().startActivity(intent);
		}
		
	}


}
