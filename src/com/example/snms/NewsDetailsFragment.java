package com.example.snms;

import com.android.volley.toolbox.NetworkImageView;
import com.example.snms.news.NewsItem;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
	NetworkImageView image;

	
	public NewsDetailsFragment() {
		super();	
	}
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		        Bundle savedInstanceState) {
		return inflater.inflate(R.layout.news_widget, null);
	
		
		/*
		image.setImageUrl(newsItem.getImgUrl(), ImageCacheManager.getInstance().getImageLoader());
		*/
		
	 }
	
	
	public NewsDetailsFragment(NewsItem newsItem) {
		super();
		this.newsItem = newsItem;
		
	}
	
	

}
