package org.zjuvipa.compression.model.res;

import lombok.Data;
import org.zjuvipa.compression.model.info.HistoriesInfo;

import java.util.List;

@Data
public class GetUserHistoriesRes {

    private List<HistoriesInfo> historiesInfos;

}
