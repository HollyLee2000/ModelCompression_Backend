package org.zjuvipa.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@ApiModel("调用算法")
public class CallAlgorithmReq {
    @ApiModelProperty(value = "用户名", required = true)
    private String userName;

    @ApiModelProperty(value = "算法名", required = true)
    private String algorithmName;

    @ApiModelProperty(value = "数据集名", required = true)
    private String datasetName;

    private int datasetId;

    @ApiModelProperty(value = "模型名", required = true)
    private String modelName;
}
