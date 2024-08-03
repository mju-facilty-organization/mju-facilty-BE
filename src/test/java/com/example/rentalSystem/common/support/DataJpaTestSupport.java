package com.example.rentalSystem.common.support;

import com.example.rentalSystem.common.config.TestAuditingConfig;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

//repository용
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestAuditingConfig.class)
public abstract class DataJpaTestSupport extends TestContainerSupport {
}