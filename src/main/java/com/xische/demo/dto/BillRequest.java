package com.xische.demo.dto;

import java.time.LocalDate;
import java.util.List;

import com.xische.demo.model.Item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

public class BillRequest {
	 @NotEmpty(message = "At least one item is required")
	private List<Item> items;

	 @NotNull(message = "User type is required")
	private String userType;

	 @NotNull(message = "Customer since date is required")
	private LocalDate customerSince;

	@NotBlank
	@Size(min = 3, max = 3)
	private String originalCurrency;

	@NotBlank
	@Size(min = 3, max = 3)
	private String targetCurrency;

	public BillRequest() {
	}

	public BillRequest(@NotEmpty List<Item> items, @NotBlank String userType, @PastOrPresent LocalDate customerSince,
			@NotBlank @Size(min = 3, max = 3) String originalCurrency,
			@NotBlank @Size(min = 3, max = 3) String targetCurrency) {
		super();
		this.items = items;
		this.userType = userType;
		this.customerSince = customerSince;
		this.originalCurrency = originalCurrency;
		this.targetCurrency = targetCurrency;
	}


	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public LocalDate getCustomerSince() {
		return customerSince;
	}

	public void setCustomerSince(LocalDate customerSince) {
		this.customerSince = customerSince;
	}

	public String getOriginalCurrency() {
		return originalCurrency;
	}

	public void setOriginalCurrency(String originalCurrency) {
		this.originalCurrency = originalCurrency;
	}

	public String getTargetCurrency() {
		return targetCurrency;
	}

	public void setTargetCurrency(String targetCurrency) {
		this.targetCurrency = targetCurrency;
	}

	// Getters and setters

}
