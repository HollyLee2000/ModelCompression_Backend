package org.zjuvipa.compression.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.zjuvipa.compression.backend.service.IHistoryService;
import org.zjuvipa.compression.model.entity.History;
import org.zjuvipa.compression.model.info.HistoryInfo;
import org.zjuvipa.compression.backend.mapper.HistoryMapper;
import org.zjuvipa.compression.model.req.FindHistoriesByUserReq;

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
public class HistoryServiceImpl extends ServiceImpl<HistoryMapper, History> implements IHistoryService {
    @Resource
    HistoryMapper historyMapper;

    @Override
    public int addHistory(HistoryInfo historyInfo) {
        return historyMapper.addHistory(new History(historyInfo));
    }

    @Override
    public HistoryInfo findHistoryByName(String username, String historyName){
        History history = historyMapper.findHistoryByName(username, historyName);
        if(history == null)
            return null;
        else
            return history.change();
    }

    @Override
    public boolean uploadHistory(String modelname, String tasktype, String checkpointpath, String username, String submittime, String status,
                                 String paramschange, String flopschange, String accchange, String losschange, String prunedpath,
                                 String structurebeforepruned, String structureafterpruned, String logpath) {
        historyMapper.uploadHistory(modelname, tasktype, checkpointpath, username, submittime, status, paramschange,
                flopschange, accchange, losschange, prunedpath, structurebeforepruned, structureafterpruned, logpath);
        return true;
    }

    @Override
    public boolean uploadTrainingHistory(String modelname, String tasktype, String checkpointpath, String username, String submittime,
                                         String status, String paramschange, String flopschange, String accchange, String losschange, String prunedpath,
                                         String structurebeforepruned, String structureafterpruned, String logpath, int istrain, int totepoch,
                                         int currentepoch, String script, String client, String algoname, String algolink, String sparsename, String sparselink) {
        historyMapper.uploadTrainingHistory(modelname, tasktype, checkpointpath, username, submittime, status, paramschange,
                flopschange, accchange, losschange, prunedpath, structurebeforepruned, structureafterpruned, logpath, istrain, totepoch,
                currentepoch, script, client, algoname, algolink, sparsename, sparselink);
        return true;
    }

    @Override
    public boolean uploadUploadingHistory(String modelname, String tasktype, String checkpointpath, String username, String submittime,
                                         String status, String paramschange, String flopschange, String accchange, String losschange, String prunedpath,
                                         String structurebeforepruned, String structureafterpruned, String logpath, int istrain, int totepoch,
                                         int currentepoch, String script, String client, String dataset, String usrModelName) {
        historyMapper.uploadUploadingHistory(modelname, tasktype, checkpointpath, username, submittime, status, paramschange,
                flopschange, accchange, losschange, prunedpath, structurebeforepruned, structureafterpruned, logpath, istrain, totepoch,
                currentepoch, script, client, dataset, usrModelName);
        return true;
    }



    @Override
    public PageInfo<History> findHistoriesByUser(Integer pageNum, Integer pageSize, FindHistoriesByUserReq req){
        PageHelper.startPage(pageNum,pageSize);
        List<History> histories =  historyMapper.findHistoriesByUser(req);
        System.out.println("装页之前的数据：" + histories);
        PageInfo<History> pageInfo = new PageInfo<>(histories);
        System.out.println("装页之后的数据：" + pageInfo);
        return pageInfo;
//        List<HistoryInfo> historyInfos = new ArrayList<>();
//        if(histories.size()>0) {
//            for(History history : histories){
//                historyInfos.add(history.change());
//            }
//            PageInfo<HistoryInfo> pageInfo = new PageInfo<>(historyInfos);
//            System.out.println("装页之后的数据：" + pageInfo);
//            return pageInfo;
//        }
//        return null;
    }

    @Override
    public List<HistoryInfo> findAllHistories(String username) {
        List<History> histories =  historyMapper.findAllHistories(username);
        List<HistoryInfo> historyInfos = new ArrayList<>();
        if(histories.size()>0) {
            for(History history : histories){
                historyInfos.add(history.change());
            }
            return historyInfos;
        }
        return null;
    }

    @Override
    public List<HistoryInfo> findHistoriesByUserAndAlgo(String username, int algoId){
        List<History> histories =  historyMapper.findHistoriesByUserAndAlgo(username, algoId);
        List<HistoryInfo> historyInfos = new ArrayList<>();
        if(histories.size()>0) {
            for(History history : histories){
                historyInfos.add(history.change());
            }
            return historyInfos;
        }
        return null;
    }

    @Override
    public List<HistoryInfo> findHistoriesByUserAndDataset(String username, int datasetId){
        List<History> histories =  historyMapper.findHistoriesByUserAndDataset(username, datasetId);
        List<HistoryInfo> historyInfos = new ArrayList<>();
        if(histories.size()>0) {
            for(History history : histories){
                historyInfos.add(history.change());
            }
            return historyInfos;
        }
        return null;
    }

    @Override
    public List<HistoryInfo> findHistoriesByUserAndAlgoAndDataset(String username, int algoId, int datasetId){
        List<History> histories =  historyMapper.findHistoriesByUserAndAlgoAndDataset(username, algoId, datasetId);
        List<HistoryInfo> historyInfos = new ArrayList<>();
        if(histories.size()>0) {
            for(History history : histories){
                historyInfos.add(history.change());
            }
            return historyInfos;
        }
        return null;
    }

    @Override
    public boolean deleteHistory(String username, String historyName){
        return historyMapper.deleteHistory(username, historyName);
    }

    @Override
    public boolean updateHistory(int historyId, String newName) {
        return historyMapper.updateHistory(historyId, newName);
    }
}
