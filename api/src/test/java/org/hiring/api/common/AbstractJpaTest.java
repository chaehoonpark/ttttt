package org.hiring.api.common;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.example.config.JpaAuditConfig;
import org.example.config.QueryDslConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({JpaAuditConfig.class, QueryDslConfig.class})
public abstract class AbstractJpaTest extends AbstractEntityManagerFactory {

    @Autowired
    protected JPAQueryFactory queryFactory;

    protected void flushAndClear() {
        em.flush();
        em.clear();
    }
}
