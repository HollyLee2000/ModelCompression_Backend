package org.zjuvipa.service.impl;

import org.zjuvipa.entity.PictureData;
import org.zjuvipa.info.PictureDataInfo;
import org.zjuvipa.mapper.PictureDataMapper;
import org.zjuvipa.service.IPictureDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
@Service
public class PictureDataServiceImpl extends ServiceImpl<PictureDataMapper, PictureData> implements IPictureDataService {

    @Resource
    PictureDataMapper pictureDataMapper;

    public boolean deletePictures(List<Integer> ids) {
        for (Integer i : ids) {
            pictureDataMapper.deleteById(i);
        }
        return true;
    }

    public List<PictureDataInfo> getPictures(int id) {
        List<PictureDataInfo> result = new ArrayList<>();
        List<PictureData> picture = pictureDataMapper.getPicture(id);
        for(PictureData pictureData: picture){
            result.add(pictureData.change());
        }
        return result;
    }

    public List<String> getAttributionPictures() {
        List<String> result = new ArrayList<>();
        result.add("10.214.211.106:7996/img/origin.png");
        result.add("10.214.211.106:7996/img/saliency.png");
        return result;
    }

    public List<String> getPoolPictures(String dataset){
        List<String> result = new ArrayList<>();
        for(int i=1; i<=36; i++){
            result.add("10.214.211.106:7996/img/" + dataset + "_sample_" + i + ".jpg");
        }
        return result;
    }

    @Override
    public boolean uploadPictures(List<String> pictureSavePaths, List<String> pictureNames, int datasetId) {
        for (int i=0; i<pictureNames.size(); i++){
            pictureDataMapper.uploadPicture(pictureSavePaths.get(i), pictureNames.get(i), datasetId);
        }
        return true;
    }

}
