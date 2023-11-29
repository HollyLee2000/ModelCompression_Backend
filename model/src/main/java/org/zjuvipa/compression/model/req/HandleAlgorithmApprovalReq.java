package org.zjuvipa.compression.model.req;

import lombok.Data;

@Data
public class HandleAlgorithmApprovalReq {
    private Integer algorithmId;
    private String username;
    private String institute;
    private String name;
    private String morf;
    private String lerf;
}
