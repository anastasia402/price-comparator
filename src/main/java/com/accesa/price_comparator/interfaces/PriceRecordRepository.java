package com.accesa.price_comparator.interfaces;

import com.accesa.price_comparator.model.PriceRecord;
import com.accesa.price_comparator.model.Product;
import com.accesa.price_comparator.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PriceRecordRepository extends JpaRepository<PriceRecord, Long> {
    List<PriceRecord> findByProductAndDate(Product product, LocalDate date);

    Optional<PriceRecord> findByProductAndStoreAndDate(Product product, Store store, LocalDate date);

    List<PriceRecord> findByProduct_ProductId(String productId);
}
