package org.zjuvipa.info;

import lombok.Data;
import org.zjuvipa.entity.leaderboardList;
import org.zjuvipa.util.MyBeanUtils;

@Data
public class leaderboardInfo {

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

    public leaderboardInfo(){}

    public leaderboardInfo(leaderboardList leaderboardlist){
        MyBeanUtils.copyProperties(leaderboardlist, this);
    }
}
