package org.zjuvipa.compression.model.info;

import lombok.Data;
import org.zjuvipa.compression.model.entity.History;
import org.zjuvipa.util.MyBeanUtils;

@Data
public class HistoryInfo {

    private Integer historyId;
    private String modelName;
    private String taskType;
    private String checkpointPath;
    private String username;
    private String submitTime;
    private String status;
    private String paramsChange;
    private String flopsChange;
    private String accChange;
    private String lossChange;
    private String prunedPath;
    private String structureBeforePruned;
    private String structureAfterPruned;
    private String logPath;

    private Integer isTraining;
    private Integer totEpoch;
    private Integer currentEpoch;
    private String script;
    private String client;


    public HistoryInfo(){}

    public HistoryInfo(History history){
        MyBeanUtils.copyProperties(history, this);
    }

    public History change(){
        History history = new History();
        MyBeanUtils.copyProperties(this, history);
        return history;
    }
}
