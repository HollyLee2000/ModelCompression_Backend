package org.zjuvipa.compression.model.req;

import lombok.Data;

@Data
public class RenameDatasetReq {
    private int datasetId;

    private String newName;
}
