package org.zjuvipa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.zjuvipa.entity.rankList;
import org.zjuvipa.info.rankListInfo;
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
}
