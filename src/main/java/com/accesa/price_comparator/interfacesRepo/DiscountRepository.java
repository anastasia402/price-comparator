package com.accesa.price_comparator.interfacesRepo;

import com.accesa.price_comparator.model.Discount;
import com.accesa.price_comparator.model.Product;
import com.accesa.price_comparator.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface DiscountRepository extends JpaRepository<Discount, Long> {
    @Query("SELECT d FROM Discount d WHERE d.product = :product AND d.store = :store AND :date BETWEEN d.fromDate AND d.toDate")
    List<Discount> findAllByProductAndStoreAndDate(Product product, Store store, LocalDate date);

    @Query("SELECT d FROM Discount d WHERE :targetDate BETWEEN d.fromDate AND d.toDate ORDER BY d.percentageOfDiscount DESC")
    List<Discount> findDiscountsByDate(@Param("targetDate") LocalDate targetDate);

    @Query("SELECT d FROM Discount d WHERE d.fromDate > :start AND d.fromDate <= :end")
    List<Discount> findNewDiscountsInWindow(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
