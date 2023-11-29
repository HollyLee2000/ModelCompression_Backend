package org.zjuvipa.compression.backend.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.zjuvipa.compression.common.util.ResultBean;
import org.zjuvipa.compression.model.info.DatasetInfo;
import org.zjuvipa.compression.model.info.PictureDataInfo;
import org.zjuvipa.compression.model.info.UserInfo;
import org.zjuvipa.compression.model.req.*;
import org.zjuvipa.compression.model.res.*;
import org.zjuvipa.compression.backend.service.IDatasetService;
import org.zjuvipa.compression.backend.service.IPictureDataService;
import org.zjuvipa.compression.backend.service.IUserService;

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
@Api(tags = "数据集")
@RestController
@RequestMapping("/dataset")
public class DatasetController {
    @Autowired
    IDatasetService iDatasetService;

    @Autowired
    IPictureDataService iPictureDataService;

    @Autowired
    IUserService iUserService;

    @ApiOperation("创建数据集")
    @CrossOrigin
    @PostMapping("/createDataset")
    public ResultBean<CreateDatasetRes> createDataset(@RequestBody CreateDatasetReq req) {
        ResultBean<CreateDatasetRes> result = new ResultBean<>();
        CreateDatasetRes res = new CreateDatasetRes();
        UserInfo userInfo = iUserService.findByUsername(req.getUsername());
        if (userInfo == null) {
            result.setMsg("用户不存在");
            result.setCode(ResultBean.FAIL);
            return result;
        }
        res.setSucceed(iDatasetService.createDataset(req.getUsername(), req.getDatasetName(), req.isPublic()));
        res.setDatasetInfo(iDatasetService.getNewDataset());
        if (res.isSucceed()) {
            result.setMsg("创建成功");
            result.setData(res);
        } else {
            result.setMsg("创建失败");
            result.setCode(ResultBean.FAIL);
        }
        return result;
    }

    @ApiOperation("查询用户数据集")
    @CrossOrigin
    @PostMapping("/getUserDatasets")
    public ResultBean<List<GetUserDatasetsRes>> getUserDatasets(@RequestBody GetUserDatasetsReq req) {
        ResultBean<List<GetUserDatasetsRes>> result = new ResultBean<>();
        List<GetUserDatasetsRes> res = new ArrayList<>();

//        System.out.println("req: " + req);
        UserInfo userInfo = iUserService.findByUsername(req.getUsername());
        Integer authority = userInfo.getAuthority();
//        System.out.println("authority: " + authority);
        List<DatasetInfo> datasetInfos;
        if(authority==0){
//            System.out.println("req.getUsername(): " + req.getUsername());
//            System.out.println("req.getSearchname(): " + req.getSearchname());
            datasetInfos = iDatasetService.getUserVisibleDatasets(req.getSearchname(), req.getUsername());
        }else{
//            System.out.println("req.getSearchname(): " + req.getSearchname());
            datasetInfos = iDatasetService.getUserDatasets(req.getSearchname());
        }
        for (DatasetInfo datasetInfo: datasetInfos){
            int datasetId = datasetInfo.getDatasetId();
            List<PictureDataInfo> pictureDataInfos = iPictureDataService.getPictures(datasetId);
            if (pictureDataInfos.size()>=10){
                pictureDataInfos = pictureDataInfos.subList(0, 9);
            }
            GetUserDatasetsRes res1 = new GetUserDatasetsRes();
            res1.setDatasetInfo(datasetInfo);
            res1.setPictureDataInfos(pictureDataInfos);
            res.add(res1);
        }
        result.setData(res);
        return result;
    }

    @ApiOperation("查询某个普通用户可见的数据集")
    @CrossOrigin
    @PostMapping("/getVisibleDataset")
    public ResultBean<List<GetVisibleDatasetsRes>> getVisibleDataset(@RequestBody GetVisibleDatasetsReq req) {
        ResultBean<List<GetVisibleDatasetsRes>> result = new ResultBean<>();
        List<GetVisibleDatasetsRes> res = new ArrayList<>();
        UserInfo userInfo = iUserService.findByUsername(req.getUsername());
        Integer authority = userInfo.getAuthority();
        List<DatasetInfo> datasetInfos;
        if(authority==0){
            datasetInfos = iDatasetService.getVisibleDatasets(req.getUsername());
        }else{
            datasetInfos = iDatasetService.getAllDatasets();
        }
//        List<DatasetInfo> datasetInfos2 = iDatasetService.getPublicDatasets();
        for (DatasetInfo datasetInfo: datasetInfos){
            int datasetId = datasetInfo.getDatasetId();
            List<PictureDataInfo> pictureDataInfos = iPictureDataService.getPictures(datasetId);
            if (pictureDataInfos.size()>=10){
                pictureDataInfos = pictureDataInfos.subList(0, 9);
            }
            GetVisibleDatasetsRes res1 = new GetVisibleDatasetsRes();
            res1.setDatasetInfo(datasetInfo);
            res1.setPictureDataInfos(pictureDataInfos);
            res.add(res1);
        }
        result.setData(res);
        return result;
    }

    @ApiOperation("查询公共数据集")
    @CrossOrigin
    @PostMapping("/getPublicDatasets")
    public ResultBean<List<GetPublicDatasetsRes>> getPublicDatasets() {
        ResultBean<List<GetPublicDatasetsRes>> result = new ResultBean<>();
        List<GetPublicDatasetsRes> res = new ArrayList<>();
        List<DatasetInfo> datasetInfos = iDatasetService.getPublicDatasets();
        for (DatasetInfo datasetInfo: datasetInfos){
            int datasetId = datasetInfo.getDatasetId();
            List<PictureDataInfo> pictureDataInfos = iPictureDataService.getPictures(datasetId);
            if (pictureDataInfos.size()>=10){
                pictureDataInfos = pictureDataInfos.subList(0, 9);
            }
            GetPublicDatasetsRes res1 = new GetPublicDatasetsRes();
            res1.setDatasetInfo(datasetInfo);
            res1.setPictureDataInfos(pictureDataInfos);
            res.add(res1);
        }
        result.setData(res);
        return result;
    }

    @ApiOperation("删除数据集")
    @CrossOrigin
    @PostMapping("/deleteDataset")
    public ResultBean<DeleteDatasetRes> deleteDataset(@RequestBody DeleteDatasetReq req) {
        ResultBean<DeleteDatasetRes> result = new ResultBean<>();
        DeleteDatasetRes res = new DeleteDatasetRes();
        res.setSucceed(iDatasetService.deleteDataset(req.getDatasetId()));
        result.setData(res);
        if (res.isSucceed()) {
            result.setMsg("删除成功");
        } else {
            result.setMsg("删除失败，请检查数据集ID是否正确");
            result.setCode(ResultBean.FAIL);
        }
        return result;
    }

    @ApiOperation("获取数据集信息")
    @CrossOrigin
    @PostMapping("/getDatasetInfo")
    public ResultBean<GetDatasetInfoRes> getDatasetInfo(@RequestBody GetDatasetInfoReq req) {
        System.out.println("req: " + req);
        ResultBean<GetDatasetInfoRes> result = new ResultBean<>();
        GetDatasetInfoRes res = new GetDatasetInfoRes();
        System.out.println("req.getDatasetId(): " + req.getDatasetId());
        System.out.println("iDatasetService.getDatasetInfo(req.getDatasetId()): " + iDatasetService.getDatasetInfo(req.getDatasetId()));
        res.setDatasetInfo(iDatasetService.getDatasetInfo(req.getDatasetId()));
        if (res.getDatasetInfo() != null) {
            System.out.println("iPictureDataService.getPictures(req.getDatasetId()): " + iPictureDataService.getPictures(req.getDatasetId()));
            res.setPictureDataInfos(iPictureDataService.getPictures(req.getDatasetId()));
            result.setMsg("查询成功");
            result.setData(res);
        } else {
            result.setMsg("未找到相应数据集信息");
            result.setCode(ResultBean.FAIL);
        }
        System.out.println("result: " + result);
        return result;
    }


    @ApiOperation("获取归因图信息")
    @CrossOrigin
    @PostMapping("/getAtributionInfo")
    public ResultBean<List<String>> getAttributionInfo(@RequestBody GetDatasetInfoReq req) {
//        System.out.println("req: " + req);
        ResultBean<List<String>> result = new ResultBean<>();
        List<String> res = iPictureDataService.getAttributionPictures();
        result.setMsg("成功获得归因图!");
        result.setData(res);
        return result;
    }

    @ApiOperation("获取各数据集随机图像池")
    @CrossOrigin
    @PostMapping("/getPool")
    public ResultBean<List<String>> getPool(@RequestBody GetDatasetInfoReq req) throws IOException, InterruptedException{
//        System.out.println("req: " + req);
        ResultBean<List<String>> result = new ResultBean<>();
        List<String> lines = new ArrayList<String>();
//        if(req.getDatasetId()==10001){
//            System.out.println("刷新cifar池");
//            Process process = Runtime.getRuntime().exec("python /nfs/lhl/OIPDL_codes/CIFAR/Sample_no_cache.py");
//            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = in.readLine()) != null) {
//                lines.add("10.214.242.155:7996/img/cifar10_train/" + line);
//            }
//            process.waitFor();
////            List<String> res = iPictureDataService.getPoolPictures("cifar");
//            result.setMsg("成功获得CIFAR池!");
//            result.setData(lines);
//        }

        if(req.getDatasetId()==10001){
            System.out.println("刷新cifar池");
            //现在读取索引随机选择
            int MAX_INDEX = 49891;
            int NUM_INDICES = 36;
            try {
                // 创建ObjectMapper对象，用于解析JSON文件
                ObjectMapper objectMapper = new ObjectMapper();

                // 从JSON文件中读取数据并解析为Map对象
                File jsonFile = new File("/nfs/lhl/OIPDL_codes/cifar_idx2pic.json");
                @SuppressWarnings("unchecked")
                Map<String, String> dataMap = objectMapper.readValue(jsonFile, Map.class);

                // 随机生成 NUM_INDICES 个整数索引
                Random rand = new Random();
                int[] indices = new int[NUM_INDICES];
                for (int i = 0; i < NUM_INDICES; i++) {
                    indices[i] = rand.nextInt(MAX_INDEX + 1);
                }

                // 根据随机索引获取对应的 value 并打印
                for (int index : indices) {
                    String value = dataMap.get(Integer.toString(index));
                    lines.add("10.214.242.155:7996/Cifar/" + value);
                    System.out.println(index + ": " + value);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            result.setMsg("成功获得Cifar池!");
            result.setData(lines);
        }else if(req.getDatasetId()==10002){
            System.out.println("刷新imagenet池");
            //现在读取索引随机选择
            int MAX_INDEX = 1281166;
            int NUM_INDICES = 36;
            try {
                // 创建ObjectMapper对象，用于解析JSON文件
                ObjectMapper objectMapper = new ObjectMapper();

                // 从JSON文件中读取数据并解析为Map对象
                File jsonFile = new File("/nfs/lhl/OIPDL_codes/imgnet_idx2pic.json");
                @SuppressWarnings("unchecked")
                Map<String, String> dataMap = objectMapper.readValue(jsonFile, Map.class);

                // 随机生成 NUM_INDICES 个整数索引
                Random rand = new Random();
                int[] indices = new int[NUM_INDICES];
                for (int i = 0; i < NUM_INDICES; i++) {
                    indices[i] = rand.nextInt(MAX_INDEX + 1);
                }

                // 根据随机索引获取对应的 value 并打印
                for (int index : indices) {
                    String value = dataMap.get(Integer.toString(index));
                    lines.add("10.214.242.155:7996/originalImgNet/" + value);
                    System.out.println(index + ": " + value);
                    System.out.println("呵呵");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            //后来用java去遍历数据集并随机选择
//            File directory = new File("/nfs/lhl/datasets/ILSVRC2012/train");
//            List<String> fileNames = new ArrayList<>(Arrays.asList(directory.list()));
//            Random random = new Random();
//            for (int i = 0; i < 30; i++) {
//                int index = random.nextInt(fileNames.size());
//                String fileName = fileNames.get(index);
//
//
//                File subdirectory = new File("/nfs/lhl/datasets/ILSVRC2012/train/" + fileName);
//                List<String> subfileNames = new ArrayList<>(Arrays.asList(subdirectory.list()));
//                int subindex = random.nextInt(subfileNames.size());
//                String subfileName = subfileNames.get(subindex);
//
////                System.out.println(fileName);
//                lines.add("10.214.242.155:7996/originalImgNet/" + fileName + "/" + subfileName);
//                fileNames.remove(index);
//            }


            //最原始的做法, 调用python去处理
//            Process process = Runtime.getRuntime().exec("python /nfs/lhl/OIPDL_codes/ImageNet/Sample_no_cache.py");
//            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = in.readLine()) != null) {
//                lines.add("10.214.242.155:7996/img/imagnet_train/" + line);
//            }
//            process.waitFor();
//            List<String> res = iPictureDataService.getPoolPictures("cifar");
            result.setMsg("成功获得ImageNet池!");
            result.setData(lines);
//            System.out.println("lines: ");
        }else if(req.getDatasetId()==10003){

            System.out.println("刷新VOC池");
            //现在读取索引随机选择
            int MAX_INDEX = 1463;
            int NUM_INDICES = 36;
            try {
                // 创建ObjectMapper对象，用于解析JSON文件
                ObjectMapper objectMapper = new ObjectMapper();

                // 从JSON文件中读取数据并解析为Map对象
                File jsonFile = new File("/nfs/lhl/OIPDL_codes/voc_idx2pic.json");
                @SuppressWarnings("unchecked")
                Map<String, String> dataMap = objectMapper.readValue(jsonFile, Map.class);

                // 随机生成 NUM_INDICES 个整数索引
                Random rand = new Random();
                int[] indices = new int[NUM_INDICES];
                for (int i = 0; i < NUM_INDICES; i++) {
                    indices[i] = rand.nextInt(MAX_INDEX + 1);
                }

                // 根据随机索引获取对应的 value 并打印
                for (int index : indices) {
                    String value = dataMap.get(Integer.toString(index));
                    lines.add("10.214.242.155:7996/VOC/" + value);
                    System.out.println(index + ": " + value);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
//            System.out.println("这是VOC");
//            Process process = Runtime.getRuntime().exec("python /nfs/lhl/OIPDL_codes/VOC/Sample_no_cache.py");
//            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = in.readLine()) != null) {
//                lines.add("10.214.242.155:7996/img/voc_train_224/" + line);
//            }
//            process.waitFor();
//            List<String> res = iPictureDataService.getPoolPictures("cifar");
            result.setMsg("成功获得VOC池!");
            result.setData(lines);
        }else if(req.getDatasetId()==10004){
            System.out.println("刷新coco池");
            //现在读取索引随机选择
            int MAX_INDEX = 118286;
            int NUM_INDICES = 36;
            try {
                // 创建ObjectMapper对象，用于解析JSON文件
                ObjectMapper objectMapper = new ObjectMapper();

                // 从JSON文件中读取数据并解析为Map对象
                File jsonFile = new File("/nfs/lhl/OIPDL_codes/coco_idx2pic.json");
                @SuppressWarnings("unchecked")
                Map<String, String> dataMap = objectMapper.readValue(jsonFile, Map.class);

                // 随机生成 NUM_INDICES 个整数索引
                Random rand = new Random();
                int[] indices = new int[NUM_INDICES];
                for (int i = 0; i < NUM_INDICES; i++) {
                    indices[i] = rand.nextInt(MAX_INDEX + 1);
                }

                // 根据随机索引获取对应的 value 并打印
                for (int index : indices) {
                    String value = dataMap.get(Integer.toString(index));
                    lines.add("10.214.242.155:7996/cocoimg/" + value);
                    System.out.println(index + ": " + value);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            result.setMsg("成功获得coco池!");
            result.setData(lines);
//            System.out.println("这是COCO");
//            Process process = Runtime.getRuntime().exec("python /nfs/lhl/OIPDL_codes/COCO/Sample_no_cache.py");
//            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = in.readLine()) != null) {
//                lines.add("10.214.242.155:7996/cocoimg/" + line);
//            }
//            process.waitFor();
////            List<String> res = iPictureDataService.getPoolPictures("cifar");
//            result.setMsg("成功获得COCO池!");
//            result.setData(lines);
        }else if(req.getDatasetId()==10005){
            System.out.println("刷新CUB池");
            //现在读取索引随机选择
            int MAX_INDEX = 11787;
            int NUM_INDICES = 36;
            try {
                // 创建ObjectMapper对象，用于解析JSON文件
                ObjectMapper objectMapper = new ObjectMapper();

                // 从JSON文件中读取数据并解析为Map对象
                File jsonFile = new File("/nfs/lhl/OIPDL_codes/cub_idx2pic.json");
                @SuppressWarnings("unchecked")
                Map<String, String> dataMap = objectMapper.readValue(jsonFile, Map.class);

                // 随机生成 NUM_INDICES 个整数索引
                Random rand = new Random();
                int[] indices = new int[NUM_INDICES];
                for (int i = 0; i < NUM_INDICES; i++) {
                    indices[i] = rand.nextInt(MAX_INDEX + 1);
                }

                // 根据随机索引获取对应的 value 并打印
                for (int index : indices) {
                    String value = dataMap.get(Integer.toString(index));
                    lines.add("10.214.242.155:7996/CUB/" + value);
                    System.out.println(index + ": " + value);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            result.setMsg("成功获得CUB池!");
            result.setData(lines);
        }else if(req.getDatasetId()==10006){
            System.out.println("刷新CARS池");
            //现在读取索引随机选择
            int MAX_INDEX = 8143;
            int NUM_INDICES = 36;
            try {
                // 创建ObjectMapper对象，用于解析JSON文件
                ObjectMapper objectMapper = new ObjectMapper();

                // 从JSON文件中读取数据并解析为Map对象
                File jsonFile = new File("/nfs/lhl/OIPDL_codes/cars_idx2pic.json");
                @SuppressWarnings("unchecked")
                Map<String, String> dataMap = objectMapper.readValue(jsonFile, Map.class);

                // 随机生成 NUM_INDICES 个整数索引
                Random rand = new Random();
                int[] indices = new int[NUM_INDICES];
                for (int i = 0; i < NUM_INDICES; i++) {
                    indices[i] = rand.nextInt(MAX_INDEX + 1);
                }

                // 根据随机索引获取对应的 value 并打印
                for (int index : indices) {
                    String value = dataMap.get(Integer.toString(index));
                    lines.add("10.214.242.155:7996/CARS/" + value);
                    System.out.println(index + ": " + value);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            result.setMsg("成功获得CARS池!");
            result.setData(lines);
        }
        return result;
    }



    @ApiOperation("获取概念的随机图像池")
    @CrossOrigin
    @PostMapping("/getConcepts")
    public ResultBean<List<GetConceptRes>> getConcepts(@RequestBody GetDatasetInfoReq req) throws IOException, InterruptedException{
//        System.out.println("req: " + req);
        ResultBean<List<GetConceptRes>> result = new ResultBean<>();
        List<GetConceptRes> concepLists = new ArrayList<GetConceptRes>();
//        ResultBean<List<String>> result = new ResultBean<>();
        System.out.println("刷新概念池");
        //现在读取索引随机选择
        int MAX_INDEX = 119;
        int NUM_INDICES = 6;
        File directory = new File("/nfs/lhl/OIPDL_codes/TCAV/lhl_concepts/");
        File[] files = directory.listFiles();
        for (File file : files) {
            GetConceptRes res = new GetConceptRes();
            res.setConceptName(file.getName());
            List<String> lines = new ArrayList<String>();
//            lines.add(file.getName());
//                System.out.println(file.getName());
            try {
                // 创建ObjectMapper对象，用于解析JSON文件
                ObjectMapper objectMapper = new ObjectMapper();

                // 从JSON文件中读取数据并解析为Map对象
                File jsonFile = new File("/nfs/lhl/OIPDL_codes/TCAV/concept_jsons/" + file.getName() + "_idx2pic.json");
                @SuppressWarnings("unchecked")
                Map<String, String> dataMap = objectMapper.readValue(jsonFile, Map.class);

                // 随机生成 NUM_INDICES 个整数索引
                Random rand = new Random();
                int[] indices = new int[NUM_INDICES];
                for (int i = 0; i < NUM_INDICES; i++) {
                    indices[i] = rand.nextInt(MAX_INDEX + 1);
                }
                // 根据随机索引获取对应的 value 并打印
                for (int index : indices) {
                    String value = dataMap.get(Integer.toString(index));
                    lines.add("10.214.242.155:7996/Concept/" + value);
//                    System.out.println(index + ": " + value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            res.setConceptAddress(lines);
            concepLists.add(res);
        }
        result.setMsg("成功获得概念池!");
        result.setData(concepLists);
    return result;
    }



    @ApiOperation("获取各公开数据集的三个图像以便预览")
    @CrossOrigin
    @PostMapping("/getPublicpreview")
    public ResultBean<List<String>> getPublicpreview(){
//        System.out.println("req: " + req);
        ResultBean<List<String>> result = new ResultBean<>();
        List<String> lines = new ArrayList<String>();
        lines.add("10.214.242.155:7996/Cifar/0_1740.jpg");
        lines.add("10.214.242.155:7996/Cifar/3_10557.jpg");
        lines.add("10.214.242.155:7996/Cifar/7_18534.jpg");
        lines.add("10.214.242.155:7996/originalImgNet/n01798484/n01798484_14557.JPEG");
        lines.add("10.214.242.155:7996/originalImgNet/n01491361/n01491361_3683.JPEG");
        lines.add("10.214.242.155:7996/originalImgNet/n01440764/n01440764_6628.JPEG");
        lines.add("10.214.242.155:7996/originalVOC/2007_000648.jpg");
        lines.add("10.214.242.155:7996/originalVOC/2007_005086.jpg");
        lines.add("10.214.242.155:7996/originalVOC/2007_000836.jpg");
        lines.add("10.214.242.155:7996/cocoimg/000000165012.jpg");
        lines.add("10.214.242.155:7996/cocoimg/000000189617.jpg");
        lines.add("10.214.242.155:7996/cocoimg/000000308175.jpg");
        result.setMsg("成功获得公开数据集预览图像!");
        result.setData(lines);
        return result;
    }

    @ApiOperation("获取各公开数据集的详细展示")
    @CrossOrigin
    @PostMapping("/getPublicDetail")
    public ResultBean<List<String>> getPublicDetail(@RequestBody GetDatasetInfoReq req) throws IOException, InterruptedException{
//        System.out.println("req: " + req);
        ResultBean<List<String>> result = new ResultBean<>();
        List<String> lines = new ArrayList<String>();
        String directoryPath;
        if(req.getDatasetId()==10001){
            System.out.println("获得所有cifar");
            ObjectMapper objectMapper = new ObjectMapper();
            File jsonFile = new File("/nfs/lhl/OIPDL_codes/cifar_idx2pic.json");
            @SuppressWarnings("unchecked")
            Map<String, String> dataMap = objectMapper.readValue(jsonFile, Map.class);
            for (int index=0; index<=49891; index++) {
                String value = dataMap.get(Integer.toString(index));
                lines.add("10.214.242.155:7996/Cifar/" + value);
//                System.out.println(index + ": " + value);
            }
            result.setMsg("成功获得CIFAR!");
            result.setData(lines);
        }else if(req.getDatasetId()==10002){
            System.out.println("获得所有imagenet");
            ObjectMapper objectMapper = new ObjectMapper();
            File jsonFile = new File("/nfs/lhl/OIPDL_codes/imgnet_idx2pic.json");
            @SuppressWarnings("unchecked")
            Map<String, String> dataMap = objectMapper.readValue(jsonFile, Map.class);
            for (int index=0; index<=1281166; index++) {
                String value = dataMap.get(Integer.toString(index));
                lines.add("10.214.242.155:7996/originalImgNet/" + value);
//                System.out.println(index + ": " + value);
            }
            result.setMsg("成功获得imagenet!");
            result.setData(lines);

//            System.out.println("获得所有imagenet");
//            directoryPath = "/nfs/lhl/datasets/ILSVRC2012/train";
//            File directory = new File(directoryPath);
//
//            File[] allFiles = directory.listFiles();
//            for (File file : allFiles) {
////                System.out.println("file: " + file);
////                System.out.println("file.getName(): " + file.getName());
//                File directory2 = new File(directoryPath + "/" + file.getName());
//                File[] dirFiles = directory2.listFiles();
//                for(File file22 : dirFiles){
////                    System.out.println("file22.getName(): " + file22.getName());
//                    lines.add("10.214.242.155:7996/originalImgNet/" + file.getName() + "/" + file22.getName());
////                    System.out.println("10.214.242.155:7996/originalImgNet/" + file.getName() + "/" + file22.getName());
//                }
//            }
//            result.setMsg("成功获得imagenet池!");
//            result.setData(lines);
        }else if(req.getDatasetId()==10003){
            System.out.println("获得所有voc");
            ObjectMapper objectMapper = new ObjectMapper();
            File jsonFile = new File("/nfs/lhl/OIPDL_codes/voc_idx2pic.json");
            @SuppressWarnings("unchecked")
            Map<String, String> dataMap = objectMapper.readValue(jsonFile, Map.class);
            for (int index=0; index<=1463; index++) {
                String value = dataMap.get(Integer.toString(index));
                lines.add("10.214.242.155:7996/originalVOC/" + value);
//                System.out.println(index + ": " + value);
            }
            result.setMsg("成功获得voc!");
            result.setData(lines);
//
//            System.out.println("获得所有VOC");
//            directoryPath = "/nfs/lhl/datasets/VOCdevkit/VOC2012/JPEGImages";
//            File directory = new File(directoryPath);
//            File[] files = directory.listFiles();
//            for (File file : files) {
//                lines.add("10.214.242.155:7996/originalVOC/" + file.getName());
////                System.out.println(file.getName());
//            }
//            result.setMsg("成功获得VOC池!");
//            result.setData(lines);
        }else if(req.getDatasetId()==10004){
            System.out.println("获得所有coco");
            ObjectMapper objectMapper = new ObjectMapper();
            File jsonFile = new File("/nfs/lhl/OIPDL_codes/coco_idx2pic.json");
            @SuppressWarnings("unchecked")
            Map<String, String> dataMap = objectMapper.readValue(jsonFile, Map.class);
            for (int index=0; index<=118286; index++) {
                String value = dataMap.get(Integer.toString(index));
                lines.add("10.214.242.155:7996/originalCoCoimg/" + value);
//                System.out.println(index + ": " + value);
            }
            result.setMsg("成功获得coco!");
            result.setData(lines);
//            System.out.println("这是COCO");
//            directoryPath = "/nfs/lhl/datasets/COCO2017/images/train";
//            File directory = new File(directoryPath);
//            File[] files = directory.listFiles();
//            for (File file : files) {
//                lines.add("10.214.242.155:7996/originalCoCoimg/" + file.getName());
////                System.out.println(file.getName());
//            }
//            result.setMsg("成功获得COCO池!");
//            result.setData(lines);
        }else if(req.getDatasetId()==10005){
            System.out.println("获得所有cub");
            ObjectMapper objectMapper = new ObjectMapper();

            File jsonFile = new File("/nfs/lhl/OIPDL_codes/cub_idx2pic.json");
            @SuppressWarnings("unchecked")
            Map<String, String> dataMap = objectMapper.readValue(jsonFile, Map.class);
            for (int index=0; index<=11787; index++) {
                String value = dataMap.get(Integer.toString(index));
                lines.add("10.214.242.155:7996/CUB/" + value);
//                System.out.println(index + ": " + value);
            }
            result.setMsg("成功获得cub!");
            result.setData(lines);
//            System.out.println("这是COCO");
//            directoryPath = "/nfs/lhl/datasets/COCO2017/images/train";
//            File directory = new File(directoryPath);
//            File[] files = directory.listFiles();
//            for (File file : files) {
//                lines.add("10.214.242.155:7996/originalCoCoimg/" + file.getName());
////                System.out.println(file.getName());
//            }
//            result.setMsg("成功获得COCO池!");
//            result.setData(lines);
        }else if(req.getDatasetId()==10006){
            System.out.println("获得所有cars");
            ObjectMapper objectMapper = new ObjectMapper();
            File jsonFile = new File("/nfs/lhl/OIPDL_codes/cars_idx2pic.json");
            @SuppressWarnings("unchecked")
            Map<String, String> dataMap = objectMapper.readValue(jsonFile, Map.class);
            for (int index=0; index<=8143; index++) {
                String value = dataMap.get(Integer.toString(index));
                lines.add("10.214.242.155:7996/CARS/" + value);
//                System.out.println(index + ": " + value);
            }
            result.setMsg("成功获得cars!");
            result.setData(lines);
//            System.out.println("这是COCO");
//            directoryPath = "/nfs/lhl/datasets/COCO2017/images/train";
//            File directory = new File(directoryPath);
//            File[] files = directory.listFiles();
//            for (File file : files) {
//                lines.add("10.214.242.155:7996/originalCoCoimg/" + file.getName());
////                System.out.println(file.getName());
//            }
//            result.setMsg("成功获得COCO池!");
//            result.setData(lines);
        }else if(req.getDatasetId()==10007){
            System.out.println("获得所有nwpu");
            ObjectMapper objectMapper = new ObjectMapper();
            File jsonFile = new File("/nfs/lhl/OIPDL_codes/nwpu_idx2pic.json");
            @SuppressWarnings("unchecked")
            Map<String, String> dataMap = objectMapper.readValue(jsonFile, Map.class);
            for (int index=0; index<=25199; index++) {
                String value = dataMap.get(Integer.toString(index));
                lines.add("10.214.242.155:7996/NWPU/" + value);
            }
            result.setMsg("成功获得nwpu!");
            result.setData(lines);
        }else if(req.getDatasetId()==10008){
            System.out.println("获得所有food101");
            ObjectMapper objectMapper = new ObjectMapper();
            File jsonFile = new File("/nfs/lhl/OIPDL_codes/food101_idx2pic.json");
            @SuppressWarnings("unchecked")
            Map<String, String> dataMap = objectMapper.readValue(jsonFile, Map.class);
            for (int index=0; index<=75731; index++) {
                String value = dataMap.get(Integer.toString(index));
                lines.add("10.214.242.155:7996/Food101/" + value);
            }
            result.setMsg("成功获得food101!");
            result.setData(lines);
        }else if(req.getDatasetId()==10009){
            System.out.println("获得所有place365");
            ObjectMapper objectMapper = new ObjectMapper();
            File jsonFile = new File("/nfs/lhl/OIPDL_codes/place365_idx2pic.json");
            @SuppressWarnings("unchecked")
            Map<String, String> dataMap = objectMapper.readValue(jsonFile, Map.class);
            for (int index=0; index<=1803460; index++) {
                String value = dataMap.get(Integer.toString(index));
                lines.add("10.214.242.155:7996/Place365/" + value);
            }
            result.setMsg("成功获得food101!");
            result.setData(lines);
        }
        return result;
    }

//    getCifarPool

    @ApiOperation("重命名数据集")
    @CrossOrigin
    @PostMapping("/renameDataset")
    public ResultBean<RenameDatasetRes> renameDataset(@RequestBody RenameDatasetReq req) {
        ResultBean<RenameDatasetRes> result = new ResultBean<>();
        RenameDatasetRes res = new RenameDatasetRes();
        res.setSucceed(iDatasetService.renameDataset(req.getDatasetId(), req.getNewName()));
        if (res.isSucceed()) {
            result.setData(res);
            result.setMsg("修改成功");
        } else {
            result.setMsg("修改失败");
            result.setCode(ResultBean.FAIL);
        }
        return result;
    }

    @ApiOperation("根据名称查询数据集")
    @CrossOrigin
    @PostMapping("/findDatasetByName")
    public ResultBean<List<FindDatasetByUserAndNameRes>> findDataset(@RequestBody FindDatasetByUserAndNameReq req) {
        ResultBean<List<FindDatasetByUserAndNameRes>> result = new ResultBean<>();
        List<FindDatasetByUserAndNameRes> tmpresult = new ArrayList<>();
        if (!StringUtils.hasText(req.getDatasetName())) {
            result.setCode(ResultBean.FAIL);
            result.setMsg("输入数据集名称不合法！");
            result.setData(null);
            return result;
        }
        FindDatasetByUserAndNameRes res = new FindDatasetByUserAndNameRes();
        res.setDatasetInfo(iDatasetService.findDatasetByUserAndName(req.getUsername(), req.getDatasetName()));
        int datasetId = res.getDatasetInfo().getDatasetId();
        List<PictureDataInfo> pictureDataInfos = iPictureDataService.getPictures(datasetId);
        if (pictureDataInfos.size()>=10){
            pictureDataInfos = pictureDataInfos.subList(0, 9);
        }
        res.setPictureDataInfos(pictureDataInfos);
        if (res.getDatasetInfo() == null) {
            result.setCode(ResultBean.FAIL);
            result.setMsg("不存在该数据集");
            result.setData(null);
        } else {
            result.setMsg("查找成功，数据集名称为：" + res.getDatasetInfo().getDatasetName());
            tmpresult.add(res);
            result.setData(tmpresult);
        }
        return result;
    }
    @ApiOperation("根据名称模糊查询数据集")
    @CrossOrigin
    @PostMapping("/blurredFindDatasetByName")
    public ResultBean<List<FindDatasetByUserAndNameRes>> blurredFindDataset(@RequestBody FindDatasetByUserAndNameReq req) {
        ResultBean<List<FindDatasetByUserAndNameRes>> result = new ResultBean<>();
        List<FindDatasetByUserAndNameRes> tmpresult = new ArrayList<>();
        if (!StringUtils.hasText(req.getDatasetName())) {
            result.setCode(ResultBean.FAIL);
            result.setMsg("输入数据集名称不合法！");
            result.setData(null);
            return result;
        }

        UserInfo userInfo = iUserService.findByUsername(req.getUsername());
        Integer authority = userInfo.getAuthority();
        List<DatasetInfo> datasetInfos;
        if(authority==0){
            datasetInfos = iDatasetService.blurredFindDatasetByUserAndName(req.getUsername(), req.getDatasetName());
        }else{
            datasetInfos = iDatasetService.blurredFindDatasetByName(req.getDatasetName());
        }
//        List<DatasetInfo>
        for(DatasetInfo d: datasetInfos){
            FindDatasetByUserAndNameRes res = new FindDatasetByUserAndNameRes();
            res.setDatasetInfo(d);
            int datasetId = d.getDatasetId();
            List<PictureDataInfo> pictureDataInfos = iPictureDataService.getPictures(datasetId);
            if (pictureDataInfos.size()>=10){
                pictureDataInfos = pictureDataInfos.subList(0, 9);
            }
            res.setPictureDataInfos(pictureDataInfos);
            tmpresult.add(res);
        }
        if (datasetInfos.isEmpty()) {
            result.setCode(ResultBean.FAIL);
            result.setMsg("不存在该数据集");
            result.setData(null);
        } else {
            result.setMsg("查找成功");
            result.setData(tmpresult);
        }
        return result;
    }
}
