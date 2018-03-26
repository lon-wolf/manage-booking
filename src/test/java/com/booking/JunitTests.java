package com.booking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.booking.config.ApplicationConfig;
import com.booking.controller.ManageBookingController;
import com.booking.rest.UpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ApplicationConfig.class })
@AutoConfigureMockMvc
public class JunitTests {
	@Autowired
	private MockMvc mvc;

	@Autowired
	private ManageBookingController manageBookingController;

	@Test
	public void emptyRequest() throws Exception {
		UpdateRequest UpdateRequest = new UpdateRequest();
		mvc.perform(post("/api/update").content(asJsonString(UpdateRequest)).contentType("application/json"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void validRequest() throws Exception {
		UpdateRequest UpdateRequest = new UpdateRequest();
		UpdateRequest.setAvailabilty(2);
		UpdateRequest.setRoomType(1);
		UpdateRequest.setStartDate("2017-03-01");
		UpdateRequest.setEndDate("2017-03-10");
		UpdateRequest.setPrice(100);
		mvc.perform(post("/api/update").content(asJsonString(UpdateRequest)).contentType("application/json"))
				.andExpect(status().isOk()).andExpect(content().string(containsString("SUCCESS")));
	}

	@Test
	public void findRequest() throws Exception {
		String startDate = "2017-03-01";
		String endDate = "2017-03-10";
		String uri = "/api/find?start_date=" + startDate + "&end_date=" + endDate;
		mvc.perform(get(uri)).andExpect(status().isOk()).andExpect(content().string(containsString("SUCCESS")));
	}

	@Test
	public void InvalidStartfindRequest() throws Exception {
		String startDate = "2017_03-01";
		String endDate = "2017-03-10";
		String uri = "/api/find?start_date=" + startDate + "&end_date=" + endDate;
		mvc.perform(get(uri)).andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Wrong format of date")));
	}

	@Test
	public void greaterStartfindRequest() throws Exception {
		String startDate = "2017-03-12";
		String endDate = "2017-03-10";
		String uri = "/api/find?start_date=" + startDate + "&end_date=" + endDate;
		mvc.perform(get(uri)).andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Start date is greater than end date")));
	}

	@Test
	public void invalidDaysRequest() throws Exception {
		UpdateRequest UpdateRequest = new UpdateRequest();
		UpdateRequest.setAvailabilty(2);
		UpdateRequest.setRoomType(1);
		UpdateRequest.setStartDate("2017-03-01");
		UpdateRequest.setEndDate("2017-03-10");
		UpdateRequest.setPrice(100);
		List<Integer> list = new LinkedList<Integer>();
		list.add(99);
		UpdateRequest.setDays(list);
		mvc.perform(post("/api/update").content(asJsonString(UpdateRequest)).contentType("application/json"))
				.andExpect(status().isBadRequest()).andExpect(content().string(containsString("Invalid day type")));
	}

	@Test
	public void negativeAvailRequest() throws Exception {
		UpdateRequest UpdateRequest = new UpdateRequest();
		UpdateRequest.setAvailabilty(-2);
		UpdateRequest.setRoomType(1);
		UpdateRequest.setStartDate("2017-03-01");
		UpdateRequest.setEndDate("2017-03-10");
		UpdateRequest.setPrice(100);
		mvc.perform(post("/api/update").content(asJsonString(UpdateRequest)).contentType("application/json"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Invalid value for price and availabilty")));
	}

	@Test
	public void negativePriceRequest() throws Exception {
		UpdateRequest UpdateRequest = new UpdateRequest();
		UpdateRequest.setAvailabilty(2);
		UpdateRequest.setRoomType(1);
		UpdateRequest.setStartDate("2017-03-01");
		UpdateRequest.setEndDate("2017-03-10");
		UpdateRequest.setPrice(-100);
		mvc.perform(post("/api/update").content(asJsonString(UpdateRequest)).contentType("application/json"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Invalid value for price and availabilty")));
	}

	@Test
	public void priceAndAvailIsNull() throws Exception {
		UpdateRequest UpdateRequest = new UpdateRequest();
		UpdateRequest.setRoomType(1);
		UpdateRequest.setStartDate("2017-03-01");
		UpdateRequest.setEndDate("2017-03-10");
		mvc.perform(post("/api/update").content(asJsonString(UpdateRequest)).contentType("application/json"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Both price and availabilty is null")));
	}

	@Test
	public void startgreaterRequest() throws Exception {
		UpdateRequest UpdateRequest = new UpdateRequest();
		UpdateRequest.setAvailabilty(2);
		UpdateRequest.setRoomType(1);
		UpdateRequest.setStartDate("2017-03-12");
		UpdateRequest.setEndDate("2017-03-10");
		UpdateRequest.setPrice(100);
		mvc.perform(post("/api/update").content(asJsonString(UpdateRequest)).contentType("application/json"))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(containsString("Start date is greater than end date")));
	}

	@Test
	public void notTypeRequest() throws Exception {
		UpdateRequest UpdateRequest = new UpdateRequest();
		UpdateRequest.setAvailabilty(2);
		UpdateRequest.setStartDate("2017-03-01");
		UpdateRequest.setEndDate("2017-03-10");
		UpdateRequest.setPrice(100);
		mvc.perform(post("/api/update").content(asJsonString(UpdateRequest)).contentType("application/json"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void invalidTypeRequest() throws Exception {
		UpdateRequest UpdateRequest = new UpdateRequest();
		UpdateRequest.setAvailabilty(2);
		UpdateRequest.setStartDate("2017-03-01");
		UpdateRequest.setEndDate("2017-03-10");
		UpdateRequest.setRoomType(9);
		UpdateRequest.setPrice(100);
		mvc.perform(post("/api/update").content(asJsonString(UpdateRequest)).contentType("application/json"))
				.andExpect(status().isBadRequest()).andExpect(content().string(containsString("Invalid room type")));
	}

	@Test
	public void formatstartRequest() throws Exception {
		UpdateRequest UpdateRequest = new UpdateRequest();
		UpdateRequest.setAvailabilty(2);
		UpdateRequest.setStartDate("2017_03-12");
		UpdateRequest.setEndDate("2017-03-10");
		UpdateRequest.setPrice(100);
		UpdateRequest.setRoomType(1);
		mvc.perform(post("/api/update").content(asJsonString(UpdateRequest)).contentType("application/json"))
				.andExpect(status().isBadRequest()).andExpect(content().string(containsString("Wrong format of date")));
	}

	@Test
	public void contexLoads() throws Exception {
		assertThat(manageBookingController).isNotNull();
	}

	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
