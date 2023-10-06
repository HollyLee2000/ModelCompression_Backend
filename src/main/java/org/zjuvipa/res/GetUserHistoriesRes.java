package org.zjuvipa.res;

import lombok.Data;
import org.zjuvipa.info.HistoriesInfo;

import java.util.List;

@Data
public class GetUserHistoriesRes {

    private List<HistoriesInfo> historiesInfos;

}
