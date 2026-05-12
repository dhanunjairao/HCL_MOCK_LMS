package com.example.HCL_MOCK_LMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.HCL_MOCK_LMS.repository")
@EntityScan(basePackages = "com.example.HCL_MOCK_LMS.entity")
public class HclMockLmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(HclMockLmsApplication.class, args);
    }
}