package org.zjuvipa.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.zjuvipa.info.HistoryInfo;
import org.zjuvipa.util.MyBeanUtils;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class History implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
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

    public History(){}

    public History(HistoryInfo historyInfo){
        MyBeanUtils.copyProperties(historyInfo, this);
    }

    public HistoryInfo change(){
        HistoryInfo historyInfo = new HistoryInfo();
        MyBeanUtils.copyProperties(this, historyInfo);
        return historyInfo;
    }


}
