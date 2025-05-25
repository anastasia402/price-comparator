package com.accesa.price_comparator.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PricePointDTO {
    private LocalDate date;
    private double price;
    private String store;
}

