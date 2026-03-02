package com.lms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Cleaning Talent LMS 主应用类
 */
@SpringBootApplication
@MapperScan("com.lms.mapper")
public class LmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LmsApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("LMS Backend Started Successfully!");
        System.out.println("API Base URL: http://localhost:8080/api/v1");
        System.out.println("========================================\n");
    }
}
