package org.zjuvipa.info;

import lombok.Data;
import org.zjuvipa.entity.History;
import org.zjuvipa.util.MyBeanUtils;

@Data
public class HistoryInfo {

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

    public HistoryInfo(){}

    public HistoryInfo(History history){
        MyBeanUtils.copyProperties(history, this);
    }

    public History change(){
        History history = new History();
        MyBeanUtils.copyProperties(this, history);
        return history;
    }
}
