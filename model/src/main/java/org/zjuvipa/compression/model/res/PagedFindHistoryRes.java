package org.zjuvipa.compression.model.res;

import com.github.pagehelper.PageInfo;
import lombok.Data;
import org.zjuvipa.compression.model.info.HistoryInfo;

import java.util.List;

@Data
public class FindHistoryRes {
    private PageInfo<HistoryInfo> historyInfos;
}
