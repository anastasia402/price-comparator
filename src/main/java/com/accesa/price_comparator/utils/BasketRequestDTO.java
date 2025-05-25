package com.accesa.price_comparator.utils;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BasketRequestDTO {
    private List<String> productNames;
    private LocalDate date;
}
