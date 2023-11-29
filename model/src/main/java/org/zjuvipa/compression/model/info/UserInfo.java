package org.zjuvipa.compression.model.info;

import lombok.Data;
import org.zjuvipa.compression.model.entity.User;
import org.zjuvipa.compression.common.util.MyBeanUtils;

@Data
public class UserInfo {

    private String username;

    private String password;

    private String salt;

    private String telephone;

    private String headshot;

    private String institute;

    private Integer authority;

    public UserInfo(){}

    public UserInfo(User user){
        MyBeanUtils.copyProperties(user, this);
    }
}
