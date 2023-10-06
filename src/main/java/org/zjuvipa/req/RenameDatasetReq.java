package org.zjuvipa.req;

import lombok.Data;

@Data
public class RenameDatasetReq {
    private int datasetId;

    private String newName;
}
