package org.zjuvipa.compression.model.req;

import lombok.Data;

@Data
public class CreateDatasetReq {
    private String username;
    private String datasetName;
    private boolean isPublic;
}
