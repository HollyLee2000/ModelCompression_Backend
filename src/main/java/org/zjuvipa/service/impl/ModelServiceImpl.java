package org.zjuvipa.service.impl;

import org.zjuvipa.entity.Model;
import org.zjuvipa.info.ModelInfo;
import org.zjuvipa.mapper.ModelMapper;
import org.zjuvipa.service.IModelService;
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
 * @since 2022-08-05
 */
@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements IModelService {
    @Resource
    ModelMapper modelMapper;

    @Override
    public int addModel(ModelInfo modelInfo) {
        return modelMapper.addModel(new Model(modelInfo));
    }

    @Override
    public ModelInfo findModelByName(String username, String modelName){
        Model model = modelMapper.findModelByName(username, modelName);
        if(model == null)
            return null;
        else
            return model.change();
    }

    @Override
    public List<ModelInfo> findModelsByUser(String username){
        List<Model> models =  modelMapper.findModelsByUser(username);
        List<ModelInfo> modelInfos = new ArrayList<>();
        if(models.size()>0) {
            for(Model model:models){
                modelInfos.add(model.change());
            }
            return modelInfos;
        }
        return null;
    }

    @Override
    public List<ModelInfo> findModelsByUserAndAlgo(String username, int algoId){
        List<Model> models =  modelMapper.findModelsByUserAndAlgo(username, algoId);
        List<ModelInfo> modelInfos = new ArrayList<>();
        if(models.size()>0) {
            for(Model model:models){
                modelInfos.add(model.change());
            }
            return modelInfos;
        }
        return null;
    }

    @Override
    public List<ModelInfo> findModelsByUserAndDataset(String username, int datasetId){
        List<Model> models =  modelMapper.findModelsByUserAndDataset(username, datasetId);
        List<ModelInfo> modelInfos = new ArrayList<>();
        if(models.size()>0) {
            for(Model model:models){
                modelInfos.add(model.change());
            }
            return modelInfos;
        }
        return null;
    }

    @Override
    public List<ModelInfo> findModelsByUserAndAlgoAndDataset(String username, int algoId, int datasetId){
        List<Model> models =  modelMapper.findModelsByUserAndAlgoAndDataset(username, algoId, datasetId);
        List<ModelInfo> modelInfos = new ArrayList<>();
        if(models.size()>0) {
            for(Model model:models){
                modelInfos.add(model.change());
            }
            return modelInfos;
        }
        return null;
    }

    @Override
    public boolean deleteModel(String username, String modelName){
        return modelMapper.deleteModel(username, modelName);
    }

    @Override
    public boolean updateModel(int modelId, String newName) {
        return modelMapper.updateModel(modelId, newName);
    }
}
