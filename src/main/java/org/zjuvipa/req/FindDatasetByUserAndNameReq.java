package org.zjuvipa.req;

import lombok.Data;

@Data
public class FindDatasetByUserAndNameReq {
    private String username;
    private String datasetName;
}
