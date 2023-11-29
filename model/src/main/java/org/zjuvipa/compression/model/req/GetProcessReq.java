package org.zjuvipa.compression.model.req;

import lombok.Data;

@Data
public class GetProcessReq {
    private int batchSize;
    private String dataset;
}
