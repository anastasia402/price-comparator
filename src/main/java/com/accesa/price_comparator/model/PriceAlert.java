package com.accesa.price_comparator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PriceAlert {

    @Id
    @GeneratedValue
    private Long id;
    private String productId;
    private String storeName;
    private double targetPrice;
}

