package org.zjuvipa.compression.client155_2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.zjuvipa.compression.model.entity.ClientHistory;
import org.zjuvipa.compression.model.info.ClientHistoryInfo;
import org.zjuvipa.compression.client155_2.mapper.ClientHistoryMapper;
import org.zjuvipa.compression.client155_2.service.IClientHistoryService;

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
public class ClientHistoryServiceImpl extends ServiceImpl<ClientHistoryMapper, ClientHistory> implements IClientHistoryService {
    @Resource
    ClientHistoryMapper historyMapper;


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
    public boolean uploadHistoryIfNotExist(int historyid, String username, String status, String paramschange, String flopschange, String accchange, String losschange, String prunedpath, String structureafterpruned, String logpath, int totepoch, int currentepoch, String script) {
        List<ClientHistory> re = historyMapper.findHistoryById(historyid);
        if(re.isEmpty()){
            System.out.println("没有查询到该id的记录，应该加入数据库");
            uploadHistory(historyid, username, status, paramschange, flopschange, accchange, losschange, prunedpath,
                    structureafterpruned, logpath, totepoch, currentepoch, script);
            return true;
        }else{
            System.out.println("查询到已有该id的记录，不应该再加入数据库");
            return false;
        }
    }

    @Override
    public boolean uploadHistory(int historyid, String username, String status, String paramschange, String flopschange, String accchange, String losschange,
                                 String prunedpath, String structureafterpruned, String logpath, int totepoch, int currentepoch,
                                 String script) {
        historyMapper.uploadHistory(historyid, username, status, paramschange, flopschange, accchange, losschange, prunedpath,
                structureafterpruned, logpath, totepoch, currentepoch, script);
        return true;
    }

    @Override
    public boolean updateHistoryAfterPruned(int taskid, String status, String paramschange, String flopschange, String accchange, String losschange, String prunedpath, String structureafterpruned, String logpath, int totepoch, int currentepoch) {
        historyMapper.updateHistoryAfterPruned(taskid, status, paramschange, flopschange, accchange, losschange, prunedpath,
                structureafterpruned, logpath, totepoch, currentepoch);
        return true;
    }

    @Override
    public List<ClientHistory> findWaitingTask() {
        return historyMapper.findWaitingTask();
    }

    @Override
    public List<ClientHistory> findExecutingTask() {
        return historyMapper.findExecutingTask();
    }

    public boolean updateHistoryAfterLaunch(int taskid){
        historyMapper.updateHistoryAfterLaunch(taskid);
        return true;
    }

    public boolean updateHistoryAfterFailed(int taskid){
        historyMapper.updateHistoryAfterFailed(taskid);
        return true;
    }
}
