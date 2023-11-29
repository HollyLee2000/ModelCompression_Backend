package org.zjuvipa.compression.backend.service;

import org.zjuvipa.compression.model.entity.Dataset;
import com.baomidou.mybatisplus.extension.service.IService;
import org.zjuvipa.compression.model.info.DatasetInfo;
import org.zjuvipa.compression.model.res.GetDatasetInfoRes;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
public interface IDatasetService extends IService<Dataset> {
    public List<DatasetInfo> getUserDatasets(String username);

    public List<DatasetInfo> getUserVisibleDatasets(String searchname, String username);

    public List<DatasetInfo> getVisibleDatasets(String username);

    public List<DatasetInfo> getAllDatasets();

    public List<DatasetInfo> getPublicDatasets();

    public DatasetInfo findDatasetByUserAndName(String username, String datasetName);

    public List<DatasetInfo> blurredFindDatasetByUserAndName(String username, String datasetName);

    public List<DatasetInfo> blurredFindDatasetByName(String datasetName);

    public boolean deleteDataset(int datasetId);

    public DatasetInfo getDatasetInfo(int datasetId);

    public boolean renameDataset(int datasetId, String newName);

    public boolean createDataset(String usernmae, String datasetName, boolean isPublic);

    public DatasetInfo getNewDataset();
}
