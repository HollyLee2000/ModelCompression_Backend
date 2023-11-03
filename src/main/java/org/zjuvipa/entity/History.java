package org.zjuvipa.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.zjuvipa.info.HistoryInfo;
import org.zjuvipa.util.MyBeanUtils;

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
