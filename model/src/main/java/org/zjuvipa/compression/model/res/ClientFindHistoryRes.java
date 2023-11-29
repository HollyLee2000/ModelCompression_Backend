package org.zjuvipa.compression.model.res;

import lombok.Data;
import org.zjuvipa.compression.model.info.ClientHistoryInfo;

import java.util.List;

@Data
public class ClientFindHistoryRes {
    private List<ClientHistoryInfo> historyInfos;
}
