package org.zjuvipa.compression.model.info;

import lombok.Data;
import org.zjuvipa.compression.model.entity.PictureData;
import org.zjuvipa.util.MyBeanUtils;

@Data
public class PictureDataInfo {

    private Integer pictureId;

    private String pictureUrl;

    private String pictureName;

    private String pictureResult;

    private Integer datasetId;


    public PictureDataInfo(){}

    public PictureDataInfo(PictureData pictureData){
        MyBeanUtils.copyProperties(pictureData, this);
    }
}
