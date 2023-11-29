package org.zjuvipa.compression.model.res;

import lombok.Data;
import org.zjuvipa.compression.model.info.HistoryInfo;

import java.util.List;

@Data
public class FindHistoryRes {
    private List<HistoryInfo> historyInfos;
}
