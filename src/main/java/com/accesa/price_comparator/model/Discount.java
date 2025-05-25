package com.accesa.price_comparator.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Discount {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Store store;

    private LocalDate fromDate;
    private LocalDate toDate;
    private int percentageOfDiscount;

    public Discount(Product product, Store store, LocalDate fromDate, LocalDate toDate, int percentageOfDiscount) {
        this.product = product;
        this.store = store;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.percentageOfDiscount = percentageOfDiscount;
    }

    public Discount() {
    }
}

