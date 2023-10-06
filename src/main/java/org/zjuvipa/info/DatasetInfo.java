package org.zjuvipa.info;

import lombok.Data;
import org.zjuvipa.entity.Dataset;
import org.zjuvipa.util.MyBeanUtils;

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
