package com.accesa.price_comparator.controller;

import com.accesa.price_comparator.model.BasketItem;
import com.accesa.price_comparator.model.Discount;
import com.accesa.price_comparator.model.PriceAlert;
import com.accesa.price_comparator.model.Product;
import com.accesa.price_comparator.service.CsvImporterService;
import com.accesa.price_comparator.service.ProductService;
import com.accesa.price_comparator.utils.BasketRequestDTO;
import com.accesa.price_comparator.utils.DiscountItemDTO;
import com.accesa.price_comparator.utils.PricePointDTO;
import com.accesa.price_comparator.utils.ProductUnitPriceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final CsvImporterService csvImporterService;

    @PostMapping("/import-csv")
    public ResponseEntity<String> importCsv(@RequestParam String file) {
        csvImporterService.importCsv("src/main/resources/csv/" + file);
        return ResponseEntity.ok("Import complet din: " + file);
    }

    @PostMapping("/optimized-by-name")
    public ResponseEntity<List<BasketItem>> getOptimizedBasketByName(@RequestBody BasketRequestDTO request) {
        List<BasketItem> optimized = productService.getBestPriceBasket(request.getProductNames(), request.getDate());
        return ResponseEntity.ok(optimized);
    }

    @GetMapping("/history")
    public ResponseEntity<List<PricePointDTO>> getPriceHistory(
            @RequestParam String productId,
            @RequestParam(required = false) String store,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand
    ) {
        List<PricePointDTO> history = productService.getPriceHistory(productId, store, category, brand);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/best-buy")
    public ResponseEntity<List<ProductUnitPriceDTO>> getRecommendations(
            @RequestParam String name,
            @RequestParam String date
    ) {
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(productService.getRecommendations(name, localDate));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createAlert(
            @RequestParam String productId,
            @RequestParam String store,
            @RequestParam double targetPrice,
            @RequestParam(required = false) String email
    ) {
        productService.createAlert(productId, store, targetPrice, email);
        return ResponseEntity.ok("Alertă salvată cu succes.");
    }

    @GetMapping("/check")
    public ResponseEntity<List<PriceAlert>> checkAlerts(@RequestParam String date) {
        LocalDate d = LocalDate.parse(date);
        return ResponseEntity.ok(productService.getTriggeredAlerts(d));
    }
}
