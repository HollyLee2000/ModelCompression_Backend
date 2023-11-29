package org.zjuvipa.compression.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.zjuvipa.compression.model.info.qualitativeInfo;
import org.zjuvipa.util.MyBeanUtils;

import java.io.Serializable;

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
public class qualitativeList implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
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

    public qualitativeList(){}

    public qualitativeList(qualitativeInfo qualitativeinfo){
        MyBeanUtils.copyProperties(qualitativeinfo, this);
    }

    public qualitativeInfo change(){
        qualitativeInfo qualitativeinfo = new qualitativeInfo();
        MyBeanUtils.copyProperties(this, qualitativeinfo);
        return qualitativeinfo;
    }


}
