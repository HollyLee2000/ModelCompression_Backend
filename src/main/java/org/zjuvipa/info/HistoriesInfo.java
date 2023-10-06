package org.zjuvipa.info;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.zjuvipa.entity.Histories;
import org.zjuvipa.util.MyBeanUtils;
import org.zjuvipa.util.ResultBean;

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
