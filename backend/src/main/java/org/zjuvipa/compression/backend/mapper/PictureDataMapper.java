package org.zjuvipa.compression.mapper;

import org.apache.ibatis.annotations.Param;
import org.zjuvipa.model.entity.PictureData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.zjuvipa.model.info.PictureDataInfo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
public interface PictureDataMapper extends BaseMapper<PictureData> {

    public List<PictureData> getPicture(@Param("datasetId") int datasetId);

//    public List<PictureData> getAttributionPicture();

    public boolean uploadPicture(@Param("url") String url, @Param("name") String name, @Param("datasetId") int datasetId);

    public int deleteDatasetPictures(@Param("datasetId") int datasetId);

}
