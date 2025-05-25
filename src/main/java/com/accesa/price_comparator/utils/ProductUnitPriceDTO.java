package com.accesa.price_comparator.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductUnitPriceDTO {
    private String productName;
    private String store;
    private double price;
    private String unit;
    private double quantity;
    private double valuePerUnit;
    private String currency;
}
