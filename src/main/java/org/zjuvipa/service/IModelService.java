package org.zjuvipa.service;

import org.zjuvipa.entity.Model;
import com.baomidou.mybatisplus.extension.service.IService;
import org.zjuvipa.info.ModelInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
public interface IModelService extends IService<Model> {
    public int addModel(ModelInfo modelInfo);

    public ModelInfo findModelByName(String username, String modelName);

    public List<ModelInfo> findModelsByUser(String username);

    public List<ModelInfo> findModelsByUserAndAlgo(String username, int algoId);

    public List<ModelInfo> findModelsByUserAndDataset(String username, int datasetId);

    public List<ModelInfo> findModelsByUserAndAlgoAndDataset(String username, int algoId, int datasetId);

    public boolean deleteModel(String username, String modelName);

    public boolean updateModel(int modelId, String newModel);
}
