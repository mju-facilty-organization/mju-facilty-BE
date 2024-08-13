package com.example.rentalSystem.common.config;

import com.example.rentalSystem.common.support.DataInitializer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@TestConfiguration
@EnableJpaAuditing
@Import(DataInitializer.class)
public class TestAuditingConfig {
}
