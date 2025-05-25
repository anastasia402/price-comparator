package com.accesa.price_comparator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Product {
    @Id
    private String productId;
    private String productName;
    private String productCategory;
    private String brand;
    private double packageQuantity;
    private String packageUnit;
}

