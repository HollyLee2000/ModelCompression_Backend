package org.zjuvipa.res;

import lombok.Data;
import org.zjuvipa.info.leaderboardInfo;

import java.util.List;

@Data
public class GetLeaderboardRes {
    private List<leaderboardInfo> leaderboardinfo;
}
