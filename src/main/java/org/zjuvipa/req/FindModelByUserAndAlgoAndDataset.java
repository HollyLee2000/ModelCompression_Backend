package org.zjuvipa.req;

import lombok.Data;

@Data
public class FindModelByUserAndAlgoAndDataset {
    private String username;
    private String algorithm;
    private String dataset;
    private int datasetId;
}
