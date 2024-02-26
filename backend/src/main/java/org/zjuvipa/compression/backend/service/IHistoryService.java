package org.zjuvipa.compression.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.zjuvipa.compression.model.entity.History;
import org.zjuvipa.compression.model.info.HistoryInfo;
import org.zjuvipa.compression.model.info.InfoInfo;
import org.zjuvipa.compression.model.req.FindHistoriesByUserReq;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
public interface IHistoryService extends IService<History> {
    public int addHistory(HistoryInfo historyInfo);

    public HistoryInfo findHistoryByName(String username, String historyName);

    public PageInfo<History> findHistoriesByUser(Integer pageNum, Integer pageSize, FindHistoriesByUserReq req);

    public PageInfo<History> findHistories(Integer pageNum, Integer pageSize, FindHistoriesByUserReq req);

    public List<InfoInfo> findHistoriesByUserNew(String username);

    public List<InfoInfo> findAllHistories(String username);

    public boolean uploadHistory(String modelname, String tasktype, String checkpointpath, String username, String submittime, String status,
                                 String paramschange, String flopschange, String accchange, String losschange, String prunedpath,
                                 String structurebeforepruned, String structureafterpruned, String logpath);

    public void setStatusWaiting(Integer historyId);

    public void setStatusRejected(Integer historyId);

//    public boolean uploadTrainingHistory(String modelname, String tasktype, String checkpointpath, String username, String submittime, String status,
//                                         String paramschange, String flopschange, String accchange, String losschange, String prunedpath,
//                                         String structurebeforepruned, String structureafterpruned, String logpath, int istrain, int totepoch, int currentepoch,
//                                         String script, String client, String algoname, String algolink, String sparsename, String sparselink);

    public boolean uploadTrainingHistory(History history);

    public boolean uploadUploadingHistory(History history);





    public List<HistoryInfo> findHistoriesByUserAndAlgo(String username, int algoId);

    public List<HistoryInfo> findHistoriesByUserAndDataset(String username, int datasetId);

    public List<HistoryInfo> findHistoriesByUserAndAlgoAndDataset(String username, int algoId, int datasetId);

    public boolean deleteHistory(String username, String historyName);

    public boolean updateHistory(int historyId, String newHistory);


}
