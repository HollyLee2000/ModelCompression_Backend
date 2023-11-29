package org.zjuvipa.compression.distributor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("org.zjuvipa.compression.distributor.mapper")
@EnableScheduling
public class DistributorApplication {
    public static void main(String[] args) {
        SpringApplication.run(DistributorApplication.class, args);
    }
}