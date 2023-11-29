package org.zjuvipa.compression.model.res;

import lombok.Data;
import org.zjuvipa.compression.model.info.AlgorithmInfo;

import java.util.List;

@Data
public class FindAlgoByNameRes {
    private List<AlgorithmInfo> algorithmInfo;
}
