package org.zjuvipa.compression.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.zjuvipa.compression.model.entity.rankList;
import org.zjuvipa.compression.model.info.rankListInfo;
import org.zjuvipa.compression.model.info.qualitativeInfo;
import org.zjuvipa.compression.model.info.leaderboardInfo;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
public interface IGetRankService extends IService<rankList> {
    public List<rankListInfo> getCurrentRank();

    public List<qualitativeInfo> getQualitativeComparison(String dataset, String model);

    public List<leaderboardInfo> getLeaderboard(String dataset, String model);
}
