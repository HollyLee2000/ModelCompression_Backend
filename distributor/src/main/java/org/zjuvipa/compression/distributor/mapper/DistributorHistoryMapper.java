package org.zjuvipa.compression.distributor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.zjuvipa.compression.distributor.entity.History;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hollylee
 * @since 2022-08-05
 */
public interface DistributorHistoryMapper extends BaseMapper<History> {

    public List<History> findTrainingTask();

    public boolean updateHistory(int historyId);
}
