package com.booking.rest;

import com.booking.common.PropertiesContext;

public class DayInfo {

	private PriceAndRoom singleType;

	private PriceAndRoom doubleType;

	private String date;

	public DayInfo(String date) {
		PriceAndRoom singleT = new PriceAndRoom();
		PriceAndRoom doubleT = new PriceAndRoom();
		singleT.setPrice(Integer.parseInt(PropertiesContext.properties.getProperty("single_room_default_price")));
		singleT.setRooms(Integer.parseInt(PropertiesContext.properties.getProperty("single_room_default_inventory")));
		doubleT.setPrice(Integer.parseInt(PropertiesContext.properties.getProperty("double_room_default_price")));
		doubleT.setRooms(Integer.parseInt(PropertiesContext.properties.getProperty("double_room_default_inventory")));
		this.singleType = singleT;
		this.doubleType = doubleT;
		this.date = date;
	}

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
