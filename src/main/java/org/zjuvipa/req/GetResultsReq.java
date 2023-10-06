package org.zjuvipa.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("获取检测结果")
public class GetResultsReq {
    @ApiModelProperty(value = "模型名", required = true)
    private String modelName;
}
