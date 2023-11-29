package org.zjuvipa.compression.model.res;

import lombok.Data;
import org.zjuvipa.compression.model.info.AlgorithmInfo;

import java.util.List;

@Data
public class SearchAlgorithmRes {
    private List<AlgorithmInfo> algorithmInfos;
}
