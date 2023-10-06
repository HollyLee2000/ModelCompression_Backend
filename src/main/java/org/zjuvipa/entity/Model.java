package org.zjuvipa.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.zjuvipa.info.ModelInfo;
import org.zjuvipa.util.MyBeanUtils;

/**
 * <p>
 * 
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Model implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
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

    public Model(){}

    public Model(ModelInfo modelInfo){
        MyBeanUtils.copyProperties(modelInfo, this);
    }

    public ModelInfo change(){
        ModelInfo modelInfo = new ModelInfo();
        MyBeanUtils.copyProperties(this, modelInfo);
        return modelInfo;
    }


}
