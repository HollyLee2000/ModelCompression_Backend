package org.zjuvipa.compression.model.res;

import lombok.Data;
import org.zjuvipa.compression.model.info.HistoryInfo;
import org.zjuvipa.compression.model.info.InfoInfo;

import java.util.List;

@Data
public class FindInfoRes {
    private List<InfoInfo> infoInfos;
}
