package org.zjuvipa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.zjuvipa.entity.qualitativeList;
import org.zjuvipa.entity.leaderboardList;
import org.zjuvipa.entity.rankList;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
public interface RankListMapper extends BaseMapper<rankList> {
    public List<rankList> getCurrentRank();

    public List<qualitativeList> getQualitativeComparison(String dataset, String model);

    public List<leaderboardList> getLeaderboard(String dataset, String model);


}
