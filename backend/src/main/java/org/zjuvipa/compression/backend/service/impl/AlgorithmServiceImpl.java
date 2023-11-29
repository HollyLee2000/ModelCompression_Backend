package org.zjuvipa.compression.service.impl;

import org.zjuvipa.compression.service.IAlgorithmService;
import org.zjuvipa.model.entity.Algorithm;
import org.zjuvipa.model.info.AlgorithmInfo;
import org.zjuvipa.compression.mapper.AlgorithmMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author panyan
 * @since 2022-08-05
 */
@Service
public class AlgorithmServiceImpl extends ServiceImpl<AlgorithmMapper, Algorithm> implements IAlgorithmService {
    @Resource
    AlgorithmMapper algorithmMapper;

    @Override
    public List<AlgorithmInfo> findAlgoByName(String algorithmName){
        List<Algorithm> algorithm = algorithmMapper.findAlgoByName(algorithmName);
        if(algorithm == null)
            return null;
        else{
            List<AlgorithmInfo> tool = new ArrayList<>();
            for(Algorithm i : algorithm){
                tool.add(i.change());
            }
            return tool;
        }

    }

    @Override
    public boolean uploadAlgorithm(String username, String name, Double score, String institute, Integer ranking, String morfPath,
                                   String lerfPath, String pythonPath, String email, String info, String dateTime, String status) {
        algorithmMapper.uploadAlgorithm(username, name, score, institute, ranking, morfPath, lerfPath, pythonPath, email, info, dateTime, status);
        return true;
    }

    public boolean algorithmReject(Integer algorithmId){
        algorithmMapper.algorithmReject(algorithmId);
        return true;
    }

    public boolean algorithmApprove(Integer algorithmId){
        algorithmMapper.algorithmApprove(algorithmId);
        return true;
    }



    @Override
    public List<AlgorithmInfo> searchAlgorithm(){
        List<Algorithm> temp= algorithmMapper.searchAlgorithm();
        List<AlgorithmInfo> result = new ArrayList<AlgorithmInfo>();
        for (Algorithm obj: temp){
            result.add(obj.change());
        }
        return result;
    }
}
