package com.xische.demo.dto;

import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExchangeRateResponse {
	@JsonProperty("result")
	private String result;

	@JsonProperty("conversion_rates")
	private Map<String, BigDecimal> conversionRates;

	@JsonProperty("base_code")
	private String baseCode;

	public boolean isSuccess() {
		return "success".equalsIgnoreCase(result);
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getBaseCode() {
		return baseCode;
	}

	public void setBaseCode(String baseCode) {
		this.baseCode = baseCode;
	}

	public void setConversionRates(Map<String, BigDecimal> conversionRates) {
		this.conversionRates = conversionRates;
	}

	public Map<String, BigDecimal> getConversionRates() {
		return conversionRates;
	}
}
