package org.zjuvipa.compression.backend.service;

import org.zjuvipa.compression.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.zjuvipa.compression.model.info.UserInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
public interface IUserService extends IService<User> {
    public UserInfo findByUsername(String username);

    public UserInfo findAuthority(String username);

    public UserInfo addUser(UserInfo userInfo);

    //个人信息更新
    boolean updateUserPassword(String username,String password);

    boolean updateUserTel(String username, String telephone);

    boolean updateUserIdentify(String username, int identify);
}
