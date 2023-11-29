package org.zjuvipa.compression.model.res;

import lombok.Data;

@Data
public class GetProcessRes {
    private int process;
    private int total;
    private boolean prunner;
}
