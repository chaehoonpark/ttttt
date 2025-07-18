package org.hiring.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
    "org.hiring.api",
    "org.example"
})
public class ComponentScanConfig {
}
