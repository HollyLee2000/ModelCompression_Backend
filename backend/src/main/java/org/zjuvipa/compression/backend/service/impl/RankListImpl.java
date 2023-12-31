package org.zjuvipa.compression.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.zjuvipa.compression.backend.service.IGetRankService;
import org.zjuvipa.compression.model.entity.rankList;
import org.zjuvipa.compression.model.entity.qualitativeList;
import org.zjuvipa.compression.model.info.leaderboardInfo;
import org.zjuvipa.compression.model.entity.leaderboardList;
import org.zjuvipa.compression.model.info.qualitativeInfo;
import org.zjuvipa.compression.model.info.rankListInfo;
import org.zjuvipa.compression.backend.mapper.RankListMapper;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
@Service
public class RankListImpl extends ServiceImpl<RankListMapper, rankList> implements IGetRankService {
    @Resource
    RankListMapper rankListMapper;


    @Override
    public List<rankListInfo> getCurrentRank() {
        List<rankList> ranklist = rankListMapper.getCurrentRank();
        System.out.println("ranklist: " + ranklist);
        if (ranklist!=null){
            List<rankListInfo> rankListInfoList = new ArrayList<>();
            for (rankList rl : ranklist) {
                rankListInfo rli = rl.change();
                rankListInfoList.add(rli);
            }
            return rankListInfoList;
        }else{
            return null;
        }
    }

    @Override
    public List<qualitativeInfo> getQualitativeComparison(String dataset, String model) {
//        if(dataset.equals("") &&model.equals(""))
        List<qualitativeList> qualitativelist = rankListMapper.getQualitativeComparison(dataset, model);
        System.out.println("qualitativelist: " + qualitativelist);
        if (qualitativelist!=null){
            List<qualitativeInfo> qualitativeInfoList = new ArrayList<>();
            for (qualitativeList rl : qualitativelist) {
                qualitativeInfo rli = rl.change();
                qualitativeInfoList.add(rli);
            }
            return qualitativeInfoList;
        }else{
            return null;
        }
    }

    @Override
    public List<leaderboardInfo> getLeaderboard(String dataset, String model) {
        List<leaderboardList> leaderboardlist = rankListMapper.getLeaderboard(dataset, model);
        System.out.println("leaderboardlist: " + leaderboardlist);
        if (leaderboardlist!=null){
            List<leaderboardInfo> leaderboardInfoList = new ArrayList<>();
            for (leaderboardList rl : leaderboardlist) {
                leaderboardInfo rli = rl.change();
                leaderboardInfoList.add(rli);
            }
            return leaderboardInfoList;
        }else{
            return null;
        }
    }


}
