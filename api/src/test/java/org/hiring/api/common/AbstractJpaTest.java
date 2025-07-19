package org.hiring.api.common;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.example.config.JpaAuditConfig;
import org.example.config.QueryDslConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import({JpaAuditConfig.class, QueryDslConfig.class})
public abstract class AbstractJpaTest {

    @Autowired
    protected EntityManager em;

    @Autowired
    protected JPAQueryFactory queryFactory;

    protected void flushAndClear() {
        em.flush();
        em.clear();
    }
}
