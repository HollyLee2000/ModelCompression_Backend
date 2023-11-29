package org.zjuvipa.compression.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.zjuvipa.compression.model.info.rankListInfo;
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
public class rankList implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username;

    private String name;

    private Float score;

    private Integer ranking;

    private String institute;

    public rankList(){}

    public rankList(rankListInfo ranklistinfo){
        MyBeanUtils.copyProperties(ranklistinfo, this);
    }

    public rankListInfo change(){
        rankListInfo ranklistinfo = new rankListInfo();
        MyBeanUtils.copyProperties(this, ranklistinfo);
        return ranklistinfo;
    }


}
