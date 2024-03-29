package org.zjuvipa.compression.distributor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.zjuvipa.compression.distributor.service.IDistributorHistoryService;
import org.zjuvipa.compression.model.info.HistoryInfo;
import org.zjuvipa.compression.distributor.mapper.DistributorHistoryMapper;
import org.zjuvipa.compression.model.entity.History;



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
public class DistributorHistoryServiceImpl extends ServiceImpl<DistributorHistoryMapper, History> implements IDistributorHistoryService {
    @Resource
    DistributorHistoryMapper historyMapper;


    @Override
    public List<HistoryInfo> findTrainingTask() {
        List<History> histories = historyMapper.findTrainingTask();
        List<HistoryInfo> historyInfos = new ArrayList<>();
        if(histories.size()>0) {
            for(History history : histories){
                historyInfos.add(history.change());
            }
            return historyInfos;
        }
        return null;
    }

    public boolean updateHistory(int historyId){
        historyMapper.updateHistory(historyId);
        return true;
    }

    public void setTaskIsTraining(int historyId){
        historyMapper.setTaskIsTraining(historyId);
    }

    public void setTaskIsReady(int historyId){
        historyMapper.setTaskIsReady(historyId);
    }

    public void updateClient(int historyId, String client){
        historyMapper.updateClient(historyId, client);
    }

    public History findHistoryById(int historyId){
        return historyMapper.findHistoryById(historyId);
    }

    public void setTaskIsFailed(int historyId){
        historyMapper.setTaskIsFailed(historyId);
    }

    @Override
    public boolean SyncHistory(int taskid, String status, String paramschange, String flopschange, String accchange, String losschange, String prunedpath, String structureafterpruned, String logpath, int totepoch, int currentepoch) {
        historyMapper.SyncHistory(taskid, status, paramschange, flopschange, accchange, losschange, prunedpath,
                structureafterpruned, logpath, totepoch, currentepoch);
        return true;
    }

    @Override
    public boolean SyncHistoryWithFinishTime(int taskid, String status, String paramschange, String flopschange, String accchange, String losschange, String prunedpath, String structureafterpruned, String logpath, int totepoch, int currentepoch) {
        historyMapper.SyncHistoryWithFinishTime(taskid, status, paramschange, flopschange, accchange, losschange, prunedpath,
                structureafterpruned, logpath, totepoch, currentepoch);
        return true;
    }




}
