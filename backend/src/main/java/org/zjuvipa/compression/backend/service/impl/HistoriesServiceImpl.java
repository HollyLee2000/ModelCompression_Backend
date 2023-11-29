package org.zjuvipa.compression.backend.service.impl;

import org.zjuvipa.compression.backend.service.IHistoriesService;
import org.zjuvipa.compression.model.entity.Histories;
import org.zjuvipa.compression.model.info.HistoriesInfo;
import org.zjuvipa.compression.backend.mapper.HistoriesMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author panyan
 * @since 2022-09-22
 */
@Service
public class HistoriesServiceImpl extends ServiceImpl<HistoriesMapper, Histories> implements IHistoriesService {

    @Resource
    HistoriesMapper historiesMapper;

    @Override
    public List<HistoriesInfo> getUserHistories(String username) {
        List<HistoriesInfo> historiesInfos = new ArrayList<>();
        List<Histories> histories = historiesMapper.getUserHistories(username);
        for(Histories h: histories){
            historiesInfos.add(h.change());
        }
        return historiesInfos;
    }
}
