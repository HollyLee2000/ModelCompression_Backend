package org.zjuvipa.compression.model.req;

import lombok.Data;

@Data
public class FindModelByUserAndAlgoAndDataset {
    private String username;
    private String algorithm;
    private String dataset;
    private int datasetId;
}
