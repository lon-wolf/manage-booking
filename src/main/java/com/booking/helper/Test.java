package com.booking.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.booking.rest.DayInfo;

public class Test {

	public static void main(String[] args) throws ParseException {
		List<DayInfo> daysList = new ArrayList<DayInfo>(3);
		System.out.println(daysList.size());
		for (DayInfo s : daysList)
			System.out.println(s);
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		List<Date> list = getRelatedDates(formatter.parse("2018-03-01"), formatter.parse("2018-03-5"), 1);
		for (Date d : list) {
			System.out.println(d);
		}
	}

	private static List<Date> getRelatedDates(Date start, Date end, int day) {
		List<Date> list = new LinkedList<Date>();
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(start);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(end);
		while (endCal.after(startCal)) {
			if (startCal.get(Calendar.DAY_OF_WEEK) == day) {
				list.add(startCal.getTime());
			}
			startCal.add(Calendar.DATE, 1);
		}
		return null;
	}
}
