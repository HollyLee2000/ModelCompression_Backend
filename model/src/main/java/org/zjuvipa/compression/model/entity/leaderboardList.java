package org.zjuvipa.compression.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.zjuvipa.compression.model.info.leaderboardInfo;
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
public class leaderboardList implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer algorithmId;

    private String dataset;

    private String model;

    private Integer year;

    private String name;

    private String base;

    private String pruned2;

    private String accChange2;

    private String pruned4;

    private String accChange4;

    private String pruned6;

    private String accChange6;

    private String source;

    private String notes;

    public leaderboardList(){}

    public leaderboardList(leaderboardInfo leaderboardinfo){
        MyBeanUtils.copyProperties(leaderboardinfo, this);
    }

    public leaderboardInfo change(){
        leaderboardInfo leaderboardinfo = new leaderboardInfo();
        MyBeanUtils.copyProperties(this, leaderboardinfo);
        return leaderboardinfo;
    }


}
