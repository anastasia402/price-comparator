package com.accesa.price_comparator.service;

import com.accesa.price_comparator.interfacesRepo.*;
import com.accesa.price_comparator.model.*;
import com.accesa.price_comparator.utils.DiscountItemDTO;
import com.accesa.price_comparator.utils.NewDiscountDTO;
import com.accesa.price_comparator.utils.PricePointDTO;
import com.accesa.price_comparator.utils.ProductUnitPriceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final PriceRecordRepository priceRecordRepository;
    private final DiscountRepository discountRepository;
    private final PriceAlertRepository alertRepository;
    private final StoreRepository storeRepository;

    public List<BasketItem> getBestPriceBasket(List<String> productNames, LocalDate date) {
        if (productNames == null || productNames.isEmpty()) {
            throw new IllegalArgumentException("Lista de produse nu poate fi goală.");
        }
        if (date == null) {
            throw new IllegalArgumentException("Introduceți data.");
        }

        List<BasketItem> basket = new ArrayList<>();

        for (String productName : productNames) {
            List<Product> matchingProducts = productRepository.findByProductNameContainingIgnoreCase(productName);
            BasketItem basketItem = null;

            for (Product product : matchingProducts) {
                List<PriceRecord> prices = priceRecordRepository.findByProductAndDate(product, date);

                for (PriceRecord priceRecord : prices) {
                    double originalPrice = priceRecord.getPrice();

                    List<Discount> discounts = discountRepository.findAllByProductAndStoreAndDate(product, priceRecord.getStore(), date);

                    int discountPercent = discounts.stream()
                            .mapToInt(Discount::getPercentageOfDiscount)
                            .max()
                            .orElse(0);

                    double finalPrice = originalPrice * (100 - discountPercent) / 100.0;

                    if (basketItem == null || finalPrice < basketItem.getFinalPrice()) {
                        basketItem = new BasketItem(product.getProductName(), priceRecord.getStore().getName(), originalPrice,
                                discountPercent, finalPrice, priceRecord.getCurrency());
                    }
                }
            }

            if (basketItem != null) {
                basket.add(basketItem);
            }
        }

        return basket;
    }

    public List<DiscountItemDTO> getBestDiscountsForDate(LocalDate date) {
        List<Discount> discounts = discountRepository.findDiscountsByDate(date);
        List<DiscountItemDTO> result = new ArrayList<>();

        for (Discount discount : discounts) {
            Optional<PriceRecord> priceOpt = priceRecordRepository.findByProductAndStoreAndDate(discount.getProduct(), discount.getStore(), date);

            if (priceOpt.isPresent()) {
                PriceRecord price = priceOpt.get();
                double originalPrice = price.getPrice();
                int discountPercent = discount.getPercentageOfDiscount();
                double finalPrice = originalPrice * (100 - discountPercent) / 100.0;

                result.add(new DiscountItemDTO(discount.getProduct().getProductName(), discount.getStore().getName(), originalPrice, discountPercent, finalPrice, price.getCurrency()));
                if (result.size() == 5) {
                    break;
                }
            }
        }

        return result;
    }

    public List<NewDiscountDTO> getNewDiscounts(LocalDate referenceDate) {
        LocalDate start = referenceDate.minusDays(1);
        List<Discount> newDiscounts = discountRepository.findNewDiscountsInWindow(start, referenceDate);

        return newDiscounts.stream()
                .map(d -> new NewDiscountDTO(d.getProduct().getProductName(), d.getStore().getName(), d.getPercentageOfDiscount(), d.getProduct().getBrand(),
                        d.getProduct().getProductCategory(), d.getFromDate(), d.getToDate()))
                .toList();
    }

    public List<PricePointDTO> getPriceHistory(String productId, String storeName, String category, String brand) {
        List<PriceRecord> allRecords = priceRecordRepository.findByProduct_ProductId(productId);

        return allRecords.stream()
                .filter(pr -> storeName == null || pr.getStore().getName().equalsIgnoreCase(storeName))
                .filter(pr -> category == null || pr.getProduct().getProductCategory().equalsIgnoreCase(category))
                .filter(pr -> brand == null || pr.getProduct().getBrand().equalsIgnoreCase(brand))
                .map(pr -> new PricePointDTO(pr.getDate(), pr.getPrice(), pr.getStore().getName()))
                .sorted(Comparator.comparing(PricePointDTO::getDate))
                .toList();
    }

    public List<ProductUnitPriceDTO> getRecommendations(String name, LocalDate date) {
        List<Product> products = productRepository.findByProductNameContainingIgnoreCase(name);
        List<ProductUnitPriceDTO> result = new ArrayList<>();

        for (Product product : products) {
            List<PriceRecord> prices = priceRecordRepository.findByProductAndDate(product, date);

            for (PriceRecord price : prices) {
                double valuePerUnit = price.getPrice() / product.getPackageQuantity();

                result.add(new ProductUnitPriceDTO(product.getProductName(), price.getStore().getName(), price.getPrice(),
                        product.getPackageUnit(), product.getPackageQuantity(), valuePerUnit, price.getCurrency()));
            }
        }
        result.sort(Comparator.comparingDouble(ProductUnitPriceDTO::getValuePerUnit));
        return result;
    }

    public void createAlert(String productId, String storeName, double targetPrice, String email) {
        PriceAlert alert = new PriceAlert();
        alert.setProductId(productId);
        alert.setStoreName(storeName);
        alert.setTargetPrice(targetPrice);
        alertRepository.save(alert);
    }

    public List<PriceAlert> getTriggeredAlerts(LocalDate date) {
        List<PriceAlert> allAlerts = alertRepository.findAll();
        List<PriceAlert> triggered = new ArrayList<>();

        for (PriceAlert alert : allAlerts) {
            Optional<Product> productOpt = productRepository.findById(alert.getProductId());
            Optional<Store> storeOpt = storeRepository.findById(alert.getStoreName());

            if (productOpt.isPresent() && storeOpt.isPresent()) {
                Optional<PriceRecord> priceOpt = priceRecordRepository.findByProductAndStoreAndDate(productOpt.get(), storeOpt.get(), date);

                if (priceOpt.isPresent() && priceOpt.get().getPrice() <= alert.getTargetPrice()) {
                    triggered.add(alert);
                }
            }
        }

        return triggered;
    }
}

