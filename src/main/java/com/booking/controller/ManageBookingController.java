package com.booking.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.booking.common.Days;
import com.booking.common.GenericException;
import com.booking.common.RoomType;
import com.booking.helper.ManageBookingHelper;
import com.booking.rest.FindResponse;
import com.booking.rest.UpdateRequest;
import com.booking.rest.UpdateResponse;

@RestController
@RequestMapping(path = "/api")
public class ManageBookingController {

	private static final Logger logger = LogManager.getLogger(ManageBookingController.class);

	@Autowired
	private ManageBookingHelper manageBookingHelper;

	private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<UpdateResponse> update(@Valid @RequestBody UpdateRequest updateRequest) {
		UpdateResponse response = new UpdateResponse();
		logger.info("Request came for update : {}", updateRequest.toString());
		try {
			validateRequest(updateRequest);
			manageBookingHelper.updateDetails(updateRequest);
			response.setMessage("SUCCESS");
		} catch (GenericException e) {
			response.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		logger.info("Sending response {}", response.toString());
		return ResponseEntity.ok(response);
	}

	@RequestMapping(value = "/find", method = RequestMethod.GET)
	public ResponseEntity<FindResponse> find(@RequestParam(value = "start_date") String start,
			@RequestParam(value = "end_date") String end) {
		FindResponse response = null;
		logger.info("Request came for find with start : {} and end : {}", start, end);
		try {
			validateFindRequest(start, end);
			response = manageBookingHelper.findDetails(formatter.parse(start).getTime(),
					formatter.parse(end).getTime());
		} catch (Exception e) {
			response = new FindResponse();
			response.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		response.setMessage("SUCCESS");
		logger.info("Sending response {}", response.toString());
		return ResponseEntity.ok(response);
	}

	private void validateFindRequest(String start, String end) throws GenericException {
		try {
			Date startDate = formatter.parse(start);
			Date endDate = formatter.parse(end);
			if (startDate.after(endDate)) {
				logger.error("Start date is greater than end date");
				throw new GenericException("Start date is greater than end date");
			}
		} catch (ParseException e) {
			logger.error("Wrong format of date");
			throw new GenericException("Wrong format of date");
		}
	}

	private void validateRequest(UpdateRequest updateRequest) throws GenericException {
		if (RoomType.getRoomType(updateRequest.getRoomType()) == null) {
			logger.error("Invalid room type");
			throw new GenericException("Invalid room type");
		}
		try {
			updateRequest.setStart(formatter.parse(updateRequest.getStartDate()).getTime());
			updateRequest.setEnd(formatter.parse(updateRequest.getEndDate()).getTime());
		} catch (ParseException e) {
			logger.error("Wrong format of date");
			throw new GenericException("Wrong format of date");
		}
		if (updateRequest.getPrice() == null && updateRequest.getAvailabilty() == null) {
			logger.error("Both price and availabilty is null");
			throw new GenericException("Both price and availabilty is null");
		}
		if ((updateRequest.getPrice() != null && updateRequest.getPrice() < 0)
				|| (updateRequest.getAvailabilty() != null && updateRequest.getAvailabilty() < 0)) {
			logger.error("Invalid value for price and availabilty");
			throw new GenericException("Invalid value for price and availabilty");
		}
		if (updateRequest.getStart() > updateRequest.getEnd()) {
			logger.error("Start date is greater than end date");
			throw new GenericException("Start date is greater than end date");
		}
		if (updateRequest.getDays() == null
				|| (updateRequest.getDays() != null && updateRequest.getDays().size() == 0)) {
			logger.info("Taking default days as all days");
			List<Integer> list = new ArrayList<Integer>();
			list.add(Days.ALL_DAYS.getValue());
			updateRequest.setDays(list);
		}
		for (int day : updateRequest.getDays()) {
			if (Days.getDay(day) == null) {
				logger.error("Invalid day type");
				throw new GenericException("Invalid day type");
			}
		}
		Collections.sort(updateRequest.getDays(), Collections.reverseOrder());
	}
}
