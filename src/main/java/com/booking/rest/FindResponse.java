package com.booking.rest;

import java.util.List;

public class FindResponse {

	private List<DayInfo> daysList;

	public List<DayInfo> getDaysList() {
		return daysList;
	}

	public void setDaysList(List<DayInfo> daysList) {
		this.daysList = daysList;
	}

	@Override
	public String toString() {
		return "FindResponse [daysList=" + daysList + "]";
	}

}
