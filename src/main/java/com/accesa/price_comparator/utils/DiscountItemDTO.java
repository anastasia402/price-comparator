package com.accesa.price_comparator.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiscountItemDTO {
    private String productName;
    private String store;
    private double originalPrice;
    private int discountPercent;
    private double finalPrice;
    private String currency;
}
