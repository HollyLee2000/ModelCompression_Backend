package org.zjuvipa.compression.client155_2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.zjuvipa.compression.client155_2.entity.ClientHistory;
import org.zjuvipa.compression.client155_2.info.ClientHistoryInfo;
import org.zjuvipa.compression.client155_2.mapper.ClientHistoryMapper2;
import org.zjuvipa.compression.client155_2.service.IClientHistoryService2;

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
public class ClientHistoryServiceImpl2 extends ServiceImpl<ClientHistoryMapper2, ClientHistory> implements IClientHistoryService2 {
    @Resource
    ClientHistoryMapper2 historyMapper;


    @Override
    public List<ClientHistoryInfo> findTrainingTask() {
        List<ClientHistory> histories = historyMapper.findTrainingTask();
        List<ClientHistoryInfo> historyInfos = new ArrayList<>();
        if(histories.size()>0) {
            for(ClientHistory history : histories){
                historyInfos.add(history.change());
            }
            return historyInfos;
        }
        return null;
    }

    @Override
    public boolean uploadHistory(String username, String status, String paramschange, String flopschange, String accchange, String losschange,
                                 String prunedpath, String structureafterpruned, String logpath, int totepoch, int currentepoch,
                                 String script) {
        historyMapper.uploadHistory(username, status, paramschange, flopschange, accchange, losschange, prunedpath,
                structureafterpruned, logpath, totepoch, currentepoch, script);
        return true;
    }
}
