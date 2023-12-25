package org.zjuvipa.compression.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.zjuvipa.compression.model.entity.History;
import org.zjuvipa.compression.model.req.FindHistoriesByUserReq;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
public interface HistoryMapper extends BaseMapper<History> {
    public int addHistory(History history);

    public History findHistoryByName(String username, String historyName);

    public List<History> findHistoriesByUser(FindHistoriesByUserReq req);

    public List<History> findAllHistories(String username);

    public List<History> findHistoriesByUserAndAlgo(String username, int algoId);

    public List<History> findHistoriesByUserAndDataset(String username, int datasetId);

    public List<History> findHistoriesByUserAndAlgoAndDataset(String username, int algoId, int datasetId);

    public boolean deleteHistory(String username, String historyName);

    public boolean updateHistory(int historyId, String newName);

    public boolean uploadHistory(String modelname, String tasktype, String checkpointpath, String username, String submittime, String status,
                                 String paramschange, String flopschange, String accchange, String losschange, String prunedpath,
                                 String structurebeforepruned, String structureafterpruned, String logpath);

    public boolean uploadTrainingHistory(String modelname, String tasktype, String checkpointpath, String username, String submittime,
                                         String status, String paramschange, String flopschange, String accchange, String losschange, String prunedpath,
                                         String structurebeforepruned, String structureafterpruned, String logpath, int istrain, int totepoch,
                                         int currentepoch, String script, String client, String algoname, String algolink, String sparsename, String sparselink);

    public boolean uploadUploadingHistory(String modelname, String tasktype, String checkpointpath, String username, String submittime, String status,
                                          String paramschange, String flopschange, String accchange, String losschange, String prunedpath,
                                          String structurebeforepruned, String structureafterpruned, String logpath, int istrain, int totepoch, int currentepoch,
                                          String script, String client, String dataset, String usrModelName);
}
