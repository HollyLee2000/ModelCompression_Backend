package org.zjuvipa.compression.model.res;

import lombok.Data;
import org.zjuvipa.compression.model.info.DatasetInfo;

@Data
public class CreateDatasetRes {
    private boolean succeed;

    private DatasetInfo datasetInfo;
}
