package com.booking.rest;

public class UpdateResponse {

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "UpdateResponse [message=" + message + "]";
	}

}
