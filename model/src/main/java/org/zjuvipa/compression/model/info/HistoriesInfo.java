package org.zjuvipa.compression.model.info;

import lombok.Data;
import org.zjuvipa.compression.model.entity.Histories;
import org.zjuvipa.compression.common.util.MyBeanUtils;

import java.time.LocalDateTime;

@Data
public class HistoriesInfo {

    private Integer historyId;

    private LocalDateTime historyTime;

    private Integer algoId;

    private String username;

    private Integer datasetId;

    private String modelPath;

    public HistoriesInfo(){}

    public HistoriesInfo(Histories histories){
        MyBeanUtils.copyProperties(histories, this);
    }

}
