package com.jeek.calendar.widget.calendar.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by Swan on 2018/1/25 19:14.
 */

public class Day extends DataSupport {
	private int year;
	private int month;
	private int day;

	/**
	 * 1. 周期性休息日；2. 非周期性休息日；3. 周期性工作日；4. 非周期性工作日
	 */
	private int status;

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Day() {
	}

	public Day(int year, int month, int day, int status) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.status = status;
	}

}
