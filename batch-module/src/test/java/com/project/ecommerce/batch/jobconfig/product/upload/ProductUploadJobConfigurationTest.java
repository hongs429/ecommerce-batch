package com.project.ecommerce.batch.jobconfig.product.upload;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.project.ecommerce.batch.config.BaseBatchIntegration;
import com.project.ecommerce.batch.service.product.ProductService;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;



@TestPropertySource(properties = {"spring.batch.job.name=productUploadJob"})
class ProductUploadJobConfigurationTest extends BaseBatchIntegration {

    @Autowired
    private ProductService productService;


    @Autowired
    private Job productUploadJob; //ProductUploadJobConfiguration에서 생성한 빈을 주입


    @Value("classpath:/data/products_for_upload.csv")
    private Resource input;

    @Test
    void testJob() throws Exception {
        JobParameters jobParameters = getJobParameters();
        jobLauncherTestUtils.setJob(productUploadJob);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        Long count = productService.countProducts();

        assertAll(
                () -> assertJobCompleted(jobExecution),
                () -> assertThat(count).isEqualTo(6L)
        );


    }


    private JobParameters getJobParameters() throws IOException {
        return new JobParametersBuilder()
                .addJobParameter(
                        "inputFilePath",
                        new JobParameter<>(
                                input.getFile().getPath(),
                                String.class,
                                false))
                .toJobParameters();
    }
}