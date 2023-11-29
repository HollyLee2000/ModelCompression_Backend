package org.zjuvipa.compression.distributor.task;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.zjuvipa.compression.client155_1.service.IClientHistoryService;
import org.zjuvipa.compression.client155_2.service.IClientHistoryService2;
import org.zjuvipa.compression.distributor.info.HistoryInfo;
import org.zjuvipa.compression.distributor.res.FindHistoryRes;
import org.zjuvipa.compression.distributor.service.IDistributorHistoryService;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;

@Component
public class DistributorTimedTask {

    private ServerSocket socket;

    @Autowired
    private IDistributorHistoryService iHistoryService;

    @Autowired
    private IClientHistoryService iHistoryService1551;

    @Autowired
    private IClientHistoryService2 iHistoryService1552;

    // 在 Spring Bean 初始化完成后执行的方法上标记 @PostConstruct 注解。常用于执行一些初始化操作，
    // 例如连接数据库、加载配置文件等。被标记的方法将在依赖注入完成后、Bean 属性设置之后被自动调用。
    @PostConstruct
    public void init() {
        try {
            socket = new ServerSocket(50016);
            socket.setSoTimeout(2000);
        } catch (IOException e) {
            System.out.println("Failed to create server socket");
        }
    }

    // 定义定时任务，使用@Scheduled注解指定调度时间表达式
    @Scheduled(cron = "0/2 * * * * ?")  //两秒执行一次
    public void receive() throws IOException {
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


    @CrossOrigin
    @ApiOperation("查找需要训练的所有历史记录，分配任务")
    @PostMapping("findTrainingTask")
    @Scheduled(cron = "0/5 * * * * ?")  //五秒执行一次
    public synchronized void findTrainingTask() {
        FindHistoryRes histories = new FindHistoryRes();
        List<HistoryInfo> result = iHistoryService.findTrainingTask();
        if(result==null){
            System.out.println("所有任务已分发");
        }else{
            histories.setHistoryInfos(result);
            System.out.println("查询到需要训练的记录： ");
            for(HistoryInfo info: histories.getHistoryInfos()){
                System.out.println(info);
                System.out.println("将此任务发送到客户端：" + info.getClient());
                String client = info.getClient();
                if(client.equals("vipa155_client1")){
                    iHistoryService1551.uploadHistory(info.getUsername(), info.getStatus(), info.getParamsChange(), info.getFlopsChange(), info.getAccChange(),
                            info.getLossChange(), info.getPrunedPath(), info.getStructureAfterPruned(), info.getLogPath(), info.getTotEpoch(), info.getCurrentEpoch(),
                            info.getScript());
                }else if(client.equals("vipa155_client2")){
                    iHistoryService1552.uploadHistory(info.getUsername(), info.getStatus(), info.getParamsChange(), info.getFlopsChange(), info.getAccChange(),
                            info.getLossChange(), info.getPrunedPath(), info.getStructureAfterPruned(), info.getLogPath(), info.getTotEpoch(), info.getCurrentEpoch(),
                            info.getScript());
                }
                System.out.println("发送完毕，需要修改任务状态......");
                iHistoryService.updateHistory(info.getHistoryId());
                System.out.println("该任务状态已修改为已分发");
            }
        }
    }

//    public boolean uploadHistory(String username, String status, String paramschange, String flopschange, String accchange, String losschange,
//                                 String prunedpath, String structureafterpruned, String logpath, String totepoch, String currentepoch,
//                                 String script);

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
