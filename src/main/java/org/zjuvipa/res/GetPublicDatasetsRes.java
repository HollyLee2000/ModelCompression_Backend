package org.zjuvipa.res;

import lombok.Data;
import org.zjuvipa.info.DatasetInfo;
import org.zjuvipa.info.PictureDataInfo;

import java.util.List;

@Data
public class GetPublicDatasetsRes {

    private DatasetInfo datasetInfo;

    private List<PictureDataInfo> pictureDataInfos;

}
