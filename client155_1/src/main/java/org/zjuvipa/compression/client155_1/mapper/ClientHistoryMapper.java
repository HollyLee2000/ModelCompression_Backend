package org.zjuvipa.compression.client155_1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.zjuvipa.compression.model.entity.ClientHistory;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hollylee
 * @since 2022-08-05
 */
public interface ClientHistoryMapper extends BaseMapper<ClientHistory> {

    public List<ClientHistory> findTrainingTask();

    public boolean uploadHistory(int historyid, String username, String status, String paramschange, String flopschange, String accchange, String losschange,
                                 String prunedpath, String structureafterpruned, String logpath, int totepoch, int currentepoch,
                                 String script);

    public List<ClientHistory> findHistoryById(int historyid);

}
