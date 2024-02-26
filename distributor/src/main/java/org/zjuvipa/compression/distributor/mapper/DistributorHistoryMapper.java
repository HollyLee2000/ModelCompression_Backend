package org.zjuvipa.compression.distributor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.zjuvipa.compression.model.entity.History;
import org.zjuvipa.compression.model.info.HistoryInfo;

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

    public void setTaskIsTraining(int historyId);

    public void setTaskIsReady(int historyId);

    public void setTaskIsFailed(int historyId);

    public void updateClient(int historyId, String client);

    public History findHistoryById(int historyId);

    public boolean SyncHistory(int taskid, String status, String paramschange, String flopschange,
                               String accchange, String losschange, String prunedpath,
                               String structureafterpruned, String logpath, int totepoch, int currentepoch);

    public boolean SyncHistoryWithFinishTime(int taskid, String status, String paramschange, String flopschange,
                               String accchange, String losschange, String prunedpath,
                               String structureafterpruned, String logpath, int totepoch, int currentepoch);

}
