package org.zjuvipa.compression.client155_2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.zjuvipa.compression.client155_2.entity.ClientHistory;
import org.zjuvipa.compression.client155_2.info.ClientHistoryInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
public interface IClientHistoryService2 extends IService<ClientHistory> {

    public List<ClientHistoryInfo> findTrainingTask();

    public boolean uploadHistory(String username, String status, String paramschange, String flopschange, String accchange, String losschange,
                                 String prunedpath, String structureafterpruned, String logpath, int totepoch, int currentepoch,
                                 String script);

}
