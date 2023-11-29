package org.zjuvipa.compression.model.req;

import lombok.Data;

@Data
public class FindModelByUserAndAlgoReq {
    private String username;
    private String algorithm;
}
