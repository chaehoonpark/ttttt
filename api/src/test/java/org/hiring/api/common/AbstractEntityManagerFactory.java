package org.hiring.api.common;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractEntityManagerFactory extends AbstractTest {

    @Autowired
    protected EntityManager em;
}
