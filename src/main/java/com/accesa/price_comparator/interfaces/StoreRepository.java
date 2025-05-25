package com.accesa.price_comparator.interfaces;

import com.accesa.price_comparator.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, String> {}
