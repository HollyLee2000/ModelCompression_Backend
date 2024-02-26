package org.zjuvipa.compression.model.info;

import lombok.Data;
import org.zjuvipa.compression.model.entity.History;
import org.zjuvipa.compression.common.util.MyBeanUtils;

import java.io.Serializable;

@Data
public class HistoryInfo implements Serializable {

    private Integer historyId;
    private String modelName;
    private String taskType;
    private String checkpointPath;
    private String username;
    private String submitTime;
    private String finishTime;
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

    //为了上传模型的任务
    private String dataset;
    private String usrModelName;

    //为了显示方法, importance没有再用上了
    private String importance;
    private String pruner;
    private String importanceLink;
    private String prunerLink;

//    //附加参数
//    private Integer isDG;
//    private Integer isSparsified;

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
