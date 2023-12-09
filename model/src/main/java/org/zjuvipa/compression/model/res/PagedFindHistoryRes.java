package org.zjuvipa.compression.model.res;

import com.github.pagehelper.PageInfo;
import lombok.Data;
import org.zjuvipa.compression.model.entity.History;
import org.zjuvipa.compression.model.info.HistoryInfo;

@Data
public class PagedFindHistoryRes {
    private PageInfo<History> historyInfos;
}
