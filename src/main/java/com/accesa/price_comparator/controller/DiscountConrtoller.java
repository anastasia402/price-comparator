package com.accesa.price_comparator.controller;

import com.accesa.price_comparator.service.ProductService;
import com.accesa.price_comparator.utils.DiscountItemDTO;
import com.accesa.price_comparator.utils.NewDiscountDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/discounts")
public class DiscountConrtoller {
    public final ProductService discountService;

    @GetMapping("/new")
    public ResponseEntity<List<NewDiscountDTO>> getNewDiscounts(@RequestParam("date") String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        return ResponseEntity.ok(discountService.getNewDiscounts(date));
    }

    @GetMapping("/best")
    public ResponseEntity<List<DiscountItemDTO>> getBestDiscountsByDate(@RequestParam("date") String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        return ResponseEntity.ok(discountService.getBestDiscountsForDate(date));
    }
}
