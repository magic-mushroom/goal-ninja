package com.goalninja.android;

import java.util.Calendar;
import java.util.Date;

public class MyGoal {

	private String title, category, schedule, freq_days, freq_time;
	private int progress, id, feed_id; 
	private Date start, date;
	private Calendar alarmtime, nextcheckin;
	
	// Constructor for the class
	public MyGoal(int id, int feed_id, String title, String category,
			String schedule, Date start, Date date, String freq_days,
			String freq_time, int progress, Calendar alarmtime,
			Calendar nextcheckin) {
		this.title = title;
		this.feed_id = feed_id;
		this.category = category;
		this.id = id;
		this.schedule=schedule;
		this.freq_days=freq_days;
		this.freq_time=freq_time;
		this.start=start;
		this.date=date;
		this.progress=progress;
		this.alarmtime=alarmtime;
		this.nextcheckin=nextcheckin;
	}
	
	// getter and setter methods for all items
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
	
	
	public String getSchedule() {
		return schedule;
	}
	
	public void setSchedule(String schedule) {
		this.schedule=schedule;
	}
	
	public Date getStart() {
		return start;
	}
	
	public void setStart(Date start) {
		this.start=start;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date=date;
	}
	
	public String getFreqDays() {
		return freq_days;
	}
	
	public void setFreqDays(String freq_days) {
		this.freq_days=freq_days;
	}
	
	public String getFreqTime() {
		return freq_time;
	}
	
	public void setFreqTime(String freq_time) {
		this.freq_time=freq_time;
	}
	
	public int getProgress() {
		return progress;
	}
	
	public void setProgress(int progress) {
		this.progress=progress;
	}
	
	public Calendar getAlarmTime() {
		return alarmtime;
	}
	
	public void setAlarmTime(Calendar newalarm) {
		this.alarmtime = newalarm;
	}
	
	public int getid() {
		return id;
	}
	
	public void setid(int id) {
		this.id=id;
	}
	
	public int getFeedId() {
		return feed_id;
	}
	
	public void setFeedId(int feed_id){
		this.feed_id = feed_id;
	}
	
	public Calendar getNextCheckin() {
		return nextcheckin;
	}
	
	public void setNextCheckin(Calendar nextcheckin) {
		this.nextcheckin = nextcheckin;
	}
	
}
