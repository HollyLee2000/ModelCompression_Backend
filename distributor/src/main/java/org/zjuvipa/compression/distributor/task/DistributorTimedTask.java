package org.zjuvipa.compression.distributor.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.annotations.ApiOperation;
//import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;
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
import org.zjuvipa.compression.model.entity.History;
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

    public static String mySubstring(String str){
        int colonIndex = str.indexOf(":");
        if (colonIndex != -1) {
            String extractedString = str.substring(colonIndex + 2);
            return extractedString;
        }else{
            return "";
        }
    }

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
            System.out.println("服务器收到任务同步信息：【" + info + "】" + LocalTime.now());
            if(info.getAction().equals("uploaded")){
                System.out.println("模型上传测试已结束，更改json文件.....");
                //根据id拿出来  复原指令（主要看怎么起名字），try这一段，有异常就设为Failed
                History history = iHistoryService.findHistoryById(info.getTaskId());
                try{
                    String model = history.getModelName().split("-")[0];
                    String dataset = history.getDataset();
                    String username = history.getUsername();
                    String ckpt = history.getCheckpointPath();
                    String usrModelName = history.getUsrModelName();

                    ObjectMapper mapper = new ObjectMapper();
                    mapper.enable(SerializationFeature.INDENT_OUTPUT); // 启用缩进输出
                    ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter(); // 使用默认的缩进格式
                    File jsonFile = new File("/nfs/lhl/ModelCompression/modelzoo.json");
                    ObjectNode rootNode = (ObjectNode) mapper.readTree(jsonFile);
                    // 2. 在 JSON 结构中查找满足条件的节点
                    boolean found = false;
                    ArrayNode childrenArray = rootNode.withArray("children");
                    for (int i = 0; i < childrenArray.size(); i++) {
                        ObjectNode datasetNode = (ObjectNode) childrenArray.get(i);
                        if (datasetNode.get("name").asText().equals(dataset)) {
                            ArrayNode modelsArray = datasetNode.withArray("children");
                            for (int j = 0; j < modelsArray.size(); j++) {
                                ObjectNode modelNode = (ObjectNode) modelsArray.get(j);
                                if (modelNode.get("name").asText().equals(model)) {
                                    // 3. 添加一个新节点
                                    ObjectNode taskNode = (ObjectNode) modelsArray.get(j);
                                    ArrayNode taskArray = taskNode.withArray("children");
                                    System.out.println("taskArray: "+ taskArray);
                                    ObjectNode newNode = mapper.createObjectNode();
                                    newNode.put("name", username + ":" + usrModelName);
                                    newNode.put("type", "usr");
                                    newNode.put("model_name", dataset + "_" + model + "_" + username + ":" + usrModelName);
                                    newNode.put("status", "done");
                                    newNode.put("path", ckpt);
                                    newNode.put("acc", mySubstring(history.getAccChange()));
                                    newNode.put("params", mySubstring(history.getParamsChange()));
                                    newNode.put("flops", mySubstring(history.getFlopsChange()));
                                    newNode.put("size", 1616);
                                    taskArray.add(newNode);
                                    found = true;
                                    break;
                                }
                            }
                            if (found) {
                                break;
                            }
                        }
                    }
                    // 4. 将修改后的 JSON 结构重新写入文件
                    writer.writeValue(jsonFile, rootNode);
                }catch(Exception e){
                    System.out.println("模型上传并校验成功，但是修改json文件失败！！");
                    iHistoryService.setTaskIsFailed(history.getHistoryId());
                }

            }
            boolean uploaded = iHistoryService.SyncHistory(info.getTaskId(), info.getStatus(), info.getParamsChange(), info.getFlopsChange(), info.getAccChange(),
                    info.getLossChange(), info.getPrunedPath(), info.getStructureAfterPruned(), info.getLogPath(), info.getTotEpoch(), info.getCurrentEpoch());
            if(uploaded){
                System.out.println("已同步任务信息！");
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
                }else if(client.equals("vipa155_client2")){
                    rabbitTemplate.convertAndSend(exchangeName, "vipa155_2", info);
                }else if(client.equals("NULL")) {  //只剪枝或上传原始模型的任务，服务器自己完成
                    String taskType = info.getTaskType();
                    String script = info.getScript();
                    if(taskType.equals("Directly Pruned")){ //直接剪枝的任务
                        System.out.println("查询到直接剪枝的任务：" + script);
                    }else if(taskType.equals("Upload Raw Model")){
                        System.out.println("查询到上传模型的任务：" + script);
                        script += " --isUpload";
                    }
                    script += " --task-id " + info.getHistoryId();
                    iHistoryService.setTaskIsTraining(info.getHistoryId());
                    System.out.println("已修改任务状态为在训练");
                    LaunchTask(script, info.getHistoryId());  //启动任务
                    iHistoryService.setTaskIsReady(info.getHistoryId());
                    System.out.println("已修改任务状态为Ready");
                }
            }
        }
    }

    public void LaunchTask(String script, int taskId){
        //创建一个线程并执行脚本script
        Thread taskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 在这里执行Python脚本
                try {
                    Process process = Runtime.getRuntime().exec(script);
                    int exitCode = process.waitFor(); // 等待进程执行完成
                    if (exitCode == 0) {
                        System.out.println("剪枝任务执行成功！");
                    } else {
                        System.out.println("剪枝任务执行失败，退出码：" + exitCode);
                        System.out.println("修改数据库，设置任务失败");
                        iHistoryService.setTaskIsFailed(taskId);
                        System.out.println("已修改任务状态为Failed");
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        taskThread.start(); // 启动线程
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
