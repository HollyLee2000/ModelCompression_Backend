package org.zjuvipa.distributor.task;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

@Component
public class DistributorTimedTask {

    private ServerSocket socket;

    // 在 Spring Bean 初始化完成后执行的方法上标记 @PostConstruct 注解。常用于执行一些初始化操作，
    // 例如连接数据库、加载配置文件等。被标记的方法将在依赖注入完成后、Bean 属性设置之后被自动调用。
    @PostConstruct
    public void init() {
        try {
            socket = new ServerSocket(50016);
        } catch (IOException e) {
            System.out.println("Failed to create server socket");
        }
    }

    // 定义定时任务，使用@Scheduled注解指定调度时间表达式
    @Scheduled(cron = "0/2 * * * * ?")  //两秒执行一次
    public void receive() throws IOException {
        socket.setSoTimeout(2000);
        System.out.println("查看是否有客户端的请求....");
        try {
            Socket s = socket.accept();
            System.out.println("客户端:" + s.getInetAddress().getLocalHost() + "已连接到服务器");
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String mess = br.readLine();
            if (StringUtils.hasText(mess)) {
                System.out.println("mess: " + mess);
            }
        } catch (SocketTimeoutException e) {
            System.out.println("在指定的超时时间内没有客户端连接到服务器");
        }
    }

    // @PreDestroy：在 Spring org.springframework.context.annotation.Bean 销毁前执行的方法上标记 @PreDestroy 注解。
    // 常用于执行一些清理操作，例如关闭连接、释放资源等。被标记的方法将在容器销毁 Bean 之前被自动调用。
    @PreDestroy
    public void cleanup() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Failed to close server socket");
            }
        }
    }
}
