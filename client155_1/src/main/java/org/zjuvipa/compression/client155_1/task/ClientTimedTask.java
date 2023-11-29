package org.zjuvipa.compression.client155_1.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.zjuvipa.compression.client155_1.service.IClientHistoryService;
import org.zjuvipa.compression.model.info.HistoryInfo;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayDeque;
import java.util.Queue;

@Component
public class ClientTimedTask {

    private Socket socket;

    private ServerSocket socket4receive;

    private Queue<String> messageQueue = new ArrayDeque<>();

    @Autowired
    private IClientHistoryService iClientHistoryService;

    // 在 Spring Bean 初始化完成后执行的方法上标记 @PostConstruct 注解。常用于执行一些初始化操作，
    // 例如连接数据库、加载配置文件等。被标记的方法将在依赖注入完成后、Bean 属性设置之后被自动调用。
    @PostConstruct
    public void init() {
        try {
            socket4receive = new ServerSocket(50026);  // 用于接收分发器的端口
            socket4receive.setSoTimeout(2000);  // 有同步锁无所谓
        } catch (IOException e) {
            System.out.println("Client155_1 failed to create server socket for receiving message.");
        }
    }

    // 定义定时任务，使用@Scheduled注解指定调度时间表达式
    @Scheduled(cron = "0/5 * * * * ?")  //5秒执行一次
    public synchronized void send() throws IOException {
        try {
            socket = new Socket("10.214.242.155", 50016);
            socket.setSoTimeout(2000);
//            System.out.println("向服务器发送信息....");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            if(messageQueue.size()==0){
                writer.write("当前无任务\n");
                writer.flush();
                System.out.println("发送信息：当前无任务");
            }else{
                String message = messageQueue.poll();
                writer.write(message);  //执行完这个之后，服务器端的receive()函数的死锁了
                writer.flush();
                System.out.println("发送信息：" + message);
            }
        } catch (SocketTimeoutException e) {
            System.out.println("在指定时间内没有连接到服务器");
        } catch (NullPointerException | ConnectException e){
            System.out.println("没有连到服务器，请查看服务器是否启动");
        }
    }

    // 定义定时任务，使用@Scheduled注解指定调度时间表达式
    @Scheduled(cron = "0/2 * * * * ?")  //两秒执行一次
    public synchronized void receive() throws IOException {
        try {
            Socket s = socket4receive.accept();
            System.out.println("服务器:" + s.getInetAddress().getLocalHost() + "发来指令！");
            ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());
            HistoryInfo info = (HistoryInfo) objectInputStream.readObject();  // 将服务器发来的信息解码成HistoryInfo类的对象
            System.out.println("接收到信息：" + info.toString());
            boolean uploaded = iClientHistoryService.uploadHistoryIfNotExist(info.getHistoryId(), info.getUsername(), info.getStatus(), info.getParamsChange(), info.getFlopsChange(), info.getAccChange(),
            info.getLossChange(), info.getPrunedPath(), info.getStructureAfterPruned(), info.getLogPath(), info.getTotEpoch(), info.getCurrentEpoch(),
            info.getScript());
            if(uploaded){
                System.out.println("已完成任务注入，告知服务器修改任务状态....");
            }else{
                System.out.println("这个任务已经被注入过了，告知服务器修改任务状态....");
            }
            messageQueue.add("Client155_1 has injected taskid: "+info.getHistoryId()+'\n');
        } catch (SocketTimeoutException | ClassNotFoundException e) {
            System.out.println("没有收到服务器发来指令");
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
