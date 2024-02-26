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

    public History(){}

    public History(HistoryInfo historyInfo){
        MyBeanUtils.copyProperties(historyInfo, this);
    }

    public History(String modelname, String tasktype, String checkpointpath,
                   String username, String status, String paramschange, String flopschange,
                   String accchange, String losschange, String prunedpath, String structurebeforepruned,
                   String structureafterpruned, String logpath, Integer isTraining, Integer totEpoch,
                   Integer currentEpoch, String script,
                   String client, String importance, String importanceLink, String pruner, String prunerLink) {
        this.modelName = modelname;
        this.taskType = tasktype;
        this.checkpointPath = checkpointpath;
        this.username = username;
        this.status = status;
        this.paramsChange = paramschange;
        this.flopsChange = flopschange;
        this.accChange = accchange;
        this.lossChange = losschange;
        this.prunedPath = prunedpath;
        this.structureBeforePruned = structurebeforepruned;
        this.structureAfterPruned = structureafterpruned;
        this.logPath = logpath;
        this.isTraining = isTraining;
        this.totEpoch = totEpoch;
        this.currentEpoch = currentEpoch;
        this.script = script;
        this.client = client;
        this.importance = importance;
        this.importanceLink = importanceLink;
        this.pruner = pruner;
        this.prunerLink = prunerLink;
    }

    //        history (model_name, task_type, checkpoint_path, username, submit_time, status, params_change, flops_change, acc_change, loss_change, pruned_path, structure_before_pruned, structure_after_pruned, log_path,
//                is_training, tot_epoch, current_epoch, script, client, dataset, usrModelName, importance, importance_link, pruner, pruner_link)
//        values (#{modelname}, #{tasktype}, #{checkpointpath}, #{username}, #{submittime}, #{status}, #{paramschange}, #{flopschange}, #{accchange}, #{losschange},
//        #{prunedpath}, #{structurebeforepruned}, #{structureafterpruned}, #{logpath}, #{istrain}, #{totepoch}, #{currentepoch}, #{script}, #{client}, #{dataset}, #{usrModelName}
    public History(String modelname, String tasktype, String checkpointpath,
                   String username, String status, String paramschange, String flopschange,
                   String accchange, String losschange, String prunedpath, String structurebeforepruned,
                   String structureafterpruned, String logpath, Integer isTraining, Integer totEpoch,
                   Integer currentEpoch, String script,
                   String client, String dataset, String usrModelName) {
        this.modelName = modelname;
        this.taskType = tasktype;
        this.checkpointPath = checkpointpath;
        this.username = username;
        this.submitTime = submitTime;
        this.status = status;
        this.paramsChange = paramschange;
        this.flopsChange = flopschange;
        this.accChange = accchange;
        this.lossChange = losschange;
        this.prunedPath = prunedpath;
        this.structureBeforePruned = structurebeforepruned;
        this.structureAfterPruned = structureafterpruned;
        this.logPath = logpath;
        this.isTraining = isTraining;
        this.totEpoch = totEpoch;
        this.currentEpoch = currentEpoch;
        this.script = script;
        this.client = client;

        this.dataset = dataset;
        this.usrModelName = usrModelName;
    }

    public HistoryInfo change(){
        HistoryInfo historyInfo = new HistoryInfo();
        MyBeanUtils.copyProperties(this, historyInfo);
        return historyInfo;
    }


}
