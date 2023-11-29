package org.zjuvipa.compression.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.zjuvipa.compression.model.info.ClientHistoryInfo;
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
public class ClientHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer taskId;
    private String username;
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
    private String script;



    public ClientHistory(){}

    public ClientHistory(ClientHistoryInfo historyInfo){
        MyBeanUtils.copyProperties(historyInfo, this);
    }

    public ClientHistoryInfo change(){
        ClientHistoryInfo historyInfo = new ClientHistoryInfo();
        MyBeanUtils.copyProperties(this, historyInfo);
        return historyInfo;
    }


}
