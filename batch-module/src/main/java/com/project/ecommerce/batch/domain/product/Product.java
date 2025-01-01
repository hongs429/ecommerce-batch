package com.project.ecommerce.batch.domain.product;


import java.time.LocalDate;
import java.time.LocalDateTime;
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
}
