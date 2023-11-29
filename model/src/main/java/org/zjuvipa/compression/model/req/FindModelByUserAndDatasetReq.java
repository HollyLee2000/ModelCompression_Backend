package org.zjuvipa.compression.model.req;

import lombok.Data;

@Data
public class FindModelByUserAndDatasetReq {

    private String username;

    private String dataset;
    private int datasetId;
}
