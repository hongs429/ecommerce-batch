package com.project.ecommerce.batch.util;

import java.time.Instant;
import java.util.UUID;

public class RandomUtils {
    public static String generateId() {
        return Instant.now().toEpochMilli() + "_" + UUID.randomUUID();
    }
}
