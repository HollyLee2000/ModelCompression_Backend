package org.zjuvipa.compression.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ScriptMessage {
    private String action;
    private Integer taskId;
    private String status;
    private String paramsChange;
    private String flopsChange;
    private String accChange;
    private String lossChange;
    private String prunedPath;
    private String structureAfterPruned;
    private String logPath;
    private Integer totEpoch;
    private Integer currentEpoch;
}
