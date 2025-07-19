package org.hiring.api.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

@SpringBootTest
public abstract class AbstractServiceTest extends AbstractEntityManagerFactory {

    @Autowired
    protected CacheManager cacheManager;
}
