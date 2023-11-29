package org.zjuvipa.compression.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.zjuvipa.compression.util.ResultBean;
import org.zjuvipa.model.info.*;
import org.zjuvipa.model.req.*;
import org.zjuvipa.model.res.*;
import org.zjuvipa.compression.service.IAlgorithmService;
import org.zjuvipa.compression.service.IModelService;
import org.zjuvipa.compression.service.IPictureDataService;

import org.springframework.web.bind.annotation.*;

import org.zjuvipa.compression.service.IDatasetService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */


@Api(tags = "算法")
@RestController
@RequestMapping("/algorithm")
public class AlgorithmController {
    @Autowired
    IAlgorithmService iAlgorithmService;

    @Autowired
    IDatasetService iDatasetService;
    @Autowired
    IPictureDataService iPictureDataService;

    @Autowired
    IModelService iModelService;
    @Resource
    private HttpServletRequest request;

    private Map<String, Integer> resolvedNum = new HashMap<String, Integer>();
    private Map<String, Integer> totalNum = new HashMap<String, Integer>();
    private boolean pruner_flag = false; //是否获得剪枝器，获得的时候开始展示进度条

    @CrossOrigin
    @ApiOperation("查询指定算法")
    @PostMapping("findAlgoByName")
    public ResultBean<FindAlgoByNameRes> findAlgo(@RequestBody FindAlgoByNameReq findAlgoByNameReq) {
        ResultBean<FindAlgoByNameRes> result = new ResultBean<FindAlgoByNameRes>();
        if(!StringUtils.hasText(findAlgoByNameReq.getAlgorithmName())){
            result.setCode(ResultBean.FAIL);
            result.setMsg("输入算法名称不合法！");
            result.setData(null);
            return result;
        }
        FindAlgoByNameRes  findAlgoByNameRes = new FindAlgoByNameRes();
        findAlgoByNameRes.setAlgorithmInfo(iAlgorithmService.findAlgoByName(findAlgoByNameReq.getAlgorithmName()));
        if(findAlgoByNameRes.getAlgorithmInfo() == null) {
            result.setCode(ResultBean.FAIL);
            result.setMsg("不存在该算法");
            result.setData(null);
        }
        else{
//            result.setMsg("查找成功，算法名称为："+findAlgoByNameRes.getAlgorithmInfo().getAlgoName());
            result.setMsg("查找成功，算法名称为");
            result.setData(findAlgoByNameRes);
        }
        return result;
    }

    @CrossOrigin
    @ApiOperation("查询可用算法")
    @PostMapping("searchAlgorithm")
    public ResultBean<SearchAlgorithmRes> searchAlgorithm(){
        SearchAlgorithmRes searchAlgorithmRes = new SearchAlgorithmRes();
        searchAlgorithmRes.setAlgorithmInfos(iAlgorithmService.searchAlgorithm());
        ResultBean<SearchAlgorithmRes> result = new ResultBean<SearchAlgorithmRes>();
        result.setData(searchAlgorithmRes);
        return result;
    }

    @CrossOrigin
    @ApiOperation("返回服务器当前剩余最大算力")
    @PostMapping("reportGpu")
    public ResultBean<CallAlgorithmRes> reportGpu(@RequestBody CallAlgorithmReq callAlgorithmReq) throws IOException, InterruptedException {
        System.out.println(callAlgorithmReq);
        ResultBean<CallAlgorithmRes> result = new ResultBean<CallAlgorithmRes>();

        String command1 = "python /nfs/lhl/OIPDL_codes/cal_gpu.py";

        System.out.println("command: " + command1);
        Process process = Runtime.getRuntime().exec(command1);
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        List<String> lines = new ArrayList<String>();
        String line;
        while ((line = in.readLine()) != null) {
            lines.add(line);
        }
        process.waitFor();
        CallAlgorithmRes callAlgorithmRes = new CallAlgorithmRes();
        callAlgorithmRes.setResult(lines);
        result.setData(callAlgorithmRes);
        result.setMsg(command1);
        System.out.println("result:" + callAlgorithmRes);
        return result;
    }

    @CrossOrigin
    @ApiOperation("拒绝算法")
    @PostMapping("/handleReject")
    public ResultBean<UploadPicturesRes> handleReject(@RequestBody HandleAlgorithmReq req) throws IOException {
        Integer algorithmId = req.getAlgorithmId();
        ResultBean<UploadPicturesRes> result = new ResultBean<>();
        UploadPicturesRes res = new UploadPicturesRes();
        System.out.println("algorithmId: "+algorithmId.toString());
        res.setSucceed(iAlgorithmService.algorithmReject(algorithmId));
        result.setData(res);
        result.setMsg("上传成功");
        return result;
    }

    @CrossOrigin
    @ApiOperation("同意算法")
    @PostMapping("/handleApprove")
    public ResultBean<UploadPicturesRes> handleApprove(@RequestBody HandleAlgorithmApprovalReq req) throws IOException, InterruptedException {
        Integer algorithmId = req.getAlgorithmId();
        ResultBean<UploadPicturesRes> result = new ResultBean<>();
        UploadPicturesRes res = new UploadPicturesRes();
        System.out.println("algorithmId: "+algorithmId.toString());


//        String command = "python /nfs3-p1/duanjr/meta-score-attribution-hollylee/update_meta.py --username "
//                + '\'' + req.getUsername() + '\'' + " --institute "+ '\'' + req.getInstitute()+ '\'' + " --name "+ '\'' + req.getName()+ '\'' + " --new_morf " + req.getMorf() + " --new_lerf " + req.getLerf();



        String command = "python /nfs3-p1/duanjr/meta-score-attribution-hollylee/update_meta.py --username "
                 + req.getUsername()  + " --institute " + req.getInstitute() + " --name " + req.getName() + " --new_morf " + req.getMorf() + " --new_lerf " + req.getLerf();


        System.out.println("command: " + command);
        Process process1 = Runtime.getRuntime().exec(command);
        System.out.println("Point 1");

        BufferedReader in = new BufferedReader(new InputStreamReader(process1.getInputStream()));
        List<String> lines = new ArrayList<String>();
        String line;
        while ((line = in.readLine()) != null) {
            lines.add(line);
        }
        process1.waitFor();

        System.out.println("Point 2");
        System.out.println("lines[0]: " + lines.get(0));


//        process1.waitFor();

        res.setSucceed(iAlgorithmService.algorithmApprove(algorithmId));
        result.setData(res);
        result.setMsg("同意算法成功");
        return result;
    }

    @CrossOrigin
    @ApiOperation("调用剪枝算法")
    @PostMapping("callPruneAlgorithm")
    public ResultBean<CallAlgorithmRes> callPruneAlgorithm(@RequestBody CallAlgorithmReq callAlgorithmReq) throws IOException, InterruptedException {
        pruner_flag = false;
        System.out.println(callAlgorithmReq);
        ResultBean<CallAlgorithmRes> result = new ResultBean<CallAlgorithmRes>();
        String command1 = callAlgorithmReq.getModelName();
        System.out.println("command: " + command1);
        Process process = Runtime.getRuntime().exec(command1);
        String algo_name = callAlgorithmReq.getAlgorithmName();
        int batchSize = callAlgorithmReq.getDatasetId();
        String dataset = callAlgorithmReq.getDatasetName();


        if(algo_name.equals("cifarAlgorithm")){

            if(dataset.equals("cifar10") || dataset.equals("cifar100")){
                Map<Integer, Integer> intToIntMap = new HashMap<>();
                intToIntMap.put(128, 79);
                intToIntMap.put(64, 157);
                intToIntMap.put(32, 313);
                // 王佳的socket
                int Num = 0;
                int size1 = intToIntMap.get(batchSize);
                resolvedNum.put("current", 0);
                ServerSocket ss = new ServerSocket(50006);
                while(Num < size1*2) {
                    System.out.println("启动服务器....");
                    Socket s = ss.accept();
                    System.out.println("客户端:"+s.getInetAddress().getLocalHost()+"已连接到服务器");
                    BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String mess = br.readLine();
                    if(StringUtils.hasText(mess)){
                        if(mess.equals("Pruner got")){
                            System.out.println("mess: " + mess);
                            System.out.println("获得了剪枝器，准备开始展示进度条");
                            pruner_flag = true;
                        }else{
                            System.out.println("mess: " + mess);
                            Num += 1;
                        }
                    }
                    else continue;
                    System.out.println("已处理batch:" +Num);
                    resolvedNum.put("current", Num);
                }
                ss.close();
            }else if(dataset.equals("imagenet")) {
                Map<Integer, Integer> intToIntMap = new HashMap<>();
                intToIntMap.put(128, 391);
                intToIntMap.put(64, 782);
                intToIntMap.put(32, 1563);

                // 王佳的socket
                int Num = 0;
                int size1 = intToIntMap.get(batchSize);
                resolvedNum.put("current", 0);
                ServerSocket ss = new ServerSocket(50006);
                while (Num < size1 * 2) {
                    System.out.println("启动服务器....");
                    Socket s = ss.accept();
                    System.out.println("客户端:" + s.getInetAddress().getLocalHost() + "已连接到服务器");
                    BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String mess = br.readLine();
                    if (StringUtils.hasText(mess)) {
                        if (mess.equals("Pruner got")) {
                            System.out.println("mess: " + mess);
                            System.out.println("获得了剪枝器，准备开始展示进度条");
                            pruner_flag = true;
                        } else {
                            System.out.println("mess: " + mess);
                            Num += 1;
                        }
                    } else continue;
                    System.out.println("已处理batch:" + Num);
                    resolvedNum.put("current", Num);
                }
                ss.close();
            }else if(dataset.equals("coco")){



                Map<Integer, Integer> intToIntMap = new HashMap<>();
                intToIntMap.put(128, 20);
                intToIntMap.put(64, 40);
                intToIntMap.put(32, 79);
                intToIntMap.put(16, 157);

                // 王佳的socket
                int Num = 0;
                int size1 = intToIntMap.get(batchSize);
                resolvedNum.put("current", 0);
                ServerSocket ss = new ServerSocket(50006);
                while (Num < size1) {
                    System.out.println("启动服务器....");
                    Socket s = ss.accept();
                    System.out.println("客户端:" + s.getInetAddress().getLocalHost() + "已连接到服务器");
                    BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String mess = br.readLine();
                    if (StringUtils.hasText(mess)) {
                        if (mess.equals("Pruner got")) {
                            System.out.println("mess: " + mess);
                            System.out.println("获得了剪枝器，准备开始展示进度条");
                            pruner_flag = true;
                        } else {
                            System.out.println("mess: " + mess);
                            Num += 1;
                        }
                    } else continue;
                    System.out.println("已处理batch:" + Num);
                    resolvedNum.put("current", Num);
                }
                ss.close();




            }

        }



        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        List<String> lines = new ArrayList<String>();
        String line;
        while ((line = in.readLine()) != null) {
            lines.add(line);
        }
        process.waitFor();
        CallAlgorithmRes callAlgorithmRes = new CallAlgorithmRes();
        callAlgorithmRes.setResult(lines);

        result.setData(callAlgorithmRes);
        result.setMsg(command1);
        System.out.println("result:" + callAlgorithmRes);
        return result;
    }



    @CrossOrigin
    @ApiOperation("读取进度")
    @PostMapping("getProcess")
    public ResultBean<GetProcessRes> getProcess(@RequestBody GetProcessReq getProcessReq) {
//        log.info("getProcessReq"+getProcessReq);
//        log.info("getProcess:"+resolvedNum);
//        log.info("getProcess:"+totalNum);
        ResultBean<GetProcessRes> resultBean = new ResultBean<>();
        GetProcessRes getProcessRes = new GetProcessRes();

        String dataset = getProcessReq.getDataset();
        Map<Integer, Integer> intToIntMap = new HashMap<>();
        if(dataset.equals("cifar10")||dataset.equals("cifar100")){
            intToIntMap.put(128, 79);
            intToIntMap.put(64, 157);
            intToIntMap.put(32, 313);
        }else if(dataset.equals("imagenet")){
            intToIntMap.put(128, 391);
            intToIntMap.put(64, 782);
            intToIntMap.put(32, 1563);
        }else if(dataset.equals("coco")){
            intToIntMap.put(128, 20);
            intToIntMap.put(64, 40);
            intToIntMap.put(32, 79);
            intToIntMap.put(16, 157);
        }



        if (resolvedNum.containsKey("current")) {
            System.out.println("有current这个建");
            getProcessRes.setProcess(resolvedNum.get("current"));
        } else {
            System.out.println("没有current键，赋0");
            getProcessRes.setProcess(0);
        }
//        getProcessRes.setProcess(resolvedNum.get("current"));
        if(getProcessReq.getBatchSize()==10086){
            if(dataset.equals("cifar10")||dataset.equals("cifar100")){
                getProcessRes.setTotal(79);
            }else if(dataset.equals("imagenet")){
                getProcessRes.setTotal(391);
            }else if(dataset.equals("coco")){
                getProcessRes.setTotal(20);
            }
        }else{
            if(dataset.equals("coco")){
                getProcessRes.setTotal(intToIntMap.get(getProcessReq.getBatchSize()));
            }else{
                getProcessRes.setTotal(intToIntMap.get(getProcessReq.getBatchSize())*2);
            }
        }


        resultBean.setData(getProcessRes);
        getProcessRes.setPrunner(pruner_flag);

        return resultBean;
    }



    @CrossOrigin
    @ApiOperation("调用算法")
    @PostMapping("callAlgorithm")
    public ResultBean<CallAlgorithmRes> callAlgorithm(@RequestBody CallAlgorithmReq callAlgorithmReq) throws IOException, InterruptedException {
        System.out.println(callAlgorithmReq);
        ResultBean<CallAlgorithmRes> result = new ResultBean<CallAlgorithmRes>();
//        AlgorithmInfo algorithmInfo = iAlgorithmService.findAlgoByName(callAlgorithmReq.getAlgorithmName());
//        DatasetInfo datasetInfo = iDatasetService.getDatasetInfo(callAlgorithmReq.getDatasetId());
        String model_name = callAlgorithmReq.getModelName();
        String dataset_name = callAlgorithmReq.getDatasetName();
        String img_name = callAlgorithmReq.getUserName();
        String algo_name = callAlgorithmReq.getAlgorithmName();
        String command1;
        if(algo_name.equals("CUB-200-2011")){
            command1 = "python /nfs/lhl/OIPDL_codes/" + dataset_name +
                    "/run_cub_pooled.py" + " --log_dir /nfs/lhl/OIPDL_codes/CUB_CARS/tests/test_" + model_name + "_pooled"
                    + " --dataset " + algo_name + " --sample_dir /nfs/lhl/datasets/CUB_200_2011/images/" + img_name
                    + " --prototree /nfs/lhl/OIPDL_codes/CUB_CARS/tests/test_" + model_name + "_pooled/checkpoints/pruned_and_projected";
        }else if(algo_name.equals("CARS")){
            command1 = "python /nfs/lhl/OIPDL_codes/" + dataset_name +
                    "/run_cars_pooled.py" + " --log_dir /nfs/lhl/OIPDL_codes/CUB_CARS/tests/test_" + model_name + "_pooled"
                    + " --dataset " + algo_name + " --sample_dir /nfs/lhl/datasets/cars/cars_train/" + img_name
                    + " --prototree /nfs/lhl/OIPDL_codes/CUB_CARS/tests/test_" + model_name + "_pooled/checkpoints/pruned_and_projected";
        }else if(algo_name.equals("CUB-200-2011-adjust")){
            String[] matches = dataset_name.split("(?<=\\])");
            String rank_adj = matches[0];
            String idx = matches[1];
            System.out.println("matches: "+matches);
            command1 = "python /nfs/lhl/OIPDL_codes/CUB_CARS/adjust_cub_pooled.py" + " --log_dir /nfs/lhl/OIPDL_codes/CUB_CARS/tests/test_" + model_name + "_pooled"
                    + " --dataset CUB-200-2011 --sample_dir /nfs/lhl/datasets/CUB_200_2011/images/" + img_name
                    + " --prototree /nfs/lhl/OIPDL_codes/CUB_CARS/tests/test_" + model_name + "_pooled/checkpoints/pruned_and_projected"
                    + " --idx " + idx + " --rank_adj " + rank_adj;
        }else if(algo_name.equals("CUB-200-2011-adjust-tree")){
            String[] matches = dataset_name.split("(?<=\\])");
            String rank_adj = matches[0];
            String idx = matches[1];
            System.out.println("matches: "+matches);
            command1 = "python /nfs/lhl/OIPDL_codes/CUB_CARS/adjust_cub_tree.py" + " --log_dir /nfs/lhl/OIPDL_codes/CUB_CARS/tests/test_" + model_name + "_pooled"
                    + " --dataset CUB-200-2011 " + " --idx " + idx + " --rank_adj " + rank_adj
                    + " --batch_size 64";
        }else if(algo_name.equals("CUB-200-2011-store-model")){
            String[] matches = dataset_name.split("(?<=\\])");
            String rank_adj = matches[0];
            String idx = matches[1];
            System.out.println("matches: "+matches);
            command1 = "python /nfs/lhl/OIPDL_codes/CUB_CARS/store_cub_tree.py" + " --log_dir /nfs/lhl/OIPDL_codes/CUB_CARS/tests/test_" + model_name + "_pooled"
                    + " --dataset CUB-200-2011 " + " --idx " + idx + " --rank_adj " + rank_adj + " --new_dir /nfs/lhl/OIPDL_codes/CUB_CARS/tests/test_" + img_name + "_pooled";

//            System.setProperty("user.dir", "/nfs/lhl/OIPDL_codes/CUB_CARS/tests");



            String workingDirectory = "/nfs/lhl/OIPDL_codes/CUB_CARS/tests";
            String command_unzip = "unzip test_"+model_name+"_pooled.zip -d test_"+img_name+"_pooled";

            try {
                System.out.println("command_unzip: "+command_unzip);
                ProcessBuilder processBuilder = new ProcessBuilder();
                processBuilder.command("bash", "-c", command_unzip);
                processBuilder.directory(new File(workingDirectory));
                Process process_unzip = processBuilder.start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process_unzip.getInputStream()));
                String line;
                int cnt = 0;
                while ((line = reader.readLine()) != null) {
                    if(cnt==100){
                        System.out.println(line);
                        cnt = 0;
                    }
                    cnt++;
                }

                process_unzip.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

//            String command_change_dir = "cd /nfs/lhl/OIPDL_codes/CUB_CARS/tests";
//            System.out.println("command_change_dir: "+command_change_dir);
//            Process process_change_dir = Runtime.getRuntime().exec(command_change_dir);
//            process_change_dir.waitFor();

            //
//            unzip /nfs/lhl/OIPDL_codes/CUB_CARS/tests/test_cub1_pooled.zip -d /nfs/lhl/OIPDL_codes/CUB_CARS/tests/test_new_pooled
            //之前的版本
//            String command_unzip = "unzip /nfs/lhl/OIPDL_codes/CUB_CARS/tests/test_"+model_name+"_pooled.zip -d /nfs/lhl/OIPDL_codes/CUB_CARS/tests/test_"+img_name+"_pooled";

//            String command_unzip = "unzip test_"+model_name+"_pooled.zip -d test_"+img_name+"_pooled";
//            String command_copy = "rsync -azP /nfs/lhl/OIPDL_codes/CUB_CARS/tests/test_"+model_name+"_pooled /nfs/lhl/OIPDL_codes/CUB_CARS/tests/test_"+img_name+"_pooled";

//            Process process_unzip = Runtime.getRuntime().exec(command_unzip);
//            System.out.println("process_unzip: "+process_unzip);

//            process_unzip.waitFor();
            System.out.println("解压完成！");
        }else{
            command1 = "python /nfs/lhl/OIPDL_codes/" + dataset_name +
                    "/run.py" + " --pic " + img_name
                    + " --net " + model_name + " --way " + algo_name;
        }
        System.out.println("command: " + command1);
        Process process = Runtime.getRuntime().exec(command1);



        if(algo_name.equals("CUB-200-2011-adjust-tree")){
            // 王佳的socket
            int Num = 0;
            int size1 = 91;
            resolvedNum.put("current", 0);
            ServerSocket ss = new ServerSocket(50006);
            while(Num < size1) {
                System.out.println("启动服务器....");
                Socket s = ss.accept();
                System.out.println("客户端:"+s.getInetAddress().getLocalHost()+"已连接到服务器");
                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String mess = br.readLine();
                if(StringUtils.hasText(mess)){
                    Num += 1;
                }
                else continue;
                System.out.println("已处理batch:" +Num);
                resolvedNum.put("current", Num);
//                if("SOS".equals(mess)) {
//                    result.setCode(ResultBean.FAIL);
//                    result.setMsg("图像数据处理失败");
//                    result.setData(null);
//                    ss.close();
//                    return result;
//                }
//                PictureDataInfo pictureDataInfo = iPictureDataService.getPictureByUrl(urlPath + mess);
//                System.out.println(urlPath+mess);
//                System.out.println(pictureDataInfo);
//                String saveUrl = Constant.HOST + ":" + Constant.PORT + "/res/" + callAlgorithmReq.getUserName() + '/'
//                        + callAlgorithmReq.getDatasetId() + '/' + mess;
//                iPictureDataService.updatePictureResult(pictureDataInfo.getPictureId(), saveUrl);
//                iCrackService.deleteCrackByPictureId(pictureDataInfo.getPictureId());
//
//
//                String csvPath = savePath + mess.substring(0,mess.indexOf('.')) + ".csv";
//                List<CrackInfo> crackInfos = readCrackFile(csvPath, pictureDataInfo.getPictureId());
//                if(!iCrackService.addCracks(crackInfos)){
//                    result.setCode(ResultBean.FAIL);
//                    result.setMsg("裂缝信息添加失败");
//                    result.setData(null);
//                    return result;
//                }

            }
            ss.close();
        }






        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        List<String> lines = new ArrayList<String>();
        String line;
        while ((line = in.readLine()) != null) {
            lines.add(line);
        }
        process.waitFor();
        CallAlgorithmRes callAlgorithmRes = new CallAlgorithmRes();
        callAlgorithmRes.setResult(lines);

        result.setData(callAlgorithmRes);
        result.setMsg(command1);
        System.out.println("result:" + callAlgorithmRes);


        if(algo_name.equals("CUB-200-2011-store-model")){
//            zip -r /nfs/lhl/OIPDL_codes/CUB_CARS/tests/test_new_pooled.zip /nfs/lhl/OIPDL_codes/CUB_CARS/tests/test_new_pooled



            String workingDirectory = "/nfs/lhl/OIPDL_codes/CUB_CARS/tests/test_"+img_name+"_pooled";
            String command_zip = "zip -r ../test_"+img_name+"_pooled.zip .";

            try {
                System.out.println("command_zip: "+command_zip);
                ProcessBuilder processBuilder = new ProcessBuilder();
                processBuilder.command("bash", "-c", command_zip);
                processBuilder.directory(new File(workingDirectory));
                Process process_zip = processBuilder.start();

                BufferedReader reader22 = new BufferedReader(new InputStreamReader(process_zip.getInputStream()));
                String line22;
                int cnt = 0;
                while ((line22 = reader22.readLine()) != null) {
                    if(cnt==100){
                        System.out.println(line22);
                        cnt = 0;
                    }
                    cnt++;
                }
                process_zip.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("压缩完成！");
//            System.setProperty("user.dir", "/nfs/lhl/OIPDL_codes/CUB_CARS/tests");
        }


        return result;
    }



    private static List<GetResultsRes> readerMethod(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        Reader reader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8);
        int ch = 0;
        StringBuilder sb = new StringBuilder();
        while ((ch = reader.read()) != -1) {
            sb.append((char) ch);
        }
        fileReader.close();
        reader.close();
        String jsonStr = sb.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        return objectMapper.readValue(jsonStr, new TypeReference<List<GetResultsRes>>() {});
    }

    @CrossOrigin
    @ApiOperation("获取检测结果")
    @PostMapping("getResults")
    public ResultBean<List<GetResultsRes>> getResults(@RequestBody GetResultsReq getResultsReq) throws IOException {
        ResultBean<List<GetResultsRes>> result = new ResultBean<List<GetResultsRes>>();
        if(!StringUtils.hasText(getResultsReq.getModelName())){
            result.setCode(ResultBean.FAIL);
            result.setMsg("模型名称不合法！");
            result.setData(null);
            return result;
        }
//        String path = "D:/Dam/models/" + getResultsReq.getModelName() + "/result.json";
        String path = "/home/py/Dam/models/" + getResultsReq.getModelName() + "/result.json";
        File file = new File(path);
        List<GetResultsRes> lst = readerMethod(file);
        result.setData(lst);
        result.setMsg("success");
        return result;
    }

    @CrossOrigin
    @ApiOperation("获取日志")
    @PostMapping("getLog")
    public ResultBean<GetLogRes> getLog() throws IOException {
        String fileName = "D:\\Dam\\log.txt";
        List<String> lines = Files.readAllLines(Paths.get(fileName),
                StandardCharsets.UTF_8);
        GetLogRes getLogRes = new GetLogRes();
        getLogRes.setResult(lines);
        ResultBean<GetLogRes> result = new ResultBean<GetLogRes>();
        result.setData(getLogRes);
        return result;
    }


}
