package org.zjuvipa.req;

import lombok.Data;

@Data
public class RegisterReq {
    private String username;

    private String password;

    private String telephone;

//    private String headshot;

    private Integer authority;

    private String code;

    private String institute;
}
