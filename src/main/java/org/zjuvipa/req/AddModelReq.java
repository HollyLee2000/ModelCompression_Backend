package org.zjuvipa.req;

import lombok.Data;

@Data
public class AddModelReq {
    private String username;

    private String modelName;

    private String algorithm;

//    private String dataset;
    private int datasetId;

    private String modelPath;
}
