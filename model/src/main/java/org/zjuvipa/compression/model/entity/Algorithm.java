package org.zjuvipa.compression.model.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.zjuvipa.compression.model.info.AlgorithmInfo;
import org.zjuvipa.compression.common.util.MyBeanUtils;

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
public class Algorithm implements Serializable {

    private static final long serialVersionUID = 1L;

//    @TableId(type = IdType.AUTO)
//    private Integer algoId;
//
//    private String algoName;
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

    public Algorithm(){}

    public Algorithm(AlgorithmInfo algorithmInfo){
        MyBeanUtils.copyProperties(algorithmInfo, this);
    }

    public AlgorithmInfo change(){
        AlgorithmInfo algorithmInfo = new AlgorithmInfo();
        MyBeanUtils.copyProperties(this, algorithmInfo);
        return algorithmInfo;
    }
}
