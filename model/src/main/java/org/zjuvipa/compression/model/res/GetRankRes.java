package org.zjuvipa.compression.model.res;

import lombok.Data;
import org.zjuvipa.compression.model.info.rankListInfo;

import java.util.List;

@Data
public class GetRankRes {
    private List<rankListInfo> ranklistinfo;
}
