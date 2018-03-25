package com.booking.rest;

public class PriceAndRoom {

	private int rooms;

	private int price;

	public int getRooms() {
		return rooms;
	}

	public void setRooms(int rooms) {
		this.rooms = rooms;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "PriceAndRoom [rooms=" + rooms + ", price=" + price + "]";
	}

}
