package org.zjuvipa.compression.distributor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.zjuvipa.compression.model.info.HistoryInfo;
import org.zjuvipa.compression.model.entity.History;

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

    public void setTaskIsTraining(int historyId);

    public void setTaskIsReady(int historyId);

    public void setTaskIsFailed(int historyId);

    public History findHistoryById(int historyId);

    public void updateClient(int historyId, String client);

    public boolean SyncHistory(int taskid, String status, String paramschange, String flopschange, String accchange, String losschange,
                               String prunedpath, String structureafterpruned, String logpath, int totepoch, int currentepoch);


}
