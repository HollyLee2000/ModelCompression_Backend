package org.zjuvipa.compression.model.res;

import lombok.Data;
import org.zjuvipa.compression.model.info.ModelInfo;

import java.util.List;

@Data
public class FindModelRes {
    private List<ModelInfo> modelInfos;
}
