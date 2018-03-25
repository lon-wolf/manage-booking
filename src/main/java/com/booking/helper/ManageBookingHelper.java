package com.booking.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.booking.common.Days;
import com.booking.common.PropertiesContext;
import com.booking.db.models.Inventroy;
import com.booking.db.models.Price;
import com.booking.db.repository.InventoryRepository;
import com.booking.db.repository.PriceRepository;
import com.booking.rest.DayInfo;
import com.booking.rest.FindResponse;
import com.booking.rest.UpdateRequest;

@Component
public class ManageBookingHelper {

	private static final int SINGLE_PRICE = Integer
			.parseInt(PropertiesContext.properties.getProperty("single_room_default_price"));

	private static final int SINGLE_INVENTORY = Integer
			.parseInt(PropertiesContext.properties.getProperty("single_room_default_inventory"));

	private static final int DOUBLE_PRICE = Integer
			.parseInt(PropertiesContext.properties.getProperty("double_room_default_price"));

	private static final int DOUBLE_INVENTORY = Integer
			.parseInt(PropertiesContext.properties.getProperty("double_room_default_inventory"));

	@Autowired
	private PriceRepository priceRepository;

	@Autowired
	private InventoryRepository inventoryRepository;

	public void updateDetails(UpdateRequest updateRequest) {
		if (updateRequest.getPrice() != null) {
			fetchDates(updateRequest, true);
		}
		if (updateRequest.getAvailabilty() != null) {
			fetchDates(updateRequest, false);
		}
	}

	public FindResponse findDetails(Date start, Date end) {
		FindResponse response = new FindResponse();
		DayInfo[] daysList = new DayInfo[(int) daysBetween(start, end)];
		List<Price> prices = priceRepository.findByStartAndEnd(start, end);
		List<Inventroy> invevtories = inventoryRepository.findByStartAndEnd(start, end);
		return null;

	}

	private void fetchDates(UpdateRequest updateRequest, boolean price) {
		List<Date> dates = null;
		boolean allDay = false;
		boolean weekday = false;
		boolean weekend = false;
		boolean flag = false;

		List<Integer> days = updateRequest.getDays();
		Days type = null;
		for (int day : days) {
			type = Days.getDay(day);
			switch (type) {
			case ALL_DAYS:
				allDay = true;
				flag = price
						? updatePriceInDB(updateRequest.getStart(), updateRequest.getEnd(), updateRequest.getPrice(),
								updateRequest.getRoomType())
						: updateRoomsInDB(updateRequest.getStart(), updateRequest.getEnd(),
								updateRequest.getAvailabilty(), updateRequest.getRoomType());
				break;
			case WEEKDAYS:
				if (allDay)
					break;
				weekday = true;
				dates = getRelatedDates(updateRequest.getStart(), updateRequest.getEnd(), Days.MONDAY.getValue());
				for (Date date : dates) {
					flag = price
							? updatePriceInDB(date, new Date(date.getTime() + (1000 * 60 * 60 * 24 * 5)),
									updateRequest.getPrice(), updateRequest.getRoomType())
							: updateRoomsInDB(date, new Date(date.getTime() + (1000 * 60 * 60 * 24 * 5)),
									updateRequest.getAvailabilty(), updateRequest.getRoomType());
				}
				break;

			case WEEKENDS:
				if (allDay)
					break;
				weekend = true;
				dates = getRelatedDates(updateRequest.getStart(), updateRequest.getEnd(), Days.SATURDAY.getValue());
				for (Date date : dates) {
					flag = price
							? updatePriceInDB(date, new Date(date.getTime() + (1000 * 60 * 60 * 24)),
									updateRequest.getPrice(), updateRequest.getRoomType())
							: updateRoomsInDB(date, new Date(date.getTime() + (1000 * 60 * 60 * 24)),
									updateRequest.getAvailabilty(), updateRequest.getRoomType());
				}
				break;

			case MONDAY:
			case TUESDAY:
			case WEDNESDAY:
			case THURSDAY:
			case FRIDAY:
				if (allDay || weekday)
					break;
				dates = getRelatedDates(updateRequest.getStart(), updateRequest.getEnd(), type.getValue());
				for (Date date : dates) {
					flag = price ? updatePriceInDB(date, date, updateRequest.getPrice(), updateRequest.getRoomType())
							: updateRoomsInDB(date, date, updateRequest.getAvailabilty(), updateRequest.getRoomType());
				}
				break;

			case SATURDAY:
			case SUNDAY:
				if (allDay || weekend)
					break;
				dates = getRelatedDates(updateRequest.getStart(), updateRequest.getEnd(), type.getValue());
				for (Date date : dates) {
					flag = price ? updatePriceInDB(date, date, updateRequest.getPrice(), updateRequest.getRoomType())
							: updateRoomsInDB(date, date, updateRequest.getAvailabilty(), updateRequest.getRoomType());
				}
				break;
			}
		}
	}

	@Transactional
	private boolean updatePriceInDB(Date start, Date end, int updatePrice, Integer roomType) {
		List<Price> prices = priceRepository.findByStartAndEndAndType(start, end, roomType);
		Price requestPrice = new Price();
		requestPrice.setStart(start);
		requestPrice.setEnd(end);
		requestPrice.setPrice(updatePrice);
		requestPrice.setType(roomType);
		for (Price price : prices) {
			if (price.getStart().before(start)) {
				price.setEnd(new Date(start.getTime() - (1000 * 60 * 60 * 24)));
				priceRepository.save(price);
			} else if (price.getStart().compareTo(start) >= 0 && price.getEnd().compareTo(end) <= 0) {
				priceRepository.delete(price.getId());
			} else {
				price.setStart(new Date(end.getTime() + (1000 * 60 * 60 * 24)));
				priceRepository.save(price);
			}
		}
		priceRepository.save(requestPrice);
		return true;
	}

	@Transactional
	private boolean updateRoomsInDB(Date start, Date end, Integer availabilty, Integer roomType) {
		List<Inventroy> invevtories = inventoryRepository.findByStartAndEndAndType(start, end, roomType);
		Inventroy requestInventory = new Inventroy();
		requestInventory.setStart(start);
		requestInventory.setEnd(end);
		requestInventory.setType(roomType);
		requestInventory.setAvailable(availabilty);
		for (Inventroy invevtory : invevtories) {
			if (invevtory.getStart().before(start)) {
				invevtory.setEnd(new Date(start.getTime() - (1000 * 60 * 60 * 24)));
				inventoryRepository.save(invevtory);
			} else if (invevtory.getStart().compareTo(start) >= 0 && invevtory.getEnd().compareTo(end) <= 0) {
				inventoryRepository.delete(invevtory.getId());
			} else {
				invevtory.setStart(new Date(end.getTime() + (1000 * 60 * 60 * 24)));
				inventoryRepository.save(invevtory);
			}
		}
		inventoryRepository.save(requestInventory);
		return true;
	}

	private List<Date> getRelatedDates(Date start, Date end, int day) {
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
		return list;
	}

	private long daysBetween(Date one, Date two) {
		long difference = (one.getTime() - two.getTime()) / 86400000;
		return Math.abs(difference) + 1;
	}

}
