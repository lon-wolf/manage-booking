package com.booking.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.booking.common.Days;
import com.booking.common.GenericException;
import com.booking.common.PropertiesContext;
import com.booking.common.RoomType;
import com.booking.controller.ManageBookingController;
import com.booking.db.models.Inventroy;
import com.booking.db.models.Price;
import com.booking.db.repository.InventoryRepository;
import com.booking.db.repository.PriceRepository;
import com.booking.rest.DayInfo;
import com.booking.rest.FindResponse;
import com.booking.rest.UpdateRequest;

@Component
public class ManageBookingHelper {

	private static final Logger logger = LogManager.getLogger(ManageBookingHelper.class);

	private static final long ONE_DAY = 1000 * 60 * 60 * 24;

	@Autowired
	private PriceRepository priceRepository;

	@Autowired
	private InventoryRepository inventoryRepository;

	public void updateDetails(UpdateRequest updateRequest) throws GenericException {
		if (updateRequest.getPrice() != null) {
			logger.info("Updating records with price");
			fetchDates(updateRequest, true);
		}
		if (updateRequest.getAvailabilty() != null) {
			logger.info("Updating records with availabilty");
			fetchDates(updateRequest, false);
		}
	}

	public FindResponse findDetails(long start, long end) throws GenericException {
		int daysBetween = (int) daysBetween(start, end);
		FindResponse response = new FindResponse();
		List<DayInfo> daysList = new ArrayList<DayInfo>(daysBetween);
		long tempStart = start;
		while (daysBetween-- > 0) {
			DayInfo info = new DayInfo(new Date(tempStart).toString());
			daysList.add(info);
			tempStart = tempStart + ONE_DAY;
		}
		constructFromPrices(start, end, daysList);
		constructFromInventory(start, end, daysList);
		response.setDaysList(daysList);
		return response;

	}

	private void constructFromInventory(long start, long end, List<DayInfo> daysList) throws GenericException {
		for (RoomType type : RoomType.values()) {
			long roomStart = start;
			List<Inventroy> invevtories = null;
			int roomCount = 0;
			try {
				invevtories = inventoryRepository.findByStartAndEndAndType(start, end, type.getValue());
				logger.info("Number of invevtories entities {}", invevtories.size());
			} catch (Exception e) {
				logger.error("Error while making query for inventory, DB Failure", e);
				throw new GenericException("Error while making query for inventory, DB Failure");
			}
			for (Inventroy inventory : invevtories) {
				logger.info("Processing inventory {}", inventory.toString());
				if (inventory.getStart() <= roomStart) {
					while (roomStart <= inventory.getEnd() && roomStart <= end) {
						if (type.getValue() == 1)
							daysList.get(roomCount).getSingleType().setRooms(inventory.getAvailable());
						else
							daysList.get(roomCount).getDoubleType().setRooms(inventory.getAvailable());
						roomCount++;
						roomStart = roomStart + ONE_DAY;
					}
				} else if (inventory.getStart() > roomStart) {
					while (roomStart != inventory.getStart() && roomStart <= end) {
						roomCount++;
						roomStart = roomStart + ONE_DAY;
					}
					if (inventory.getStart() <= roomStart) {
						while (roomStart <= inventory.getEnd() && roomStart <= end) {
							if (type.getValue() == 1)
								daysList.get(roomCount).getSingleType().setRooms(inventory.getAvailable());
							else
								daysList.get(roomCount).getDoubleType().setRooms(inventory.getAvailable());
							roomCount++;
							roomStart = roomStart + ONE_DAY;
						}
					}
				}
			}
		}
	}

	private void constructFromPrices(long start, long end, List<DayInfo> daysList) throws GenericException {
		for (RoomType type : RoomType.values()) {
			long priceStart = start;
			List<Price> prices = null;
			int priceCount = 0;
			try {
				prices = priceRepository.findByStartAndEndAndType(start, end, type.getValue());
				logger.info("Number of price entities {}", prices.size());
			} catch (Exception e) {
				logger.error("Error while making query for prices, DB Failure", e);
				throw new GenericException("Error while making query for prices, DB Failure");
			}
			for (Price price : prices) {
				logger.info("Processing price {}", price.toString());
				if (price.getStart() <= priceStart) {
					while (priceStart <= price.getEnd() && priceStart <= end) {
						if (type.getValue() == 1)
							daysList.get(priceCount).getSingleType().setPrice(price.getPrice());
						else
							daysList.get(priceCount).getDoubleType().setPrice(price.getPrice());
						priceCount++;
						priceStart = priceStart + ONE_DAY;
					}
				} else if (price.getStart() > priceStart) {
					while (priceStart != price.getStart() && priceStart <= end) {
						priceCount++;
						priceStart = priceStart + ONE_DAY;
					}
					if (price.getStart() <= priceStart) {
						while (priceStart <= price.getEnd() && priceStart <= end) {
							if (type.getValue() == 1)
								daysList.get(priceCount).getSingleType().setPrice(price.getPrice());
							else
								daysList.get(priceCount).getDoubleType().setPrice(price.getPrice());
							priceCount++;
							priceStart = priceStart + ONE_DAY;
						}
					}
				}
			}
		}
	}

	private void fetchDates(UpdateRequest updateRequest, boolean price) throws GenericException {
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
							? updatePriceInDB(date.getTime(), new Date(date.getTime() + (ONE_DAY * 5)).getTime(),
									updateRequest.getPrice(), updateRequest.getRoomType())
							: updateRoomsInDB(date.getTime(), new Date(date.getTime() + (ONE_DAY * 5)).getTime(),
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
							? updatePriceInDB(date.getTime(), new Date(date.getTime() + (ONE_DAY)).getTime(),
									updateRequest.getPrice(), updateRequest.getRoomType())
							: updateRoomsInDB(date.getTime(), new Date(date.getTime() + (ONE_DAY)).getTime(),
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
					flag = price
							? updatePriceInDB(date.getTime(), date.getTime(), updateRequest.getPrice(),
									updateRequest.getRoomType())
							: updateRoomsInDB(date.getTime(), date.getTime(), updateRequest.getAvailabilty(),
									updateRequest.getRoomType());
				}
				break;

			case SATURDAY:
			case SUNDAY:
				if (allDay || weekend)
					break;
				dates = getRelatedDates(updateRequest.getStart(), updateRequest.getEnd(), type.getValue());
				for (Date date : dates) {
					flag = price
							? updatePriceInDB(date.getTime(), date.getTime(), updateRequest.getPrice(),
									updateRequest.getRoomType())
							: updateRoomsInDB(date.getTime(), date.getTime(), updateRequest.getAvailabilty(),
									updateRequest.getRoomType());
				}
				break;
			}
		}
	}

	@Transactional
	private boolean updatePriceInDB(long start, long end, int updatePrice, Integer roomType) throws GenericException {
		List<Price> toUpdate = new LinkedList<Price>();
		List<Price> todelete = new LinkedList<Price>();
		List<Price> prices = priceRepository.findByStartAndEndAndType(start, end, roomType);
		Price requestPrice = new Price();
		requestPrice.setStart(start);
		requestPrice.setEnd(end);
		requestPrice.setPrice(updatePrice);
		requestPrice.setType(roomType);
		for (Price price : prices) {
			if (price.getStart() < start) {
				price.setEnd(start - (ONE_DAY));
				toUpdate.add(price);
			} else if (price.getStart() >= start && price.getEnd() <= end) {
				todelete.add(price);
			} else {
				price.setStart(end + (ONE_DAY));
				toUpdate.add(price);
			}
		}
		toUpdate.add(requestPrice);
		try {
			priceRepository.save(toUpdate);
			priceRepository.delete(todelete);
		} catch (Exception e) {
			logger.error("Error in updating records in SQL", e);
			throw new GenericException("Error in updating records in SQL");
		}
		return true;
	}

	@Transactional
	private boolean updateRoomsInDB(Long start, Long end, Integer availabilty, Integer roomType)
			throws GenericException {
		List<Inventroy> toUpdate = new LinkedList<Inventroy>();
		List<Inventroy> todelete = new LinkedList<Inventroy>();
		List<Inventroy> invevtories = inventoryRepository.findByStartAndEndAndType(start, end, roomType);
		Inventroy requestInventory = new Inventroy();
		requestInventory.setStart(start);
		requestInventory.setEnd(end);
		requestInventory.setType(roomType);
		requestInventory.setAvailable(availabilty);
		for (Inventroy invevtory : invevtories) {
			if (invevtory.getStart() < start) {
				invevtory.setEnd(start - (ONE_DAY));
				toUpdate.add(invevtory);
			} else if (invevtory.getStart() >= start && invevtory.getEnd() <= end) {
				todelete.add(invevtory);
			} else {
				invevtory.setStart(end + (ONE_DAY));
				toUpdate.add(invevtory);
			}
		}
		toUpdate.add(requestInventory);
		try {
			inventoryRepository.save(toUpdate);
			inventoryRepository.delete(todelete);
		} catch (Exception e) {
			logger.error("Error in updating records in SQL", e);
			throw new GenericException("Error in updating records in SQL");
		}
		return true;
	}

	private List<Date> getRelatedDates(long start, long end, int day) {
		List<Date> list = new LinkedList<Date>();
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(new Date(start));
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(new Date(end));
		while (endCal.after(startCal)) {
			if (startCal.get(Calendar.DAY_OF_WEEK) == day) {
				list.add(startCal.getTime());
			}
			startCal.add(Calendar.DATE, 1);
		}
		return list;
	}

	private long daysBetween(long one, long two) {
		long difference = (one - two) / 86400000;
		return Math.abs(difference) + 1;
	}

}
