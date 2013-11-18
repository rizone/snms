package com.example.snms.domain;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;

import org.joda.time.DateTime;

public class PreyItem implements Comparable<PreyItem> {
	
	private String name; 
	
	private DateTime time; 
	
	public PreyItem(String name, DateTime time) {
		this.name = name; 
		this.time = time; 
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DateTime getTime() {
		return time;
	}

	public void setTime(DateTime time) {
		this.time = time;
	}

	@Override
	public int compareTo( PreyItem arg1) {
		return this.getTime().compareTo(arg1.getTime());
	}
	

}
