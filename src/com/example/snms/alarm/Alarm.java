package com.example.snms.alarm;

public class Alarm {
	
	public String getPrey() {
		return prey;
	}

	public void setPrey(String prey) {
		this.prey = prey;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	String prey; 
	
	int offset; 
	
	public Alarm(String p, int o) {
		this.prey = p; 
		this.offset = o;
	}
}
