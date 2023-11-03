package org.zjuvipa.req;

import lombok.Data;

@Data
public class SubmitAlgorithmReq {
    private String username;
    private String name;
    private Double score;
    private String institute;
    private Integer ranking;
    private String morfPath;
    private String lerfPath;
    private String pythonPath;
    private String email;
    private String info;
}
