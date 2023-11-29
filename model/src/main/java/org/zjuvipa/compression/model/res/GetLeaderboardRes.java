package org.zjuvipa.compression.model.res;

import lombok.Data;
import org.zjuvipa.compression.model.info.leaderboardInfo;

import java.util.List;

@Data
public class GetLeaderboardRes {
    private List<leaderboardInfo> leaderboardinfo;
}
