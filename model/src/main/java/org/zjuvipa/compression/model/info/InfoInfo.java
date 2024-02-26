package org.zjuvipa.compression.model.info;

import lombok.Data;
import org.zjuvipa.compression.model.entity.Info;
import org.zjuvipa.compression.common.util.MyBeanUtils;

@Data
public class InfoInfo {

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

    public InfoInfo(){}

    public InfoInfo(Info history){
        MyBeanUtils.copyProperties(history, this);
    }

    public Info change(){
        Info history = new Info();
        MyBeanUtils.copyProperties(this, history);
        return history;
    }
}
