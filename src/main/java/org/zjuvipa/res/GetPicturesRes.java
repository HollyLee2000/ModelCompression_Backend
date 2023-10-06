package org.zjuvipa.res;

import lombok.Data;
import org.zjuvipa.info.PictureDataInfo;

import java.util.List;

@Data
public class GetPicturesRes {

    private List<PictureDataInfo> pictures;

}
