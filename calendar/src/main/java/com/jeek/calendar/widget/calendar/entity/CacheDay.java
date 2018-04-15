package com.jeek.calendar.widget.calendar.entity;

import org.litepal.crud.DataSupport;

/**
 * 数据库操作的缓存日期类
 * Created by Swan on 2018/1/25 19:14.
 */

public class CacheDay extends DataSupport {
	private Integer year;
	private Integer month;
	private Integer day;

	/**
	 * 1. 周期性休息日；2. 非周期性休息日；3. 周期性工作日；4. 非周期性工作日
	 */
	private Integer status;

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public CacheDay() {
	}

	public CacheDay(Integer year, Integer month, Integer day, Integer status) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.status = status;
	}
}
