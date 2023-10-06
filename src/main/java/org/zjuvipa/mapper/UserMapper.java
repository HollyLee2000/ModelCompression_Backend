package org.zjuvipa.mapper;

import org.apache.ibatis.annotations.Param;
import org.zjuvipa.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
public interface UserMapper extends BaseMapper<User> {
    public User findByUsername(@Param("username") String username);

    public User findAuthority(@Param("username") String username);

    public int addUser(User user);

    public boolean updateUserPassword(String username,String password);

    public boolean updateUserTel(String username, String telephone);

    public boolean updateUserIdentify(String username, int identify);
}
