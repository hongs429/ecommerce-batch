package com.project.ecommerce.batch.config;


import static org.assertj.core.api.Assertions.assertThat;

import com.project.ecommerce.batch.EcommerceBatchApplication;
import javax.sql.DataSource;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@Sql("/sql/schema.sql")  // 테스트 실행 시 스키마를 초기화
@SpringBatchTest
@SpringJUnitConfig(classes = {EcommerceBatchApplication.class})
public abstract class BaseBatchIntegration {

    @Autowired
    protected JobLauncherTestUtils jobLauncherTestUtils;

    protected JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    protected static void assertJobCompleted(JobExecution jobExecution) {
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
    }
}
