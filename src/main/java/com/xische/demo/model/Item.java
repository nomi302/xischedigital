package com.xische.demo.model;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class Item {
	@NotBlank(message = "Category is mandatory")
	private String category;

	@NotNull(message = "Price cannot be null")
	@Positive(message = "Price must be positive")
	private BigDecimal price;

	public Item(String category, BigDecimal price) {
		this.category = category;
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
