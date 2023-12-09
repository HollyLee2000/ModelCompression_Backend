package org.zjuvipa.compression.model.req;

import lombok.Data;

@Data
public class FindHistoriesByUserReq {
    private String username;
    private String model;
    private String type;
    private String createTimeBegin;
    private String createTimeEnd;
    private String client;
}
