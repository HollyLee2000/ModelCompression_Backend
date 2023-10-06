package org.zjuvipa.mapper;

import org.apache.ibatis.annotations.Param;
import org.zjuvipa.entity.Histories;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author panyan
 * @since 2022-09-22
 */
public interface HistoriesMapper extends BaseMapper<Histories> {

    public List<Histories> getUserHistories(@Param("username") String username);

}
