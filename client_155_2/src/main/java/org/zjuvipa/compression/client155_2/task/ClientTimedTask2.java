package org.zjuvipa.compression.client155_2.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;

@Component
public class ClientTimedTask2 {

    private Socket socket;

    // 在 Spring Bean 初始化完成后执行的方法上标记 @PostConstruct 注解。常用于执行一些初始化操作，
    // 例如连接数据库、加载配置文件等。被标记的方法将在依赖注入完成后、Bean 属性设置之后被自动调用。
//    @PostConstruct
//    public void init() {
//        try {
//            socket = new Socket("10.214.242.155", 50016);
//            socket.setSoTimeout(2000);
//        } catch (IOException e) {
//            System.out.println("Failed to create server socket");
//        }
//    }

    // 定义定时任务，使用@Scheduled注解指定调度时间表达式
    @Scheduled(cron = "0/5 * * * * ?")  //两秒执行一次
    public void send() throws IOException {
        try {
            socket = new Socket("10.214.242.155", 50016);
            socket.setSoTimeout(2000);
            System.out.println("向服务器发送信息....");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write("Hello, TCPDistributor! I'm client 155_2! \n");
            writer.flush();
            System.out.println("发送信息：" + "Hello, TCPDistributor! I'm client 155_2! ");
        } catch (SocketTimeoutException e) {
            System.out.println("在指定时间内没有连接到服务器");
        } catch (NullPointerException | ConnectException e){
            System.out.println("没有连到服务器，请查看服务器是否启动");
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
