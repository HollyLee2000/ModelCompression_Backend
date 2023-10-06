package org.zjuvipa.req;

import lombok.Data;

@Data
public class CreateDatasetReq {
    private String username;
    private String datasetName;
    private boolean isPublic;
}
