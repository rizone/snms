package com.example.snms.domain;

import java.sql.Timestamp;
import java.util.Date;

public class PreyItem {
	
	private String name; 
	
	private Date time; 
	
	PreyItem(String name, Timestamp time) {
		this.name = name; 
		this.time = time; 
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
	
	
	

	
	

}
