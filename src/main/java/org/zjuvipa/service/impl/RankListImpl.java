package org.zjuvipa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.zjuvipa.entity.rankList;
import org.zjuvipa.info.rankListInfo;
import org.zjuvipa.mapper.RankListMapper;
import org.zjuvipa.service.IGetRankService;

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

}
