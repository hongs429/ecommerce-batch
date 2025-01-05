package com.project.ecommerce.batch.util;

import com.project.ecommerce.batch.domain.product.ProductStatus;
import com.project.ecommerce.batch.dto.ProductUploadCsvRow;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Random;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class ProductGenerator {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        String csvFilePath = "data/random_product.csv";
        int recordCount = 10_000_000;

        try(FileWriter fileWriter = new FileWriter(csvFilePath)) {

            CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.builder()
                    .setHeader(ReflectionUtils.getFieldNames(ProductUploadCsvRow.class).toArray(String[]::new))
                    .build()
            );

            for (int i = 0; i < recordCount; i++) {
                csvPrinter.printRecord(generateRecord());
                if (i % 100_000 == 0) {
                    System.out.println("Generated " + i + " records");
                }
            }

        }catch (IOException e) {
            System.out.println("IO Exception occurs");
        }
    }

    private static Object[] generateRecord() {
        ProductUploadCsvRow row = randomProductRow();
        return new Object[]{
                row.getSellerId(),
                row.getCategory(),
                row.getProductName(),
                row.getSaleStartDate(),
                row.getSaleEndDate(),
                row.getProductStatus(),
                row.getBrand(),
                row.getManufacturer(),
                row.getSalesPrice(),
                row.getStockQuantity()
        };
    }

    private static ProductUploadCsvRow randomProductRow() {

        String[] categories = {
                "가전", "가구", "식품", "패션", "화장품", "서적", "스포츠",
                "완구", "음악", "디지털"
        };
        String[] productNames = {
                "TV", "침대", "물", "바지", "비비크림", "책", "축구화",
                "연필", "드럼", "핸드폰"
        };
        String[] brands = {
                "삼성", "노브랜드", "Lg", "캘빈클라인", "아티스트리", "중고나라",
                "나이키", "스텐리", "101", "삼성"
        };
        String[] manufacturers = {
                "삼성전자", "노브랜드", "LG 식품", "켈빈", "암웨이", "웹",
                "나이키 회사", "스텐리 회사", "101 회사", "삼성전자"
        };

        String[] statusArray = Arrays.stream(ProductStatus.values())
                .map(Enum::toString)
                .toArray(String[]::new);

        return ProductUploadCsvRow.of(
                randomSellerId(),
                randomSelection(categories),
                randomSelection(productNames),
                randomDate(2020, 2023),
                randomDate(2024, 2026),
                randomSelection(statusArray),
                randomSelection(brands),
                randomSelection(manufacturers),
                randomPrice(),
                randomQuantity()
        );
    }

    private static int randomQuantity() {
        return RANDOM.nextInt(1, 5000);
    }

    private static int randomPrice() {
        return RANDOM.nextInt(10_000, 500_000);
    }

    private static String randomDate(int startYear, int endYear) {
        int year = RANDOM.nextInt(startYear, endYear + 1);
        int month = RANDOM.nextInt(1, 13);
        int day = RANDOM.nextInt(1, 29);
        return LocalDate.of(year, month, day).toString();
    }

    private static String randomSelection(String[] array) {
        return array[RANDOM.nextInt(array.length)];
    }


    private static Long randomSellerId() {
        return RANDOM.nextLong(1, 101);
    }
}
