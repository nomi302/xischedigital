package com.xische.demo.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.xische.demo.dto.ExchangeRateResponse;


@Service
public class CurrencyConverterService {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyConverterService.class);

    @Value("${exchange.api.url}")
    private String apiUrl;

    @Value("${exchange.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public CurrencyConverterService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Cacheable(value = "exchangeRates", key = "#baseCurrency")
    public BigDecimal getExchangeRate(String baseCurrency, String targetCurrency) {
        String url = String.format("%s/v6/%s/latest/%s",
                                 apiUrl,
                                 apiKey,
                                 baseCurrency.toUpperCase());

        try {
            ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);
            
            if (response == null || !response.isSuccess()) {
                logger.error("Invalid API response: {}", response);
                throw new RuntimeException("Exchange rate service unavailable");
            }
            
            if (response.getConversionRates() == null) {
                logger.error("No conversion rates in response");
                throw new RuntimeException("Invalid exchange rate data");
            }
            
            BigDecimal rate = response.getConversionRates()
                .get(targetCurrency.toUpperCase());
            
            if (rate == null) {
                throw new IllegalArgumentException(
                    "Unsupported target currency: " + targetCurrency
                );
            }
            
            return rate;
            
        } catch (HttpClientErrorException e) {
            logger.error("API Error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to retrieve exchange rates");
        }
    }

    public BigDecimal convert(BigDecimal amount, String baseCurrency, String targetCurrency) {
        if (baseCurrency.equalsIgnoreCase(targetCurrency)) {
            return amount.setScale(2, RoundingMode.HALF_UP);
        }
        
        BigDecimal rate = getExchangeRate(baseCurrency, targetCurrency);
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}