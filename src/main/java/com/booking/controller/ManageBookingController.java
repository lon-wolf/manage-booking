package com.booking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.booking.dao.UpdateRequest;
import com.booking.dao.UpdateResponse;

@RestController
@RequestMapping(path = "/api")
public class ManageBookingController {

	@RequestMapping(value = "/update/", method = RequestMethod.POST)
	public ResponseEntity<UpdateResponse> update(@RequestBody UpdateRequest updateRequest) {
		UpdateResponse response = new UpdateResponse();
		return ResponseEntity.ok(response);
	}
}
