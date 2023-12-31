package org.zjuvipa.compression.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.zjuvipa.compression.model.info.HistoryInfo;
import org.zjuvipa.compression.common.util.MyBeanUtils;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class History implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
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

    public History(){}

    public History(HistoryInfo historyInfo){
        MyBeanUtils.copyProperties(historyInfo, this);
    }

    public HistoryInfo change(){
        HistoryInfo historyInfo = new HistoryInfo();
        MyBeanUtils.copyProperties(this, historyInfo);
        return historyInfo;
    }


}
