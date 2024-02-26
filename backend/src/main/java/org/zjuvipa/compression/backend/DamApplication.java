package org.zjuvipa.compression.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.zjuvipa.compression.backend.properties.MinioProperties;
import org.zjuvipa.compression.backend.properties.UserProperties;

@SpringBootApplication
@MapperScan("org.zjuvipa.compression.backend.mapper")
@EnableScheduling
@ComponentScan(basePackages = {"org.zjuvipa.compression"})
@EnableConfigurationProperties(value = {UserProperties.class, MinioProperties.class})
public class DamApplication {
    public static void main(String[] args) {
        SpringApplication.run(DamApplication.class, args);
    }
}