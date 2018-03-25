package com.booking.common;

public enum Days {
	SUNDAY(1), MONDAY(2), TUESDAY(3), WEDNESDAY(4), THURSDAY(5), FRIDAY(6), SATURDAY(7), WEEKENDS(8), WEEKDAYS(
			9), ALL_DAYS(10);

	private int value;

	private Days(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static Days getDay(int value) {
		for (Days day : Days.values()) {
			if (day.getValue() == value)
				return day;
		}
		return null;
	}

}
