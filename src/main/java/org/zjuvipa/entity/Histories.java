package org.zjuvipa.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.zjuvipa.info.DatasetInfo;
import org.zjuvipa.info.HistoriesInfo;
import org.zjuvipa.util.MyBeanUtils;

/**
 * <p>
 * 
 * </p>
 *
 * @author panyan
 * @since 2022-09-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Histories implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "history_id", type = IdType.AUTO)
    private Integer historyId;

    private LocalDateTime historyTime;

    private Integer algoId;

    private String username;

    private Integer datasetId;

    private String modelPath;

    public Histories(){}

    public Histories(HistoriesInfo historiesInfo){
        MyBeanUtils.copyProperties(historiesInfo, this);
    }

    public HistoriesInfo change(){
        HistoriesInfo historiesInfo = new HistoriesInfo();
        MyBeanUtils.copyProperties(this, historiesInfo);
        return historiesInfo;
    }

}
