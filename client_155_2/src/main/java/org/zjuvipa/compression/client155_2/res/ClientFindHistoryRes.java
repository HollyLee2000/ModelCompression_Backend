package org.zjuvipa.compression.client155_2.res;

import lombok.Data;
import org.zjuvipa.compression.client155_2.info.ClientHistoryInfo;

import java.util.List;

@Data
public class ClientFindHistoryRes {
    private List<ClientHistoryInfo> historyInfos;
}
