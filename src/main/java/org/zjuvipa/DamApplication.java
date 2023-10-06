package org.zjuvipa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("org.zjuvipa.mapper")
@EnableScheduling
public class DamApplication {
    public static void main(String[] args) {
        SpringApplication.run(DamApplication.class, args);
    }
}