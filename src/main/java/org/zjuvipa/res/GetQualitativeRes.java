package org.zjuvipa.res;

import lombok.Data;
import org.zjuvipa.info.rankListInfo;
import java.util.List;

@Data
public class GetRankRes {
    private List<rankListInfo> ranklistinfo;
}
