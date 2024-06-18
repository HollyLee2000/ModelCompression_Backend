package org.zjuvipa.compression.client155_1.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
import org.zjuvipa.compression.client155_1.service.IClientHistoryService;
import org.zjuvipa.compression.model.entity.ClientHistory;
import org.zjuvipa.compression.model.entity.RabbitMQMessage;
import org.zjuvipa.compression.model.entity.ScriptMessage;
import org.zjuvipa.compression.model.info.ClientHistoryInfo;
import org.zjuvipa.compression.model.info.HistoryInfo;
import org.zjuvipa.compression.model.res.FindHistoryRes;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import java.util.Queue;

@Component
public class ClientTimedTask {

//    private Socket socket;

//    private ServerSocket socket4receive;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private java.util.Queue<String> messageQueue = new ArrayDeque<>();

    // 交换机名称
    private final String exchangeName = "vipaCompression.direct";

    @Autowired
    private IClientHistoryService iClientHistoryService;

    // 在 Spring Bean 初始化完成后执行的方法上标记 @PostConstruct 注解。常用于执行一些初始化操作，
    // 例如连接数据库、加载配置文件等。被标记的方法将在依赖注入完成后、Bean 属性设置之后被自动调用。
//    @PostConstruct
//    public void init() {
//        try {
//            socket4receive = new ServerSocket(50026);  // 用于接收分发器的端口
//            socket4receive.setSoTimeout(2000);  // 有同步锁无所谓
//        } catch (IOException e) {
//            System.out.println("Client155_1 failed to create server socket for receiving message.");
//        }
//    }

    // 定义定时任务，使用@Scheduled注解指定调度时间表达式
//    @Scheduled(cron = "0/5 * * * * ?")  //5秒执行一次
    @Scheduled(fixedRate = 500)  //500毫秒执行一次
    public synchronized void send() throws IOException {
        if(messageQueue.size()==0){
            List<ClientHistory> resultExecuting = iClientHistoryService.findExecutingTask();
            if(resultExecuting.isEmpty()){
                rabbitTemplate.convertAndSend(exchangeName, "server", "客户端155_1当前无任务");
                System.out.println("发送信息：当前无任务");
            }else{
                rabbitTemplate.convertAndSend(exchangeName, "server",
                        "客户端155_1当前正在剪枝、微调模型，任务id: " + resultExecuting.get(0).getTaskId());
                System.out.println("发送信息：客户端155_1当前正在剪枝、微调模型，任务id: " + resultExecuting.get(0).getTaskId());
            }
        }else{
            String message = messageQueue.poll();
            rabbitTemplate.convertAndSend(exchangeName, "server", message);
//            writer.write(message);  //执行完这个之后，服务器端的receive()函数的死锁了
//            writer.flush();
            System.out.println("发送信息：" + message);
        }
//        try {
//            socket = new Socket("10.214.242.155", 50016);
//            socket.setSoTimeout(2000);
////            System.out.println("向服务器发送信息....");
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//            if(messageQueue.size()==0){
//                writer.write("当前无任务\n");
//                writer.flush();
//                System.out.println("发送信息：当前无任务");
//            }else{
//                String message = messageQueue.poll();
//                writer.write(message);  //执行完这个之后，服务器端的receive()函数的死锁了
//                writer.flush();
//                System.out.println("发送信息：" + message);
//            }
//        } catch (SocketTimeoutException e) {
//            System.out.println("在指定时间内没有连接到服务器");
//        } catch (NullPointerException | ConnectException e){
//            System.out.println("没有连到服务器，请查看服务器是否启动");
//        }
    }



    @CrossOrigin
    @ApiOperation("查找需要训练的任务，执行进程")
    @Scheduled(cron = "0/5 * * * * ?")  //五秒执行一次
    public synchronized void ExecuteTrainingTask() {
        List<ClientHistory> resultExecuting = iClientHistoryService.findExecutingTask(); //查询正在执行的任务
        if(resultExecuting.isEmpty()){
            System.out.println("没有正在执行的任务，查询是否有任务等待......");
            List<ClientHistory> result = iClientHistoryService.findWaitingTask();
            if(result.isEmpty()){
                System.out.println("没有正在等待训练的任务");
            }else{
                System.out.println("有正在等待的任务，它们是："+result);
                ClientHistory minTask = result.get(0);
                System.out.println("取出task id最小的任务进行训练："+minTask);
                String script = minTask.getScript();
                script += " --task-id " + minTask.getTaskId();
                LaunchTask(script, minTask.getTaskId());  //启动任务
                iClientHistoryService.updateHistoryAfterLaunch(minTask.getTaskId());
                System.out.println("已修改任务状态为Ready");
                System.out.println("告知服务器同步信息.....");
                rabbitTemplate.convertAndSend(exchangeName, "server", "task ready: " + minTask.getTaskId());
                System.out.println("已告知服务器！");
                //这个result里面有许多个ClientHistory，帮我取出taskId最小的那个
            }
        }else{
            System.out.println("有任务正在执行：" + resultExecuting);
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
                        iClientHistoryService.updateHistoryAfterFailed(taskId);
                        System.out.println("告知服务器同步信息.....");
                        rabbitTemplate.convertAndSend(exchangeName, "server", "task failed: " + taskId);
                        System.out.println("已告知服务器！");
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        taskThread.start(); // 启动线程
    }






//    // 定义定时任务，使用@Scheduled注解指定调度时间表达式
//    @Scheduled(cron = "0/2 * * * * ?")  //两秒执行一次

//    public int hIndex(int[] citations) {
//        Arrays.sort(citations, (a, b)->{
//            return b-a;
//        });
//        for(int i: citations){
//            System.out.println(i);
//        }
//
//        //不能使用基本数据类型
//        Integer[] arr = {5,4,7,9,2,12,54,21,1};
//        //降序
//        Arrays.sort(arr, new Comparator<Integer>() {
//            //重写compare方法，最好加注解，不加也没事
//            public int compare(Integer a, Integer b) {
//                //返回值>0交换
//                return b-a;
//            }
//        });
//        System.out.println(Arrays.toString(arr));
//        return 0;
//    }


//    public long findKthSmallest(int[] coins, int k) {
//        Arrays.sort(coins);
//        int maxNum = coins[0]*k;
//        PriorityQueue<LinkedList<Integer>> queue = new PriorityQueue<>((a,b)->{
//            return a.getFirst()-b.getFirst();
//        });
//        for(int coinValue: coins){
//            LinkedList<Integer> tp= new LinkedList<>();
//            while(coinValue<=maxNum){
//                tp.offerLast(coinValue);
//                coinValue *= 2;
//            }
//            queue.offer(tp);
//        }
//        int now = 0;
//        while(now<k){
//            LinkedList<Integer> nowPeek = queue.poll();
//            nowPeek.getFirst();
//            nowPeek.pollFirst();
//            queue.offer(nowPeek);
//            now++;
//        }
//        return queue.poll().getFirst();
//    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue155_1"),
            exchange = @Exchange(name = "vipaCompression.direct", type = ExchangeTypes.DIRECT),
            key = {"vipa155_1", "all_client"}
    ))
    public void receive(Message mess) throws InterruptedException, IOException {
            byte[] body = mess.getBody();
            //如何根据body判断它可以被映射为RabbitMQMessage类还是HistoryInfo类
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            Class<?> targetType;
            // 判断body可以被映射为RabbitMQMessage类还是HistoryInfo类
            if (isJson(body, RabbitMQMessage.class)) {
                targetType = RabbitMQMessage.class;
            } else if (isJson(body, HistoryInfo.class)) {
                targetType = HistoryInfo.class;
            } else if (isJson(body, ScriptMessage.class)) {
                targetType = ScriptMessage.class;
            }else {
                System.out.println("vipa155_1接收到不支持的消息类型：【" + mess + "】");
                throw new IllegalArgumentException("不支持的消息类型，目前只支持RabbitMQMessage类、HistoryInfo类和ScriptMessage类！！");
            }
            Object myObject = objectMapper.readValue(body, typeFactory.constructType(targetType));
            if(myObject instanceof HistoryInfo){
                System.out.println("vipa155_1接收到服务器的消息：【" + myObject + "】" + LocalTime.now());
                HistoryInfo info = (HistoryInfo) myObject;
                boolean uploaded = iClientHistoryService.uploadHistoryIfNotExist(info.getHistoryId(), info.getUsername(), info.getStatus(), info.getParamsChange(), info.getFlopsChange(), info.getAccChange(),
                        info.getLossChange(), info.getPrunedPath(), info.getStructureAfterPruned(), info.getLogPath(), info.getTotEpoch(), info.getCurrentEpoch(),
                        info.getScript());
                if(uploaded){
                    System.out.println("已完成任务注入，告知服务器修改任务状态....");
                }else{
                    System.out.println("这个任务已经被注入过了，告知服务器修改任务状态....");
                }
                messageQueue.add("Client155_1 has injected taskid: "+info.getHistoryId());
            }else if (myObject instanceof RabbitMQMessage) {
                RabbitMQMessage info = (RabbitMQMessage) myObject;
                System.out.println("vipa155_1接收到训练进程的消息：【" + info.getMessage() + "】" + LocalTime.now());
            }else if (myObject instanceof ScriptMessage) {
                ScriptMessage info = (ScriptMessage) myObject;
                switch (info.getAction()) {
                    case "sparse_learning":
                        System.out.println("进程已结束等待，开始进行稀疏学习：【" + info + "】" + LocalTime.now());
                        break;
                    case "pruning":
                        System.out.println("进程已结束等待，启动剪枝程序：【" + info + "】" + LocalTime.now());
                        break;
                    case "pruned":
                        System.out.println("进程已完成剪枝并发来信息：【" + info + "】" + LocalTime.now());
                        break;
                    case "epoch++":
                        System.out.println("进程完成了一个epoch并发来消息：【" + info + "】" + LocalTime.now());
                        break;
                    case "sparsify epoch++":
                        System.out.println("进程完成了一个稀疏训练的epoch并发来消息：【" + info + "】" + LocalTime.now());
                        break;
                    case "finetuned":
                        System.out.println("进程完成了finetune并发来消息：【" + info + "】" + LocalTime.now());
                        break;
                    default:
                        System.out.println("进程发来未知类型的消息：【" + info + "】" + LocalTime.now());
                        break;
                }
                System.out.println("修改数据库信息.....");
                boolean uploaded = iClientHistoryService.updateHistoryAfterPruned(info.getTaskId(), info.getStatus(), info.getParamsChange(), info.getFlopsChange(), info.getAccChange(),
                        info.getLossChange(), info.getPrunedPath(), info.getStructureAfterPruned(), info.getLogPath(), info.getTotEpoch(), info.getCurrentEpoch());
                if(uploaded){
                    System.out.println("修改完成！");
                }
                System.out.println("告知服务器同步信息.....");
                rabbitTemplate.convertAndSend(exchangeName, "server", info);
                System.out.println("已告知服务器！");
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

//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(name = "direct.queue155_1"),
//            exchange = @Exchange(name = "vipaCompression.direct", type = ExchangeTypes.DIRECT),
//            key = {"vipa155_1", "all_client"}
//    ))
//    public void receiveString(RabbitMQMessage info) throws InterruptedException {
//        System.out.println("vipa155_1接收到训练进程的消息：【" + info.getMessage() + "】" + LocalTime.now());
//    }
}


