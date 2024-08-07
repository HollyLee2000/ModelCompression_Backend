package org.zjuvipa.compression.backend.controller;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.zjuvipa.compression.common.util.*;
import org.zjuvipa.compression.common.util.*;
import org.zjuvipa.compression.common.util.*;
import org.zjuvipa.compression.model.entity.History;
import org.zjuvipa.compression.model.info.UserInfo;
import org.zjuvipa.compression.model.req.*;
import org.zjuvipa.compression.model.res.*;
import org.zjuvipa.compression.backend.service.IGetRankService;
import org.zjuvipa.compression.backend.service.IAlgorithmService;
import org.zjuvipa.compression.backend.service.IHistoryService;
import org.zjuvipa.compression.backend.service.IUserService;
import org.zjuvipa.compression.common.util.CookieUtil;
import org.zjuvipa.compression.common.util.MD5Util;
import org.zjuvipa.compression.common.util.ResultBean;
import org.zjuvipa.compression.common.util.UUIDUtil;
import org.zjuvipa.compression.model.info.rankListInfo;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.zjuvipa.compression.common.util.Base64Util.encode;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
@Api(tags = "用户")
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    IUserService iUserService;

    @Autowired
    IGetRankService iGetRankService;


    //这种需要同时导入依赖包且修改@ComponentScan的basePackages才能自动注入，因为@ComponentScan会默认扫描当前包
    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @Resource
    private HttpServletResponse response;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Resource
    private HttpServletRequest request;

    @Autowired
    IAlgorithmService iAlgorithmService;

    @Autowired
    IHistoryService iHistoryService;

    //SubmitTrainingHistory
    private final Map<String, Integer> resolvedNum = new HashMap<String, Integer>();
    private final Map<String, Integer> totalNum = new HashMap<String, Integer>();
    private final Map<String, String> algo_name = new HashMap<String, String>();
    private final Map<String, String> algo_link = new HashMap<String, String>();
    private final Map<String, String> sparse_name = new HashMap<String, String>();
    private final Map<String, String> sparse_link = new HashMap<String, String>();

//    MagnitudeImportance能够剪的层:
//        线性/卷积输入输出层、BatchNorm层

    {
        algo_name.put("random", "Random");
        algo_link.put("random", "https://arxiv.org/pdf/1801.10447.pdf");
        sparse_name.put("random", "N/A");
        sparse_link.put("random", "N/A");

        algo_name.put("l1", "MagnitudeL1");
        algo_link.put("l1", "https://arxiv.org/pdf/1608.08710.pdf");
        sparse_name.put("l1", "N/A");
        sparse_link.put("l1", "N/A");

        algo_name.put("l2", "MagnitudeL2");
        algo_link.put("l2", "https://arxiv.org/pdf/1608.08710.pdf");
        sparse_name.put("l2", "N/A");
        sparse_link.put("l2", "N/A");

        algo_name.put("lamp", "LAMP");
        algo_link.put("lamp", "https://arxiv.org/pdf/2010.07611.pdf");
        sparse_name.put("lamp", "N/A");
        sparse_link.put("lamp", "N/A");

        algo_name.put("cp", "CP");
        algo_link.put("cp", "https://arxiv.org/abs/1707.06168.pdf");
        sparse_name.put("cp", "N/A");
        sparse_link.put("cp", "N/A");

        algo_name.put("thinet", "ThiNet");
        algo_link.put("thinet", "https://openaccess.thecvf.com/content_ICCV_2017/papers/Luo_ThiNet_A_Filter_ICCV_2017_paper.pdf");
        sparse_name.put("thinet", "N/A");
        sparse_link.put("thinet", "N/A");

        algo_name.put("hrank", "HRank");
        algo_link.put("hrank", "https://arxiv.org/abs/2002.10179.pdf");
        sparse_name.put("hrank", "N/A");
        sparse_link.put("hrank", "N/A");

        algo_name.put("obdc", "OBD-C");
        algo_link.put("obdc", "https://proceedings.mlr.press/v97/wang19g/wang19g.pdf");
        sparse_name.put("obdc", "N/A");
        sparse_link.put("obdc", "N/A");

        algo_name.put("fpgm", "FPGM");
        algo_link.put("fpgm", "https://openaccess.thecvf.com/content_CVPR_2019/papers/He_Filter_Pruning_via_Geometric_Median_for_Deep_Convolutional_Neural_Networks_CVPR_2019_paper.pdf");
        sparse_name.put("fpgm", "N/A");
        sparse_link.put("fpgm", "N/A");


        algo_name.put("slim", "BNScale");
        algo_link.put("slim", "https://arxiv.org/pdf/1708.06519.pdf");
        sparse_name.put("slim", "BNScale");
        sparse_link.put("slim", "https://arxiv.org/pdf/1708.06519.pdf");

        algo_name.put("group_slim", "BNScale");
        algo_link.put("group_slim", "https://arxiv.org/pdf/1708.06519.pdf");
        sparse_name.put("group_slim", "GroupLASSO");
        sparse_link.put("group_slim", "https://tibshirani.su.domains/ftp/sparse-grlasso.pdf");

        algo_name.put("group_sl", "MagnitudeL2");
        algo_link.put("group_sl", "https://arxiv.org/pdf/1608.08710.pdf");
        sparse_name.put("group_sl", "GroupNorm");
        sparse_link.put("group_sl", "https://openaccess.thecvf.com/content/CVPR2023/papers/Fang_DepGraph_Towards_Any_Structural_Pruning_CVPR_2023_paper.pdf");

        algo_name.put("growing_reg", "MagnitudeL2");
        algo_link.put("growing_reg", "https://arxiv.org/pdf/1608.08710.pdf");
        sparse_name.put("growing_reg", "GrowingReg");
        sparse_link.put("growing_reg", "https://arxiv.org/abs/2012.09243");

        algo_name.put("taylor", "TaylorFO");
        algo_link.put("taylor", "https://openaccess.thecvf.com/content_CVPR_2019/papers/Molchanov_Importance_Estimation_for_Neural_Network_Pruning_CVPR_2019_paper.pdf");
        sparse_name.put("taylor", "N/A");
        sparse_link.put("taylor", "N/A");

        algo_name.put("hessian", "Hessian");
        algo_link.put("hessian", "https://proceedings.neurips.cc/paper/1989/hash/6c9882bbac1c7093bd25041881277658-Abstract.html");
        sparse_name.put("hessian", "N/A");
        sparse_link.put("hessian", "N/A");

        algo_name.put("l2_lasso", "MagnitudeL2");
        algo_link.put("l2_lasso", "https://arxiv.org/pdf/1608.08710.pdf");
        sparse_name.put("l2_lasso", "GroupLASSO");
        sparse_link.put("l2_lasso", "https://tibshirani.su.domains/ftp/sparse-grlasso.pdf");
    }


    private boolean pruner_flag = false; //是否获得剪枝器，获得的时候开始展示进度条
    private boolean process_error = false;

    public static final String staticPath = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
    private String saveHeadImg(MultipartFile file) {
        String defaultHead = staticPath + File.separator +"head" + File.separator + "img.jpg";
        if (file == null) {
            //若用户没有上传头像，则使用默认的头像
            System.out.println("imgUrl: empty");
            return defaultHead;
        }
        // 原始文件名
        String originalFileName = file.getOriginalFilename();
        // 获取图片后缀
        if(originalFileName.lastIndexOf(".") == -1) return defaultHead;
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        // 生成图片存储的名称，UUID 避免相同图片名冲突，并加上图片后缀
        String fileName = UUID.randomUUID().toString() + suffix;
        // 图片存储目录及图片名称
        String url_path = "head" + File.separator + fileName;
        //图片保存路径
        String savePath = staticPath + File.separator + url_path;
        System.out.println("图片保存地址："+savePath);

        File saveFile = new File(savePath);
        if (!saveFile.exists()){
            saveFile.mkdirs();
        }
        try {
            file.transferTo(saveFile);  //将临时存储的文件移动到真实存储路径下
            return savePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultHead;
    }

    @CrossOrigin
    @ApiOperation("用户注册")
    @PostMapping("register")
    public ResultBean<RegisterRes> Register(RegisterReq registerReq, MultipartFile file) {
//        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
//        System.out.println(registerReq);
//        System.out.println(file);
        ResultBean<RegisterRes> result = new ResultBean<>();
        //验证验证码
        String captcha = redisTemplate.opsForValue().get("user:validate");
//        String captcha = (String) request.getSession().getAttribute("captcha");
        if (!StringUtils.hasText(registerReq.getCode())||!captcha.equals(registerReq.getCode())){
            result.setMsg("The verification code is wrong!");
            result.setCode(ResultBean.FAIL);
            result.setData(null);
            return result;
        }

        //5 如果一致，删除redis里面验证码
        redisTemplate.delete("user:validate");
// 测试新电脑的push
        if(!StringUtils.hasText(registerReq.getUsername())) {
            result.setMsg("User name is null!");
            result.setCode(ResultBean.FAIL);
            result.setData(null);
        }
        else {
            RegisterRes registerRes = new RegisterRes();
            registerRes.setUserInfo(iUserService.findByUsername(registerReq.getUsername()));
            if(registerRes.getUserInfo() != null) {
//                System.out.println("3");
                result.setMsg("User already exist!");
                result.setCode(ResultBean.FAIL);
                result.setData(null);
            }
            else{
                UserInfo userInfo = new UserInfo();
                userInfo.setHeadshot(this.saveHeadImg(file));
                userInfo.setUsername(registerReq.getUsername());
                userInfo.setSalt(MD5Util.getSalt());
                userInfo.setInstitute(registerReq.getInstitute());
                userInfo.setPassword(MD5Util.formPassToDBPass(registerReq.getPassword(), userInfo.getSalt()));
                userInfo.setTelephone(registerReq.getTelephone());
                userInfo.setAuthority(registerReq.getAuthority());
                registerRes.setUserInfo(iUserService.addUser(userInfo));
                result.setMsg("Success");
                result.setData(registerRes);
            }
        }
//        System.out.println("注册成功了");
        return result;
    }

    @CrossOrigin
    @ApiOperation("获取验证码")
    @GetMapping(value = "captcha")
    public ResultBean<CaptchaRes> captcha(){
        System.out.println("成功进入");
//        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        ResultBean<CaptchaRes> result = new ResultBean<>();
        //获取验证码文本内容
        String text = defaultKaptcha.createText();
//        System.out.println("验证码: " + text);
        //将验证码放到session中(已经废除这种做法，改为用redis)
//        request.getSession().setAttribute("captcha",text);



        redisTemplate.opsForValue().set("user:validate", text, 5, TimeUnit.MINUTES);



        //request.getSession().setMaxInactiveInterval(4*60*60);
        //根据文本内容创建图形验证码
        BufferedImage image = defaultKaptcha.createImage(text);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image,"jpg",outputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
            result.setMsg("图片转换失败");
            result.setCode(ResultBean.FAIL);
            return result;
        }
        finally {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        byte[] imageByte = outputStream.toByteArray();
        String base64 = Base64Util.encode(imageByte);
        CaptchaRes captchaRes = new CaptchaRes();
        captchaRes.setCode(text);
        captchaRes.setBase64(base64);
        result.setData(captchaRes);
        result.setMsg("返回验证码和图片base64编码");
//        System.out.println("生成验证码时session里验证码的数据:");
//        String captcha = (String) request.getSession().getAttribute("captcha");
//        System.out.println(captcha);
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("10秒钟后session里验证码的数据:");
//        String captcha2 = (String) request.getSession().getAttribute("captcha");
//        System.out.println(captcha2);
//        System.out.println("产生验证码后的session和cookie:");
//        myTest();
        return result;
    }


    @CrossOrigin
    @ApiOperation("用户登录")
    @PostMapping("login")
    public ResultBean<LoginRes> login(@RequestBody LoginReq req){
//        System.out.println("Test0");
//        System.out.println("点击登录时的set和cookie");
//        myTest();
        ResultBean<LoginRes> result = new ResultBean<>();
//        System.out.println("Test1");
//        System.out.println(req);
//        System.out.println("Test2");
        //验证验证码，已放弃从session中拿验证码
//        String captcha = (String) request.getSession().getAttribute("captcha");

        //2 根据获取的redis里面key ，查询redis里面存储验证码
        // set("user:validate"+key
        String captcha = redisTemplate.opsForValue().get("user:validate");


//        System.out.println(request);
//        System.out.println(request.getSession());
//        System.out.println(req.getCode());
//        System.out.println(captcha);

        //这里有session丢失导致验证码为null的问题（是跨域造成的，现在已经用redis了）
        if (!StringUtils.hasText(req.getCode())||!captcha.equals(req.getCode())){
            result.setMsg("验证码错误！");
            result.setCode(ResultBean.FAIL);
            result.setData(null);
            return result;
        }

        //5 如果一致，删除redis里面验证码
        redisTemplate.delete("user:validate");

        //验证账号密码
        LoginRes loginRes = new LoginRes();
        UserInfo userInfo = iUserService.findByUsername(req.getUsername());
        loginRes.setUserInfo(userInfo);
//        System.out.println(loginRes);
        if (loginRes.getUserInfo()!=null){
            String salt = loginRes.getUserInfo().getSalt();
            if(loginRes.getUserInfo().getPassword().equals(MD5Util.formPassToDBPass(req.getPassword(), salt))){
//                System.out.println("入口1");
                result.setMsg("登陆成功");
                //生成cookie
                String userTicket = UUIDUtil.uuid();

                loginRes.setToken(userTicket);


                //8 把登录成功用户信息放到redis里面
                // key : token   value: 用户信息
                redisTemplate.opsForValue()
                        .set("user:login"+userTicket,
                                JSON.toJSONString(userInfo),
                                7,
                                TimeUnit.HOURS);

                //8 单独存储用户权限信息
                // key : token   value: 用户信息
                redisTemplate.opsForValue()
                        .set("user:access"+userTicket,
                                String.valueOf(userInfo.getAuthority()),
                                7,
                                TimeUnit.HOURS);



//                request.getSession().setAttribute(userTicket, loginRes.getUserInfo());
//                request.getSession().setMaxInactiveInterval(4*60*60);
//                CookieUtil.setCookie(request, response, "userTicket", userTicket, 60*60);

//                System.out.println("标签");
//                System.out.println(Arrays.toString(request.getCookies()));
                result.setData(loginRes);
            } else {
//                System.out.println("入口2");
                result.setCode(ResultBean.FAIL);
                result.setMsg("用户名或密码错误");
                result.setData(null);
            }
        } else {
//            System.out.println("入口3");
            result.setMsg("用户不存在");
            result.setCode(ResultBean.FAIL);
            result.setData(null);
        }
//        System.out.println("登陆完后cookie和session中的内容:");
//        myTest();
        return result;
    }


    @CrossOrigin
    @ApiOperation("查询当前排行榜信息")
    @PostMapping("getCurrentrank")
    public ResultBean<GetRankRes> getCurrentrank(){
        ResultBean<GetRankRes> result = new ResultBean<>();
        GetRankRes getRankRes = new GetRankRes();
        getRankRes.setRanklistinfo(iGetRankService.getCurrentRank());
//        System.out.println("getRankRes: "+getRankRes);
        if (getRankRes.getRanklistinfo()!=null){
            result.setData(getRankRes);
        } else {
            result.setMsg("排行榜上没有查到任何算法信息");
            result.setCode(ResultBean.FAIL);
            result.setData(null);
        }
        return result;
    }

    @CrossOrigin
    @ApiOperation("查询当前ModelZoo中的信息")
    @PostMapping("getModelZoo")
    public String getModelZoo() throws IOException{
        // 指定JSON文件的路径
        String filePath = "/nfs/lhl/ModelCompression/modelzoo.json";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            System.out.println("jsonContent.toString(): "+jsonContent.toString());

            // 将JSON数据作为HTTP响应返回给前端
            return jsonContent.toString();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading JSON file");
            return "Error reading JSON file";
        }
    }

    @CrossOrigin
    @ApiOperation("查询用户提交数据之后的实时信息")
    @PostMapping("getUsrRank")
    public ResultBean<GetRankRes> getUsrRank()  throws IOException, InterruptedException {
        ResultBean<GetRankRes> result = new ResultBean<>();
        GetRankRes getRankRes = new GetRankRes();
        String command1 = "python /nfs3-p1/duanjr/meta-score-attribution-hollylee/usr_meta_compute.py";
        System.out.println("command: " + command1);
        Process process = Runtime.getRuntime().exec(command1);
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        List<String> lines = new ArrayList<String>();
        String line;
        while ((line = in.readLine()) != null) {
            lines.add(line);
        }
        process.waitFor();
        System.out.println("lines[0]: " + lines.get(0));

        List<rankListInfo> rankList;
        String jsonString = lines.get(0).replace("'", "\"");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            rankList = objectMapper.readValue(jsonString, new TypeReference<List<rankListInfo>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        getRankRes.setRanklistinfo(rankList);
        if (getRankRes.getRanklistinfo()!=null){
            result.setData(getRankRes);
        } else {
            result.setMsg("出现了异常！！");
            result.setCode(ResultBean.FAIL);
            result.setData(null);
        }
        return result;
    }

    @CrossOrigin
    @ApiOperation("上传非训练的历史记录")
    @PostMapping("/SubmitHistory")
    public ResultBean<UploadPicturesRes> SubmitHistory(@RequestBody SubmitHistoryReq req) throws IOException {
        String username = req.getUsername();

        ResultBean<UploadPicturesRes> result = new ResultBean<>();
        UploadPicturesRes res = new UploadPicturesRes();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        String day = df.format(date);  //放弃转换成String, 直接比较date
        System.out.println("Debug!!!: "+req.getUsername()+" "+req.getModelname()+" "+req.getTasktype()+" "+req.getCheckpointpath()+" "+
                req.getStatus()+" "+req.getParamschange()+" "+req.getFlopschange()+" "+req.getAccchange()+" "+req.getLosschange()+" "+req.getPrunedpath()+" "+day
                +" "+req.getStructurebeforepruned()+" "+req.getStructureafterpruned()+" "+req.getLogpath());

        res.setSucceed(iHistoryService.uploadHistory(req.getModelname(), req.getTasktype(), req.getCheckpointpath(), req.getUsername(),
                day, req.getStatus(),req.getParamschange(),req.getFlopschange(),req.getAccchange(),req.getLosschange(), req.getPrunedpath(),
                req.getStructurebeforepruned(), req.getStructureafterpruned(), req.getLogpath()));

//        res.setSucceed(iHistoryService.uploadHistory(req.getUsername(), req.getName(), req.getScore(), req.getInstitute(),
//                req.getRanking(),req.getMorfPath(),req.getLerfPath(),req.getPythonPath(),req.getEmail(),req.getInfo(), day, "In Process"));


        result.setData(res);
        result.setMsg("上传历史记录成功");
        return result;
    }





    @CrossOrigin
    @ApiOperation("上传要训练的历史记录")
    @PostMapping("/SubmitTrainingHistory")
    public ResultBean<UploadPicturesRes> SubmitTrainingHistory(@RequestBody SubmitTrainingHistoryReq req) throws Exception {
        String[] parts = req.getUsername().split(" "); // 使用空格分隔字符串
        String username;
        String tot_epoch;
        String criterion;
        if (parts.length == 3) {
            username  = parts[0];
            tot_epoch = parts[1];
            criterion = parts[2];
        } else {
            throw new RuntimeException("Invalid parameter: " + req.getUsername());
        }

        ResultBean<UploadPicturesRes> result = new ResultBean<>();
        UploadPicturesRes res = new UploadPicturesRes();
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        Date date = new Date();
//        String day = df.format(date);  //放弃转换成String, 直接比较date

//        String userAccesss = redisTemplate.opsForValue().get("user:access" + token);
//        if()
        String access = AuthContextUtil.getAccess();  //前端区分了，这里权限用不上


        System.out.println("Debug!!!: "+username+" "+req.getModelname()+" "+req.getTasktype()+" "+req.getCheckpointpath()+" "+
                req.getStatus()+" "+req.getParamschange()+" "+req.getFlopschange()+" "+req.getAccchange()+" "+req.getLosschange()+" "+req.getPrunedpath()+" "+req.getStructurebeforepruned()+" "+req.getStructureafterpruned()+" "+req.getLogpath());

//        updateJsonTree

        History history = new History(req.getModelname(), req.getTasktype(), req.getCheckpointpath(), username, req.getStatus(),req.getParamschange(),req.getFlopschange(),req.getAccchange(),req.getLosschange(), req.getPrunedpath(),
                req.getStructurebeforepruned(), req.getStructureafterpruned(), req.getLogpath(), 1, Integer.parseInt(tot_epoch), 0, req.getScript(), req.getClient(),
                algo_name.get(criterion), algo_link.get(criterion), sparse_name.get(criterion), sparse_link.get(criterion));

        res.setSucceed(iHistoryService.uploadTrainingHistory(history));

        int generatedHistoryId = history.getHistoryId();

        System.out.println("current task id: "+generatedHistoryId);

        res.setTaskId(generatedHistoryId);

        //1表示需要训练，2表示已经分发给了客户端
        //3是total epoch, 后期要根据具体数据集更改

//        res.setSucceed(iHistoryService.uploadHistory(req.getUsername(), req.getName(), req.getScore(), req.getInstitute(),
//                req.getRanking(),req.getMorfPath(),req.getLerfPath(),req.getPythonPath(),req.getEmail(),req.getInfo(), day, "In Process"));


        result.setData(res);
        result.setMsg("上传历史记录成功");
        return result;
    }



    @CrossOrigin
    @ApiOperation("上传算法")
    @PostMapping("/SubmitAlgorithm")
    public ResultBean<UploadPicturesRes> SubmitAlgorithm(@RequestBody SubmitAlgorithmReq req) throws IOException {
        String username = req.getUsername();
        ResultBean<UploadPicturesRes> result = new ResultBean<>();
        UploadPicturesRes res = new UploadPicturesRes();
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = new Date();
//        String day = df.format(date);
        System.out.println("Debug!!!: "+req.getUsername().toString()+" "+req.getName().toString()+" "+req.getScore().toString()+" "+req.getInstitute().toString()+" "+
                req.getRanking().toString()+" "+req.getMorfPath().toString()+" "+req.getLerfPath().toString()+" "+req.getPythonPath().toString()+" "+req.getEmail().toString()+" "+req.getInfo().toString());
        res.setSucceed(iAlgorithmService.uploadAlgorithm(req.getUsername(), req.getName(), req.getScore(), req.getInstitute(),
                req.getRanking(),req.getMorfPath(),req.getLerfPath(),req.getPythonPath(),req.getEmail(),req.getInfo(),"In Process"));
        result.setData(res);
        result.setMsg("上传成功");
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
                getProcessRes.setTotal(79);
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
    @ApiOperation("修改json树")
    @PostMapping("/updateJsonTree")
    public ResultBean<UploadPicturesRes> updateJsonTree(@RequestBody SubmitAlgorithmReq req) throws IOException, InterruptedException {



//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        Date date = new Date();
//        String day = df.format(date);  //放弃转换成String, 直接比较date




        process_error = false;
        String username = req.getUsername();
        String model = req.getName();
        String dataset = req.getInstitute();
        String ckpt = req.getMorfPath();
        String usrModelName = req.getLerfPath();
        ResultBean<UploadPicturesRes> result = new ResultBean<>();
        UploadPicturesRes res = new UploadPicturesRes();

        String Script = req.getPythonPath();
//        System.out.println("Script: "+ Script);
//        System.out.println("dataset: "+ dataset);
//        System.out.println("model: "+ model);

        //检测文件系统中是否存在ckpt这个文件
        File ckptFile = new File(ckpt);




        //updateJsonTree

//        public History(String modelname, String tasktype, String checkpointpath,
//                String username, String submitTime, String status, String paramschange, String flopschange,
//                String accchange, String losschange, String prunedpath, String structurebeforepruned,
//                String structureafterpruned, String logpath, Integer isTraining, Integer totEpoch,
//                Integer currentEpoch, String script,
//                String client, String dataset, String usrModelName, String importance, String importanceLink, String pruner, String prunerLink) {


            if(ckptFile.exists()){

            History history = new History(model+"-"+dataset, "Upload Raw Model", ckpt, username,
                    req.getInfo(), "Params: N/A", "FLOPs: N/A", "Acc: N/A", "Val Loss: N/A", "N/A",
                    req.getEmail(), "N/A", "N/A", 1, 233, 0, req.getPythonPath(), "NULL", dataset, usrModelName);


            res.setSucceed(iHistoryService.uploadUploadingHistory(history));

            int generatedHistoryId = history.getHistoryId();

            System.out.println("current task id: "+generatedHistoryId);

            res.setTaskId(generatedHistoryId);


            res.setSucceed(true);
            result.setData(res);
            result.setMsg("上传成功");
            return result;
        }else{
            res.setSucceed(false);
            result.setData(res);
            result.setMsg("上传失败");
            return result;
        }



//            int size1 = 0;
//            if(dataset.equals("CIFAR10")||dataset.equals("CIFAR100")){
//                size1 = 79;
//            }else if(dataset.equals("ImageNet")){
//                size1 = 391;
//            }else if(dataset.equals("COCO")){
//                size1 = 79;
//            }else{
//                System.out.println("dataset: "+ dataset);
//            }
//
//
//            Process process = Runtime.getRuntime().exec(Script);


//            int batchSize = 128;
//            Map<Integer, Integer> intToIntMap = new HashMap<>();
//            intToIntMap.put(128, 79);
//            intToIntMap.put(64, 157);
//            intToIntMap.put(32, 313);
//            int Num = 0;
//            int size1 = intToIntMap.get(batchSize);


//            resolvedNum.put("current", 0);
//            ServerSocket ss = new ServerSocket(50006);
//            ss.setSoTimeout(1000);
//            // 获取进程的错误输出流
//            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//
//            while(Num < size1) {
//                System.out.println("启动服务器....");
//                try {
//                    Socket s = ss.accept();
//                    // 如果在超时时间内有客户端连接，将会执行这里的代码
//                    System.out.println("客户端:" + s.getInetAddress().getLocalHost() + "已连接到服务器");
//                    BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
//                    String mess = br.readLine();
//                    if(StringUtils.hasText(mess)){
//                        if(mess.equals("Pruner got")){
//                            System.out.println("mess: " + mess);
//                            System.out.println("获得了剪枝器，准备开始展示进度条");
//                            pruner_flag = true;
//                        }else{
//                            System.out.println("mess: " + mess);
//                            Num += 1;
//                        }
//                    }
//                    System.out.println("已处理batch:" +Num);
//                    resolvedNum.put("current", Num);
//                } catch (SocketTimeoutException e) {
//                    // 如果超时时间内没有客户端连接，将会执行这里的代码
//                    System.out.println("在指定的超时时间内没有客户端连接到服务器");
//
//                    if(!errorReader.ready()){
//                        System.out.println("还没出现错误");
//                    }else{
//                        //补充错误输出流的信息
//                        if(!dataset.equals("COCO")){
//                            System.out.println("程序出错了,退出！");
//                            break;
//                        }
//
//                    }
//                }
//            }
//            ss.close();
//
//            System.out.println("任务结束，服务器关闭");



//            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            List<String> lines = new ArrayList<String>();
//            String line;
//            while ((line = in.readLine()) != null) {
//                lines.add(line);
//            }
//            process.waitFor();
//
//            System.out.println("lines: "+lines);

            //获取lines最后一行的内容，检测是否是"finished"
//            if(lines.get(lines.size() - 1).equals("finished")){
//                ObjectMapper mapper = new ObjectMapper();
//                mapper.enable(SerializationFeature.INDENT_OUTPUT); // 启用缩进输出
//                ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter(); // 使用默认的缩进格式
//                File jsonFile = new File("/nfs/lhl/ModelCompression/modelzoo.json");
//                ObjectNode rootNode = (ObjectNode) mapper.readTree(jsonFile);
//                // 2. 在 JSON 结构中查找满足条件的节点
//                boolean found = false;
//                ArrayNode childrenArray = rootNode.withArray("children");
//                for (int i = 0; i < childrenArray.size(); i++) {
//                    ObjectNode datasetNode = (ObjectNode) childrenArray.get(i);
//                    if (datasetNode.get("name").asText().equals(dataset)) {
//                        ArrayNode modelsArray = datasetNode.withArray("children");
//                        for (int j = 0; j < modelsArray.size(); j++) {
//                            ObjectNode modelNode = (ObjectNode) modelsArray.get(j);
//                            if (modelNode.get("name").asText().equals(model)) {
//                                // 3. 添加一个新节点
//                                ObjectNode taskNode = (ObjectNode) modelsArray.get(j);
//                                ArrayNode taskArray = taskNode.withArray("children");
//                                System.out.println("taskArray: "+ taskArray);
//                                ObjectNode newNode = mapper.createObjectNode();
//                                newNode.put("name", username + ":" + usrModelName);
//                                newNode.put("type", "usr");
//                                newNode.put("model_name", dataset + "_" + model + "_" + username + ":" + usrModelName);
//                                newNode.put("status", "done");
//                                newNode.put("path", ckpt);
//                                newNode.put("acc", lines.get(lines.size() - 4));
//                                newNode.put("params", lines.get(lines.size() - 3));
//                                newNode.put("flops", lines.get(lines.size() - 2));
//                                newNode.put("size", 1616);
//                                taskArray.add(newNode);
//                                found = true;
//                                break;
//                            }
//                        }
//                        if (found) {
//                            break;
//                        }
//                    }
//                }
//                res.setSucceed(found);
//                // 4. 将修改后的 JSON 结构重新写入文件
//                writer.writeValue(jsonFile, rootNode);
////        // 4. 将修改后的 JSON 结构重新写入文件
////        mapper.writeValue(jsonFile, rootNode);
//                result.setData(res);
//                result.setMsg("上传成功");
//                return result;
//            }else{
//                res.setSucceed(false);
//                result.setData(res);
//                result.setMsg("上传失败");
//                return result;
//            }
//        }else{
//            res.setSucceed(false);
//            result.setData(res);
//            result.setMsg("上传失败");
//            return result;
//        }






        //读取/nfs/lhl/ModelCompression/modelzoo.json文件，找到"name"为model，且其上一级的"name"为dataset的项，如果找到，在它的children里添加一项：
//        {
//            "name": username+":"+usrModelName,
//            "type": "usr",
//            "model_name": dataset+"_"+model+"_"+username+":"+usrModelName,
//            "path": ckpt,
//            "acc": "N/A",
//            "params": "N/A",
//            "flops": "N/A",
//            "size": 1616
//        }
//        并设置res.setSucceed(true);
        //若找不到类似的项，设置res.setSucceed(false);


        // 1. 读取并解析 JSON 文件

    }

    @CrossOrigin
    @ApiOperation("查询用户提交数据之后包括用户方法的实时信息")
    @PostMapping("getUsrRank2")
    public ResultBean<GetRankRes> getUsrRank2(@RequestBody GetCurrentRankReq req)  throws IOException, InterruptedException {
        ResultBean<GetRankRes> result = new ResultBean<>();
        GetRankRes getRankRes = new GetRankRes();
        String morf = req.getMorfpath();
        String lerf = req.getLerfpath();
        String command = "python /nfs3-p1/duanjr/meta-score-attribution-hollylee/generate_usr_result.py --new_morf "
                + morf + " --new_lerf " + lerf;
        System.out.println("command: " + command);
        Process process1 = Runtime.getRuntime().exec(command);
        process1.waitFor();


        String command1 = "python /nfs3-p1/duanjr/meta-score-attribution-hollylee/usr_meta_compute_current.py";
        System.out.println("command1: " + command1);
        Process process = Runtime.getRuntime().exec(command1);
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        List<String> lines = new ArrayList<String>();
        String line;
        while ((line = in.readLine()) != null) {
            lines.add(line);
        }
        process.waitFor();
        System.out.println("lines[0]: " + lines.get(0));

        List<rankListInfo> rankList;
        String jsonString = lines.get(0).replace("'", "\"");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            rankList = objectMapper.readValue(jsonString, new TypeReference<List<rankListInfo>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        getRankRes.setRanklistinfo(rankList);
        if (getRankRes.getRanklistinfo()!=null){
            result.setData(getRankRes);
        } else {
            result.setMsg("出现了异常！！");
            result.setCode(ResultBean.FAIL);
            result.setData(null);
        }
        return result;
    }





    @CrossOrigin
    @ApiOperation("获得已有paper里的定性比较")
    @PostMapping("getQualitativeComparison")
    public ResultBean<GetQualitativeRes> getQualitativeComparison(@RequestBody GetCurrentRankReq req){
        String dataset = req.getMorfpath();
        String model = req.getLerfpath();

        ResultBean<GetQualitativeRes> result = new ResultBean<>();
        GetQualitativeRes getQualitativeRes = new GetQualitativeRes();
        getQualitativeRes.setQualitativeinfo(iGetRankService.getQualitativeComparison(dataset, model));
//        System.out.println("getRankRes: "+getRankRes);
        if (getQualitativeRes.getQualitativeinfo()!=null){
            result.setData(getQualitativeRes);
        } else {
            result.setMsg("paper上没有查到任何算法信息");
            result.setCode(ResultBean.FAIL);
            result.setData(null);
        }
        return result;
    }


    @CrossOrigin
    @ApiOperation("我们的Leaderboard")
    @PostMapping("getLeaderboard")
    public ResultBean<GetLeaderboardRes> getLeaderboard(@RequestBody GetCurrentRankReq req){
        String dataset = req.getMorfpath();
        String model = req.getLerfpath();

        ResultBean<GetLeaderboardRes> result = new ResultBean<>();
        GetLeaderboardRes getLeaderboardRes = new GetLeaderboardRes();
        getLeaderboardRes.setLeaderboardinfo(iGetRankService.getLeaderboard(dataset, model));
//        System.out.println("getRankRes: "+getRankRes);
        if (getLeaderboardRes.getLeaderboardinfo()!=null){
            result.setData(getLeaderboardRes);
        } else {
            result.setMsg("paper上没有查到任何算法信息");
            result.setCode(ResultBean.FAIL);
            result.setData(null);
        }
        return result;
    }




    @CrossOrigin
    //测试后端cookie和session
    @ApiOperation("测试后端cookie和session")
    @GetMapping("test_cookie_session")
    public ResultBean<LoginRes> myTest(){
        if(request.getCookies() == null){
            System.out.println("cookie里啥都没有");
        }else{
            System.out.println(Arrays.toString(request.getCookies()));
        }
        // 获取session
        HttpSession session = request.getSession();
        // 获取session中所有的键值
        Enumeration<?> enumeration = session.getAttributeNames();
        if(!enumeration.hasMoreElements()){
            System.out.println("session里啥都没有");
        }else{
            // 遍历enumeration
            System.out.println("session里有东西");
            while (enumeration.hasMoreElements()) {
                // 获取session的属性名称
                String name = enumeration.nextElement().toString();
                // 根据键值取session中的值
                Object value = session.getAttribute(name);
                // 打印结果
                System.out.println("name:" + name + ",value:" + value );
            }
        }

        return new ResultBean<>();
    }

    //登出功能
    @CrossOrigin
    @ApiOperation("用户退出")
    @PostMapping("logout")
    public ResultBean<LoginRes> unLogin(@RequestHeader(name = "userTicket") String token){
//        System.out.println(Arrays.toString(request.getCookies()));
        ResultBean<LoginRes> result = new ResultBean<>();
        HttpSession httpSession = request.getSession();
        org.zjuvipa.compression.common.util.UserInfo userInfo = AuthContextUtil.getUserInfo();
//            System.out.println(httpSession.getId());
        if(userInfo == null){
            result.setMsg("用户已退出");
            result.setCode(ResultBean.FAIL);
            result.setData(null);
        }
        else{
//            httpSession.removeAttribute(ticket);
            LoginRes loginRes = new LoginRes();
//            loginRes.setUserInfo(userInfo);  //退出时就不用返回了
            result.setData(loginRes);
            redisTemplate.delete("user:login"+token);
            redisTemplate.delete("user:access"+token);
            result.setMsg("logout success!");
        }
        return result;
    }

    //    根据用户名查询个人信息
    @CrossOrigin
    @ApiOperation("查询指定用户")
    @PostMapping("find")
    public ResultBean<UserRes> find(@RequestBody UserReq userReq) {
        UserRes userRes = new UserRes();
        userRes.setUserInfo(iUserService.findByUsername(userReq.getUsername()));
        ResultBean<UserRes> result = new ResultBean<>();
        if(userRes.getUserInfo()!=null) {
            String salt = userRes.getUserInfo().getSalt();
            if(userRes.getUserInfo().getPassword().equals(MD5Util.formPassToDBPass(userReq.getPassword(), salt))){
                result.setMsg("查询成功");
                userRes.getUserInfo().setPassword(null);//获取信息不必返回密码
                result.setData(userRes);
            }
        }
        else {
            result.setCode(ResultBean.FAIL);
            result.setMsg("用户不存在");
            result.setData(null);
        }
        return result;
    }

    @CrossOrigin
    @ApiOperation("查询指定用户")
    @PostMapping("findNoPass")
    public ResultBean<UserRes> findNoPass(@RequestBody UserReq userReq) {
        UserRes userRes = new UserRes();
        UserInfo userInfo = iUserService.findByUsername(userReq.getUsername());

//        org.zjuvipa.compression.common.util.UserInfo userInfo = AuthContextUtil.getUserInfo();  //这样是不行的，还是得放行


//        UserInfo userInfo1 = new UserInfo();
//        MyBeanUtils.copyProperties(userInfo, userInfo1);
        userRes.setUserInfo(userInfo);  //现在是从当中获取


        ResultBean<UserRes> result = new ResultBean<>();
        if(userRes.getUserInfo()!=null) {
            System.out.println("查询到有内容");
            result.setMsg("查询成功");
            userRes.getUserInfo().setPassword(null);//获取信息不必返回密码
            result.setData(userRes);
//            String salt = userRes.getUserInfo().getSalt();
//            if(userRes.getUserInfo().getPassword().equals(formPassToDBPass(userReq.getPassword(), salt))){
//                result.setMsg("查询成功");
//                userRes.getUserInfo().setPassword(null);//获取信息不必返回密码
//                result.setData(userRes);
//            }
        }
        else {
            System.out.println("查询不到内容");
            result.setCode(ResultBean.FAIL);
            result.setMsg("查询不到内容");
            result.setData(null);
        }
        return result;
    }

    //修改个人密码
    @CrossOrigin
    @ApiOperation("修改密码")
    @PostMapping("updatepwd")
    public ResultBean<UpdatepwdRes> updatepwd(UpdatepwdReq updatepwdReq) {
        UpdatepwdRes updatepwdRes = new UpdatepwdRes();
        ResultBean<UpdatepwdRes> result = new ResultBean<>();
        //            System.out.println("ticket:"+ticket);
        HttpSession httpSession = request.getSession();
        org.zjuvipa.compression.common.util.UserInfo userInfo = AuthContextUtil.getUserInfo();
        if(userInfo == null) {
            result.setMsg("用户未登录，请跳转login页面");
            result.setCode(ResultBean.NO_PERMISSION);
            result.setData(null);
        }
        else{
//                System.out.println(userInfo.getPassword());
            updatepwdRes.setUserInfo(iUserService.findByUsername(updatepwdReq.getUsername()));
            if(updatepwdRes.getUserInfo() != null) {
                if(userInfo.getPassword().equals(updatepwdRes.getUserInfo().getPassword())){
                    String newPwd = MD5Util.formPassToDBPass(updatepwdReq.getNewPassword(), userInfo.getSalt());
                    iUserService.updateUserPassword(updatepwdReq.getUsername(), newPwd);
                    updatepwdRes.getUserInfo().setPassword(newPwd);
                    result.setMsg("修改密码成功！");
                    result.setData(updatepwdRes);
                }
                else{
                    result.setMsg("用户访问错误，请跳转login页面");
                    result.setCode(ResultBean.NO_PERMISSION);
                    result.setData(null);
                }
            }
            else{
                result.setMsg("修改密码失败！用户不存在");
                result.setCode(ResultBean.FAIL);
                result.setData(null);
            }
        }
        return  result;
    }


    //修改手机号
    @CrossOrigin
    @ApiOperation("修改手机号")
    @PostMapping("updatetel")
    public ResultBean<String> updatetel(@RequestBody UpdatetelReq updatetelReq){
        ResultBean<String> result = new ResultBean<>();
        boolean flag = iUserService.updateUserTel(updatetelReq.getUsername(), updatetelReq.getTelephone());
        if(flag){
            result.setMsg("更新号码成功！更新为："+updatetelReq.getTelephone());
        }
        else{
            result.setCode(ResultBean.FAIL);
            result.setMsg("更新号码失败！");
        }
        return result;
    }

    //修改权限
    @CrossOrigin
    @ApiOperation("修改权限")
    @PostMapping("updateidentify")
    public ResultBean<String> updateidentify(@RequestBody UpdateidentifyReq updateidentifyReq) {
        ResultBean<String> result = new ResultBean<>();
        boolean flag = iUserService.updateUserIdentify(updateidentifyReq.getUsername(), updateidentifyReq.getIdentify());
        if(flag){
            result.setMsg("更新权限角色成功！更新为："+updateidentifyReq.getIdentify());
        }
        else{
            result.setCode(ResultBean.FAIL);
            result.setMsg("更新权限角色失败！");
        }
        return result;
    }
}
