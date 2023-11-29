package org.zjuvipa.compression.distributor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.zjuvipa.compression.distributor.info.HistoryInfo;
import org.zjuvipa.compression.distributor.entity.History;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
public interface IDistributorHistoryService extends IService<History> {

    public List<HistoryInfo> findTrainingTask();

    public boolean updateHistory(int historyId);

}
