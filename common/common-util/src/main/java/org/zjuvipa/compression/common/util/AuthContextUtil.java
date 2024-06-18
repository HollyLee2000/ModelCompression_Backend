package org.zjuvipa.compression.common.util;


public class AuthContextUtil {

    private static final ThreadLocal<UserInfo> userInfoThreadLocal = new ThreadLocal<>() ;

    // 定义存储数据的静态方法
    public static void setUserInfo(UserInfo userInfo) {
        userInfoThreadLocal.set(userInfo);
    }

    // 定义获取数据的方法
    public static UserInfo getUserInfo() {
        return userInfoThreadLocal.get();
    }

    // 删除数据的方法
    public static void removeUserInfo() {
        userInfoThreadLocal.remove();
    }

    //创建threadLocal对象
    private static final ThreadLocal<User> threadLocal = new ThreadLocal<>();

    //添加数据
    public static void set(User sysUser) {
        threadLocal.set(sysUser);
    }

    //获取数据
    public static User get() {
        return threadLocal.get();
    }

    //删除数据
    public static void remove() {
        threadLocal.remove();
    }

    //创建threadLocal对象
    private static final ThreadLocal<String> accessThreadLocal = new ThreadLocal<>();

    //添加数据
    public static void setAccess(String access) {
        accessThreadLocal.set(access);
    }

    //获取数据
    public static String getAccess() {
        return accessThreadLocal.get();
    }

    //删除数据
    public static void removeAccess() {
        accessThreadLocal.remove();
    }

}
