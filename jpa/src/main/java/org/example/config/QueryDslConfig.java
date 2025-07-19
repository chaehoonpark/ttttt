package org.example.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class QueryDslConfig {

    @Bean
    JPAQueryFactory queryFactory(final EntityManager em) {
        return new JPAQueryFactory(em);
    }
}
