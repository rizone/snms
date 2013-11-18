package com.example.snms.domain;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class NewsItem {

	String title;
	String text;
	DateTime createdDate;
	String ingress; 
	String imageText;
	String author; 
	String imgUrl; 
	String _id; 
	
	
	public String getIngress() {
		return ingress;
	}
	
	public void setIngress(String ingress) {
		this.ingress = ingress;
	}
	
	public String getImageText() {
		return imageText;
	}
	
	public void setImageText(String imageText) {
		this.imageText = imageText;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imageUrl) {
		this.imgUrl = imageUrl;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
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


