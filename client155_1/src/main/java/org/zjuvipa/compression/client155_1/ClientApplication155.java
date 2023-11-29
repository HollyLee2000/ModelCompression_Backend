package org.zjuvipa.compression.client155_1;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("org.zjuvipa.compression.client155_1.mapper")
@EnableScheduling
public class ClientApplication155 {
    public static void main(String[] args) {
        SpringApplication.run(ClientApplication155.class, args);
    }
}
