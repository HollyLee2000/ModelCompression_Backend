package org.zjuvipa.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;




import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.zjuvipa.info.UserInfo;
import org.zjuvipa.req.*;
import org.zjuvipa.res.*;
import org.zjuvipa.service.IGetRankService;
import org.zjuvipa.service.IAlgorithmService;
import org.zjuvipa.service.IUserService;
import org.zjuvipa.util.CookieUtil;
import org.zjuvipa.util.MD5Util;
import org.zjuvipa.util.ResultBean;
import org.zjuvipa.util.UUIDUtil;
import org.zjuvipa.info.rankListInfo;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.zjuvipa.util.Base64Util.encode;
import static org.zjuvipa.util.MD5Util.formPassToDBPass;

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

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @Resource
    private HttpServletResponse response;

    @Resource
    private HttpServletRequest request;

    @Autowired
    IAlgorithmService iAlgorithmService;

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
        String captcha = (String) request.getSession().getAttribute("captcha");
        //这里有session丢失导致验证码为null的问题,
//        if (!StringUtils.hasText(registerReq.getCode())|| !captcha.equals(registerReq.getCode())){
//            result.setMsg("验证码错误！");
//            result.setCode(ResultBean.FAIL);
//            result.setData(null);
//            return result;
//        }
// 测试新电脑的push
        if(!StringUtils.hasText(registerReq.getUsername())) {
            result.setMsg("注册失败！用户名为空");
            result.setCode(ResultBean.FAIL);
            result.setData(null);
        }
        else {
            RegisterRes registerRes = new RegisterRes();
            registerRes.setUserInfo(iUserService.findByUsername(registerReq.getUsername()));
            if(registerRes.getUserInfo() != null) {
//                System.out.println("3");
                result.setMsg("注册失败！用户名已存在");
                result.setCode(ResultBean.FAIL);
                result.setData(null);
            }
            else{
                UserInfo userInfo = new UserInfo();
                userInfo.setHeadshot(this.saveHeadImg(file));
                userInfo.setUsername(registerReq.getUsername());
                userInfo.setSalt(MD5Util.getSalt());
                userInfo.setInstitute(registerReq.getInstitute());
                userInfo.setPassword(formPassToDBPass(registerReq.getPassword(), userInfo.getSalt()));
                userInfo.setTelephone(registerReq.getTelephone());
                userInfo.setAuthority(registerReq.getAuthority());
                registerRes.setUserInfo(iUserService.addUser(userInfo));
                result.setMsg("注册成功！");
                result.setData(registerRes);
            }
        }
        System.out.println("注册成功了");
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
        //将验证码放到session中
        request.getSession().setAttribute("captcha",text);
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
        String base64 = encode(imageByte);
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
        //验证验证码
        String captcha = (String) request.getSession().getAttribute("captcha");
//        System.out.println(request);
//        System.out.println(request.getSession());
//        System.out.println(req.getCode());
//        System.out.println(captcha);

        //这里有session丢失导致验证码为null的问题
        if (!StringUtils.hasText(req.getCode())||!captcha.equals(req.getCode())){
            result.setMsg("验证码错误！");
            result.setCode(ResultBean.FAIL);
            result.setData(null);
            return result;
        }
        //验证账号密码
        LoginRes loginRes = new LoginRes();
        loginRes.setUserInfo(iUserService.findByUsername(req.getUsername()));
//        System.out.println(loginRes);
        if (loginRes.getUserInfo()!=null){
            String salt = loginRes.getUserInfo().getSalt();
            if(loginRes.getUserInfo().getPassword().equals(formPassToDBPass(req.getPassword(), salt))){
//                System.out.println("入口1");
                result.setMsg("登陆成功");
                //生成cookie
                String userTicket = UUIDUtil.uuid();
                request.getSession().setAttribute(userTicket, loginRes.getUserInfo());
                request.getSession().setMaxInactiveInterval(4*60*60);
                CookieUtil.setCookie(request, response, "userTicket", userTicket, 60*60);

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
    @ApiOperation("上传算法")
    @PostMapping("/SubmitAlgorithm")
    public ResultBean<UploadPicturesRes> SubmitAlgorithm(@RequestBody SubmitAlgorithmReq req) throws IOException {
        String username = req.getUsername();

        ResultBean<UploadPicturesRes> result = new ResultBean<>();
        UploadPicturesRes res = new UploadPicturesRes();
//        List<String> savePaths = new ArrayList<>();
//        List<String> names = new ArrayList<>();
//        for (MultipartFile f : pictures) {
//            String s = upload(f);
//            savePaths.add(s);
//            names.add(f.getName());
//        }

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String day = df.format(date);
//        historyInfo.setDateTime(day);

        System.out.println("Debug!!!: "+req.getUsername().toString()+" "+req.getName().toString()+" "+req.getScore().toString()+" "+req.getInstitute().toString()+" "+
                req.getRanking().toString()+" "+req.getMorfPath().toString()+" "+req.getLerfPath().toString()+" "+req.getPythonPath().toString()+" "+req.getEmail().toString()+" "+req.getInfo().toString());
        res.setSucceed(iAlgorithmService.uploadAlgorithm(req.getUsername(), req.getName(), req.getScore(), req.getInstitute(),
                req.getRanking(),req.getMorfPath(),req.getLerfPath(),req.getPythonPath(),req.getEmail(),req.getInfo(), day, "In Process"));
        result.setData(res);
        result.setMsg("上传成功");
        return result;
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
    @GetMapping("logout")
    public ResultBean<LoginRes> unLogin( @CookieValue("userTicket")String ticket){
//        System.out.println(Arrays.toString(request.getCookies()));
        ResultBean<LoginRes> result = new ResultBean<>();
        if(!StringUtils.hasText(ticket)){
            result.setMsg("无用户登录");
            result.setCode(ResultBean.NO_PERMISSION);
            result.setData(null);
        }
        else{
            HttpSession httpSession = request.getSession();
            UserInfo userInfo = (UserInfo)httpSession.getAttribute(ticket);
//            System.out.println(httpSession.getId());
            if(userInfo == null){
                result.setMsg("用户已退出");
                result.setCode(ResultBean.FAIL);
                result.setData(null);
            }
            else{
                httpSession.removeAttribute(ticket);
                LoginRes loginRes = new LoginRes();
                loginRes.setUserInfo(userInfo);
                result.setData(loginRes);
                result.setMsg("logout success!");
            }
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
            if(userRes.getUserInfo().getPassword().equals(formPassToDBPass(userReq.getPassword(), salt))){
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
        userRes.setUserInfo(iUserService.findByUsername(userReq.getUsername()));
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
    public ResultBean<UpdatepwdRes> updatepwd(UpdatepwdReq updatepwdReq, @CookieValue("userTicket")String ticket) {
        UpdatepwdRes updatepwdRes = new UpdatepwdRes();
        ResultBean<UpdatepwdRes> result = new ResultBean<>();
        if(!StringUtils.hasText(ticket)) {
            result.setMsg("用户未登录，请跳转login页面");
            result.setCode(ResultBean.NO_PERMISSION);
            result.setData(null);
        }
        else{
//            System.out.println("ticket:"+ticket);
            HttpSession httpSession = request.getSession();
            UserInfo userInfo = (UserInfo)httpSession.getAttribute(ticket);
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
                        String newPwd = formPassToDBPass(updatepwdReq.getNewPassword(), userInfo.getSalt());
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
