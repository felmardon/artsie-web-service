package com.felmardon.artsie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Artsie Web Service.
 * <p>
 * The {@code @SpringBootApplication} annotation scans the entire
 * {@code com.felmardon.artsie} package tree, automatically picking up
 * entities, repositories, services, controllers, and configuration
 * from all sub-modules.
 */
@SpringBootApplication
public class ArtsieApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtsieApplication.class, args);
    }
}
