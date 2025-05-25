package com.accesa.price_comparator.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class NewDiscountDTO {
    private String productName;
    private String store;
    private int discountPercent;
    private String brand;
    private String category;
    private LocalDate fromDate;
    private LocalDate toDate;
}

