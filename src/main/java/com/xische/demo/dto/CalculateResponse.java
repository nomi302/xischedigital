package com.xische.demo.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculateResponse {
	private BigDecimal netAmount;

	public CalculateResponse(BigDecimal netAmount) {
		this.netAmount = netAmount.setScale(2, RoundingMode.HALF_UP);
	}

	public BigDecimal getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(BigDecimal netAmount) {
		this.netAmount = netAmount;
	}

	// Getter

}
