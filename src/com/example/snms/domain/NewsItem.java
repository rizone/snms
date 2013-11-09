package com.example.snms.domain;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class NewsItem {

	String title;
	String text;
	DateTime createdDate;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public DateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}
	
	
}


