package org.zjuvipa.compression.model.req;

import lombok.Data;

@Data
public class LoginReq {
    private String username;

    private String password;

    private String code;
}
