package com.booking.rest;

import java.util.List;

public class FindResponse {

	private List<DayInfo> daysList;

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<DayInfo> getDaysList() {
		return daysList;
	}

	public void setDaysList(List<DayInfo> daysList) {
		this.daysList = daysList;
	}

	@Override
	public String toString() {
		return "FindResponse [daysList=" + daysList + ", message=" + message + "]";
	}

}
