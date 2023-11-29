package org.zjuvipa.compression.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("org.zjuvipa.compression.backend.mapper")
@EnableScheduling
@ComponentScan(basePackages = {"org.zjuvipa.compression"})
public class DamApplication {
    public static void main(String[] args) {
        SpringApplication.run(DamApplication.class, args);
    }
}