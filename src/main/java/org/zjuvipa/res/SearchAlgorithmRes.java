package org.zjuvipa.res;

import lombok.Data;
import org.zjuvipa.info.AlgorithmInfo;
import org.zjuvipa.info.UserInfo;

import java.util.List;

@Data
public class SearchAlgorithmRes {
    private List<AlgorithmInfo> algorithmInfos;
}
