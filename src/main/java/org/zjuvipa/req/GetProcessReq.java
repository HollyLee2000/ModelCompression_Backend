package org.zjuvipa.req;

import lombok.Data;

@Data
public class GetProcessReq {
    private int batchSize;
    private String dataset;
}
