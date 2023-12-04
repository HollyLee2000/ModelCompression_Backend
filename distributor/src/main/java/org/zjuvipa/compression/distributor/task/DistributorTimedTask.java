package org.zjuvipa.compression.distributor.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.zjuvipa.compression.model.entity.RabbitMQMessage;
import org.zjuvipa.compression.model.entity.ScriptMessage;
import org.zjuvipa.compression.model.info.HistoryInfo;
import org.zjuvipa.compression.model.res.FindHistoryRes;
import org.zjuvipa.compression.distributor.service.IDistributorHistoryService;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

@Component
public class DistributorTimedTask {

//    private ServerSocket socket;
//
//    private Socket socket4client155_1;
//
//    private Socket socket4client155_2;

    @Autowired
    private IDistributorHistoryService iHistoryService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 交换机名称
    private final String exchangeName = "vipaCompression.direct";

//    @Autowired
//    private IClientHistoryService iHistoryService1551;
//
//    @Autowired
//    private IClientHistoryService2 iHistoryService1552;

    // 在 Spring Bean 初始化完成后执行的方法上标记 @PostConstruct 注解。常用于执行一些初始化操作，
    // 例如连接数据库、加载配置文件等。被标记的方法将在依赖注入完成后、Bean 属性设置之后被自动调用。
//    @PostConstruct
//    public void init() {
//        try {
//            socket = new ServerSocket(50016);
//            socket.setSoTimeout(2000);
//        } catch (IOException e) {
//            System.out.println("Distributor failed to create server socket");
//        }
//    }

//    // 定义定时任务，使用@Scheduled注解指定调度时间表达式
//    @Scheduled(cron = "0/2 * * * * ?")  //两秒执行一次

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.server"),
            exchange = @Exchange(name = "vipaCompression.direct", type = ExchangeTypes.DIRECT),
            key = {"server"}
    ))
    public void receive(Message msg) throws InterruptedException, IOException {
        byte[] body = msg.getBody();
        //如何根据body判断它可以被映射为RabbitMQMessage类还是HistoryInfo类
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        Class<?> targetType;
        // 判断body可以被映射为RabbitMQMessage类还是HistoryInfo类
        if (isJson(body, ScriptMessage.class)) {
            targetType = ScriptMessage.class;
        } else if (isJson(body, String.class)) {
            targetType = String.class;
        }else {
            throw new IllegalArgumentException("不支持的消息类型，目前只支持String类和ScriptMessage类！！");
        }

        Object myObject = objectMapper.readValue(body, typeFactory.constructType(targetType));
        if(myObject instanceof String){
            String info = (String) myObject;
            System.out.println("服务器收到客户端消息：【" + info + "】" + LocalTime.now());
            if(info.contains("has injected taskid:")){
                int startIndex = info.indexOf("has injected taskid:");
                int taskId = Integer.parseInt(info.substring(startIndex + "has injected taskid: ".length()));
                System.out.println("修改taskId为" + taskId + "的任务的状态......");
                iHistoryService.setTaskIsTraining(taskId);
                System.out.println("修改完成");
            }else if(info.contains("task ready:")){
                int taskId = Integer.parseInt(info.substring("task ready: ".length()));
                System.out.println("修改taskId为" + taskId + "的任务的状态......");
                iHistoryService.setTaskIsReady(taskId);
                System.out.println("修改完成");
            }else if(info.contains("task failed:")){
                int taskId = Integer.parseInt(info.substring("task failed: ".length()));
                System.out.println("修改taskId为" + taskId + "的任务的状态......");
                iHistoryService.setTaskIsFailed(taskId);
                System.out.println("修改完成");
            }
        }else if(myObject instanceof ScriptMessage){
            ScriptMessage info = (ScriptMessage) myObject;
            System.out.println("服务器收到客户端发来的训练同步信息：【" + info + "】" + LocalTime.now());
            boolean uploaded = iHistoryService.SyncHistory(info.getTaskId(), info.getStatus(), info.getParamsChange(), info.getFlopsChange(), info.getAccChange(),
                    info.getLossChange(), info.getPrunedPath(), info.getStructureAfterPruned(), info.getLogPath(), info.getTotEpoch(), info.getCurrentEpoch());
            if(uploaded){
                System.out.println("已同步训练信息！");
            }
        }



    }


    @CrossOrigin
    @ApiOperation("查找需要训练的所有历史记录，分配任务")
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
                String client = info.getClient();
                if(client.equals("Random")) {
                    String[] clients = {"vipa155_client1", "vipa155_client2"};
                    Random random = new Random();
                    client = clients[random.nextInt(clients.length)];
                    info.setClient(client);
                    String script = info.getScript();
                    script = script.replace("--client Random", "--client " + client);
                    info.setScript(script);
                    iHistoryService.updateClient(info.getHistoryId(), client);
                }
                System.out.println("将此任务发送到客户端：" + info.getClient());
                if(client.equals("vipa155_client1")){
                    rabbitTemplate.convertAndSend(exchangeName, "vipa155_1", info);
//                    try {
//                        socket4client155_1 = new Socket("10.214.242.155", 50026);
//                        socket4client155_1.setSoTimeout(2000);
//                        System.out.println("向客户端155_1发送任务: " + info);
//                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket4client155_1.getOutputStream());
//                        objectOutputStream.writeObject(info);  // 将info的信息传给客户端
//                        objectOutputStream.flush();
//                        System.out.println("发送完成");
//                    } catch (SocketTimeoutException e) {
//                        System.out.println("在指定时间内没有连接到客户端155_1");
//                    } catch (NullPointerException | ConnectException e){
//                        System.out.println("没有连到客户端155_1，请查看客户端155_1是否启动");
//                    } catch (SocketException e) {
//                        e.printStackTrace();
//                    } catch (UnknownHostException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    iHistoryService1551.uploadHistory(info.getUsername(), info.getStatus(), info.getParamsChange(), info.getFlopsChange(), info.getAccChange(),
//                            info.getLossChange(), info.getPrunedPath(), info.getStructureAfterPruned(), info.getLogPath(), info.getTotEpoch(), info.getCurrentEpoch(),
//                            info.getScript());
                }else if(client.equals("vipa155_client2")){
                    rabbitTemplate.convertAndSend(exchangeName, "vipa155_2", info);

//                    try {
//                        socket4client155_2 = new Socket("10.214.242.155", 50036);
//                        socket4client155_2.setSoTimeout(2000);
//                        System.out.println("向客户端155_2发送任务: " + info);
//                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket4client155_2.getOutputStream());
//                        objectOutputStream.writeObject(info);  // 将info的信息传给客户端
//                        objectOutputStream.flush();
//                        System.out.println("发送完成");
//                    } catch (SocketTimeoutException e) {
//                        System.out.println("在指定时间内没有连接到客户端155_2");
//                    } catch (NullPointerException | ConnectException e){
//                        System.out.println("没有连到客户端155_2，请查看客户端155_2是否启动");
//                    } catch (SocketException e) {
//                        e.printStackTrace();
//                    } catch (UnknownHostException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    iHistoryService1552.uploadHistory(info.getUsername(), info.getStatus(), info.getParamsChange(), info.getFlopsChange(), info.getAccChange(),
//                            info.getLossChange(), info.getPrunedPath(), info.getStructureAfterPruned(), info.getLogPath(), info.getTotEpoch(), info.getCurrentEpoch(),
//                            info.getScript());
                }
//                System.out.println("发送完毕，需要修改任务状态......");
//                iHistoryService.updateHistory(info.getHistoryId());
//                System.out.println("该任务状态已修改为已分发");
            }
        }
    }

    private boolean isJson(byte[] body, Class<?> targetType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readValue(body, targetType);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

//    public boolean uploadHistory(String username, String status, String paramschange, String flopschange, String accchange, String losschange,
//                                 String prunedpath, String structureafterpruned, String logpath, String totepoch, String currentepoch,
//                                 String script);

    // @PreDestroy：在 Spring org.springframework.context.annotation.Bean 销毁前执行的方法上标记 @PreDestroy 注解。
    // 常用于执行一些清理操作，例如关闭连接、释放资源等。被标记的方法将在容器销毁 Bean 之前被自动调用。
//    @PreDestroy
//    public void cleanup() {
//        if (socket != null && !socket.isClosed()) {
//            try {
//                socket.close();
//            } catch (IOException e) {
//                System.out.println("Failed to close server socket");
//            }
//        }
//    }
}
