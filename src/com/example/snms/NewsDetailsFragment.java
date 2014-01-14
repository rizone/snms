package com.example.snms;

import org.joda.time.DateTime;

import com.android.volley.toolbox.NetworkImageView;
import com.example.snms.images.ImageCacheManager;
import com.example.snms.news.NewsItem;
import com.example.snms.news.NewsManager;
import com.example.snms.news.EventListFragment.NewsScrollListner;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


@SuppressLint("ValidFragment")
public class NewsDetailsFragment extends Fragment {
	
	NewsItem newsItem;
	
	TextView createdDate; 
	TextView text;
	TextView title;
	
	TextView ingress;
	NetworkImageView imageHeader;
	NetworkImageView image;
	TextView imageText;

	
	public NewsDetailsFragment() {
		super();	
	}
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		        Bundle savedInstanceState) {
		
		View root = inflater.inflate(R.layout.news_widget, null);
		imageHeader = (NetworkImageView) root.findViewById(R.id.headerImage1); 
		image = (NetworkImageView) root.findViewById(R.id.newsImage); 
		imageText = (TextView) root.findViewById(R.id.headerText1); 
		createdDate = (TextView) root.findViewById(R.id.created); 
		ingress = (TextView) root.findViewById(R.id.newsIngress); 
		text = (TextView) root.findViewById(R.id.Newstext); 
	//	ingress = (TextView) root.findViewById(R.id.newsIngress); 
	//	text = (TextView) root.findViewById(R.id.newsIngress); 
		return root; 
	
		
		/*
		image.setImageUrl(newsItem.getImgUrl(), ImageCacheManager.getInstance().getImageLoader());
		*/
		
	 }
	
	@Override
	public void onResume() {
		super.onResume();
		Uri uri = Uri.parse(newsItem.getImgUrl()); 
		imageHeader.setImageUrl(newsItem.getImgUrl(), ImageCacheManager.getInstance().getImageLoader());
		image.setImageUrl(newsItem.getImgUrl(), ImageCacheManager.getInstance().getImageLoader());
		imageText.setText(newsItem.getTitle());
		String created = "Publisert " + newsItem.getCreatedDate().toString() + " av " + newsItem.getAuthor();
		createdDate.setText(created);
		ingress.setText(newsItem.getIngress());
		text.setText(newsItem.getText());
		//ingress.setText(newsItem.getIngress());
	}
	
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	public NewsDetailsFragment(NewsItem newsItem) {
		super();
		this.newsItem = newsItem;
		
	}
	
	

}
