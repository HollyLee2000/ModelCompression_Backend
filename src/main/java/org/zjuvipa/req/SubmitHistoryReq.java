package org.zjuvipa.req;

import lombok.Data;

@Data
public class SubmitHistoryReq {
    private String username;
    private String modelname;
    private String tasktype;
    private String checkpointpath;
    private String status;
    private String paramschange;
    private String flopschange;
    private String accchange;
    private String losschange;
    private String prunedpath;
    private String structurebeforepruned;
    private String structureafterpruned;
    private String logpath;
}
