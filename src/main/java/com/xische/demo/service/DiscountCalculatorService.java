package com.xische.demo.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.stereotype.Service;

import com.xische.demo.dto.BillRequest;
import com.xische.demo.model.Item;

@Service
public class DiscountCalculatorService {

    public BigDecimal calculateDiscountedTotal(BillRequest request) {
        BigDecimal totalAmount = calculateTotal(request.getItems());
        BigDecimal eligibleAmount = calculateEligibleAmount(request.getItems());
        BigDecimal percentageDiscount = calculatePercentageDiscount(request, eligibleAmount);
        BigDecimal discountedTotal = totalAmount.subtract(percentageDiscount);
        return applyPerHundredDiscount(discountedTotal);
    }

    private BigDecimal calculateTotal(List<Item> items) {
        return items.stream()
                .map(Item::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateEligibleAmount(List<Item> items) {
        return items.stream()
                .filter(item -> !"groceries".equalsIgnoreCase(item.getCategory()))
                .map(Item::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculatePercentageDiscount(BillRequest request, BigDecimal eligibleAmount) {
        return eligibleAmount.multiply(determineDiscountRate(request));
    }

    private BigDecimal determineDiscountRate(BillRequest request) {
        if ("employee".equalsIgnoreCase(request.getUserType())) return new BigDecimal("0.30");
        if ("affiliate".equalsIgnoreCase(request.getUserType())) return new BigDecimal("0.10");
        if ("customer".equalsIgnoreCase(request.getUserType()) && 
            isCustomerOverTwoYears(request.getCustomerSince())) {
            return new BigDecimal("0.05");
        }
        return BigDecimal.ZERO;
    }

    private boolean isCustomerOverTwoYears(LocalDate customerSince) {
        if (customerSince == null) return false;
        return Period.between(customerSince, LocalDate.now()).getYears() >= 2;
    }

    private BigDecimal applyPerHundredDiscount(BigDecimal amount) {
        BigDecimal hundreds = amount.divide(BigDecimal.valueOf(100), 0, RoundingMode.DOWN);
        return amount.subtract(hundreds.multiply(BigDecimal.valueOf(5)));
    }
}
