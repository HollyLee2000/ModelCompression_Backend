package org.zjuvipa.compression.backend.interceptor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import org.jetbrains.annotations.NotNull;
import org.zjuvipa.compression.common.util.AuthContextUtil;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.zjuvipa.compression.common.util.ResultBean;
import org.zjuvipa.compression.common.util.User;
import org.zjuvipa.compression.common.util.UserInfo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class LoginAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final List<String> ALLOWED_URIS = Arrays.asList(
            "/user/getModelZoo",
            "/algorithm/gpuInfoList",
            "/user/updateJsonTree",
            "/user/findNoPass",
            "/user/getUsrRank",
            "/user/login",
            "/user/getQualitativeComparison",
            "/user/getLeaderboard",
            "/img",
            "/Cifar",
            "/VOC",
            "/data/uploadCkpt",
            "/WorkSpace",
            "/ckpt"
    );

    private boolean isAllowedPath(String requestURI) {
        return ALLOWED_URIS.stream().anyMatch(requestURI::startsWith);
    }

    //在请求处理之前被调用,postHandle在目标方法执行之后，视图渲染之前被调用(这里没有重写)

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1 获取请求方式
        //如果请求方式是options(跨域预检请求)，直接放行
        String method = request.getMethod();
//        System.out.println("preHandle method: " + method);

        // 获取请求路径
        String requestURI = request.getRequestURI();
        System.out.println("preHandle requestURI: " + requestURI);

        if("OPTIONS".equals(method)) {
            return true;
        }

        //可以给游客放行的请求
        if(isAllowedPath(requestURI)) {
            return true;
        }

        //2 从请求头获取token
        String token = request.getHeader("userTicket");

//        //2 从请求头获取权限等级
        String access = request.getHeader("access");

        System.out.println("token: " + token);
        System.out.println("access: " + access);

        //3 如果token为空，返回错误提示
        if(StrUtil.isEmpty(token)) {
            System.out.println("token为空");
            responseNoLoginInfo(response);
            return false;
        }

        //3 如果权限为空，返回错误提示
        if(StrUtil.isEmpty(access)) {
            System.out.println("权限为空");
            responseNoLoginInfo(response);
            return false;
        }

        //4 如果token不为空，拿着token查询redis
        String userInfoString = redisTemplate.opsForValue().get("user:login" + token);

        String userAccesss = redisTemplate.opsForValue().get("user:access" + token);

        //5 如果redis查询不到数据，返回错误提示
        if(StrUtil.isEmpty(userInfoString)) {
            System.out.println("redis用户为空");
            responseNoLoginInfo(response);
            return false;
        }

        //5 如果redis查询不到权限信息，也返回错误提示
        if(StrUtil.isEmpty(userAccesss)) {
            System.out.println("redis权限为空");
            responseNoLoginInfo(response);
            return false;
        }

        //6 如果redis查询到用户信息，把用户信息放到ThreadLocal里面

        User sysUser = JSON.parseObject(userInfoString, User.class);
        UserInfo userInfo = sysUser.change();
        AuthContextUtil.set(sysUser);
        AuthContextUtil.setUserInfo(userInfo);
        AuthContextUtil.setAccess(userAccesss);

        //7 把redis用户信息数据更新过期时间
        redisTemplate.expire("user:login" + token,7, TimeUnit.HOURS);
        redisTemplate.expire("user:access" + token,7, TimeUnit.HOURS);

        //8 放行
        return true;
    }

    //响应208状态码给前端
    private void responseNoLoginInfo(HttpServletResponse response) {
//         = Result.build(null, ResultCodeEnum.LOGIN_AUTH)
        ResultBean<Object> result = new ResultBean<>();
        result.setMsg("用户未登录");
        result.setCode(-1);
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(JSON.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
        }
    }

    //在视图渲染完成之后被调用
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        //ThreadLocal删除
        AuthContextUtil.remove();
        AuthContextUtil.removeUserInfo();
        AuthContextUtil.removeAccess();
    }
}
