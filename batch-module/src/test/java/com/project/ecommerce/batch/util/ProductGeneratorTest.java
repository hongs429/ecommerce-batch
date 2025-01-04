package com.project.ecommerce.batch.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductGeneratorTest {

    private static class TestClass {
        private String stringField;
        private int intField;
        private static String staticStringField;
    }

    @DisplayName("필드 네임을 뽑아올 수 있다.")
    @Test
    void getFieldNames() {
        List<String> fieldNames = ReflectionUtils.getFieldNames(TestClass.class);

        assertThat(fieldNames).hasSize(2)
                .containsExactly("stringField", "intField")
                .doesNotContain("staticStringField");

    }

}