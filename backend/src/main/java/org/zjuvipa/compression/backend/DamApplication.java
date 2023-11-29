package org.zjuvipa.compression;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("org.zjuvipa.mapper")
@EnableScheduling
@ComponentScan(basePackages = {"org.zjuvipa"})
public class DamApplication {
    public static void main(String[] args) {
        SpringApplication.run(DamApplication.class, args);
    }
}