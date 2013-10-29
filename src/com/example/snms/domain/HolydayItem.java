package com.example.snms.domain;

import java.util.Date;

public class HolydayItem {

	public String name;
	
	Date from;
	
	Date to;

	public String getName() {
		return name;
	}
	
	

	public Date getFrom() {
		return from;
	}



	public void setFrom(Date from) {
		this.from = from;
	}



	public Date getTo() {
		return to;
	}



	public void setTo(Date to) {
		this.to = to;
	}



	public void setName(String name) {
		this.name = name;
	} 
}
