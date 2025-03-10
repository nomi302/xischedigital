package com.xische.demo.controller;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xische.demo.dto.BillRequest;
import com.xische.demo.dto.CalculateResponse;
import com.xische.demo.service.CurrencyConverterService;
import com.xische.demo.service.DiscountCalculatorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class BillController {

    private final DiscountCalculatorService discountService;
    private final CurrencyConverterService currencyConverter;

    public BillController(DiscountCalculatorService discountService,
                         CurrencyConverterService currencyConverter) {
        this.discountService = discountService;
        this.currencyConverter = currencyConverter;
    }

    @PostMapping("/calculate")
    public ResponseEntity<CalculateResponse> calculateBill(
            @Valid @RequestBody BillRequest request) {
        
        BigDecimal discountedAmount = discountService.calculateDiscountedTotal(request);
        BigDecimal convertedAmount = currencyConverter.convert(
            discountedAmount, 
            request.getOriginalCurrency(), 
            request.getTargetCurrency()
        );
        
        return ResponseEntity.ok(new CalculateResponse(convertedAmount));
    }
}