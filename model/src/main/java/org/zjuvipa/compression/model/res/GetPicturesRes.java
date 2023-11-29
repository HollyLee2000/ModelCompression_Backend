package org.zjuvipa.compression.model.res;

import lombok.Data;
import org.zjuvipa.compression.model.info.PictureDataInfo;

import java.util.List;

@Data
public class GetPicturesRes {

    private List<PictureDataInfo> pictures;

}
