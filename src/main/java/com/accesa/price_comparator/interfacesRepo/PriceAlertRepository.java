package com.accesa.price_comparator.interfacesRepo;

import com.accesa.price_comparator.model.PriceAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {
    List<PriceAlert> findAll();
}

