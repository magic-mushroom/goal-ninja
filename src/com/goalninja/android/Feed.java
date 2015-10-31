package com.goalninja.android;

public class Feed {
	
	private String title, category, schedule;
//	private int level, count; 
	private int id;
	int isAdded;
	
	// Constructor for the class
	public Feed(String title, String category, String schedule, int id, int isAdded) {
		this.title = title;
		this.category = category;
//		this.level = level;
//		this.count = count;
		this.schedule = schedule;
		this.id = id;
		this.isAdded = isAdded;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category=category;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title=title;
	}
	
//	public int getLevel() {
//		return level;
//	}
//	
//	public void setLevel(int level) {
//		this.level=level;
//	}
//	
//	public int getCount() {
//		return count;
//	}
//	
//	public void setCount(int count) {
//		this.count=count;
//	}
	
	public String getSchedule() {
		return schedule;
	}
	
	public void setSchedule(String schedule) {
		this.schedule=schedule;
	}
	
	public int getAdded() {
		return isAdded;
	}
	
	public void setAdded(int isAdded) {
		this.isAdded=isAdded;
	}
	
	public int getId() {
		return id;
	}
	
}
