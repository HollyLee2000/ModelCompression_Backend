package org.zjuvipa.compression.model.info;

import lombok.Data;
import org.zjuvipa.compression.model.entity.qualitativeList;
import org.zjuvipa.compression.common.util.MyBeanUtils;

@Data
public class qualitativeInfo {

    private Integer algorithmId;

    private String dataset;

    private String model;

    private Integer year;

    private String name;

    private String base;

    private String pruned;

    private String accChange;

    private String speedUp;

    private String source;

    private String notes;

    public qualitativeInfo(){}

    public qualitativeInfo(qualitativeList qualitativelist){
        MyBeanUtils.copyProperties(qualitativelist, this);
    }
}
