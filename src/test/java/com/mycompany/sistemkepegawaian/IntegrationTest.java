package com.mycompany.sistemkepegawaian;

import com.mycompany.sistemkepegawaian.config.AsyncSyncConfiguration;
import com.mycompany.sistemkepegawaian.config.DatabaseTestcontainer;
import com.mycompany.sistemkepegawaian.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        SistemKepegawaianApp.class,
        JacksonConfiguration.class,
        AsyncSyncConfiguration.class,
        com.mycompany.sistemkepegawaian.config.JacksonHibernateConfiguration.class,
    }
)
@ImportTestcontainers(DatabaseTestcontainer.class)
public @interface IntegrationTest {}
