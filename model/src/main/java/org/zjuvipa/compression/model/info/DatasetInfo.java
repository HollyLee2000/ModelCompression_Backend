package org.zjuvipa.compression.model.info;

import lombok.Data;
import org.zjuvipa.compression.model.entity.Dataset;
import org.zjuvipa.compression.common.util.MyBeanUtils;

@Data
public class DatasetInfo {

    private Integer datasetId;

    private String username;

    private String datasetName;

    private Integer datasetIspublic;

    public DatasetInfo(){}

    public DatasetInfo(Dataset dataset){
        MyBeanUtils.copyProperties(dataset, this);
    }
}
