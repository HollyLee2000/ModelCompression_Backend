package org.zjuvipa.service.impl;

import org.zjuvipa.entity.User;
import org.zjuvipa.info.UserInfo;
import org.zjuvipa.mapper.UserMapper;
import org.zjuvipa.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    UserMapper userMapper;


    @Override
    public UserInfo findByUsername(String username) {
        User user = userMapper.findByUsername(username);
        if (user!=null){
            return user.change();
        }else{
            return null;
        }
    }

    @Override
    public UserInfo findAuthority(String username) {
        User user = userMapper.findAuthority(username);
        if (user!=null){
            return user.change();
        }else{
            return null;
        }
    }

    @Override
    public UserInfo addUser(UserInfo userInfo){
        int success = userMapper.addUser(new User(userInfo));
        if(success > 0)
            return userInfo;
        else
            return null;
    }

    @Override
    public boolean updateUserPassword(String username, String password) {
        return userMapper.updateUserPassword(username,password);
    }

    @Override
    public boolean updateUserTel(String username, String telephone) {
        return userMapper.updateUserTel(username, telephone);
    }

    @Override
    public  boolean updateUserIdentify(String username, int identify) {
        return userMapper.updateUserIdentify(username, identify);
    }
}
