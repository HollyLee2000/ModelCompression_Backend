package org.zjuvipa.compression.backend.service;

import org.zjuvipa.compression.model.entity.Histories;
import com.baomidou.mybatisplus.extension.service.IService;
import org.zjuvipa.compression.model.info.HistoriesInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author panyan
 * @since 2022-09-22
 */
public interface IHistoriesService extends IService<Histories> {

    public List<HistoriesInfo> getUserHistories(String username);

}
