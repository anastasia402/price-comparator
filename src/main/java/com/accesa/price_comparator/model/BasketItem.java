package com.accesa.price_comparator.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasketItem {
    private String productName;
    private String store;
    private double originalPrice;
    private int discountPercent;
    private double finalPrice;
    private String currency;

    public BasketItem() {}
}
