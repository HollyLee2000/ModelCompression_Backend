package org.zjuvipa.info;

import org.zjuvipa.entity.Algorithm;
import org.zjuvipa.util.MyBeanUtils;

import lombok.Data;

@Data
public class AlgorithmInfo {

//    private Integer algoId;
//
//    private String algoName;
    private Integer algorithmId;
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
    private String dateTime;
    private String status;

    public AlgorithmInfo(){}

    public AlgorithmInfo(Algorithm algorithm){
        MyBeanUtils.copyProperties(algorithm, this);
    }
}