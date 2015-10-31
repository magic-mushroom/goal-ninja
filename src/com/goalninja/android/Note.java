package com.goalninja.android;

import java.util.Date;

public class Note {
	
	//int id;
	int GoalID;
	String Note;
	Date date;
	
	public Note(int GoalID, Date date, String Note) {
		this.GoalID = GoalID;
		this.Note = Note;
		this.date = date;
	}
	
	public int getGoalID() {
		return GoalID;
	}
	
	public void setGoalID(int GoalID) {
		this.GoalID = GoalID;
	}
	
	public String getNote() {
		return Note;
	}
	
	public void setNote(String Note) {
		this.Note = Note;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
//	public int getid() {
//		return id;
//	}
}

