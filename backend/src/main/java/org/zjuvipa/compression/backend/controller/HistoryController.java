package org.zjuvipa.compression.backend.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
//import org.zjuvipa.config.Constant;
import org.zjuvipa.compression.backend.service.*;
import org.zjuvipa.compression.backend.service.*;
import org.zjuvipa.compression.common.util.ResultBean;
import org.zjuvipa.compression.backend.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.zjuvipa.compression.model.entity.History;
import org.zjuvipa.compression.model.info.*;
import org.zjuvipa.compression.model.req.FindHistoriesByUserReq;
//import org.zjuvipa.compression.model.res.AddHistoryRes;
import org.zjuvipa.compression.model.res.FindHistoryRes;
import org.zjuvipa.compression.model.res.FindInfoRes;
import org.zjuvipa.compression.model.res.PagedFindHistoryRes;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
@Api(tags = "历史记录")
@RestController
@RequestMapping("/history")
public class HistoryController {
    @Autowired
    IHistoryService iHistoryService;

    @Autowired
    IAlgorithmService iAlgorithmService;

    @Autowired
    IDatasetService iDatasetService;

    @Autowired
    ICrackService iCrackService;

    @Autowired
    IPictureDataService iPictureDataService;

    @Resource
    private HttpServletResponse response;

    @Resource
    private HttpServletRequest request;

//    @CrossOrigin
//    @ApiOperation("添加历史记录")
//    @PostMapping("add")
//    public ResultBean<AddHistoryRes> addHistory (@RequestBody AddHistoryReq historyReq){
//        ResultBean<AddHistoryRes> result = new ResultBean<>();
//        if(!StringUtils.hasText(historyReq.getHistoryName())){
//            result.setCode(ResultBean.FAIL);
//            result.setMsg("输入历史记录名称不合法！");
//            result.setData(null);
//            return result;
//        }
//        AddHistoryRes addHistoryRes = new AddHistoryRes();
//        addHistoryRes.setHistoryInfo(iHistoryService.findHistoryByName(historyReq.getUsername(),historyReq.getHistoryName()));
//        if(addHistoryRes.getHistoryInfo()==null){
//            HistoryInfo historyInfo = new HistoryInfo();
//            historyInfo.setHistoryName(historyReq.getHistoryName());
//            historyInfo.setUsername(historyReq.getUsername());
//            AlgorithmInfo algorithmInfo = iAlgorithmService.findAlgoByName(historyReq.getAlgorithm());
//            if(algorithmInfo == null){
//                result.setCode(ResultBean.FAIL);
//                result.setMsg("算法不存在");
//                result.setData(null);
//                return result;
//            }
//            historyInfo.setAlgoId(algorithmInfo.getAlgoId());
//            historyInfo.setAlgoName(algorithmInfo.getAlgoName());
//            historyInfo.setUsername(historyReq.getUsername());
//            DatasetInfo datasetInfo = iDatasetService.getDatasetInfo(historyReq.getDatasetId());
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
//            historyInfo.setDatasetId(datasetInfo.getDatasetId());
//            historyInfo.setDatasetName(datasetInfo.getDatasetName());
//            historyInfo.setModelPath(Constant.HOST + ":" + Constant.PORT + "/3dmodel/car.glb");
//            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//            Date date = new Date();
//            String day = df.format(date);
//            historyInfo.setDateTime(day);
//            //modelpath应该是用户传入的参数，callAlgotithm之后保存文件，这里暂时不处理
//            int num = iHistoryService.addHistory(historyInfo);
//            addHistoryRes.setHistoryInfo(historyInfo);
//            if(num > 0){
//                result.setMsg("历史记录插入成功！");
//                result.setData(addHistoryRes);
//            }
//            else{
//                result.setMsg("历史记录插入失败");
//                result.setCode(ResultBean.FAIL);
//                result.setData(null);
//            }
//        }
//        else{
//            result.setData(null);
//            result.setMsg("同一用户下历史记录名称重复，请重新命名！");
//            result.setCode(ResultBean.FAIL);
//        }
//        return result;
//    }

    @CrossOrigin
    @ApiOperation("展示某一用户申请加入排行榜的历史记录")
    @PostMapping("findHistoryByUserNew")
    public ResultBean<FindInfoRes> findHistoryByUserNew(@RequestBody FindHistoriesByUserReq req) {
        HttpSession httpSession = request.getSession();
        ResultBean<FindInfoRes> result = new ResultBean<>();
//        UserInfo userInfo = (UserInfo)httpSession.getAttribute(ticket);
//        if(userInfo == null || !StringUtils.hasText(ticket)) {
//            System.out.println("用户未登录，请跳转login页面");
//            result.setMsg("用户未登录，请跳转login页面");
//            result.setCode(ResultBean.NO_PERMISSION);
//            result.setData(null);
//            return result;
//        }else{
//            System.out.println("用户已登录，可以进行查询");
//        }
        FindInfoRes histories = new FindInfoRes();
        histories.setInfoInfos(iHistoryService.findHistoriesByUserNew(req.getUsername()));  //new
        if(histories.getInfoInfos() != null) {
            result.setMsg("查询成功！共"+histories.getInfoInfos().size()+"条记录");
            result.setData(histories);
        }
        else {
            result.setMsg("查询失败！共0条记录");
            result.setCode(ResultBean.FAIL);
            result.setData(null);
        }
        return result;
    }

    @CrossOrigin
    @ApiOperation("展示某一用户的所有历史记录")
    @PostMapping("findHistoryByUser/{pageNum}/{pageSize}")
    public ResultBean<PagedFindHistoryRes> findHistoryByUser(@PathVariable("pageNum") Integer pageNum,
                                                             @PathVariable("pageSize") Integer pageSize,
                                                             @RequestBody FindHistoriesByUserReq req,
                                                             @RequestHeader(name = "access") String access) {
        HttpSession httpSession = request.getSession();
        ResultBean<PagedFindHistoryRes> result = new ResultBean<>();
        PagedFindHistoryRes histories = new PagedFindHistoryRes();

//        UserInfo userInfo = (UserInfo)httpSession.getAttribute(ticket);
//
//        if(userInfo == null || !StringUtils.hasText(ticket)) {
//            System.out.println("用户未登录，请跳转login页面");
//            result.setMsg("用户未登录，请跳转login页面");
//            result.setCode(ResultBean.NO_PERMISSION);
//            result.setData(null);
//        }
        PageInfo<History> res;
        if(access.equals("0")){
            System.out.println("管理员的查询");
            res = iHistoryService.findHistories(pageNum,pageSize,req);
        }else if(access.equals("1")){
            System.out.println("普通用户的查询");
            res = iHistoryService.findHistoriesByUser(pageNum,pageSize,req);
        }else{
            System.out.println("出bug了");
            res = null;
        }

//            System.out.println("结果："+res);
        histories.setHistoryInfos(res);
        if(histories.getHistoryInfos() != null) {
            result.setMsg("查询成功！");
//                result.setMsg("查询成功！共"+histories.getHistoryInfos().size()+"条记录");
            result.setData(histories);
        }
        else {
            result.setMsg("查询失败！共0条记录");
            result.setCode(ResultBean.FAIL);
            result.setData(null);
        }
        return result;
    }

    @CrossOrigin
    @ApiOperation("展示所有历史记录")
    @PostMapping("findAll")
    public ResultBean<FindInfoRes> findAll(@RequestBody FindHistoriesByUserReq req) {
        HttpSession httpSession = request.getSession();
        ResultBean<FindInfoRes> result = new ResultBean<>();
//        UserInfo userInfo = (UserInfo)httpSession.getAttribute(ticket);
//        if(userInfo == null || !StringUtils.hasText(ticket)) {
//            System.out.println("用户未登录，请跳转login页面");
//            result.setMsg("用户未登录，请跳转login页面");
//            result.setCode(ResultBean.NO_PERMISSION);
//            result.setData(null);
//            return result;
//        }else{
//            System.out.println("用户已登录，可以进行查询");
//        }


//        if(userInfo.getAuthority() > 0 && !req.getUsername().equals(userInfo.getUsername())){//非管理员无权限查看别的用户的历史记录
//            System.out.println("无权限查看该用户历史记录！");
//            result.setMsg("无权限查看该用户历史记录！");
//            result.setCode(ResultBean.FAIL);
//            result.setData(null);
//            return result;
//        }

//        iAlgorithmService

        FindInfoRes infos = new FindInfoRes();
//        histories.setHistoryInfos(iHistoryService.findHistoriesByUser(req.getUsername()));
        infos.setInfoInfos(iHistoryService.findAllHistories(req.getUsername()));

        if(infos.getInfoInfos() != null) {
            result.setMsg("查询成功！共"+infos.getInfoInfos().size()+"条记录");
            result.setData(infos);
        }
        else {
            result.setMsg("查询失败！共0条记录");
            result.setCode(ResultBean.FAIL);
            result.setData(null);
        }
        return result;
    }

//    @CrossOrigin
//    @ApiOperation("根据算法查询历史记录")
//    @PostMapping("findByaAlgo")
//    public ResultBean<FindHistoryRes> findHistoryByUserAndAlgo(@RequestBody FindHistoryByUserAndAlgoReq req, @CookieValue("userTicket")String ticket){
//        ResultBean<FindHistoryRes> result = new ResultBean<>();
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
//        FindHistoryRes histories = new FindHistoryRes();
//        AlgorithmInfo algorithmInfo = iAlgorithmService.findAlgoByName(req.getAlgorithm());
//        if(algorithmInfo == null){
//            result.setCode(ResultBean.FAIL);
//            result.setMsg("算法不存在");
//            result.setData(null);
//            return result;
//        }
//        histories.setHistoryInfos(iHistoryService.findHistoriesByUserAndAlgo(req.getUsername(), algorithmInfo.getAlgoId()));
//        if(histories.getHistoryInfos() != null) {
//            result.setMsg("查询成功！共"+histories.getHistoryInfos().size()+"条记录");
//            result.setData(histories);
//        }
//        else {
//            result.setMsg("查询失败！共0条记录");
//            result.setCode(ResultBean.FAIL);
//            result.setData(null);
//        }
//        return result;
//    }
//
//    @CrossOrigin
//    @ApiOperation("根据数据集查询历史记录")
//    @PostMapping("findByDataset")
//    public ResultBean<FindHistoryRes> findHistoryByUserAndDataset(@RequestBody FindHistoryByUserAndDatasetReq req, @CookieValue("userTicket")String ticket){
//        ResultBean<FindHistoryRes> result = new ResultBean<>();
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
//        DatasetInfo datasetInfo = iDatasetService.getDatasetInfo(req.getDatasetId());
//        if(datasetInfo == null){
//            result.setCode(ResultBean.FAIL);
//            result.setMsg("数据集不存在");
//            result.setData(null);
//            return result;
//        }
//        FindHistoryRes histories = new FindHistoryRes();
//        histories.setHistoryInfos(iHistoryService.findHistoriesByUserAndDataset(req.getUsername(), datasetInfo.getDatasetId()));
//        if(histories.getHistoryInfos() != null) {
//            result.setMsg("查询成功！共"+histories.getHistoryInfos().size()+"条记录");
//            result.setData(histories);
//        }
//        else {
//            result.setMsg("查询失败！共0条记录");
//            result.setCode(ResultBean.FAIL);
//            result.setData(null);
//        }
//        return result;
//    }
//
//    @CrossOrigin
//    @ApiOperation("根据算法和数据集查询历史记录")
//    @PostMapping("findByALgoAndDataset")
//    public ResultBean<FindHistoryRes> findHistoryByUserAndAlgoAndDataset(@RequestBody FindHistoryByUserAndAlgoAndDataset req, @CookieValue("userTicket")String ticket){
//        ResultBean<FindHistoryRes> result = new ResultBean<>();
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
//        FindHistoryRes histories = new FindHistoryRes();
//        histories.setHistoryInfos(iHistoryService.findHistoriesByUserAndAlgoAndDataset(req.getUsername(), algorithmInfo.getAlgoId(), datasetInfo.getDatasetId()));
//        if(histories.getHistoryInfos() != null) {
//            result.setMsg("查询成功！共"+histories.getHistoryInfos().size()+"条记录");
//            result.setData(histories);
//        }
//        else {
//            result.setMsg("查询失败！共0条记录");
//            result.setCode(ResultBean.FAIL);
//            result.setData(null);
//        }
//        return result;
//    }
//
//    @CrossOrigin
//    @ApiOperation("删除历史记录")
//    @PostMapping("delete")
//    public ResultBean<AddHistoryRes> deleteHistory(@RequestBody DeleteHistoryReq req){
//        ResultBean<AddHistoryRes> result = new ResultBean<>();
//        AddHistoryRes addHistoryRes = new AddHistoryRes();
//        addHistoryRes.setHistoryInfo(iHistoryService.findHistoryByName(req.getUsername(),req.getHistoryName()));
//        if(addHistoryRes.getHistoryInfo() == null){
//            result.setCode(ResultBean.FAIL);
//            result.setMsg("该条历史记录不存在");
//            result.setData(null);
//        }
//        else{
//            boolean flag = iHistoryService.deleteHistory(req.getUsername(), req.getHistoryName());
//            List<PictureDataInfo> pictureDataInfos = iPictureDataService.getPictures(addHistoryRes.getHistoryInfo().getDatasetId());
//            for(PictureDataInfo pictureDataInfo: pictureDataInfos){
//                flag &= iCrackService.deleteCrackByPictureId(pictureDataInfo.getPictureId());
//            }
//            if(flag){
//                result.setMsg("删除历史记录成功");
//                result.setData(addHistoryRes);
//            }
//            else{
//                result.setCode(ResultBean.FAIL);
//                result.setMsg("删除历史记录失败");
//                result.setData(null);
//            }
//        }
//        return result;
//    }

//    @CrossOrigin
//    @ApiOperation("更新历史记录")
//    @PostMapping("update")
//    public ResultBean<AddHistoryRes> updateHistory(@RequestBody UpdateHistoryReq req){
//        ResultBean<AddHistoryRes> result = new ResultBean<>();
//        AddHistoryRes res = new AddHistoryRes();
//        HistoryInfo historyInfo = iHistoryService.findHistoryByName(req.getUsername(), req.getHistoryName());
//        if(historyInfo == null) {
//            result.setMsg("历史记录不存在");
//            result.setData(null);
//            result.setCode(ResultBean.FAIL);
//            return result;
//        }
//        res.setHistoryInfo(iHistoryService.findHistoryByName(req.getUsername(), req.getNewName()));
//        if(res.getHistoryInfo() == null) {
//            boolean flag = iHistoryService.updateHistory(historyInfo.getHistoryId(), req.getNewName());
//            if(flag){
//                historyInfo.setHistoryName(req.getNewName());
//                res.setHistoryInfo(historyInfo);
//                result.setData(res);
//                result.setMsg("修改历史记录名称成功");
//            }
//            else{
//                result.setMsg("修改失败");
//                result.setData(null);
//                result.setCode(ResultBean.FAIL);
//            }
//        }
//        else{
//            result.setMsg("同一用户下历史记录名称重复！");
//            result.setData(null);
//            result.setCode(ResultBean.FAIL);
//        }
//
//        return result;
//    }

//
//    @CrossOrigin
//    @ApiOperation("下载历史记录")
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
//                result.setMsg("历史记录存储地址错误！");
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
//    @ApiOperation("下载多个历史记录")
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
