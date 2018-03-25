package com.booking.rest;

import java.util.Date;
import java.util.List;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateRequest {

	private Integer price;

	@JsonIgnore
	private Date start;

	@JsonIgnore
	private Date end;

	@JsonProperty("start_date")
	@NotEmpty(message = "Start date cannot be empty or null")
	private String startDate;

	@JsonProperty("end_date")
	@NotEmpty(message = "End date cannot be empty or null")
	private String endDate;

	private Integer availabilty;

	@JsonProperty("room_type")
	@NotNull(message = "Room type cannot be empty or null")
	private Integer roomType;

	private List<Integer> days;

	public List<Integer> getDays() {
		return days;
	}

	public void setDays(List<Integer> days) {
		this.days = days;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Integer getAvailabilty() {
		return availabilty;
	}

	public void setAvailabilty(Integer availabilty) {
		this.availabilty = availabilty;
	}

	public Integer getRoomType() {
		return roomType;
	}

	public void setRoomType(Integer roomType) {
		this.roomType = roomType;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "UpdateRequest [price=" + price + ", start=" + start + ", end=" + end + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", availabilty=" + availabilty + ", roomType=" + roomType + ", days=" + days
				+ "]";
	}

}
