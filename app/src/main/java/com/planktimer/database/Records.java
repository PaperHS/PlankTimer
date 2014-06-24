package com.planktimer.database;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * database for records
 * 
 * @author Peggy
 * @email  potm@163.com
 *
 * @date   2014年6月17日 上午9:32:10
 */

@DatabaseTable(tableName = "records")
public class Records implements Serializable{
	
	private static final long serialVersionUID = 2160799120397135836L;
	
	public static final String ID = "id";
	public static final String USERNAME = "username";
	//the time when you add
	public static final String RECORDTIME = "recordtime";
	public static final String RECORDDATE = "recorddate";
	//the total time of you sports
	public static final String TOTALTIME = "totaltime";
	//the plank total time at this time
	public static final String TOTALPLANKTIME = "totalplanktime";
	
	@DatabaseField(generatedId=true,useGetSet=true,columnName=ID)
	private int id;
	@DatabaseField(useGetSet=true,columnName=USERNAME,canBeNull=true)
	private String username;
	@DatabaseField(useGetSet=true,columnName=RECORDTIME)
	private String recordTime;
	@DatabaseField(useGetSet=true,columnName=RECORDDATE)
	private String recordDate;
	@DatabaseField(useGetSet=true,columnName=TOTALTIME)
	private int totaltime;
	@DatabaseField(useGetSet=true,columnName=TOTALPLANKTIME)
	private int totalplanktime;
	
	public int getTotaltime() {
		return totaltime;
	}

	public void setTotaltime(int totaltime) {
		this.totaltime = totaltime;
	}

	public int getTotalplanktime() {
		return totalplanktime;
	}

	public void setTotalplanktime(int totalplanktime) {
		this.totalplanktime = totalplanktime;
	}

	public Records() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}

	public String  getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
}
