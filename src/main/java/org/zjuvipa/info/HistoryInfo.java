package org.zjuvipa.info;

import lombok.Data;
import org.zjuvipa.entity.History;
import org.zjuvipa.util.MyBeanUtils;

@Data
public class HistoryInfo {

    private Integer algorithmId;
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
