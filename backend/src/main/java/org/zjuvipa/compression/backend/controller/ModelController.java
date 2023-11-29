package org.zjuvipa.compression.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.zjuvipa.compression.util.ResultBean;
import org.zjuvipa.model.info.AlgorithmInfo;
import org.zjuvipa.model.info.DatasetInfo;
import org.zjuvipa.model.info.ModelInfo;
import org.zjuvipa.model.info.UserInfo;
import org.zjuvipa.model.req.*;
import org.zjuvipa.model.res.AddModelRes;
import org.zjuvipa.model.res.FindModelRes;
import org.zjuvipa.model.res.ModelRes;
import org.zjuvipa.compression.service.IAlgorithmService;
import org.zjuvipa.compression.service.IDatasetService;
import org.zjuvipa.compression.service.IModelService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
@Api(tags = "模型")
@RestController
@RequestMapping("/model")
public class ModelController {
    @Autowired
    IModelService iModelService;

    @Autowired
    IAlgorithmService iAlgorithmService;

    @Autowired
    IDatasetService iDatasetService;

    @Resource
    private HttpServletResponse response;

    @Resource
    private HttpServletRequest request;

//    @CrossOrigin
//    @ApiOperation("添加模型")
//    @PostMapping("add")
//    public ResultBean<AddModelRes> addModel (@RequestBody AddModelReq modelReq){
//        ResultBean<AddModelRes> result = new ResultBean<>();
//        if(!StringUtils.hasText(modelReq.getModelName())){
//            result.setCode(ResultBean.FAIL);
//            result.setMsg("输入模型名称不合法！");
//            result.setData(null);
//            return result;
//        }
//        AddModelRes addModelRes = new AddModelRes();
//        addModelRes.setModelInfo(iModelService.findModelByName(modelReq.getUsername(),modelReq.getModelName()));
//        if(addModelRes.getModelInfo()==null){
//            ModelInfo modelInfo = new ModelInfo();
//            modelInfo.setModelName(modelReq.getModelName());
//            modelInfo.setUsername(modelReq.getUsername());
//            AlgorithmInfo algorithmInfo = iAlgorithmService.findAlgoByName(modelReq.getAlgorithm());
//            if(algorithmInfo == null){
//                result.setCode(ResultBean.FAIL);
//                result.setMsg("算法不存在");
//                result.setData(null);
//                return result;
//            }
////            modelInfo.setAlgoId(algorithmInfo.getAlgoId());
////            modelInfo.setAlgoName(algorithmInfo.getAlgoName());
//            modelInfo.setUsername(modelReq.getUsername());
//            DatasetInfo datasetInfo = iDatasetService.getDatasetInfo(modelReq.getDatasetId());
//            if(datasetInfo == null){
//                result.setCode(ResultBean.FAIL);
//                result.setMsg("数据集不存在");
//                result.setData(null);
//                return result;
//            }
//            if(datasetInfo.getDatasetIspublic() != 1){
//                result.setCode(ResultBean.NO_PERMISSION);
//                result.setMsg("非公开数据集");
//                result.setData(null);
//                return result;
//            }
//            modelInfo.setDatasetId(datasetInfo.getDatasetId());
//            modelInfo.setDatasetName(datasetInfo.getDatasetName());
////            modelInfo.setModelPath(modelReq.getModelPath());
//            modelInfo.setModelPath("10.214.242.155:8888/3dmodel/car.glb");
//            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//            Date date = new Date();
//            String day = df.format(date);
//            modelInfo.setDateTime(day);
//            //modelpath应该是用户传入的参数，callAlgotithm之后保存文件，这里暂时不处理
//            int num = iModelService.addModel(modelInfo);
//            addModelRes.setModelInfo(modelInfo);
//            if(num > 0){
//                result.setMsg("模型插入成功！");
//                result.setData(addModelRes);
//            }
//            else{
//                result.setMsg("模型插入失败");
//                result.setCode(ResultBean.FAIL);
//                result.setData(null);
//            }
//        }
//        else{
//            result.setData(null);
//            result.setMsg("同一用户下模型名称重复，请重新命名！");
//            result.setCode(ResultBean.FAIL);
//        }
//        return result;
//    }




    @CrossOrigin
    @ApiOperation("添加可解释模型")
    @PostMapping("addOipdlModel")
    public ResultBean<AddModelRes> addOipdlModel (@RequestBody AddModelReq modelReq){
        ResultBean<AddModelRes> result = new ResultBean<>();
        if(!StringUtils.hasText(modelReq.getModelName())){
            result.setCode(ResultBean.FAIL);
            result.setMsg("输入模型名称不合法！");
            result.setData(null);
            return result;
        }
        AddModelRes addModelRes = new AddModelRes();
        addModelRes.setModelInfo(iModelService.findModelByName(modelReq.getUsername(),modelReq.getModelName()));
        if(addModelRes.getModelInfo()==null){
            ModelInfo modelInfo = new ModelInfo();
            modelInfo.setModelName(modelReq.getModelName());
            modelInfo.setUsername(modelReq.getUsername());
            //我们不设计算法库
//            AlgorithmInfo algorithmInfo = iAlgorithmService.findAlgoByName(modelReq.getAlgorithm());
//            if(algorithmInfo == null){
//                result.setCode(ResultBean.FAIL);
//                result.setMsg("算法不存在");
//                result.setData(null);
//                return result;
//            }
            modelInfo.setAlgoId(1008601);
            modelInfo.setAlgoName(modelReq.getAlgorithm());
            modelInfo.setUsername(modelReq.getUsername());
//            DatasetInfo datasetInfo = iDatasetService.getDatasetInfo(modelReq.getDatasetId());
//            if(datasetInfo == null){
//                result.setCode(ResultBean.FAIL);
//                result.setMsg("数据集不存在");
//                result.setData(null);
//                return result;
//            }
//            if(datasetInfo.getDatasetIspublic() != 1){
//                result.setCode(ResultBean.NO_PERMISSION);
//                result.setMsg("非公开数据集");
//                result.setData(null);
//                return result;
//            }
            modelInfo.setDatasetId(1008601);
            modelInfo.setDatasetName("CUB");
//            modelInfo.setModelPath(modelReq.getModelPath());
            modelInfo.setModelPath(modelReq.getModelPath());
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String day = df.format(date);
            modelInfo.setDateTime(day);
            //modelpath应该是用户传入的参数，callAlgotithm之后保存文件，这里暂时不处理
            int num = iModelService.addModel(modelInfo);
            addModelRes.setModelInfo(modelInfo);
            if(num > 0){
                result.setMsg("模型插入成功！");
                result.setData(addModelRes);
            }
            else{
                result.setMsg("模型插入失败");
                result.setCode(ResultBean.FAIL);
                result.setData(null);
            }
        }
        else{
            result.setData(null);
            result.setMsg("同一用户下模型名称重复，请重新命名！");
            result.setCode(ResultBean.FAIL);
        }
        return result;
    }


//取消了cookie
// , @CookieValue("userTicket")String ticket

    @CrossOrigin
    @ApiOperation("展示所有模型")
    @PostMapping("findAll")
    public ResultBean<FindModelRes> findModelByUser(@RequestBody FindModelsByUserReq req) {
        HttpSession httpSession = request.getSession();
        ResultBean<FindModelRes> result = new ResultBean<>();
//        UserInfo userInfo = (UserInfo)httpSession.getAttribute(ticket);

        //之前的写法
//        if(userInfo == null || !StringUtils.hasText(ticket)) {
//            result.setMsg("用户未登录，请跳转login页面");
//            result.setCode(ResultBean.NO_PERMISSION);
//            result.setData(null);
//            return result;
//        }
        //不知道这段是干啥的，注释了
//        if(userInfo.getAuthority() > 0 && !req.getUsername().equals(userInfo.getUsername())){//非管理员无权限查看别的用户的历史记录
//            result.setMsg("无权限查看该用户历史记录！");
//            result.setCode(ResultBean.FAIL);
//            result.setData(null);
//            return result;
//        }
        FindModelRes models = new FindModelRes();
        models.setModelInfos(iModelService.findModelsByUser(req.getUsername()));
        if(models.getModelInfos() != null) {
            result.setMsg("查询成功！共"+models.getModelInfos().size()+"条记录");
            result.setData(models);
        }
        else {
            result.setMsg("查询失败！共0条记录");
            result.setCode(ResultBean.FAIL);
            result.setData(null);
        }
        return result;
    }

//    @CrossOrigin
//    @ApiOperation("根据算法查询模型")
//    @PostMapping("findByaAlgo")
//    public ResultBean<FindModelRes> findModelByUserAndAlgo(@RequestBody FindModelByUserAndAlgoReq req, @CookieValue("userTicket")String ticket){
//        ResultBean<FindModelRes> result = new ResultBean<>();
//        HttpSession httpSession = request.getSession();
//        UserInfo userInfo = (UserInfo)httpSession.getAttribute(ticket);
//        if(userInfo == null || !StringUtils.hasText(ticket)) {
//            result.setMsg("用户未登录，请跳转login页面");
//            result.setCode(ResultBean.NO_PERMISSION);
//            result.setData(null);
//            return result;
//        }
//        if(userInfo.getAuthority() > 0 && !req.getUsername().equals(userInfo.getUsername())){//非管理员无权限查看别的用户的历史记录
//            result.setMsg("无权限查看该用户历史记录！");
//            result.setCode(ResultBean.FAIL);
//            result.setData(null);
//            return result;
//        }
//        FindModelRes models = new FindModelRes();
//        AlgorithmInfo algorithmInfo = iAlgorithmService.findAlgoByName(req.getAlgorithm());
//        if(algorithmInfo == null){
//            result.setCode(ResultBean.FAIL);
//            result.setMsg("算法不存在");
//            result.setData(null);
//            return result;
//        }
////        models.setModelInfos(iModelService.findModelsByUserAndAlgo(req.getUsername(), algorithmInfo.getAlgoId()));
//        if(models.getModelInfos() != null) {
//            result.setMsg("查询成功！共"+models.getModelInfos().size()+"条记录");
//            result.setData(models);
//        }
//        else {
//            result.setMsg("查询失败！共0条记录");
//            result.setCode(ResultBean.FAIL);
//            result.setData(null);
//        }
//        return result;
//    }

    @CrossOrigin
    @ApiOperation("根据数据集查询模型")
    @PostMapping("findByDataset")
    public ResultBean<FindModelRes> findModelByUserAndDataset(@RequestBody FindModelByUserAndDatasetReq req, @CookieValue("userTicket")String ticket){
        ResultBean<FindModelRes> result = new ResultBean<>();
        HttpSession httpSession = request.getSession();
        UserInfo userInfo = (UserInfo)httpSession.getAttribute(ticket);
        if(userInfo == null || !StringUtils.hasText(ticket)) {
            result.setMsg("用户未登录，请跳转login页面");
            result.setCode(ResultBean.NO_PERMISSION);
            result.setData(null);
            return result;
        }
        if(userInfo.getAuthority() > 0 && !req.getUsername().equals(userInfo.getUsername())){//非管理员无权限查看别的用户的历史记录
            result.setMsg("无权限查看该用户历史记录！");
            result.setCode(ResultBean.FAIL);
            result.setData(null);
            return result;
        }
        DatasetInfo datasetInfo = iDatasetService.getDatasetInfo(req.getDatasetId());
        if(datasetInfo == null){
            result.setCode(ResultBean.FAIL);
            result.setMsg("数据集不存在");
            result.setData(null);
            return result;
        }
        FindModelRes models = new FindModelRes();
        models.setModelInfos(iModelService.findModelsByUserAndDataset(req.getUsername(), datasetInfo.getDatasetId()));
        if(models.getModelInfos() != null) {
            result.setMsg("查询成功！共"+models.getModelInfos().size()+"条记录");
            result.setData(models);
        }
        else {
            result.setMsg("查询失败！共0条记录");
            result.setCode(ResultBean.FAIL);
            result.setData(null);
        }
        return result;
    }

//    @CrossOrigin
//    @ApiOperation("根据算法和数据集查询模型")
//    @PostMapping("findByALgoAndDataset")
//    public ResultBean<FindModelRes> findModelByUserAndAlgoAndDataset(@RequestBody FindModelByUserAndAlgoAndDataset req, @CookieValue("userTicket")String ticket){
//        ResultBean<FindModelRes> result = new ResultBean<>();
//        HttpSession httpSession = request.getSession();
//        UserInfo userInfo = (UserInfo)httpSession.getAttribute(ticket);
//        if(userInfo == null || !StringUtils.hasText(ticket)) {
//            result.setMsg("用户未登录，请跳转login页面");
//            result.setCode(ResultBean.NO_PERMISSION);
//            result.setData(null);
//            return result;
//        }
//        if(userInfo.getAuthority() > 0 && !req.getUsername().equals(userInfo.getUsername())){//非管理员无权限查看别的用户的历史记录
//            result.setMsg("无权限查看该用户历史记录！");
//            result.setCode(ResultBean.FAIL);
//            result.setData(null);
//            return result;
//        }
//        AlgorithmInfo algorithmInfo = iAlgorithmService.findAlgoByName(req.getAlgorithm());
//        if(algorithmInfo == null){
//            result.setCode(ResultBean.FAIL);
//            result.setMsg("算法不存在");
//            result.setData(null);
//            return result;
//        }
//        DatasetInfo datasetInfo = iDatasetService.getDatasetInfo(req.getDatasetId());
//        if(datasetInfo == null){
//            result.setCode(ResultBean.FAIL);
//            result.setMsg("数据集不存在");
//            result.setData(null);
//            return result;
//        }
//        FindModelRes models = new FindModelRes();
////        models.setModelInfos(iModelService.findModelsByUserAndAlgoAndDataset(req.getUsername(), algorithmInfo.getAlgoId(), datasetInfo.getDatasetId()));
//        if(models.getModelInfos() != null) {
//            result.setMsg("查询成功！共"+models.getModelInfos().size()+"条记录");
//            result.setData(models);
//        }
//        else {
//            result.setMsg("查询失败！共0条记录");
//            result.setCode(ResultBean.FAIL);
//            result.setData(null);
//        }
//        return result;
//    }

    @CrossOrigin
    @ApiOperation("删除模型")
    @PostMapping("delete")
    public ResultBean<AddModelRes> deleteModel(@RequestBody DeleteModelReq req){
        ResultBean<AddModelRes> result = new ResultBean<>();
        AddModelRes addModelRes = new AddModelRes();
        addModelRes.setModelInfo(iModelService.findModelByName(req.getUsername(),req.getModelName()));
        if(addModelRes.getModelInfo() == null){
            result.setCode(ResultBean.FAIL);
            result.setMsg("该条历史记录不存在");
            result.setData(null);
        }
        else{
            boolean flag = iModelService.deleteModel(req.getUsername(), req.getModelName());
            if(flag){
                result.setMsg("删除历史记录成功");
                result.setData(addModelRes);
            }
            else{
                result.setCode(ResultBean.FAIL);
                result.setMsg("删除历史记录失败");
                result.setData(null);
            }
        }
        return result;
    }

    @CrossOrigin
    @ApiOperation("更新模型")
    @PostMapping("update")
    public ResultBean<AddModelRes> updateModel(@RequestBody UpdateModelReq req){
        ResultBean<AddModelRes> result = new ResultBean<>();
        AddModelRes res = new AddModelRes();
        ModelInfo modelInfo = iModelService.findModelByName(req.getUsername(), req.getModelName());
        if(modelInfo == null) {
            result.setMsg("历史记录不存在");
            result.setData(null);
            result.setCode(ResultBean.FAIL);
            return result;
        }
        res.setModelInfo(iModelService.findModelByName(req.getUsername(), req.getNewName()));
        if(res.getModelInfo() == null) {
            boolean flag = iModelService.updateModel(modelInfo.getModelId(), req.getNewName());
            if(flag){
                modelInfo.setModelName(req.getNewName());
                res.setModelInfo(modelInfo);
                result.setData(res);
                result.setMsg("修改模型名称成功");
            }
            else{
                result.setMsg("修改失败");
                result.setData(null);
                result.setCode(ResultBean.FAIL);
            }
        }
        else{
            result.setMsg("同一用户下模型名称重复！");
            result.setData(null);
            result.setCode(ResultBean.FAIL);
        }

        return result;
    }

//
//    @CrossOrigin
//    @ApiOperation("下载模型")
//    @PostMapping("/downloadModel")
//    public ResultBean<ModelRes> download(@RequestBody DownloadModelReq req) {
//        ResultBean<ModelRes> result = new ResultBean<>();
//        ModelRes modelRes =  new ModelRes();
//        modelRes.setModelInfo(iModelService.findModelByName(req.getUsername(), req.getModel()));
//        if(modelRes.getModelInfo() == null) {
//            result.setCode(ResultBean.FAIL);
//            result.setData(null);
//            result.setMsg("该条历史记录不存在！");
//        }
//        else {
//            //文件本地位置
//            String filePath = modelRes.getModelInfo().getModelPath();
//            int pos = filePath.lastIndexOf(".");
//            if(pos == -1){
//                result.setCode(ResultBean.FAIL);
//                result.setData(null);
//                result.setMsg("模型存储地址错误！");
//            }
//            else{
////        String filePath ="F:\\java\\Dam-Backend\\src\\main\\resources\\static\\model\\test.txt";
//                // 文件名称
//                String fileName = filePath.substring(pos);
//                File file = new File(filePath);
//                FileUtil.downloadFile(file, request, response, fileName);
//                result.setMsg("下载成功");
//                result.setData(modelRes);
//            }
//        }
//        return result;
//    }
//
//    @CrossOrigin
//    @ApiOperation("下载多个模型")
//    @PostMapping(value = "/downZip")
//    public ResultBean<ModelRes> downloadZipStream(@RequestBody DownloadModelReq req) {
//        ResultBean<ModelRes> result = new ResultBean<>();
//        ModelRes modelRes =  new ModelRes();
//        modelRes.setModelInfo(iModelService.findModelByName(req.getUsername(), req.getModel()));
//        if(modelRes.getModelInfo() == null) {
//            result.setCode(ResultBean.FAIL);
//            result.setData(null);
//            result.setMsg("该条历史记录不存在！");
//            return result;
//        }
//        //文件本地位置
//        String basePath = modelRes.getModelInfo().getModelPath();
//        List<Map<String, String>> mapList = new ArrayList<>();
//        mapList = getFiles(basePath);
//        System.out.println(mapList);
//        FileUtil.zipDirFileToFile(mapList, request, response);
//        result.setMsg("下载成功");
//        result.setData(modelRes);
//        return result;
//    }
//
//    public static List<Map<String, String>> getFiles(String path) {
//        List<Map<String, String>> mapList = new ArrayList<>();
//        File file = new File(path);
//        // 如果这个路径是文件夹
//        if (file.isDirectory()) {
//            // 获取路径下的所有文件
//            File[] files = file.listFiles();
//            for (int i = 0; i < files.length; i++) {
//                // 如果还是文件夹 递归获取里面的文件 文件夹
//                if (files[i].isDirectory()) {
//                    System.out.println("目录：" + files[i].getPath());
//                    mapList.addAll(getFiles(files[i].getPath()));
//
//                } else {
//                    Map<String, String> map = new HashMap<>();
//                    String fileName = files[i].getName();
//                    map.put("path", path+File.separator+fileName);
//                    map.put("name", fileName);
//                    mapList.add(map);
//                    System.out.println("文件：" + files[i].getName()); // files[i].getPath());
//                }
//            }
//
//        } else {
//            Map<String, String> map = new HashMap<>();
//            map.put("path",  file.getPath());
//            map.put("name", file.getName());
//            mapList.add(map);
//            System.out.println("文件：" + file.getPath());
//
//        }
//        return mapList;
//    }
}
