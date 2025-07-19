package org.example;

import org.example.config.JpaModule;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(JpaModule.class)
public class JpaModuleAutoConfiguration {
}
