package com.booking.rest;

public class DayInfo {

	private PriceAndRoom singleType;

	private PriceAndRoom doubleType;

	private String date;

	public PriceAndRoom getSingleType() {
		return singleType;
	}

	public void setSingleType(PriceAndRoom singleType) {
		this.singleType = singleType;
	}

	public PriceAndRoom getDoubleType() {
		return doubleType;
	}

	public void setDoubleType(PriceAndRoom doubleType) {
		this.doubleType = doubleType;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "DayInfo [singleType=" + singleType + ", doubleType=" + doubleType + ", date=" + date + "]";
	}

}
