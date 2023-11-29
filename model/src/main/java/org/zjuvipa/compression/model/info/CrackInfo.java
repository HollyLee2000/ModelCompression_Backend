package org.zjuvipa.compression.model.info;

import lombok.Data;
import org.zjuvipa.compression.model.entity.Crack;
import org.zjuvipa.util.MyBeanUtils;

@Data
public class CrackInfo {

    private Integer dataId;

    private Float crackLength;

    private Float crackMaxwidth;

    private Float crackAvgwidth;

    private Integer crackId;

    public CrackInfo(){}

    public CrackInfo(Crack crack){
        MyBeanUtils.copyProperties(crack, this);
    }
}
