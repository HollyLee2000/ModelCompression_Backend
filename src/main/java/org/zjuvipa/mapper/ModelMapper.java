package org.zjuvipa.mapper;

import org.zjuvipa.entity.Model;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
public interface ModelMapper extends BaseMapper<Model> {
    public int addModel(Model model);

    public Model findModelByName(String username, String modelName);

    public List<Model> findModelsByUser(String username);

    public List<Model> findModelsByUserAndAlgo(String username, int algoId);

    public List<Model> findModelsByUserAndDataset(String username, int datasetId);

    public List<Model> findModelsByUserAndAlgoAndDataset(String username, int algoId, int datasetId);

    public boolean deleteModel(String username, String modelName);

    public boolean updateModel(int modelId, String newName);
}
