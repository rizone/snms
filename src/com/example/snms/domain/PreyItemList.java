package com.example.snms.domain;

import java.util.List;

public class PreyItemList {
	
	List <PreyItem> preylist; 
	
	PreyItemList(List <PreyItem> preylist) {
		this.preylist = preylist; 
	}

	public List<PreyItem> getPreylist() {
		return preylist;
	}

	public void setPreylist(List<PreyItem> preylist) {
		this.preylist = preylist;
	}
	
	
	
}
