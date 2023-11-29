package org.zjuvipa.compression.model.req;

import lombok.Data;

@Data
public class GetUserDatasetsReq {
    private String searchname;
    private String username;
}
