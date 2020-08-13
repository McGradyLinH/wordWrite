package com.test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Hello world!
 */
@SpringBootApplication
@MapperScan("com.test.dao")
@EnableCaching
@EnableAsync
public class WordWriteApp {
    public static void main(String[] args) {
        SpringApplication.run(WordWriteApp.class, args);
    }
}
