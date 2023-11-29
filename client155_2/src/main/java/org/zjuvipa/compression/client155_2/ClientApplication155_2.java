package org.zjuvipa.compression.client155_2;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("org.zjuvipa.compression.client155_2.mapper")
@EnableScheduling
public class ClientApplication155_2 {
    public static void main(String[] args) {
        SpringApplication.run(ClientApplication155_2.class, args);
    }
}
