package org.zjuvipa.compression.model.info;

import lombok.Data;
import org.zjuvipa.compression.model.entity.rankList;
import org.zjuvipa.compression.common.util.MyBeanUtils;

@Data
public class rankListInfo {

    private Integer id;

    private String username;

    private String name;

    private Float score;

    private Integer ranking;

    private String institute;

    public rankListInfo(){}

    public rankListInfo(rankList ranklist){
        MyBeanUtils.copyProperties(ranklist, this);
    }
}
