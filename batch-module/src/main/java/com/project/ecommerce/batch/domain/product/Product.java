package com.project.ecommerce.batch.domain.product;


import com.project.ecommerce.batch.dto.ProductUploadCsvRow;
import com.project.ecommerce.batch.util.RandomUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product {
    private String productId;
    private Long sellerId;

    private String category;
    private String productName;
    private LocalDate saleStartDate;
    private LocalDate saleEndDate;
    private String productStatus;
    private String brand;
    private String manufacturer;

    private int salesPrice;
    private int stockQuantity;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public static Product from(ProductUploadCsvRow row) {

        LocalDateTime now = LocalDateTime.now();
        return new Product(
                RandomUtils.generateId(),
                row.getSellerId(),
                row.getCategory(),
                row.getProductName(),
                LocalDate.parse(row.getSaleStartDate(), DateTimeFormatter.ISO_DATE),
                LocalDate.parse(row.getSaleEndDate(), DateTimeFormatter.ISO_DATE),
                row.getProductStatus(),
                row.getBrand(),
                row.getManufacturer(),
                row.getSalesPrice(),
                row.getStockQuantity(),
                now,
                now
        );
    }
}
