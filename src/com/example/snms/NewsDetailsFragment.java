package com.example.snms;

import com.android.volley.toolbox.NetworkImageView;
import com.example.snms.domain.NewsItem;
import com.example.snms.domain.image.ImageCacheManager;

import android.annotation.SuppressLint;
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
		View rootView = inflater.inflate(R.layout.news_widget, container, false);
		title = (TextView)rootView.findViewById(R.id.details_news_title); 
		title.setText(newsItem.getTitle());
		
	
		text = (TextView)rootView.findViewById(R.id.details_news_text); 
		text.setText(newsItem.getText());
		
		ingress = (TextView)rootView.findViewById(R.id.details_news_ingress); 
		ingress.setText(newsItem.getIngress());
		
	
		createdDate = (TextView)rootView.findViewById(R.id.details_news_createddate); 
		createdDate.setText(newsItem.getCreatedDate().toString());
		
		image = (NetworkImageView)rootView.findViewById(R.id.details_news_newsImage);
		image.setImageUrl(newsItem.getImgUrl(), ImageCacheManager.getInstance().getImageLoader());
		
		
		return rootView;
	 }
	
	
	public NewsDetailsFragment(NewsItem newsItem) {
		super();
		this.newsItem = newsItem;
		
	}
	
	

}
