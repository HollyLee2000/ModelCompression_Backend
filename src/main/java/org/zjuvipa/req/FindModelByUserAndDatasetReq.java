package org.zjuvipa.req;

import lombok.Data;

@Data
public class FindModelByUserAndDatasetReq {

    private String username;

    private String dataset;
    private int datasetId;
}
