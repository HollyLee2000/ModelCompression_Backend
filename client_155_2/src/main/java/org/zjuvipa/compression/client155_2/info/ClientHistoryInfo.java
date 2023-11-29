package org.zjuvipa.compression.client155_2.info;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.zjuvipa.compression.client155_2.entity.ClientHistory;
import org.zjuvipa.compression.client155_2.util.MyBeanUtils;

@Data
public class ClientHistoryInfo {

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


    public ClientHistoryInfo(){}

    public ClientHistoryInfo(ClientHistory history){
        MyBeanUtils.copyProperties(history, this);
    }

    public ClientHistory change(){
        ClientHistory history = new ClientHistory();
        MyBeanUtils.copyProperties(this, history);
        return history;
    }
}
