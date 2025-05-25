package com.accesa.price_comparator.service;

import com.accesa.price_comparator.interfacesRepo.DiscountRepository;
import com.accesa.price_comparator.interfacesRepo.PriceRecordRepository;
import com.accesa.price_comparator.interfacesRepo.ProductRepository;
import com.accesa.price_comparator.interfacesRepo.StoreRepository;
import com.accesa.price_comparator.model.Discount;
import com.accesa.price_comparator.model.PriceRecord;
import com.accesa.price_comparator.model.Product;
import com.accesa.price_comparator.model.Store;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvImporterService {
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final PriceRecordRepository priceRecordRepository;
    private final DiscountRepository discountRepository;

    public void importCsv(String path) {
        try (var reader = new CSVReaderBuilder(new FileReader(path))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {

            List<String[]> rows = reader.readAll();
            String fileName = path.substring(path.lastIndexOf("/") + 1);

            boolean isDiscount = fileName.contains("discounts");

            String storeName = fileName.split("_")[0];
            Store store = storeRepository.findById(storeName).orElseGet(() -> {
                Store s = new Store();
                s.setName(storeName);
                return storeRepository.save(s);
            });

            if (!isDiscount) {
                LocalDate date = LocalDate.parse(fileName.split("_")[1].replace(".csv", ""));

                for (String[] row : rows) {
                    String productId = row[0];

                    Product product = productRepository.findById(productId).orElseGet(() -> {
                        Product p = new Product();
                        p.setProductId(productId);
                        p.setProductName(row[1]);
                        p.setProductCategory(row[2]);
                        p.setBrand(row[3]);
                        p.setPackageQuantity(Double.parseDouble(row[4]));
                        p.setPackageUnit(row[5]);
                        return productRepository.save(p);
                    });

                    PriceRecord record = new PriceRecord();
                    record.setProduct(product);
                    record.setStore(store);
                    record.setDate(date);
                    record.setPrice(Double.parseDouble(row[6]));
                    record.setCurrency(row[7]);

                    priceRecordRepository.save(record);
                }

            } else {
                for (String[] row : rows) {
                    String productId = row[0];

                    Product product = productRepository.findById(productId).orElseGet(() -> {
                        Product p = new Product();
                        p.setProductId(productId);
                        p.setProductName(row[1]);
                        p.setBrand(row[2]);
                        p.setPackageQuantity(Double.parseDouble(row[3]));
                        p.setPackageUnit(row[4]);
                        p.setProductCategory(row[5]);
                        return productRepository.save(p);
                    });

                    Discount discount = new Discount();
                    discount.setProduct(product);
                    discount.setStore(store);
                    discount.setFromDate(LocalDate.parse(row[6]));
                    discount.setToDate(LocalDate.parse(row[7]));
                    discount.setPercentageOfDiscount(Integer.parseInt(row[8]));

                    discountRepository.save(discount);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Eroare la importul din fișier: " + e.getMessage(), e);
        }
    }
}
