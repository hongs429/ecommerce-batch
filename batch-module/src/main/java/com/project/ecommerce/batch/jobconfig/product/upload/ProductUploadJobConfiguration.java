package com.project.ecommerce.batch.jobconfig.product.upload;


import com.project.ecommerce.batch.domain.product.Product;
import com.project.ecommerce.batch.dto.ProductUploadCsvRow;
import com.project.ecommerce.batch.util.ReflectionUtils;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ProductUploadJobConfiguration {

    @Bean
    public Job productUploadJob(JobRepository jobRepository,
                                Step productUploadStep,
                                JobExecutionListener listener) {
        return new JobBuilder("productUploadJob", jobRepository)
                .listener(listener)
                .start(productUploadStep)
                .build();
    }


    /**
     * Step은 리더 -> 프로세서 -> 라이터 의 단계를 따른다
     *
     * @param jobRepository
     * @return
     */
    @Bean
    public Step productUploadStep(JobRepository jobRepository,
                                  PlatformTransactionManager transactionManager,
                                  StepExecutionListener stepExecutionListener,
                                  ItemReader<ProductUploadCsvRow> productReader,
                                  ItemProcessor<ProductUploadCsvRow, Product> productProcessor,
                                  ItemWriter<Product> productWriter,
                                  TaskExecutor batchTaskExecutor
    ) {
        return new StepBuilder("productUploadStep", jobRepository)
                .<ProductUploadCsvRow, Product>chunk(1000, transactionManager)
                .reader(productReader)
                .processor(productProcessor)
                .writer(productWriter)
                .allowStartIfComplete(true)     // 이전 실행 결과가 COMPLETED 상태라도 해당 Step을 다시 실행. 개발단계에서만 적용
                .listener(stepExecutionListener)
                .taskExecutor(batchTaskExecutor)
                .build();
    }


    @Bean
    @StepScope
    // - Spring Batch에서 기본적으로 Reader, Processor, Writer와 같은 구성 요소는 애플리케이션이 시작될 때 빈으로 초기화
    // - Step 실행 시점에만 사용할 데이터를 빈 생성 시점에 주입해야 하는 경우, @StepScope가 필요
    // - @StepScope를 적용하면 Step 실행 시점에 빈이 초기화
    // - @StepScope는 JobParameter를 사용할 수 있도록 지원
    // - JobParameter는 Step 실행 시 동적으로 제공되는 값
    public FlatFileItemReader<ProductUploadCsvRow> productReader(
            @Value("#{jobParameters['inputFilePath']}") String inputFilePath
    ) {
        return new FlatFileItemReaderBuilder<ProductUploadCsvRow>()
                .name("productReader")
                .resource(new FileSystemResource(inputFilePath))
                .delimited()
                .names(ReflectionUtils.getFieldNames(ProductUploadCsvRow.class).toArray(String[]::new))
                .targetType(ProductUploadCsvRow.class)
                .linesToSkip(1)
                .build();
    }


    @Bean
    public ItemProcessor<ProductUploadCsvRow, Product> productProcessor() {
        return Product::from;
    }


    @Bean
    public JdbcBatchItemWriter<Product> productWriter(DataSource dataSource) {
        String sql = """
                    INSERT INTO products (
                       product_id,
                        seller_id,
                        category,
                        product_name,
                        sale_start_date,
                        sale_end_date,
                        product_status,
                        brand,
                        manufacturer,
                        sales_price,
                        stock_quantity,
                        create_at,
                        update_at
                    ) VALUES (
                        :productId,
                        :sellerId,
                        :category,
                        :productName,
                        :saleStartDate,
                        :saleEndDate,
                        :productStatus,
                        :brand,
                        :manufacturer,
                        :salesPrice,
                        :stockQuantity,
                        :createAt,
                        :updateAt
                    )
                """;
        return new JdbcBatchItemWriterBuilder<Product>()
                .dataSource(dataSource)
                .sql(sql)
                .beanMapped() // 객체의 필드와 데이터베이스 테이블의 열(column)을 자동으로 매핑해주는 설정
                .build();
    }
}
