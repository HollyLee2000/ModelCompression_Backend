package org.zjuvipa.req;

import lombok.Data;

@Data
public class FindModelByUserAndAlgoReq {
    private String username;
    private String algorithm;
}
