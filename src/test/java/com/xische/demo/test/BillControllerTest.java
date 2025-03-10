package com.xische.demo.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xische.demo.config.SecurityConfig;
import com.xische.demo.controller.BillController;
import com.xische.demo.dto.BillRequest;
import com.xische.demo.model.Item;
import com.xische.demo.service.CurrencyConverterService;
import com.xische.demo.service.DiscountCalculatorService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(BillController.class)
@Import(SecurityConfig.class)
public class BillControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@SuppressWarnings("removal")
	@MockBean
	private DiscountCalculatorService discountService;

	@SuppressWarnings("removal")
	@MockBean
	private CurrencyConverterService currencyConverter;

	@InjectMocks
	private BillController billController;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${api.key}") // Read API key from properties file
	private String apiKey;

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext) {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()) 
	    .build();
	}

	@Test
	void testCalculateBill() throws Exception {
		// Mocking the services
		when(discountService.calculateDiscountedTotal(any(BillRequest.class))).thenReturn(new BigDecimal("200.00"));

		when(currencyConverter.convert(any(BigDecimal.class), anyString(), anyString()))
				.thenReturn(new BigDecimal("170.72"));

		// Creating request payload with proper types
		BillRequest request = new BillRequest();
		request.setItems(
				List.of(new Item("electronics", new BigDecimal("300")), new Item("clothing", new BigDecimal("150"))));
		request.setUserType("employee");
		request.setCustomerSince(LocalDate.parse("2019-05-15")); // Fix: Using LocalDate
		request.setOriginalCurrency("USD");
		request.setTargetCurrency("GBP");

		// Performing the test
		mockMvc.perform(MockMvcRequestBuilders.post("/api/calculate").contentType(MediaType.APPLICATION_JSON)
				.header("X-API-Key", apiKey).content(objectMapper.writeValueAsString(request)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.netAmount").value(170.72));
	}

	// Test Case 1: Employee Discount + Non-Grocery Items

	@Test
	void testEmployeeDiscountNonGrocery() throws Exception {
		when(discountService.calculateDiscountedTotal(any(BillRequest.class))).thenReturn(new BigDecimal("270.00"));

		when(currencyConverter.convert(any(BigDecimal.class), anyString(), anyString()))
				.thenReturn(new BigDecimal("230.50"));

		BillRequest request = new BillRequest();
		request.setItems(
				List.of(new Item("electronics", new BigDecimal("400")), new Item("clothing", new BigDecimal("200"))));
		request.setUserType("employee");
		request.setCustomerSince(LocalDate.parse("2021-08-10"));
		request.setOriginalCurrency("USD");
		request.setTargetCurrency("GBP");

		mockMvc.perform(MockMvcRequestBuilders.post("/api/calculate").contentType(MediaType.APPLICATION_JSON)
				.header("X-API-Key", apiKey).content(objectMapper.writeValueAsString(request)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.netAmount").value(230.50));
	}

	// Test Case 2: Affiliate + Mixed Categories

	@Test
	void testAffiliateMixedCategories() throws Exception {
		when(discountService.calculateDiscountedTotal(any(BillRequest.class))).thenReturn(new BigDecimal("315.00"));

		when(currencyConverter.convert(any(BigDecimal.class), anyString(), anyString()))
				.thenReturn(new BigDecimal("285.75"));

		BillRequest request = new BillRequest();
		request.setItems(List.of(new Item("electronics", new BigDecimal("400")),
				new Item("grocery", new BigDecimal("100")), new Item("clothing", new BigDecimal("150"))));
		request.setUserType("affiliate");
		request.setCustomerSince(LocalDate.parse("2022-01-01"));
		request.setOriginalCurrency("USD");
		request.setTargetCurrency("EUR");

		mockMvc.perform(MockMvcRequestBuilders.post("/api/calculate").contentType(MediaType.APPLICATION_JSON)
				.header("X-API-Key", apiKey).content(objectMapper.writeValueAsString(request)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.netAmount").value(285.75));
	}

	// Test Case 3: Loyal Customer (5+ Years)
	@Test
	void testLoyalCustomerDiscount() throws Exception {
		when(discountService.calculateDiscountedTotal(any(BillRequest.class))).thenReturn(new BigDecimal("250.00"));

		when(currencyConverter.convert(any(BigDecimal.class), anyString(), anyString()))
				.thenReturn(new BigDecimal("220.00"));

		BillRequest request = new BillRequest();
		request.setItems(
				List.of(new Item("electronics", new BigDecimal("300")), new Item("clothing", new BigDecimal("150"))));
		request.setUserType("customer");
		request.setCustomerSince(LocalDate.parse("2015-03-10"));
		request.setOriginalCurrency("USD");
		request.setTargetCurrency("EUR");

		mockMvc.perform(MockMvcRequestBuilders.post("/api/calculate").contentType(MediaType.APPLICATION_JSON)
				.header("X-API-Key", apiKey).content(objectMapper.writeValueAsString(request)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.netAmount").value(220.00));
	}

	// Test Case 4: All Grocery Items
	@Test
	void testAllGroceryItems() throws Exception {
		when(discountService.calculateDiscountedTotal(any(BillRequest.class))).thenReturn(new BigDecimal("500.00"));

		when(currencyConverter.convert(any(BigDecimal.class), anyString(), anyString()))
				.thenReturn(new BigDecimal("470.00"));

		BillRequest request = new BillRequest();
		request.setItems(
				List.of(new Item("grocery", new BigDecimal("300")), new Item("grocery", new BigDecimal("200"))));
		request.setUserType("customer");
		request.setCustomerSince(LocalDate.parse("2018-07-15"));
		request.setOriginalCurrency("USD");
		request.setTargetCurrency("GBP");

		mockMvc.perform(MockMvcRequestBuilders.post("/api/calculate").contentType(MediaType.APPLICATION_JSON)
				.header("X-API-Key", apiKey).content(objectMapper.writeValueAsString(request)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.netAmount").value(470.00));
	}

	// Test Case 5: Edge Case - Exactly $100

	@Test
	void testEdgeCaseExactly100() throws Exception {
		when(discountService.calculateDiscountedTotal(any(BillRequest.class))).thenReturn(new BigDecimal("95.00"));

		when(currencyConverter.convert(any(BigDecimal.class), anyString(), anyString()))
				.thenReturn(new BigDecimal("90.25"));

		BillRequest request = new BillRequest();
		request.setItems(List.of(new Item("clothing", new BigDecimal("100"))));
		request.setUserType("customer");
		request.setCustomerSince(LocalDate.parse("2020-01-10"));
		request.setOriginalCurrency("USD");
		request.setTargetCurrency("EUR");

		mockMvc.perform(MockMvcRequestBuilders.post("/api/calculate").contentType(MediaType.APPLICATION_JSON)
				.header("X-API-Key", apiKey).content(objectMapper.writeValueAsString(request)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.netAmount").value(90.25));
	}

// Test Case 6: High Amount Test
	@Test
	void testHighAmountTransaction() throws Exception {
		when(discountService.calculateDiscountedTotal(any(BillRequest.class))).thenReturn(new BigDecimal("9000.00"));

		when(currencyConverter.convert(any(BigDecimal.class), anyString(), anyString()))
				.thenReturn(new BigDecimal("8500.00"));

		BillRequest request = new BillRequest();
		request.setItems(List.of(new Item("electronics", new BigDecimal("5000")),
				new Item("furniture", new BigDecimal("4000"))));
		request.setUserType("customer");
		request.setCustomerSince(LocalDate.parse("2017-02-14"));
		request.setOriginalCurrency("USD");
		request.setTargetCurrency("EUR");

		mockMvc.perform(MockMvcRequestBuilders.post("/api/calculate").contentType(MediaType.APPLICATION_JSON)
				.header("X-API-Key", apiKey).content(objectMapper.writeValueAsString(request)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.netAmount").value(8500.00));
	}
// Test Case 7: Multiple Discount Eligibility (Seniority Test)

	@Test
	void testMultipleDiscountEligibility() throws Exception {
		when(discountService.calculateDiscountedTotal(any(BillRequest.class))).thenReturn(new BigDecimal("500.00"));

		when(currencyConverter.convert(any(BigDecimal.class), anyString(), anyString()))
				.thenReturn(new BigDecimal("460.00"));

		BillRequest request = new BillRequest();
		request.setItems(List.of(new Item("electronics", new BigDecimal("700"))));
		request.setUserType("employee");
		request.setCustomerSince(LocalDate.parse("2012-06-10"));
		request.setOriginalCurrency("USD");
		request.setTargetCurrency("GBP");

		mockMvc.perform(MockMvcRequestBuilders.post("/api/calculate").contentType(MediaType.APPLICATION_JSON)
				.header("X-API-Key", apiKey).content(objectMapper.writeValueAsString(request)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.netAmount").value(460.00));
	}
// Test Case 8: Currency Conversion Test

	@Test
	void testCurrencyConversion() throws Exception {
		when(discountService.calculateDiscountedTotal(any(BillRequest.class))).thenReturn(new BigDecimal("300.00"));

		when(currencyConverter.convert(new BigDecimal("300.00"), "USD", "EUR")).thenReturn(new BigDecimal("275.50"));

		BillRequest request = new BillRequest();
		request.setItems(List.of(new Item("electronics", new BigDecimal("400"))));
		request.setUserType("customer");
		request.setCustomerSince(LocalDate.parse("2019-07-20"));
		request.setOriginalCurrency("USD");
		request.setTargetCurrency("EUR");

		mockMvc.perform(MockMvcRequestBuilders.post("/api/calculate").contentType(MediaType.APPLICATION_JSON)
				.header("X-API-Key", apiKey).content(objectMapper.writeValueAsString(request)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.netAmount").value(275.50));
	}

}
