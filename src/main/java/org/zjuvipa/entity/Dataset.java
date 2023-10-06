package org.zjuvipa.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.zjuvipa.info.DatasetInfo;
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
public class Dataset implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer datasetId;

    private String username;

    private Integer dataId;

    private String datasetName;

    @TableField("dataset_isPublic")
    private Integer datasetIspublic;

    public Dataset(){}

    public Dataset(DatasetInfo datasetInfo){
        MyBeanUtils.copyProperties(datasetInfo, this);
    }

    public DatasetInfo change(){
        DatasetInfo datasetInfo = new DatasetInfo();
        MyBeanUtils.copyProperties(this, datasetInfo);
        return datasetInfo;
    }


}
