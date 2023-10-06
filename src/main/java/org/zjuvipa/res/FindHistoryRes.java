package org.zjuvipa.res;

import lombok.Data;
import org.zjuvipa.info.HistoryInfo;

import java.util.List;

@Data
public class FindHistoryRes {
    private List<HistoryInfo> historyInfos;
}
