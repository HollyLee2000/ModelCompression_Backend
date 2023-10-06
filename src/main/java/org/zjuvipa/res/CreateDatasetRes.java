package org.zjuvipa.res;

import lombok.Data;
import org.zjuvipa.info.DatasetInfo;

@Data
public class CreateDatasetRes {
    private boolean succeed;

    private DatasetInfo datasetInfo;
}
