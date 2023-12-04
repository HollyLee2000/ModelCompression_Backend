package org.zjuvipa.compression.distributor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("org.zjuvipa.compression.distributor.mapper")
@EnableScheduling
public class DistributorApplication {
    //通过在方法上使用 @Bean 注解，告诉Spring容器要将 messageConverter 方法返回的对象纳入Spring的容器管理中，
    // 其他组件可以通过 @Autowired 或其他方式来引用和使用这个 MessageConverter Bean

    @Bean
    public MessageConverter messageConverter(){
        // 1.定义消息转换器
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        // 2.配置自动创建消息id，用于识别不同消息，也可以在业务中基于ID判断是否是重复消息
        jackson2JsonMessageConverter.setCreateMessageIds(true);
        return jackson2JsonMessageConverter;
    }
    public static void main(String[] args) {
        SpringApplication.run(DistributorApplication.class, args);
    }
}