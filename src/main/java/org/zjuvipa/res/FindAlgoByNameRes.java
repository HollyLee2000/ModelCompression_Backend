package org.zjuvipa.res;

import lombok.Data;
import org.zjuvipa.info.AlgorithmInfo;
import java.util.List;

@Data
public class FindAlgoByNameRes {
    private List<AlgorithmInfo> algorithmInfo;
}
