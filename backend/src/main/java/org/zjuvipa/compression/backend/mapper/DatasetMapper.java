package org.zjuvipa.compression.backend.mapper;

import org.apache.ibatis.annotations.Param;
import org.zjuvipa.compression.model.entity.Dataset;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.zjuvipa.compression.model.info.DatasetInfo;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
public interface DatasetMapper extends BaseMapper<Dataset> {
    public List<Dataset> getUserDatasets(@Param("username") String username);

    public List<Dataset> getUserVisibleDatasets(@Param("searchname") String searchname, @Param("username") String username);

    public List<Dataset> getVisibleDatasets(@Param("username") String username);

    public List<Dataset> getAllDatasets();

    public List<Dataset> getPublicDatasets();

    public Dataset findDatasetByUserAndName(String username, String datasetName);

    public List<Dataset> blurredFindDatasetByUserAndName(String username, String datasetName);

    public List<Dataset> blurredFindDatasetByName(String datasetName);

    public boolean deleteDataset(@Param("datasetId") int datasetId);

    public Dataset getDatasetInfo(@Param("datasetId") int datasetId);

    public boolean renameDataset(@Param("datasetId") int datasetId, @Param("newName") String newName);

    public boolean createDataset(@Param("username") String usernmae, @Param("datasetName") String datasetName, @Param("isPublic") boolean isPublic);

    public Dataset getNewDataset();

}
