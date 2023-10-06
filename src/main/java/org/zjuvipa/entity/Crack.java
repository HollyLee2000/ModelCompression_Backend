package org.zjuvipa.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.zjuvipa.info.CrackInfo;
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
public class Crack implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer dataId;

    private Float crackLength;

    private Float crackMaxwidth;

    private Float crackAvgwidth;

    @TableId(type = IdType.AUTO)
    private Integer crackId;

    public Crack(){}

    public Crack(CrackInfo crackInfo){
        MyBeanUtils.copyProperties(crackInfo, this);
    }

    public CrackInfo change(){
        CrackInfo crackInfo = new CrackInfo();
        MyBeanUtils.copyProperties(this, crackInfo);
        return crackInfo;
    }


}
