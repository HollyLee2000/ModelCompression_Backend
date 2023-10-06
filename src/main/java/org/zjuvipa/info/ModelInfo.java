package org.zjuvipa.info;

import lombok.Data;
import org.zjuvipa.entity.Model;
import org.zjuvipa.util.MyBeanUtils;

@Data
public class ModelInfo {

    private Integer modelId;

    private String modelName;

    private Integer algoId;

    private String algoName;

    private String username;

    private Integer datasetId;

    private String datasetName;

    private String modelPath;

//    private Timestamp dateTime;
    private String dateTime;

    public ModelInfo(){}

    public ModelInfo(Model model){
        MyBeanUtils.copyProperties(model, this);
    }
}
