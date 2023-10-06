package org.zjuvipa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.zjuvipa.entity.History;
import org.zjuvipa.info.HistoryInfo;

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

    public List<HistoryInfo> findHistoriesByUser(String username);

    public List<HistoryInfo> findAllHistories(String username);

    public List<HistoryInfo> findHistoriesByUserAndAlgo(String username, int algoId);

    public List<HistoryInfo> findHistoriesByUserAndDataset(String username, int datasetId);

    public List<HistoryInfo> findHistoriesByUserAndAlgoAndDataset(String username, int algoId, int datasetId);

    public boolean deleteHistory(String username, String historyName);

    public boolean updateHistory(int historyId, String newHistory);
}
