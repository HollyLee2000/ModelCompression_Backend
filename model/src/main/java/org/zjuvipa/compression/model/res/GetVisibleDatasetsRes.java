package org.zjuvipa.compression.model.res;

import lombok.Data;
import org.zjuvipa.compression.model.info.DatasetInfo;
import org.zjuvipa.compression.model.info.PictureDataInfo;

import java.util.List;

@Data
public class GetVisibleDatasetsRes {

    private DatasetInfo datasetInfo;

    private List<PictureDataInfo> pictureDataInfos;
}
